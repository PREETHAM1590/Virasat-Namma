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
    version = 5,
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

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE check_ins ADD COLUMN imageUrl TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): VirasatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VirasatDatabase::class.java,
                    "virasat_database"
                // #25: fallback for missing 1→2 and 2→3 migrations; destructive is safe
                // because Room data is a cache of Firestore / KarnatakaSites seed data.
                ).addMigrations(MIGRATION_3_4, MIGRATION_4_5).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
