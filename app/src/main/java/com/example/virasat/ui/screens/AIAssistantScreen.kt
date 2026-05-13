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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.service.GeminiHeritageService
import com.example.virasat.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val ctx = LocalContext.current
    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val allSites by produceState<List<com.example.virasat.data.model.HeritageSite>>(emptyList(), ctx) {
        value = repo.getAllSitesList()
    }
    val sitesSummary by remember(allSites) {
        derivedStateOf {
            allSites.take(20).joinToString("\n") { "- ${it.name} (${it.type}): ${it.shortDescription}" }
        }
    }
    val scope = rememberCoroutineScope()
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Namaste. I am your Heritage Guide. Ask me about Karnataka's magnificent forts, temples, palaces, and monuments.", false, null),
            ChatMessage("Tell me about Hampi.", true, null),
            ChatMessage("Hampi was the capital of the Vijayanagara Empire in the 14th century. At its peak in 1500 CE, it was the world's second-largest medieval city after Beijing.", false, "https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=400"),
            ChatMessage("Key highlights include the stone chariot at Vittala Temple, 56 musical pillars, and the second-largest monolithic Nandi statue in India. Legend says the boulders were thrown by Hanuman during the battle in the Ramayana.", false, null)
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
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = cs.onSurface,
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
                                        imageVector = Icons.Default.AccountBalance,
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

                // Loading indicator
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = cs.tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    "Virasat Guide is thinking...",
                                    style = type.labelMedium,
                                    color = cs.tertiary
                                )
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = cs.tertiary,
                                    strokeWidth = 2.dp
                                )
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
                            "Ask about Karnataka heritage...",
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
                        if (input.isNotBlank() && !isLoading) {
                            val userMsg = input
                            messages.add(ChatMessage(userMsg, true, null))
                            input = ""
                            isLoading = true
                            scope.launch {
                                val response = GeminiHeritageService.chatWithHeritageGuide(userMsg, sitesSummary = sitesSummary)
                                messages.add(ChatMessage(response, false, null))
                                isLoading = false
                            }
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
