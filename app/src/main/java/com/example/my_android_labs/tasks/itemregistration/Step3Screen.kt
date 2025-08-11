package com.example.my_android_labs.tasks.itemregistration


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.my_android_labs.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun Step3Screen(
    viewModel: ItemViewModel,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    var showPermissionDialog by remember { mutableStateOf(false) }

    // Состояние для изображения
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Создаем временный файл для изображения
    val tempFile = remember {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            File.createTempFile(
                "app_icon_${timestamp}_",
                ".jpg",
                context.cacheDir
            )
        } catch (e: IOException) {
            Log.e("Step3Screen", "Error creating temp file", e)
            null
        }
    }

    // Инициализируем URI
    LaunchedEffect(tempFile) {
        tempFile?.let { file ->
            imageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        }
    }

    // Лаунчер для камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri?.let { uri ->
                viewModel.appIconUri = uri.toString()
            }
        }
    }

    // Лаунчер для галереи
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.appIconUri = it.toString()
        }
    }

    // Проверка разрешений
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            imageUri?.let { cameraLauncher.launch(it) }
        } else {
            showPermissionDialog = true
        }
    }

    // Диалог при отсутствии разрешений
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Требуется разрешение") },
            text = { Text("Для использования камеры необходимо предоставить разрешение") },
            confirmButton = {
                Button(onClick = {
                    showPermissionDialog = false
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Повторить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Шаг 3/3: Контактные данные", style = MaterialTheme.typography.titleLarge)

        // Превью данных
        Text("Итоговые данные:", style = MaterialTheme.typography.titleMedium)
        Text("Название: ${viewModel.name}")
        Text("Версия: ${viewModel.version}")
        Text("Лицензия: ${viewModel.licenseType}")

        // Контактные поля
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") },
            isError = viewModel.email.isNotBlank() && !viewModel.isEmailValid(),
            supportingText = {
                if (viewModel.email.isNotBlank() && !viewModel.isEmailValid()) {
                    Text("Некорректный email", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.phone = it },
            label = { Text("Телефон") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = viewModel.phone.isNotBlank() && !viewModel.isPhoneValid(),
            supportingText = {
                if (viewModel.phone.isNotBlank() && !viewModel.isPhoneValid()) {
                    Text("Телефон должен содержать не менее 10 цифр", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Загрузка изображения
        Text("Иконка приложения:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    // Проверяем разрешение перед запуском камеры
                    val permissionCheck = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    )
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        imageUri?.let { cameraLauncher.launch(it) }
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = imageUri != null
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Сделать фото")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Камера")
            }

            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Image, contentDescription = "Выбрать из галереи")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Галерея")
            }
        }

        // Отображение изображения
        viewModel.appIconUri?.let { uri ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .crossfade(true)
                    .build(),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                placeholder = painterResource(R.drawable.ic_loading),
                error = painterResource(R.drawable.ic_broken_image)
            )
        }

        // Кнопки действий
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text("Назад")
            }

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${viewModel.phone}")
                    }
                    activity?.startActivity(intent)
                },
                modifier = Modifier.weight(1f),
                enabled = viewModel.isPhoneValid()
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Позвонить")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Звонок")
            }

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${viewModel.email}")
                        putExtra(Intent.EXTRA_SUBJECT, "Регистрация приложения ${viewModel.name}")
                    }
                    activity?.startActivity(intent)
                },
                modifier = Modifier.weight(1f),
                enabled = viewModel.isEmailValid()
            ) {
                Icon(Icons.Default.Email, contentDescription = "Отправить email")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Email")
            }

            Button(
                onClick = onComplete,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Завершить")
            }
        }
    }
}