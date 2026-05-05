package com.example.virasat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unlocked_facts")
data class UnlockedFact(
    @PrimaryKey
    val factId: String,
    val siteId: String,
    val title: String,
    val description: String,
    val unlockedAt: Long = System.currentTimeMillis()
)
