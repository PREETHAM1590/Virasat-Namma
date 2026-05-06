package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Hello! I'm your Virasat AI assistant. Ask me about Karnataka's heritage sites, history, architecture, or travel tips!", false)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Heritage Assistant") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatMaroon)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(msg)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text("Ask about Hampi, temples...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (input.isNotBlank()) {
                            messages.add(ChatMessage(input, true))
                            val response = generateAIResponse(input)
                            messages.add(ChatMessage(response, false))
                            input = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(VirasatMaroon)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val alignment = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (msg.isUser) VirasatMaroon else Color.White
    val textColor = if (msg.isUser) Color.White else Color.DarkGray

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bgColor),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                msg.text,
                color = textColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

fun generateAIResponse(question: String): String {
    val q = question.lowercase()
    return when {
        q.contains("hampi") -> "Hampi is a UNESCO World Heritage Site that was the capital of the Vijayanagara Empire. Key attractions include the Vittala Temple with its stone chariot and musical pillars."
        q.contains("mysore") -> "Mysore Palace is the official residence of the Wadiyar dynasty. During Dussehra, it's illuminated with 97,000 light bulbs — a breathtaking sight!"
        q.contains("best time") -> "The best time to visit Karnataka's heritage sites is between October and March when the weather is pleasant."
        q.contains("ticket") || q.contains("entry") || q.contains("fee") -> "Entry fees vary by site. Hampi: ₹40 (Indian), Mysore Palace: ₹100 (Indian), Badami: ₹35 (Indian). Foreigner rates are 10-15x higher."
        q.contains("qr") || q.contains("check in") -> "Look for QR codes at the entrance of each heritage site. Scan them with the app to check in and unlock hidden facts about the location!"
        else -> "Great question! Karnataka has over 750 heritage sites. I'd recommend starting with Hampi, Mysore Palace, Belur, and Badami. Would you like details on any of these?"
    }
}

data class ChatMessage(val text: String, val isUser: Boolean)
