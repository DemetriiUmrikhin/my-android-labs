package com.example.my_android_labs.tasks.dialog

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun Step2Screen(
    viewModel: DialogViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showContextMenu1 by remember { mutableStateOf(false) }
    var showPopupMenu by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var popupAnchor by remember { mutableStateOf<Any?>(null) }

    // Launcher для запроса разрешения на уведомления
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(context, "Разрешение на уведомления отклонено", Toast.LENGTH_SHORT).show()
        }
    }

    // Проверяем и запрашиваем разрешение
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(permission)
            }
        }
    }

    // Создаем канал для уведомлений
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "validation_channel",
            "Проверка данных",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Уведомления о проверке данных"
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // AlertDialog для перехода на следующий экран
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Подтверждение") },
            text = { Text("Вы уверены, что хотите перейти к следующему шагу?") },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Подтверждение"
                )
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    onNext()
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    // Snackbar
    if (showSnackbar) {
        Snackbar(
            action = {
                Button(onClick = {
                    showNotification(context, "Notification from snackbar")
                    showSnackbar = false
                }) {
                    Text("Уведомление")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(snackbarMessage)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Шаг 2") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Поле 1 с контекстным меню
            Box {
                OutlinedTextField(
                    value = viewModel.step2Field1,
                    onValueChange = { viewModel.step2Field1 = it },
                    label = { Text("Поле 1") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showContextMenu1 = true }
                )

                // Контекстное меню для поля 1
                DropdownMenu(
                    expanded = showContextMenu1,
                    onDismissRequest = { showContextMenu1 = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Заполнить поле") },
                        onClick = {
                            viewModel.step2Field1 = "Значение по умолчанию"
                            showContextMenu1 = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Очистить поле") },
                        onClick = {
                            viewModel.step2Field1 = ""
                            showContextMenu1 = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Проверить данные") },
                        onClick = {
                            val isValid = viewModel.step2Field1.isNotBlank()
                            snackbarMessage = if (isValid) "Поле 1 заполнено верно" else "Ошибка в поле 1"
                            showSnackbar = true
                            showContextMenu1 = false
                        }
                    )
                }
            }

            // Поле 2 с Popup меню
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { popupAnchor = it }
            ) {
                OutlinedTextField(
                    value = viewModel.step2Field2,
                    onValueChange = { viewModel.step2Field2 = it },
                    label = { Text("Поле 2") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showPopupMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                        }
                    }
                )

                // Popup меню для поля 2
                DropdownMenu(
                    expanded = showPopupMenu,
                    onDismissRequest = { showPopupMenu = false },
                    offset = DpOffset(0.dp, (-48).dp) // Смещение для правильного позиционирования
                ) {
                    DropdownMenuItem(
                        text = { Text("Заполнить поле") },
                        onClick = {
                            viewModel.step2Field2 = "Пример значения"
                            showPopupMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Очистить поле") },
                        onClick = {
                            viewModel.step2Field2 = ""
                            showPopupMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Проверить данные") },
                        onClick = {
                            val isValid = viewModel.step2Field2.isNotBlank()
                            snackbarMessage = if (isValid) "Поле 2 заполнено верно" else "Ошибка в поле 2"
                            showSnackbar = true

                            // Проверяем разрешение перед показом уведомления
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED) {
                                showNotification(context, "Проверка: ${if (isValid) "Успешно" else "Ошибка"}")
                            }
                            showPopupMenu = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Далее")
            }
        }
    }
}

// Функция для показа уведомлений с проверкой разрешения
fun showNotification(context: Context, message: String) {
    // Дополнительная проверка разрешения
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
            return
        }
    }

    val builder = NotificationCompat.Builder(context, "validation_channel")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Проверка данных")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    try {
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    } catch (e: SecurityException) {
        Log.e("Notification", "Security exception: ${e.message}")
    }
}