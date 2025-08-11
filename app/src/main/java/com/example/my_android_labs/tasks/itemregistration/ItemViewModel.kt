package com.example.my_android_labs.tasks.itemregistration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Date

class ItemViewModel : ViewModel() {
    // Шаг 1: Основные характеристики
    var name by mutableStateOf("")
    var version by mutableStateOf("")
    var licenseType by mutableStateOf("Free")
    var hasInAppPurchases by mutableStateOf(false)

    // Шаг 2: Дополнительные характеристики
    var releaseDate by mutableStateOf<Date?>(null)
    var category by mutableStateOf("")
    var rating by mutableStateOf(3)
    var description by mutableStateOf("")

    // Шаг 3: Контактные данные
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var appIconUri by mutableStateOf<String?>(null)

    // Валидация email
    fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Валидация телефона
    fun isPhoneValid(): Boolean {
        return phone.length >= 10
    }

    // Сброс состояния
    fun reset() {
        name = ""
        version = ""
        licenseType = "Free"
        hasInAppPurchases = false
        releaseDate = null
        category = ""
        rating = 3
        description = ""
        email = ""
        phone = ""
        appIconUri = null
    }
}