package com.example.virasat.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.model.BadgeResult
import com.example.virasat.data.model.QuizResult
import com.example.virasat.domain.BadgeEngine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BadgesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RepositoryProvider.getRepository(application)
    private val prefs = application.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)

    private val _badges = MutableStateFlow<List<BadgeResult>>(emptyList())
    val badges: StateFlow<List<BadgeResult>> = _badges.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repo.getAllCheckIns(),
                repo.getAllUnlockedFacts()
            ) { checkIns, facts ->
                val quizResults = loadQuizResults()
                val aiTourUsed = prefs.getBoolean("ai_tour_used", false)
                BadgeEngine.evaluate(checkIns, facts, quizResults, aiTourUsed)
            }.collect { _badges.value = it }
        }
    }

    private fun loadQuizResults(): List<QuizResult> {
        val json = prefs.getString("quiz_results", null) ?: return emptyList()
        return try {
            json.split(";").filter { it.isNotBlank() }.map { entry ->
                val parts = entry.split(",")
                QuizResult(parts[0], parts[1].toInt(), parts.getOrNull(2)?.toLongOrNull() ?: 0L)
            }
        } catch (_: Exception) { emptyList() }
    }
}
