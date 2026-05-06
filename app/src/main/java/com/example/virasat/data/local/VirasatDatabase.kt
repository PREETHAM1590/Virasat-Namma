package com.example.virasat.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.virasat.data.model.CheckIn
import com.example.virasat.data.model.UnlockedFact

@Database(entities = [CheckIn::class, UnlockedFact::class], version = 2, exportSchema = false)
abstract class VirasatDatabase : RoomDatabase() {
    abstract fun checkInDao(): CheckInDao
    abstract fun unlockedFactDao(): UnlockedFactDao

    companion object {
        @Volatile
        private var INSTANCE: VirasatDatabase? = null

        fun getDatabase(context: Context): VirasatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VirasatDatabase::class.java,
                    "virasat_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
