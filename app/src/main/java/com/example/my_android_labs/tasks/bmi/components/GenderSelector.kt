package com.example.my_android_labs.tasks.bmi.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GenderSelector(
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = "Пол",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedGender == "male",
                onClick = { onGenderSelected("male") }
            )
            Icon(
                imageVector = Icons.Default.Male,
                contentDescription = "Мужской",
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text("Мужской")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedGender == "female",
                onClick = { onGenderSelected("female") }
            )
            Icon(
                imageVector = Icons.Default.Female,
                contentDescription = "Женский",
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text("Женский")
        }
    }
}