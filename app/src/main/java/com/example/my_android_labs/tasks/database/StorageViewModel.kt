package com.example.my_android_labs.tasks.database

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_android_labs.tasks.database.data.BuildingMaterial
import com.example.my_android_labs.tasks.database.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StorageViewModel(private val repository: StorageRepository) : ViewModel() {
    val materials = repository.getMaterials()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveMaterial(material: BuildingMaterial) {
        viewModelScope.launch {
            repository.saveMaterial(material)
        }
    }

    fun deleteMaterial(id: Long) {
        viewModelScope.launch {
            repository.deleteMaterial(id)
        }
    }

    fun updateMaterial(material: BuildingMaterial) {
        viewModelScope.launch {
            repository.updateMaterial(material)
        }
    }

}