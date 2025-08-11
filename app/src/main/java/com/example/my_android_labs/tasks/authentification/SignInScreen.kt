package com.example.my_android_labs.tasks.authentification

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.my_android_labs.navigation.Screen
import com.google.firebase.auth.GoogleAuthProvider


@Composable
fun SignInScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поле email с валидацией
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле пароля с валидацией
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )

        // Сообщение об ошибке
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка входа
        Button(
            onClick = {
                // Валидация полей
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Пожалуйста, заполните все поля"
                    return@Button
                }

                errorMessage = null
                authViewModel.signIn(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate(Screen.Profile.route)
                    } else {
                        errorMessage = task.exception?.message ?: "Ошибка аутентификации"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка "Забыли пароль?"
        TextButton(
            onClick = { /* Восстановление пароля */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Forgot Password?")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка регистрации
        TextButton(
            onClick = { navController.navigate(Screen.SignUp.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Google Sign-In
        GoogleSignInButton { token ->
            if (token != null) {
                val credential = GoogleAuthProvider.getCredential(token, null)
                authViewModel.googleSignIn(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate(Screen.Profile.route)
                    } else {
                        Toast.makeText(
                            context,
                            "Google sign-in failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}