package com.example.virasat.data.model

data class Itinerary(
    val id: String = "",
    val name: String = "",
    val siteIds: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
