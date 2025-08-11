package com.example.my_android_labs.tasks.bmi.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalculateButton(
    onCalculate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onCalculate,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(8.dp)
    ) {
        Text(
            text = "Рассчитать ИМТ",
            style = MaterialTheme.typography.titleMedium
        )
    }
}