package com.example.virasat.data.model

data class QuizResult(
    val siteId: String,
    val score: Int,
    val completedAt: Long = System.currentTimeMillis()
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String = ""
)
