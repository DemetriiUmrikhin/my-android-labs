package com.example.my_android_labs.tasks.itemregistration

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Step2Screen(
    viewModel: ItemViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val isSmallScreen = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Шаг 2/3: Дополнительные данные", style = MaterialTheme.typography.titleLarge)

            // Разная компоновка для портрета и ландшафта
            if (isPortrait || isSmallScreen) {
                Step2PortraitLayout(viewModel)
            } else {
                Step2LandscapeLayout(viewModel)
            }

            // Общие кнопки навигации
            //NavigationButtons(onBack, onNext, viewModel)

            // Кнопка "Далее" - общая для всех вариантов
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.name.isNotBlank() && viewModel.version.isNotBlank()
            ) {
                Text("Далее")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Step2PortraitLayout(viewModel: ItemViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ReleaseDateField(viewModel)
        CategorySelector(viewModel)
        RatingSelector(viewModel)
        DescriptionField(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Step2LandscapeLayout(viewModel: ItemViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReleaseDateField(viewModel)
            CategorySelector(viewModel)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RatingSelector(viewModel)
            DescriptionField(viewModel)
        }
    }
}

@Composable
private fun NavigationButtons(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: ItemViewModel
) {
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
            onClick = onNext,
            modifier = Modifier.weight(1f),
            enabled = viewModel.releaseDate != null && viewModel.category.isNotBlank()
        ) {
            Text("Далее")
        }
    }
}

// Компоненты

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReleaseDateField(viewModel: ItemViewModel) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    // Преобразуем Date в строку для отображения
    val dateString = remember(viewModel.releaseDate) {
        viewModel.releaseDate?.let { dateFormatter.format(it) } ?: ""
    }

    OutlinedTextField(
        value = dateString,
        onValueChange = {},
        label = { Text("Дата выпуска") },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, "Выбрать дату")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (showDatePicker) {
        MaterialDatePickerDialog(
            onDateSelected = { localDate ->
                // Преобразуем LocalDate в java.util.Date
                localDate?.let {
                    val date = Date.from(
                        it.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    )
                    viewModel.releaseDate = date
                }
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(viewModel: ItemViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Игры", "Образование", "Бизнес", "Здоровье", "Социальные")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = viewModel.category,
            onValueChange = {},
            label = { Text("Категория") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        viewModel.category = category
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun RatingSelector(viewModel: ItemViewModel) {
    Column {
        Text("Рейтинг: ${viewModel.rating}/5",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp))

        Slider(
            value = viewModel.rating.toFloat(),
            onValueChange = { viewModel.rating = it.toInt() },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 1..5) {
                Text("$i",
                    color = if (i <= viewModel.rating) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun DescriptionField(viewModel: ItemViewModel) {
    OutlinedTextField(
        value = viewModel.description,
        onValueChange = { viewModel.description = it },
        label = { Text("Описание") },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        maxLines = 5
    )
}

// Material3 Date Picker Dialog
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialDatePickerDialog(
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onDateSelected(selectedDate)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}