package com.example.my_android_labs.tasks.authentification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    val currentUser = auth.currentUser

    fun signIn(email: String, password: String) = auth.signInWithEmailAndPassword(email, password)

    fun signUp(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password)

    fun signOut() = auth.signOut()

    fun googleSignIn(credential: AuthCredential) = auth.signInWithCredential(credential)

    fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("Auth", "Email verification failed", task.exception)
                }
            }
    }
}