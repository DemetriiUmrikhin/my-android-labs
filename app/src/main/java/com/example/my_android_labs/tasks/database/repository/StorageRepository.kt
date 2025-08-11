package com.example.my_android_labs.tasks.database.repository

import com.example.my_android_labs.tasks.database.data.BuildingMaterial
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    suspend fun saveMaterial(material: BuildingMaterial)
    fun getMaterials(): Flow<List<BuildingMaterial>>
    suspend fun deleteMaterial(id: Long)
    suspend fun updateMaterial(material: BuildingMaterial)
}