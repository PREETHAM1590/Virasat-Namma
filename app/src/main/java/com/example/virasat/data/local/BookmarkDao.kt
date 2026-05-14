package com.example.virasat.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE siteId = :siteId")
    suspend fun delete(siteId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE siteId = :siteId)")
    suspend fun exists(siteId: String): Boolean

    @Query("SELECT siteId FROM bookmarks ORDER BY bookmarkedAt DESC")
    fun observeAll(): Flow<List<String>>

    @Query("DELETE FROM bookmarks")
    suspend fun clearAll()
}
