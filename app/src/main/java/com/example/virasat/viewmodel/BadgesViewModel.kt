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

    // Cache quiz results in StateFlow so they're not re-parsed from SharedPreferences
    // on every check-in / fact Flow emission (#11)
    private val _quizResults = MutableStateFlow(loadQuizResults())
    val quizResults: StateFlow<List<QuizResult>> = _quizResults.asStateFlow()

    fun refreshQuizResults() {
        _quizResults.value = loadQuizResults()
    }

    init {
        viewModelScope.launch {
            combine(
                repo.getAllCheckIns(),
                repo.getAllUnlockedFacts(),
                _quizResults
            ) { checkIns, facts, quizResults ->
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
