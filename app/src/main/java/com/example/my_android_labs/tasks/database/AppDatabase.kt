package com.example.my_android_labs.tasks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.my_android_labs.tasks.database.data.BuildingMaterial

@Database(entities = [BuildingMaterial::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun materialDao(): MaterialDao
}

// Диспетчер базы данных
object DatabaseProvider {
    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "building_materials_db"
            ).fallbackToDestructiveMigration()
                .build()
                .also { database = it }
        }
    }
}