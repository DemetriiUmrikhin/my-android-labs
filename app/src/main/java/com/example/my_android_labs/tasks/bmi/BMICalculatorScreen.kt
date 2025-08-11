package com.example.my_android_labs.tasks.bmi

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculatorScreen(onBack: () -> Unit) {
    val viewModel: BMIViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    //val snackbarHostState = remember { SnackbarHostState() }
    //val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Калькулятор ИМТ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        //snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        if (isPortrait) {
            BMIPortrait(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel,
                state = state,
                innerPadding = innerPadding

            )
        } else {
            BMILandscape(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel,
                innerPadding = innerPadding,
                state = state
            )
        }
    }
}