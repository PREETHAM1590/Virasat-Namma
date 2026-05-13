package com.example.virasat.data.source

import com.example.virasat.data.model.Fact
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.SiteType
import com.google.firebase.firestore.DocumentSnapshot

fun HeritageSite.toMap(): Map<String, Any?> = hashMapOf(
    "id" to id,
    "name" to name,
    "nameLocal" to nameLocal,
    "location" to location,
    "district" to district,
    "type" to type.name,
    "description" to description,
    "shortDescription" to shortDescription,
    "history" to history,
    "architecture" to architecture,
    "legends" to legends,
    "imageUrl" to imageUrl,
    "galleryImages" to galleryImages,
    "facts" to facts.map { mapOf("id" to it.id, "title" to it.title, "description" to it.description) },
    "latitude" to latitude,
    "longitude" to longitude,
    "visitingHours" to visitingHours,
    "entryFee" to entryFee,
    "qrCodeId" to qrCodeId,
    "audioGuideUrl" to audioGuideUrl,
    "isFavourite" to isFavourite,
    "rating" to rating,
    "reviews" to reviews
)

fun DocumentSnapshot.toHeritageSite(): HeritageSite? = try {
    val factsRaw = get("facts") as? List<Map<String, String>>
    val facts = factsRaw?.map { Fact(it["id"] ?: "", it["title"] ?: "", it["description"] ?: "") } ?: emptyList()

    HeritageSite(
        id = id,
        name = getString("name") ?: "",
        nameLocal = getString("nameLocal") ?: "",
        location = getString("location") ?: "",
        district = getString("district") ?: "",
        type = SiteType.valueOf(getString("type") ?: "MONUMENT"),
        description = getString("description") ?: "",
        shortDescription = getString("shortDescription") ?: "",
        history = getString("history") ?: "",
        architecture = getString("architecture") ?: "",
        legends = getString("legends") ?: "",
        imageUrl = getString("imageUrl") ?: "",
        galleryImages = (get("galleryImages") as? List<String>) ?: emptyList(),
        facts = facts,
        latitude = getDouble("latitude") ?: 0.0,
        longitude = getDouble("longitude") ?: 0.0,
        visitingHours = getString("visitingHours") ?: "",
        entryFee = getString("entryFee") ?: "",
        qrCodeId = getString("qrCodeId") ?: "",
        audioGuideUrl = getString("audioGuideUrl"),
        isFavourite = getBoolean("isFavourite") ?: false,
        rating = (getDouble("rating") ?: 4.5f).toFloat(),
        reviews = (getLong("reviews") ?: 0L).toInt()
    )
} catch (_: Exception) {
    null
}
