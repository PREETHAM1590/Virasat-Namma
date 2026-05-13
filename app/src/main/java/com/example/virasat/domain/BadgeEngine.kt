package com.example.virasat.domain

import com.example.virasat.data.model.*

object BadgeEngine {
    fun evaluate(
        checkIns: List<CheckIn>,
        unlockedFacts: List<UnlockedFact>,
        quizResults: List<QuizResult>,
        aiTourUsed: Boolean
    ): List<BadgeResult> {
        val templeCount = checkIns.count { it.stampIcon == "temple" }
        val uniqueCount = checkIns.map { it.siteId }.distinct().size
        val unescoVisited = checkIns.any { it.stampIcon == "unesco" }
        val factCount = unlockedFacts.size
        val perfectQuiz = quizResults.any { it.score == 100 }

        return BadgeCatalogue.all.map { def ->
            when (val cond = def.condition) {
                is BadgeCondition.TempleCheckIns -> BadgeResult(def, templeCount >= cond.required, templeCount, cond.required)
                is BadgeCondition.FactsUnlocked -> BadgeResult(def, factCount >= cond.required, factCount, cond.required)
                is BadgeCondition.UnescoCheckIn -> BadgeResult(def, unescoVisited, if (unescoVisited) 1 else 0, 1)
                is BadgeCondition.UniqueCheckIns -> BadgeResult(def, uniqueCount >= cond.required, uniqueCount, cond.required)
                is BadgeCondition.QuizPerfect -> BadgeResult(def, perfectQuiz, if (perfectQuiz) 1 else 0, 1)
                is BadgeCondition.AiTourUsed -> BadgeResult(def, aiTourUsed, if (aiTourUsed) 1 else 0, 1)
            }
        }
    }
}
