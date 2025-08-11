package com.example.my_android_labs.tasks.database.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.my_android_labs.tasks.database.StorageViewModel
import com.example.my_android_labs.tasks.database.data.BuildingMaterial
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMaterialScreen(
    material: BuildingMaterial,
    viewModel: StorageViewModel,
    onBack: () -> Unit
) {
    // Состояния для полей редактирования
    var name by remember { mutableStateOf(material.name) }
    var price by remember { mutableStateOf(material.price.toString()) }
    var category by remember { mutableStateOf(material.category) }
    var description by remember { mutableStateOf(material.description) }
    var quantity by remember { mutableStateOf(material.quantity.toString()) }
    var supplier by remember { mutableStateOf(material.supplier) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование товара") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Создаем обновленный объект материала
                    val updatedMaterial = material.copy(
                        name = name,
                        price = price.toDoubleOrNull() ?: 0.0,
                        category = category,
                        description = description,
                        quantity = quantity.toIntOrNull() ?: 0,
                        supplier = supplier,
                        lastUpdated = System.currentTimeMillis()
                    )

                    viewModel.updateMaterial(updatedMaterial)
                    onBack()
                }
            ) {
                Icon(Icons.Default.Save, "Сохранить изменения")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Наименование") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Цена") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Категория") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 3
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Количество") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = supplier,
                onValueChange = { supplier = it },
                label = { Text("Поставщик") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}