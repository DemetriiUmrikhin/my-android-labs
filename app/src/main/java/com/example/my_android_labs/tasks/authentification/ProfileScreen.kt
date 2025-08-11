package com.example.my_android_labs.tasks.authentification

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.my_android_labs.navigation.Screen


@Composable
fun ProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val user = authViewModel.currentUser

    Column {
        Text("Email: ${user?.email ?: "Unknown"}")
        Button(onClick = {
            authViewModel.signOut()
            navController.popBackStack(Screen.Auth.route, inclusive = false)
        }) { Text("Logout") }
    }
}