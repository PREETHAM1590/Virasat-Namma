package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.ui.theme.OnSecondaryFixed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Namaste. I am your Modern Custodian. How can I assist you in exploring our rich cultural heritage and natural sanctuaries today?", false, null),
            ChatMessage("I'm looking for hidden ancient structures in the Western Ghats that are surrounded by dense forests.", true, null),
            ChatMessage("The Western Ghats hold many architectural secrets perfectly integrated with nature. One exceptional site is the Tambdi Surla Temple.", false, "https://images.unsplash.com/photo-1561361058-4e7e3d831413?w=800"),
            ChatMessage("It is a 12th-century Shaivite temple built in the Kadamba style, uniquely carved from black basalt, and remains deeply hidden within the Bhagwan Mahaveer Sanctuary.", false, null)
        )
    }

    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.secondaryContainer)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopAppBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "Back",
                        tint = cs.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    "Virasat",
                    style = type.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.primary
                )
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = cs.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Chat Canvas
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Date marker
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Today",
                            style = type.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.05.sp
                            ),
                            color = cs.onSecondaryContainer,
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(cs.surfaceContainerLowest.copy(alpha = 0.5f))
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }

                items(messages) { msg ->
                    if (msg.isUser) {
                        // User message
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.85f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp).copy(topEnd = CornerSize(4.dp)))
                                        .background(cs.primaryContainer)
                                        .padding(horizontal = 24.dp, vertical = 24.dp)
                                ) {
                                    Text(
                                        text = msg.text,
                                        style = type.bodyMedium,
                                        color = cs.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    } else {
                        // AI message
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(0.85f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Avatar row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = cs.tertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        "Virasat Guide",
                                        style = type.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 0.05.sp
                                        ),
                                        color = cs.tertiary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp).copy(topStart = CornerSize(4.dp)))
                                        .background(cs.surfaceContainerLowest)
                                        .border(
                                            width = 1.dp,
                                            color = cs.surfaceVariant.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(16.dp).copy(topStart = CornerSize(4.dp))
                                        )
                                        .padding(horizontal = 24.dp, vertical = 24.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Text(
                                            text = msg.text,
                                            style = type.bodyMedium,
                                            color = cs.onSurface
                                        )
                                        msg.imageUrl?.let { url ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(224.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(cs.surfaceVariant)
                                            ) {
                                                AsyncImage(
                                                    model = url,
                                                    contentDescription = null,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Spacer for input area
                item { Spacer(modifier = Modifier.height(96.dp)) }
            }
        }

        // Floating input area
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            cs.secondaryContainer,
                            cs.secondaryContainer
                        )
                    )
                )
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(999.dp))
                    .background(cs.surfaceContainerLowest)
                    .border(
                        width = 1.dp,
                        color = cs.surfaceVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = cs.tertiary
                    )
                }
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = {
                        Text(
                            "Ask about heritage or nature...",
                            color = cs.outlineVariant
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = cs.onSurface,
                        unfocusedTextColor = cs.onSurface
                    )
                )
                IconButton(
                    onClick = {
                        if (input.isNotBlank()) {
                            messages.add(ChatMessage(input, true, null))
                            val response = generateAIResponse(input)
                            messages.add(ChatMessage(response, false, null))
                            input = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(OnSecondaryFixed)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = cs.inverseOnSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
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

data class ChatMessage(val text: String, val isUser: Boolean, val imageUrl: String?)
