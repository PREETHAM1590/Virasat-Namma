package com.example.virasat.data.local

import androidx.room.TypeConverter
import com.example.virasat.data.model.SiteType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return if (value.isBlank()) emptyList() else value.split("||")
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return list.joinToString("||")
    }

    @TypeConverter
    fun fromSiteType(type: SiteType): String = type.name

    @TypeConverter
    fun toSiteType(value: String): SiteType {
        return try {
            SiteType.valueOf(value)
        } catch (_: Exception) {
            SiteType.MONUMENT
        }
    }

    @TypeConverter
    fun fromFactList(value: String): List<com.example.virasat.data.model.Fact> {
        return if (value.isBlank()) emptyList() else try {
            json.decodeFromString(value)
        } catch (_: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun toFactList(list: List<com.example.virasat.data.model.Fact>): String {
        return try {
            json.encodeToString(list)
        } catch (_: Exception) {
            ""
        }
    }
}
