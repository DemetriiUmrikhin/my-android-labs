package com.example.my_android_labs.tasks.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.my_android_labs.tasks.database.data.BuildingMaterial
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(material: BuildingMaterial)

    @Query("SELECT * FROM building_materials ORDER BY lastUpdated DESC")
    fun getAll(): Flow<List<BuildingMaterial>>

    @Query("DELETE FROM building_materials WHERE id = :id")
    suspend fun delete(id: Long)

    @Update
    suspend fun update(material: BuildingMaterial)
}