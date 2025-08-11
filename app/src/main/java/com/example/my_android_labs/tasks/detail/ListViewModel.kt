package com.example.my_android_labs.tasks.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ListViewModel : ViewModel() {
    val selectedItem = mutableStateOf<Item?>(null)
    val items = sampleItems

    fun selectItem(item: Item) {
        selectedItem.value = item
    }
}