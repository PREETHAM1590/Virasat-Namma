package com.example.virasat.data.model

data class QuizResult(
    val siteId: String,
    val score: Int,
    val completedAt: Long = System.currentTimeMillis()
)
