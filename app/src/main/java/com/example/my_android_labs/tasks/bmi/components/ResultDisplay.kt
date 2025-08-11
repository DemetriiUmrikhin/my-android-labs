package com.example.my_android_labs.tasks.bmi.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultDisplay(
    bmi: Float,
    category: String,
    idealWeight: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ваш ИМТ",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "%.1f".format(bmi),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                color = getCategoryColor(category)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Идеальный вес: $idealWeight",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun getCategoryColor(category: String): Color {
    return when {
        category.contains("Норма") -> Color(0xFF4CAF50)
        category.contains("Недостаточный") -> Color(0xFF00BCD4)
        category.contains("Избыточный") -> Color(0xFFFF9800)
        category.contains("Ожирение") -> Color(0xFFFF5722)
        else -> MaterialTheme.colorScheme.onSurface
    }
}