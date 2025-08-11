package com.example.my_android_labs.tasks.itemregistration

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.layout.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemRegistrationScreen(onBack: () -> Unit) {
    val viewModel: ItemViewModel = viewModel()
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Регистрация приложения") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = "step1"
        ) {
            composable("step1") {
                Step1Screen(
                    viewModel = viewModel,
                    onNext = { navController.navigate("step2") }
                )
            }
            composable("step2") {
                Step2Screen(
                    viewModel = viewModel,
                    onNext = { navController.navigate("step3") },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("step3") {
                Step3Screen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onComplete = { viewModel.reset(); onBack() }
                )
            }
        }
    }
}