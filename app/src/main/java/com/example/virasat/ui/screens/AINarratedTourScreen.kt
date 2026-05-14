package com.example.virasat.ui.screens

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.model.Fact
import com.example.virasat.R
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.service.GeminiHeritageService
import com.example.virasat.data.service.TriviaQuestion
import com.example.virasat.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Locale

// --- Karnataka Heritage Tour Data ---
sealed class TourStop(
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val narrationKey: String,
    val facts: List<Fact> = emptyList()
)

fun buildTourForSite(site: com.example.virasat.data.model.HeritageSite?, context: android.content.Context? = null): List<TourStop> {
    if (site == null) return emptyList()
    val images = listOf(site.imageUrl) + site.galleryImages
    return buildList {
        // Overview
        add(
            OverviewStop(
                site.name,
                site.shortDescription,
                images.getOrElse(0) { site.imageUrl },
                "overview",
                site.facts.take(3)
            )
        )
        // History
        if (site.history.isNotBlank()) {
            add(
                HistoryStop(
                    "A Walk Through Time",
                    site.history,
                    images.getOrElse(1) { site.imageUrl },
                    "history",
                    listOf(Fact("${site.id}-h1", "Founded", "${site.name} holds over 600 years of documented history and architectural evolution."))
                )
            )
        }
        // Architecture
        if (site.architecture.isNotBlank()) {
            add(
                ArchitectureStop(
                    context?.getString(R.string.tour_architectural_marvel) ?: "Architectural Marvel",
                    site.architecture,
                    images.getOrElse(2) { site.imageUrl },
                    "architecture"
                )
            )
        }
        // Legends
        if (site.legends.isNotBlank()) {
            add(
                LegendStop(
                    context?.getString(R.string.tour_legends_lore) ?: "Legends & Lore",
                    site.legends,
                    images.getOrElse(0) { site.imageUrl },
                    "legends"
                )
            )
        }
    }
}

class OverviewStop(t: String, s: String, img: String, key: String, f: List<Fact>) :
    TourStop(t, s, img, key, f)
class HistoryStop(t: String, s: String, img: String, key: String, f: List<Fact>) :
    TourStop(t, s, img, key, f)
class ArchitectureStop(t: String, s: String, img: String, key: String) :
    TourStop(t, s, img, key)
class LegendStop(t: String, s: String, img: String, key: String) :
    TourStop(t, s, img, key)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AINarratedTourScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember(context) { RepositoryProvider.getRepository(context) }
    val site by produceState<com.example.virasat.data.model.HeritageSite?>(null, siteId) {
        value = try { repo.getSiteById(siteId) } catch (_: Exception) { null }
    }
    val tourStops = remember(siteId, site) { buildTourForSite(site, context) }
    val pagerState = rememberPagerState(pageCount = { tourStops.size.coerceAtLeast(1) })
    val coroutineScope = rememberCoroutineScope()

    // AI + TTS state
    var isNarrating by remember { mutableStateOf(false) }
    var narrationText by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("English") }

    // Overlay states
    var showTrivia by remember { mutableStateOf(false) }
    var triviaQuestions by remember { mutableStateOf(listOf<TriviaQuestion>()) }
    var showSnapshot by remember { mutableStateOf(false) }
    var snapshotFact by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // TTS init
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = if (language == "Kannada") Locale("kn", "IN") else Locale.ENGLISH
            }
        }
    }
    LaunchedEffect(language) {
        tts?.language = if (language == "Kannada") Locale("kn", "IN") else Locale.ENGLISH
    }
    DisposableEffect(Unit) {
        onDispose { tts?.stop(); tts?.shutdown() }
    }

    // Auto-generate narration on page change
    LaunchedEffect(pagerState.currentPage, language) {
        tts?.stop()
        snapshotFact = ""
        showSnapshot = false
        isNarrating = false
        val stop = tourStops.getOrNull(pagerState.currentPage)
        val baseText = stop?.subtitle ?: ""
        narrationText = baseText

        if (GeminiHeritageService.isInitialized() && stop != null) {
            val focus = when (stop) {
                is HistoryStop -> "history"
                is ArchitectureStop -> "architecture"
                is LegendStop -> "legends"
                else -> "overview"
            }
            val aiText = GeminiHeritageService.generateNarration(site, language)
            narrationText = aiText.ifBlank { baseText }
            tts?.speak(narrationText, TextToSpeech.QUEUE_FLUSH, null, "narration")
            tts?.setOnUtteranceProgressListener(
                object : UtteranceProgressListener() {
                    override fun onStart(id: String?) { isNarrating = true }
                    override fun onDone(id: String?) { isNarrating = false }
                    override fun onError(id: String?) { isNarrating = false }
                }
            )
            isNarrating = true
        }
    }

    // Current stop
    val currentStop = tourStops.getOrNull(pagerState.currentPage)
    val isLastPage = pagerState.currentPage == tourStops.size - 1
    val progress = if (tourStops.isNotEmpty()) (pagerState.currentPage + 1).toFloat() / tourStops.size else 0f

    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Box(modifier = Modifier.fillMaxSize().background(cs.background)) {
        // ===== Immersive Background Image =====
        currentStop?.let { stop ->
            AsyncImage(
                model = stop.imageUrl,
                contentDescription = stop.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Gradient overlay for readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Black.copy(alpha = 0.0f),
                                Color.Black.copy(alpha = 0.0f),
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.85f)
                            ),
                            startY = 0f
                        )
                    )
            )
        }

        // ===== Top Bar (transparent, floating) =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back pill
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = cs.surfaceContainerLowest.copy(alpha = 0.15f),
                modifier = Modifier
                    .size(48.dp)
                    .clickable { tts?.stop(); onBack() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Site name pill
            val currentSite = site
            if (currentSite != null) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = cs.surfaceContainerLowest.copy(alpha = 0.2f)
                ) {
                    Text(
                        currentSite.nameLocal,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

            // Language toggle pill
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = cs.surfaceContainerLowest.copy(alpha = 0.15f),
                onClick = {
                    language = if (language == "English") "Kannada" else "English"
                }
            ) {
                Text(
                    if (language == "English") "EN" else "ಕ",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        // ===== Page Indicators (floating above card) =====
        if (tourStops.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 320.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tourStops.forEachIndexed { index, _ ->
                    val active = index == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(if (active) 24.dp else 8.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (active) cs.primaryContainer
                                else Color.White.copy(alpha = 0.35f)
                            )
                            .animateContentSize()
                    )
                }
            }
        }

        // ===== Story Card (floating at bottom) =====
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            // Snapshot overlay card (above story card)
            AnimatedVisibility(
                visible = showSnapshot && snapshotFact.isNotEmpty(),
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cs.surfaceContainerLowest.copy(alpha = 0.97f)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = cs.primaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    null,
                                    tint = cs.onPrimaryContainer,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.ai_insight),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = cs.primary
                            )
                            Text(
                                snapshotFact,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = cs.onSurfaceVariant,
                                maxLines = 4
                            )
                        }
                        IconButton(
                            onClick = { showSnapshot = false; snapshotFact = "" }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                null,
                                tint = cs.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            // Bottom Story Card
            Card(
                colors = CardDefaults.cardColors(containerColor = cs.surfaceContainerLowest.copy(alpha = 0.97f)),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Progress line
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(999.dp)),
                        color = cs.primaryContainer,
                        trackColor = cs.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Title row with narration icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        currentStop?.let { stop ->
                            val icon = when (stop) {
                                is OverviewStop -> Icons.Default.Info
                                is HistoryStop -> Icons.Default.History
                                is ArchitectureStop -> Icons.Default.AccountBalance
                                is LegendStop -> Icons.Default.AutoStories
                                else -> Icons.Default.Info
                            }
                            Surface(
                                shape = CircleShape,
                                color = cs.primaryContainer,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        icon,
                                        null,
                                        tint = cs.onPrimaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                currentStop?.title ?: stringResource(R.string.tour_heritage_tour),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = cs.onSurface
                            )
                            Text(
                                stringResource(R.string.tour_step_of, pagerState.currentPage + 1, tourStops.size),
                                fontSize = 13.sp,
                                color = cs.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = {
                                if (isNarrating) {
                                    tts?.stop()
                                    isNarrating = false
                                } else {
                                    isNarrating = true
                                    tts?.speak(
                                        narrationText,
                                        TextToSpeech.QUEUE_FLUSH,
                                        null,
                                        "narration"
                                    )
                                    tts?.setOnUtteranceProgressListener(
                                        object : UtteranceProgressListener() {
                                            override fun onStart(id: String?) {}
                                            override fun onDone(id: String?) { isNarrating = false }
                                            override fun onError(id: String?) { isNarrating = false }
                                        }
                                    )
                                }
                            }
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isNarrating) cs.primaryContainer else cs.surfaceVariant
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        if (isNarrating) Icons.Default.Stop
                                        else @Suppress("DEPRECATION")
                                        Icons.Default.VolumeUp,
                                        null,
                                        tint = if (isNarrating) cs.onPrimaryContainer
                                        else cs.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Narration / description text
                    Text(
                        currentStop?.subtitle ?: "",
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = cs.onSurfaceVariant,
                        maxLines = if (showSnapshot) 2 else 5,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Snapshot button
                        TourActionButton(
                            icon = Icons.Default.AutoAwesome,
                            label = stringResource(R.string.ai_insight),
                            onClick = {
                                showSnapshot = true
                                isLoading = true
                                coroutineScope.launch {
                                    snapshotFact = GeminiHeritageService.describeView(
                                        site,
                                        when (currentStop) {
                                            is HistoryStop -> "history"
                                            is ArchitectureStop -> "architecture"
                                            is LegendStop -> "legends"
                                            else -> "overview"
                                        },
                                        language
                                    )
                                    isLoading = false
                                }
                            }
                        )

                        // Trivia button
                        TourActionButton(
                            icon = Icons.Default.Quiz,
                            label = "Quiz",
                            onClick = {
                                showTrivia = !showTrivia
                                if (showTrivia && triviaQuestions.isEmpty()) {
                                    isLoading = true
                                    coroutineScope.launch {
                                        triviaQuestions = GeminiHeritageService.generateTrivia(site, 3, language)
                                        isLoading = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Prev / Next navigation
                        FilledTonalIconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        (pagerState.currentPage - 1).coerceAtLeast(0)
                                    )
                                }
                            },
                            enabled = pagerState.currentPage > 0
                        ) {
                            Icon(Icons.Default.SkipPrevious, "Previous")
                        }
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (isLastPage) {
                                        onBack()
                                    } else {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cs.primaryContainer,
                                contentColor = cs.onPrimaryContainer
                            )
                        ) {
                            Text(
                                if (isLastPage) "Finish Tour" else "Next",
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                if (isLastPage) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                                null,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // ===== Trivia Bottom Sheet =====
        if (showTrivia) {
            ModalBottomSheet(
                onDismissRequest = { showTrivia = false },
                containerColor = cs.surfaceContainerLowest
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Heritage Quiz",
                            style = type.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = cs.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { showTrivia = false }) {
                            Icon(Icons.Default.Close, null, tint = cs.onSurfaceVariant)
                        }
                    }
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(32.dp),
                            color = cs.primaryContainer
                        )
                    } else {
                        triviaQuestions.forEachIndexed { qi, q ->
                            Text(
                                "Q${qi + 1}. ${q.question}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = cs.onSurface,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            q.options.forEachIndexed { oi, opt ->
                                val selected = remember { mutableStateOf(false) }
                                val isCorrect = q.correctAnswer == oi
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selected.value && isCorrect) {
                                        cs.secondaryContainer.copy(alpha = 0.6f)
                                    } else if (selected.value) {
                                        cs.errorContainer.copy(alpha = 0.4f)
                                    } else {
                                        cs.surfaceContainerLow
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { selected.value = true }
                                ) {
                                    Text(
                                        opt,
                                        modifier = Modifier.padding(16.dp),
                                        fontSize = 15.sp,
                                        color = cs.onSurface
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        // ===== Loading overlay =====
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = cs.primaryContainer,
                    modifier = Modifier.size(48.dp),
                    trackColor = Color.Transparent
                )
            }
        }
    }
}

@Composable
private fun TourActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = cs.surfaceContainerLow,
        modifier = Modifier.height(40.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = cs.primary, modifier = Modifier.size(18.dp))
            Text(
                label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = cs.onSurface
            )
        }
    }
}
