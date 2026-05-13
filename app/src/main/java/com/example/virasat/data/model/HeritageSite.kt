package com.example.virasat.data.model

data class HeritageSite(
    val id: String,
    val name: String,
    val nameLocal: String,
    val location: String,
    val district: String,
    val type: SiteType,
    val description: String,
    val shortDescription: String,
    val history: String,
    val architecture: String,
    val legends: String,
    val imageUrl: String,
    val galleryImages: List<String> = emptyList(),
    val facts: List<Fact> = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val visitingHours: String,
    val entryFee: String,
    val qrCodeId: String,
    val audioGuideUrl: String? = null,
    val isFavourite: Boolean = false,
    val rating: Float = 4.5f,
    val reviews: Int = 0
)

enum class SiteType {
    TEMPLE,
    PALACE,
    FORT,
    MONUMENT,
    CAVE,
    UNESCO,
    JAIN,
    MUSEUM,
    NATURE,
    TREK,
    LAKE,
    MISC
}

@kotlinx.serialization.Serializable
data class Fact(
    @kotlinx.serialization.SerialName("id") val id: String,
    @kotlinx.serialization.SerialName("title") val title: String,
    @kotlinx.serialization.SerialName("desc") val description: String,
    @kotlinx.serialization.SerialName("unlocked") val isUnlocked: Boolean = false
)

data class AudioChapter(
    val id: String,
    val title: String,
    val durationSeconds: Int,
    val audioUrl: String,
    val transcript: String
)
