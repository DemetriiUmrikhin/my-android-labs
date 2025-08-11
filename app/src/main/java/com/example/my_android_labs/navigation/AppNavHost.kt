package com.example.my_android_labs.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.my_android_labs.tasks.bmi.BMICalculatorScreen
import com.example.my_android_labs.HomeScreen
import com.example.my_android_labs.tasks.authentification.AuthViewModel
import com.example.my_android_labs.tasks.authentification.DataListScreen
import com.example.my_android_labs.tasks.authentification.DatabaseChoiceScreen
import com.example.my_android_labs.tasks.authentification.ItemEditScreen

import com.example.my_android_labs.tasks.authentification.ProfileScreen

import com.example.my_android_labs.tasks.authentification.SignInScreen
import com.example.my_android_labs.tasks.authentification.SignUpScreen
import com.example.my_android_labs.tasks.contact.ContactsScreen
import com.example.my_android_labs.tasks.database.DataStorageScreen
import com.example.my_android_labs.tasks.detail.DetailMainScreen
import com.example.my_android_labs.tasks.dialog.DialogScreen
import com.example.my_android_labs.tasks.itemregistration.ItemRegistrationScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()


    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onTaskSelected = { taskRoute ->
                    navController.navigate(taskRoute)
                }
            )
        }
        composable(Screen.BMICalculator.route) {
            BMICalculatorScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ItemRegistration.route) {
            ItemRegistrationScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Contacts.route) {
            ContactsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Dialog.route) {
            DialogScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Details.route) {
            DetailMainScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.DataStorage.route) {
            DataStorageScreen(
                onBack = { navController.popBackStack() }
            )
        }
        // Аутентификация
        composable(Screen.Auth.route) {
            SignInScreen(navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // Базы данных
        composable(Screen.DatabaseChoice.route) {
            DatabaseChoiceScreen(navController)
        }
        composable(
            route = Screen.DataList.route,
            arguments = listOf(navArgument("dbType") { type = NavType.StringType })
        ) { backStackEntry ->
            DataListScreen(
                dbType = backStackEntry.arguments?.getString("dbType")!!,
                navController = navController
            )
        }
        composable(
            route = Screen.ItemEdit.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.StringType },
                navArgument("dbType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ItemEditScreen(
                itemId = backStackEntry.arguments?.getString("itemId"),
                dbType = backStackEntry.arguments?.getString("dbType") ?: "realtime",
                navController = navController
            )
        }
    }
}
