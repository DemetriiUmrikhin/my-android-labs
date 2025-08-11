package com.example.my_android_labs.tasks.database

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.my_android_labs.tasks.database.components.EditMaterialDialog
import com.example.my_android_labs.tasks.database.repository.ExternalStorageRepository
import com.example.my_android_labs.tasks.database.repository.InternalStorageRepository
import com.example.my_android_labs.tasks.database.repository.RoomRepository
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.my_android_labs.tasks.database.components.MaterialForm
import com.example.my_android_labs.tasks.database.components.MaterialItem
import com.example.my_android_labs.tasks.database.components.MaterialList
import com.example.my_android_labs.tasks.database.data.BuildingMaterial

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataStorageScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("building_materials_prefs", Context.MODE_PRIVATE) }

    // Выбор хранилища
    val storageType = remember { mutableStateOf("Room") }

    // Создаем ViewModel
    val viewModel: StorageViewModel = remember(storageType.value) {
        when (storageType.value) {
            "Internal" -> StorageViewModel(InternalStorageRepository(context))
            "External" -> StorageViewModel(ExternalStorageRepository(context))
            "Room" -> {
                val database = DatabaseProvider.getDatabase(context)
                StorageViewModel(RoomRepository(database.materialDao()))
            }
            else -> StorageViewModel(InternalStorageRepository(context))
        }
    }

    // Собираем состояние материалов
    val materials by viewModel.materials.collectAsStateWithLifecycle()
    var materialToEdit by remember { mutableStateOf<BuildingMaterial?>(null) }

    // Состояние для выбранной вкладки
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Диалог редактирования
    materialToEdit?.let { material ->
        EditMaterialDialog(
            material = material,
            onSave = { updatedMaterial ->
                viewModel.updateMaterial(updatedMaterial)
                materialToEdit = null
            },
            onDismiss = { materialToEdit = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Хранение стройматериалов") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.Storage, "Тип хранилища")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Внутреннее хранилище") },
                                onClick = {
                                    storageType.value = "Internal"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Внешнее хранилище") },
                                onClick = {
                                    storageType.value = "External"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("База данных SQLite") },
                                onClick = {
                                    storageType.value = "Room"
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // TabRow для переключения между вводом данных и списком товаров
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Ввод данных") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Список товаров") }
                )
            }

            // Отображаем содержимое в зависимости от выбранной вкладки
            when (selectedTabIndex) {
                0 -> {
                    // Вкладка "Ввод данных" - показываем форму
                    MaterialForm(viewModel, prefs)
                }
                1 -> {
                    // Вкладка "Список товаров" - показываем список материалов
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        if (materials.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Нет сохраненных товаров", style = MaterialTheme.typography.bodyLarge)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                            ) {
                                items(materials) { material ->
                                    MaterialItem(
                                        material = material,
                                        onEdit = { materialToEdit = material },
                                        onDelete = { viewModel.deleteMaterial(material.id) }
                                    )
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}