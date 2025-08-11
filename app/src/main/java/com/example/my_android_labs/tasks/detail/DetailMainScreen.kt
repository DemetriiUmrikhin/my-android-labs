package com.example.my_android_labs.tasks.detail

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// MainScreen.kt
@Composable
fun DetailMainScreen(onBack: () -> Boolean) {
    val viewModel: ListViewModel = viewModel()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Состояние для навигации (только для портретной ориентации)
    var showDetail by remember { mutableStateOf(false) }

    // Восстановление состояния при изменении конфигурации
    LaunchedEffect(Unit) {
        if (isLandscape && viewModel.selectedItem.value == null && viewModel.items.isNotEmpty()) {
            viewModel.selectItem(viewModel.items.first())
        }
    }

    if (isLandscape) {
        // Альбомная ориентация - два экрана рядом
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Список (1/3 ширины)
            ListScreen(
                viewModel = viewModel,
                onItemSelected = {}, // В альбомной ориентации не нужно навигации
                modifier = Modifier.weight(1f)
            )

            // Детали (2/3 ширины)
            viewModel.selectedItem.value?.let { item ->
                DetailScreen(
                    item = item,
                    modifier = Modifier.weight(2f)
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Select an item")
                }
            }
        }
    } else {
        // Портретная ориентация - либо список, либо детали
        if (showDetail) {
            viewModel.selectedItem.value?.let { item ->
                DetailScreen(
                    item = item,
                    modifier = Modifier.fillMaxSize()
                )

                // Кнопка назад
                IconButton(
                    onClick = { showDetail = false },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            ListScreen(
                viewModel = viewModel,
                onItemSelected = { item ->
                    viewModel.selectItem(item)
                    showDetail = true
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}