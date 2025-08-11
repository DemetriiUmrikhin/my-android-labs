package com.example.my_android_labs.tasks.dialog

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(
    viewModel: DialogViewModel,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Шаг 1") },
                actions = {
                    // Кнопка меню
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Опции")
                    }

                    // Выпадающее меню
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        // Заполнить первое поле
                        DropdownMenuItem(
                            text = { Text("Заполнить поле 1") },
                            onClick = {
                                viewModel.field1 = "test@iba.by"
                                showMenu = false
                            }
                        )

                        // Заполнить второе поле
                        DropdownMenuItem(
                            text = { Text("Заполнить поле 2") },
                            onClick = {
                                viewModel.field2 = "Пример текста"
                                showMenu = false
                            }
                        )

                        // Заполнить всё
                        DropdownMenuItem(
                            text = { Text("Заполнить всё") },
                            onClick = {
                                viewModel.field1 = "test@iba.by"
                                viewModel.field2 = "Пример текста"
                                showMenu = false
                            }
                        )

                        // Очистить данные (только если есть данные)
                        if (viewModel.hasData) {
                            DropdownMenuItem(
                                text = { Text("Очистить данные") },
                                onClick = {
                                    viewModel.field1 = ""
                                    viewModel.field2 = ""
                                    showMenu = false
                                }
                            )
                        }

                        // Проверить данные
                        DropdownMenuItem(
                            text = { Text("Проверить данные") },
                            onClick = {
                                val isValid = viewModel.validateFields()
                                val message = if (isValid) "Данные верны" else "Ошибка в данных"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                showMenu = false
                            }
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.field1,
                onValueChange = { viewModel.field1 = it },
                label = { Text("Поле 1") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.field2,
                onValueChange = { viewModel.field2 = it },
                label = { Text("Поле 2") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Кастомный Toast при переходе
                    Toast.makeText(context, "Переход на шаг 2", Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                    onNext()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Далее")
            }
        }
    }
}