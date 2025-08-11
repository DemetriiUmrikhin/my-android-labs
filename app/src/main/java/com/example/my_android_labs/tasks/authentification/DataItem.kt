package com.example.my_android_labs.tasks.authentification

data class DataItem(
    var id: String? = null,
    var title: String = "",
    var content: String = ""
) {
    // Явный конструктор без аргументов для Firebase
    constructor() : this(null, "", "")
}