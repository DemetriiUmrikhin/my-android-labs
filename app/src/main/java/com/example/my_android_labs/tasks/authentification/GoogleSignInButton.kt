package com.example.my_android_labs.tasks.authentification


import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.my_android_labs.R

@Composable
fun GoogleSignInButton(onTokenReceived: (String?) -> Unit) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isLoading = false
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let {
                onTokenReceived(it)
            } ?: run {
                onTokenReceived(null)
                Log.e("GoogleSignIn", "ID token is null")
            }
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Error: ${e.message}")
            onTokenReceived(null)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true
                    // Принудительно выходим из текущего аккаунта
                    GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                        .addOnCompleteListener {
                            // Создаем новый интент для входа
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(context.getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()

                            val client = GoogleSignIn.getClient(context, gso)

                            // Добавляем флаг, чтобы всегда показывать выбор аккаунта
                            val signInIntent = client.signInIntent.apply {
                                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            }

                            launcher.launch(signInIntent)
                        }
                }
            ) {
                Text("Sign In with Google")
            }
        }
    }
}