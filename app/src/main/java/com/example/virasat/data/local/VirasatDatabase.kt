package com.example.virasat.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.virasat.data.model.CheckIn
import com.example.virasat.data.model.HeritageSiteEntity
import com.example.virasat.data.model.UnlockedFact

@Database(
    entities = [HeritageSiteEntity::class, CheckIn::class, UnlockedFact::class, BookmarkEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VirasatDatabase : RoomDatabase() {
    abstract fun heritageSiteDao(): HeritageSiteDao
    abstract fun checkInDao(): CheckInDao
    abstract fun unlockedFactDao(): UnlockedFactDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: VirasatDatabase? = null

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS bookmarks (siteId TEXT NOT NULL PRIMARY KEY, bookmarkedAt INTEGER NOT NULL DEFAULT 0)"
                )
            }
        }

        fun getDatabase(context: Context): VirasatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VirasatDatabase::class.java,
                    "virasat_database"
                ).addMigrations(MIGRATION_3_4).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
