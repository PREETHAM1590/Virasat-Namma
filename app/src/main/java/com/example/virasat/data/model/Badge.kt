package com.example.virasat.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BadgeCondition {
    data class TempleCheckIns(val required: Int) : BadgeCondition()
    data class FactsUnlocked(val required: Int) : BadgeCondition()
    data object UnescoCheckIn : BadgeCondition()
    data class UniqueCheckIns(val required: Int) : BadgeCondition()
    data object QuizPerfect : BadgeCondition()
    data object AiTourUsed : BadgeCondition()
}

data class BadgeDefinition(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val condition: BadgeCondition
)

object BadgeCatalogue {
    val all: List<BadgeDefinition> = listOf(
        BadgeDefinition("temple_seeker", "Temple Seeker", "Check in at 3 temples", Icons.Default.Star, BadgeCondition.TempleCheckIns(3)),
        BadgeDefinition("fact_collector", "Fact Collector", "Unlock 5 facts", Icons.Default.Lightbulb, BadgeCondition.FactsUnlocked(5)),
        BadgeDefinition("unesco_explorer", "UNESCO Explorer", "Check in at a UNESCO site", Icons.Default.Public, BadgeCondition.UnescoCheckIn),
        BadgeDefinition("pathfinder", "Pathfinder", "Check in at 5 unique sites", Icons.Default.Map, BadgeCondition.UniqueCheckIns(5)),
        BadgeDefinition("quiz_master", "Quiz Master", "Score 100% on a quiz", Icons.Default.Star, BadgeCondition.QuizPerfect),
        BadgeDefinition("ai_voyager", "AI Voyager", "Use the AI Tour for any site", Icons.Default.SmartToy, BadgeCondition.AiTourUsed)
    )
}

data class BadgeResult(
    val definition: BadgeDefinition,
    val isUnlocked: Boolean,
    val progress: Int,
    val target: Int
)
