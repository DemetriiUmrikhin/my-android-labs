package com.example.my_android_labs.tasks.detail

data class Item(
    val id: Int,
    val title: String,
    val description: String
)

val sampleItems = listOf(
    Item(1, "Item 1", "Description for item 1"),
    Item(2, "Item 2", "Description for item 2"),
    Item(3, "Item 3", "Description for item 3"),
    Item(4, "Item 4", "Description for item 4"),
    Item(5, "Item 5", "Description for item 5")
)