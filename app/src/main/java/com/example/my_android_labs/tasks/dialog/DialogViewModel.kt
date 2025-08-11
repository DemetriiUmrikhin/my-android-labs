package com.example.my_android_labs.tasks.dialog

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Date

class DialogViewModel : ViewModel() {
    // Шаг 1
    var field1 by mutableStateOf("")
    var field2 by mutableStateOf("")

    // Шаг 2
    var step2Field1 by mutableStateOf("")
    var step2Field2 by mutableStateOf("")

    // Шаг 3
    var selectedDateTime by mutableStateOf<Date?>(null)

    // Проверка заполненности для меню
    val hasData: Boolean
        get() = field1.isNotBlank() || field2.isNotBlank()

    // Валидация
    fun validateFields(): Boolean {
        return field1.isNotBlank() && field2.isNotBlank()
    }

    fun reset() {
        TODO("Not yet implemented")
    }
}