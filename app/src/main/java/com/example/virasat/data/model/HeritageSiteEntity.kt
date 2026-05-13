package com.example.virasat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heritage_sites")
data class HeritageSiteEntity(
    @PrimaryKey
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

fun HeritageSiteEntity.toModel(): HeritageSite = HeritageSite(
    id = id,
    name = name,
    nameLocal = nameLocal,
    location = location,
    district = district,
    type = type,
    description = description,
    shortDescription = shortDescription,
    history = history,
    architecture = architecture,
    legends = legends,
    imageUrl = imageUrl,
    galleryImages = galleryImages,
    facts = facts,
    latitude = latitude,
    longitude = longitude,
    visitingHours = visitingHours,
    entryFee = entryFee,
    qrCodeId = qrCodeId,
    audioGuideUrl = audioGuideUrl,
    isFavourite = isFavourite,
    rating = rating,
    reviews = reviews
)

fun HeritageSite.toEntity(): HeritageSiteEntity = HeritageSiteEntity(
    id = id,
    name = name,
    nameLocal = nameLocal,
    location = location,
    district = district,
    type = type,
    description = description,
    shortDescription = shortDescription,
    history = history,
    architecture = architecture,
    legends = legends,
    imageUrl = imageUrl,
    galleryImages = galleryImages,
    facts = facts,
    latitude = latitude,
    longitude = longitude,
    visitingHours = visitingHours,
    entryFee = entryFee,
    qrCodeId = qrCodeId,
    audioGuideUrl = audioGuideUrl,
    isFavourite = isFavourite,
    rating = rating,
    reviews = reviews
)
