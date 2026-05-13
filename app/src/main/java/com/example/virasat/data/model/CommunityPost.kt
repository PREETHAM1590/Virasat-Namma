package com.example.virasat.data.model

data class CommunityPost(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val siteId: String = "",
    val siteName: String = "",
    val imageUrl: String? = null,
    val text: String = "",
    val timestamp: com.google.firebase.Timestamp? = null
)
