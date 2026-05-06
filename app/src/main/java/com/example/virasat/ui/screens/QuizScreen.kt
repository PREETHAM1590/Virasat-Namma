package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(onBack: () -> Unit) {
    var currentQuestion by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var answered by remember { mutableStateOf(false) }

    val questions = remember {
        listOf(
            QuizQuestion("Which empire built Hampi as its capital?", listOf("Chola", "Vijayanagara", "Mughal", "Maurya"), 1),
            QuizQuestion("What is the famous dance pose count of Hampi's Nataraja?", listOf("56", "81", "108", "18"), 3),
            QuizQuestion("Which site is known as the 'Whispering Gallery'?", listOf("Mysore Palace", "Gol Gumbaz", "Hampi", "Badami"), 1),
            QuizQuestion("The Hoysala temples use which stone for carvings?", listOf("Marble", "Granite", "Soapstone", "Sandstone"), 2),
            QuizQuestion("How many light bulbs illuminate Mysore Palace during Dussehra?", listOf("50,000", "75,000", "97,000", "1,00,000"), 2)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heritage Challenge") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
                .padding(24.dp)
        ) {
            if (showResult) {
                QuizResult(score = score, total = questions.size, onRetry = {
                    currentQuestion = 0; score = 0; selectedAnswer = null; answered = false; showResult = false
                }, onBack = onBack)
            } else {
                val q = questions[currentQuestion]
                Text("Question ${currentQuestion + 1} of ${questions.size}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (currentQuestion + 1).toFloat() / questions.size },
                    modifier = Modifier.fillMaxWidth(),
                    color = VirasatMaroon
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(q.question, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
                Spacer(modifier = Modifier.height(24.dp))
                q.options.forEachIndexed { index, option ->
                    val isSelected = selectedAnswer == index
                    val isCorrect = index == q.correctAnswer
                    val bgColor = when {
                        !answered -> if (isSelected) VirasatMaroon.copy(alpha = 0.1f) else Color.White
                        isCorrect -> Color(0xFFE8F5E9)
                        isSelected -> Color(0xFFFFEBEE)
                        else -> Color.White
                    }
                    val borderColor = when {
                        !answered && isSelected -> VirasatMaroon
                        answered && isCorrect -> Color(0xFF2E7D32)
                        answered && isSelected -> Color(0xFFB00020)
                        else -> Color.LightGray
                    }
                    Card(
                        onClick = { if (!answered) selectedAnswer = index },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) VirasatMaroon else Color.LightGray.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${('A'.code + index).toChar()}",
                                    color = if (isSelected) Color.White else Color.DarkGray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(option, fontSize = 15.sp, color = Color.DarkGray, modifier = Modifier.weight(1f))
                            if (answered && isCorrect) {
                                Icon(Icons.Default.Check, null, tint = Color(0xFF2E7D32))
                            } else if (answered && isSelected && !isCorrect) {
                                Icon(Icons.Default.Close, null, tint = Color(0xFFB00020))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                if (!answered) {
                    Button(
                        onClick = {
                            answered = true
                            if (selectedAnswer == q.correctAnswer) score++
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        enabled = selectedAnswer != null,
                        colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Check Answer", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentQuestion < questions.size - 1) {
                                currentQuestion++
                                selectedAnswer = null
                                answered = false
                            } else {
                                showResult = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (currentQuestion < questions.size - 1) "Next Question" else "See Results", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun QuizResult(score: Int, total: Int, onRetry: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val isExcellent = score >= total * 0.8
        Icon(
            if (isExcellent) Icons.Default.EmojiEvents else Icons.Default.Star,
            null,
            modifier = Modifier.size(80.dp),
            tint = VirasatGold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Quiz Complete!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
        Spacer(modifier = Modifier.height(8.dp))
        Text("You scored $score out of $total", fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Try Again", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp)) {
            Text("Back to Home")
        }
    }
}

data class QuizQuestion(val question: String, val options: List<String>, val correctAnswer: Int)
