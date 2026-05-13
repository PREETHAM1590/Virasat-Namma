package com.example.virasat.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.virasat.data.di.RepositoryProvider

@Composable
fun QuizScreen(onBack: () -> Unit) {
    var currentQuestion by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var answered by remember { mutableStateOf(false) }

    val ctx = LocalContext.current
    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val allSites by produceState<List<com.example.virasat.data.model.HeritageSite>>(emptyList(), ctx) {
        value = repo.getAllSitesList()
    }
    val questions = remember(allSites) {
        generateQuizQuestionsFromSites(allSites).shuffled().take(8)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                        RoundedCornerShape(999.dp)
                    )
                    .clickable(onClick = onBack)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Heritage Quiz",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            if (showResult) {
                QuizResult(
                    score = score,
                    total = questions.size,
                    onRetry = {
                        currentQuestion = 0; score = 0; selectedAnswer = null; answered = false; showResult = false
                    },
                    onBack = onBack
                )
            } else {
                val q = questions[currentQuestion]

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${currentQuestion + 1} / ${questions.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Score: $score",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { (currentQuestion + 1).toFloat() / questions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = q.question,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                q.options.forEachIndexed { index, option ->
                    AnswerPill(
                        index = index,
                        option = option,
                        isSelected = selectedAnswer == index,
                        isCorrect = index == q.correctAnswer,
                        answered = answered,
                        enabled = !answered,
                        onClick = { selectedAnswer = index }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!answered) {
                    val active = selectedAnswer != null
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (active) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceContainerHighest,
                                RoundedCornerShape(999.dp)
                            )
                            .clickable(enabled = active) {
                                answered = true
                                if (selectedAnswer == q.correctAnswer) score++
                            }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Check Answer",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (active) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(999.dp)
                            )
                            .clickable {
                                if (currentQuestion < questions.size - 1) {
                                    currentQuestion++
                                    selectedAnswer = null
                                    answered = false
                                } else {
                                    showResult = true
                                }
                            }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentQuestion < questions.size - 1) "Next Question" else "See Results",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AnswerPill(
    index: Int,
    option: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    answered: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val bg by animateColorAsState(
        targetValue = when {
            answered && isCorrect -> MaterialTheme.colorScheme.primaryContainer
            answered && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surfaceContainerLowest
        },
        label = "pill_bg"
    )
    val content by animateColorAsState(
        targetValue = when {
            answered && isCorrect -> MaterialTheme.colorScheme.onPrimaryContainer
            answered && isSelected && !isCorrect -> MaterialTheme.colorScheme.onErrorContainer
            isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
            else -> MaterialTheme.colorScheme.onSurface
        },
        label = "pill_content"
    )
    val letterBg by animateColorAsState(
        targetValue = when {
            answered && isCorrect -> MaterialTheme.colorScheme.primary
            answered && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outlineVariant
        },
        label = "letter_bg"
    )
    val letterContent by animateColorAsState(
        targetValue = when {
            answered && isCorrect -> MaterialTheme.colorScheme.onPrimary
            answered && isSelected && !isCorrect -> MaterialTheme.colorScheme.onError
            isSelected -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "letter_content"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(999.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(letterBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${('A'.code + index).toChar()}",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = letterContent
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = option,
                style = MaterialTheme.typography.bodyLarge,
                color = content,
                modifier = Modifier.weight(1f)
            )
            if (answered && isCorrect) {
                Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary)
            } else if (answered && isSelected && !isCorrect) {
                Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun QuizResult(
    score: Int,
    total: Int,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val isExcellent = score >= total * 0.8

        Text(
            text = if (isExcellent) "Excellent!" else "Quiz Complete",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You scored $score out of $total",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    RoundedCornerShape(999.dp)
                )
                .clickable(onClick = onRetry)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Try Again",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLowest,
                    RoundedCornerShape(999.dp)
                )
                .clickable(onClick = onBack)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Back to Home",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

data class QuizQuestion(val question: String, val options: List<String>, val correctAnswer: Int)

private fun generateQuizQuestionsFromSites(allSites: List<com.example.virasat.data.model.HeritageSite>): List<QuizQuestion> {
    val questions = mutableListOf<QuizQuestion>()

    // Generate questions from site facts
    allSites.shuffled().take(15).forEach { site ->
        site.facts.take(2).forEach { fact ->
            val wrongSites = allSites.filter { it.id != site.id }.shuffled().take(3)
            val opts = listOf(site.name) + wrongSites.map { it.name }
            val shuffledOpts = opts.shuffled()
            questions.add(
                QuizQuestion(
                    question = "${site.name} is known for: ${fact.title}. Which site is this?",
                    options = shuffledOpts,
                    correctAnswer = shuffledOpts.indexOf(site.name)
                )
            )
        }
    }

    // Add some district-based questions
    allSites.shuffled().take(10).forEach { site ->
        val wrongDistricts = allSites.filter { it.district != site.district }.map { it.district }.distinct().shuffled().take(3)
        val opts = listOf(site.district) + wrongDistricts
        val shuffledOpts = opts.shuffled()
        questions.add(
            QuizQuestion(
                question = "${site.name} is located in which district?",
                options = shuffledOpts,
                correctAnswer = shuffledOpts.indexOf(site.district)
            )
        )
    }

    // Add type-based questions
    allSites.shuffled().take(10).forEach { site ->
        val otherTypes = com.example.virasat.data.model.SiteType.values().filter { it != site.type }.shuffled().take(3)
        val opts = listOf(site.type.name.replaceFirstChar { it.uppercase() }) + otherTypes.map { it.name.replaceFirstChar { it.uppercase() } }
        val shuffledOpts = opts.shuffled()
        questions.add(
            QuizQuestion(
                question = "What type of heritage site is ${site.name}?",
                options = shuffledOpts,
                correctAnswer = shuffledOpts.indexOf(site.type.name.replaceFirstChar { it.uppercase() })
            )
        )
    }

    return questions.ifEmpty {
        listOf(
            QuizQuestion("Which empire built Hampi as its capital?", listOf("Chola", "Vijayanagara", "Mughal", "Maurya"), 1)
        )
    }
}
