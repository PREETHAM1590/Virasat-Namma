package com.example.virasat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_ins")
data class CheckIn(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val siteId: String,
    val siteName: String,
    val siteLocation: String,
    val timestamp: Long = System.currentTimeMillis(),
    val qrCodeId: String,
    val stampIcon: String = "default"
)
