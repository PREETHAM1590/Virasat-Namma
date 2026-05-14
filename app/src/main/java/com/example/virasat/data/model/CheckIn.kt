package com.example.virasat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_ins")
data class CheckIn(
    @PrimaryKey
    // No default: callers must supply a UUID so no two check-ins share an empty-string PK
    val id: String,
    val siteId: String,
    val siteName: String,
    val siteLocation: String,
    val timestamp: Long = System.currentTimeMillis(),
    val qrCodeId: String,
    val stampIcon: String = "default",
    val imageUrl: String = ""
)
