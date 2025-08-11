package com.example.my_android_labs

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.my_android_labs.navigation.Screen
import com.example.my_android_labs.tasks.TaskItem
import com.example.my_android_labs.tasks.authentification.AuthViewModel
import com.example.my_android_labs.tasks.authentification.ProfileDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onTaskSelected: (String) -> Unit) {
    val authViewModel: AuthViewModel = hiltViewModel()
    var showProfile by remember { mutableStateOf(false) }


    if (showProfile) {
        ProfileDialog(
            onDismiss = { showProfile = false },
            onLogout = {
                authViewModel.signOut()
                showProfile = false
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Выберите задание") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = rememberLazyListState(),
            flingBehavior = ScrollableDefaults.flingBehavior(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item{
                TaskItem(
                title = "Калькулятор ИМТ",
                description = "Расчет идеального веса по ИМТ",
                onClick = { onTaskSelected(Screen.BMICalculator.route) }
            )
            }

            item{
                TaskItem(
                title = "Регистрация ITEM",
                description = "Создание нового элемента",
                onClick = { onTaskSelected(Screen.ItemRegistration.route) }
            )
            }

            item{
                TaskItem(
                title = "Контакты",
                description = "Работа с контактами",
                onClick = { onTaskSelected(Screen.Contacts.route) }
            )
            }

            item{
                TaskItem(
                title = "Диалоги/нотификации",
                description = "Работа с диалогами",
                onClick = { onTaskSelected(Screen.Dialog.route) }
            )
            }

            item{
                TaskItem(
                title = "Фрагменты",
                description = "Работа с фрагментами",
                onClick = { onTaskSelected(Screen.Details.route) }
            )
            }

            item{
                TaskItem(
                title = "Хранение данных",
                description = "Работа с разными типами хранилищ",
                onClick = { onTaskSelected(Screen.DataStorage.route) }
            )
            }

            item{
                TaskItem(
                title = "Аутентификация",
                description = "Регистрация и авторизация",
                onClick = { onTaskSelected(Screen.Auth.route) }
            )
            }

            item{
                TaskItem(
                title = "Firebase Databases",
                description = "Работа с Realtime DB и Firestore",
                onClick = { onTaskSelected(Screen.DatabaseChoice.route) }
            )
            }

            // Добавьте новые задания здесь:
            /*
            TaskItem(
                title = "Следующее задание",
                description = "Описание задания",
                onClick = { onTaskSelected(Screen.NextTask.route) }
            )
            */
        }
    }
}