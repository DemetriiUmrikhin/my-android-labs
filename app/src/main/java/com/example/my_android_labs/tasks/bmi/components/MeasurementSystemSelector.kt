package com.example.my_android_labs.tasks.bmi.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MeasurementSystemSelector(
    selectedSystem: String,
    onSystemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = "Система мер",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedSystem == "metric",
                onClick = { onSystemSelected("metric") }
            )
            Text(
                text = "Метрическая (кг/см)",
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedSystem == "imperial",
                onClick = { onSystemSelected("imperial") }
            )
            Text(
                text = "Имперская (фунты/дюймы)",
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}