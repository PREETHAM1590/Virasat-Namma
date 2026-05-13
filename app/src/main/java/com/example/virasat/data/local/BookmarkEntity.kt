package com.example.virasat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val siteId: String,
    val bookmarkedAt: Long = System.currentTimeMillis()
)
