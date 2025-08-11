package com.example.my_android_labs.tasks.authentification

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProfileDialog(
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Профиль") },
        text = { Text("Вы вошли в систему") },
        confirmButton = {
            Button(onClick = onLogout) {
                Text("Выйти")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}