package com.example.my_android_labs.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object BMICalculator : Screen("bmi_calculator")
    object ItemRegistration : Screen("item_registration")
    object Contacts : Screen("contacts")
    object Dialog : Screen("dialog")
    object Details : Screen("details")
    object DataStorage : Screen("data_storage")
    // Аутентификация
    object Auth : Screen("auth")
    object SignUp : Screen("sign_up")
    object Profile : Screen("profile")

    // Базы данных
    object DatabaseChoice : Screen("database_choice")
    object DataList : Screen("data_list/{dbType}") {
        fun createRoute(dbType: String) = "data_list/$dbType"
    }
    object ItemEdit : Screen("item_edit/{itemId}/{dbType}") {
        fun createRoute(itemId: String?, dbType: String) =
            "item_edit/${itemId ?: "NEW"}/$dbType"
    }
}