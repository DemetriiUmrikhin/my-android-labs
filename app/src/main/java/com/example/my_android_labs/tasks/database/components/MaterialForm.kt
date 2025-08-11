package com.example.my_android_labs.tasks.database.components

import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.my_android_labs.tasks.database.StorageViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.my_android_labs.tasks.database.data.BuildingMaterial

@Composable
fun MaterialForm(
    viewModel: StorageViewModel,
    prefs: SharedPreferences
) {
    var name by remember { mutableStateOf(prefs.getString("last_name", "") ?: "") }
    var price by remember { mutableStateOf(prefs.getString("last_price", "") ?: "") }
    var category by remember { mutableStateOf(prefs.getString("last_category", "") ?: "") }
    var description by remember { mutableStateOf(prefs.getString("last_description", "") ?: "") }
    var quantity by remember { mutableStateOf(prefs.getString("last_quantity", "") ?: "") }
    var supplier by remember { mutableStateOf(prefs.getString("last_supplier", "") ?: "") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
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
            modifier = Modifier.fillMaxWidth(),
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

        Button(
            onClick = {
                val material = BuildingMaterial(
                    name = name,
                    price = price.toDoubleOrNull() ?: 0.0,
                    category = category,
                    description = description,
                    quantity = quantity.toIntOrNull() ?: 0,
                    supplier = supplier
                )

                viewModel.saveMaterial(material)

                // Сохраняем в SharedPreferences
                with(prefs.edit()) {
                    putString("last_name", name)
                    putString("last_price", price)
                    putString("last_category", category)
                    putString("last_description", description)
                    putString("last_quantity", quantity)
                    putString("last_supplier", supplier)
                    apply()
                }

                // Сбрасываем форму
                name = ""
                price = ""
                category = ""
                description = ""
                quantity = ""
                supplier = ""

                Toast.makeText(context, "Товар сохранен", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить товар")
        }
    }
}