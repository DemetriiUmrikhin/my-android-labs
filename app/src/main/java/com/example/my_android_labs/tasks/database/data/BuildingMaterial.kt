package com.example.my_android_labs.tasks.database.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "building_materials")
data class BuildingMaterial(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val category: String,
    val description: String,
    val quantity: Int,
    val supplier: String,
    val lastUpdated: Long = System.currentTimeMillis()
)