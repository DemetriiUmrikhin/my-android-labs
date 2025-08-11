package com.example.my_android_labs.tasks.database.repository

import com.example.my_android_labs.tasks.database.MaterialDao
import com.example.my_android_labs.tasks.database.data.BuildingMaterial
import kotlinx.coroutines.flow.Flow

class RoomRepository(
    private val materialDao: MaterialDao
) : StorageRepository {

    override suspend fun saveMaterial(material: BuildingMaterial) {
        materialDao.insert(material)
    }

    override fun getMaterials(): Flow<List<BuildingMaterial>> {
        return materialDao.getAll()
    }

    override suspend fun deleteMaterial(id: Long) {
        materialDao.delete(id)
    }

    override suspend fun updateMaterial(material: BuildingMaterial) {
        materialDao.update(material)
    }
}