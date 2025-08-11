package com.example.my_android_labs.tasks.authentification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemEditScreen(
    itemId: String?,
    dbType: String,
    navController: NavHostController,
    viewModel: DataViewModel = hiltViewModel()
) {

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(itemId != "NEW") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

// Инициализируем ViewModel с dbType
    LaunchedEffect(dbType) {
        viewModel.init(dbType)
    }

    // Загружаем данные только для существующих элементов
    LaunchedEffect(itemId) {
        if (itemId != null && itemId != "NEW") {
            viewModel.getItemById(itemId) { item ->
                if (item != null) {
                    title = item.title
                    content = item.content
                } else {
                    errorMessage = "Item not found"
                }
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    // Показываем индикатор загрузки
    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }



    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (title.isBlank() || content.isBlank()) {
                errorMessage = "Заполните все поля"
                return@Button
            }

            val newItem = DataItem(
                id = if (itemId != "NEW") itemId else null,
                title = title,
                content = content
            )

            viewModel.saveItem(dbType, newItem)
            navController.popBackStack()
        }) {
            Text(if (itemId == "NEW") "Создать" else "Обновить")
        }

        // Кнопка удаления только для существующих элементов
        if (itemId != null && itemId != "NEW") {
            Button(
                onClick = {
                    viewModel.deleteItem(dbType, itemId)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text("Удалить")
            }
        }
    }
}