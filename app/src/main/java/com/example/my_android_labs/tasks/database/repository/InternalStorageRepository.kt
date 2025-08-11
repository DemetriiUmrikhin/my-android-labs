package com.example.my_android_labs.tasks.database.repository

import android.content.Context
import android.util.Log
import com.example.my_android_labs.tasks.database.data.BuildingMaterial
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.File

class InternalStorageRepository(
    private val context: Context
) : StorageRepository {

    private val fileName = "materials.json"
    private val gson = Gson()
    private val _materials = MutableStateFlow<List<BuildingMaterial>>(emptyList())
    private val lock = Any()

    init {
        // Загружаем данные при инициализации
        loadMaterials()
    }

    override fun getMaterials(): Flow<List<BuildingMaterial>> = _materials

    override suspend fun saveMaterial(material: BuildingMaterial) {
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                val materials = _materials.value.toMutableList()
                materials.add(material)
                saveToFile(materials)
                _materials.value = materials
            }
        }
    }

    override suspend fun deleteMaterial(id: Long) {
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                val materials = _materials.value.filter { it.id != id }
                saveToFile(materials)
                _materials.value = materials
            }
        }
    }

    override suspend fun updateMaterial(material: BuildingMaterial) {
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                val materials = _materials.value.map {
                    if (it.id == material.id) material else it
                }
                saveToFile(materials)
                _materials.value = materials
            }
        }
    }

    private fun loadMaterials() {
        try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return

            val json = file.readText()
            val type = object : TypeToken<List<BuildingMaterial>>() {}.type
            val materials = gson.fromJson<List<BuildingMaterial>>(json, type) ?: emptyList()
            _materials.value = materials
        } catch (e: Exception) {
            Log.e("InternalStorageRepo", "Error loading materials", e)
        }
    }

    private fun saveToFile(materials: List<BuildingMaterial>) {
        try {
            val file = File(context.filesDir, fileName)
            file.writeText(gson.toJson(materials))
        } catch (e: Exception) {
            Log.e("InternalStorageRepo", "Error saving materials", e)
        }
    }
}