package com.example.my_android_labs.tasks.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step3Screen(
    viewModel: DialogViewModel,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showContextMenu by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Шаг 3") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Поле для даты/времени с контекстным меню
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                val dateText = viewModel.selectedDateTime?.toString() ?: "Дата/время не выбрано"

                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showContextMenu = true }
                )

                // Контекстное меню для выбора даты/времени
                DropdownMenu(
                    expanded = showContextMenu,
                    onDismissRequest = { showContextMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Выбрать дату") },
                        onClick = {
                            showContextMenu = false
                            showDatePicker(context, viewModel)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Выбрать время") },
                        onClick = {
                            showContextMenu = false
                            showTimePicker(context, viewModel)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Завершить")
            }
        }
    }

    // Snackbar для отображения выбранной даты/времени
    if (showSnackbar) {
        Snackbar(
            action = {
                Button(onClick = { showSnackbar = false }) {
                    Text("OK")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(snackbarMessage)
        }
    }
}

// Функция для показа DatePicker
private fun showDatePicker(context: Context, viewModel: DialogViewModel) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePicker = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val newCalendar = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            viewModel.selectedDateTime = newCalendar.time
            showSnackbar(context, "Выбрана дата: $selectedDay.${selectedMonth + 1}.$selectedYear")
        },
        year, month, day
    )
    datePicker.show()
}

// Функция для показа TimePicker
private fun showTimePicker(context: Context, viewModel: DialogViewModel) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val newCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }
            viewModel.selectedDateTime = newCalendar.time
            showSnackbar(context, "Выбрано время: ${selectedHour}:${selectedMinute.toString().padStart(2, '0')}")
        },
        hour, minute, true
    )
    timePicker.show()
}

// Функция для показа Snackbar (используется в функциях выбора)
private fun showSnackbar(context: Context, message: String) {
    // В реальном приложении это должно быть передано в Composable
    // Для простоты здесь просто показываем Toast
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}