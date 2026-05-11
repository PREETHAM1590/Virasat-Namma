package com.example.virasat.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.service.GeminiHeritageService
import com.example.virasat.data.service.TriviaQuestion
import com.example.virasat.data.source.StreetViewData
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.StreetViewPanoramaView
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

data class TalkingTourStop(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val bodyText: String,
    val highlight: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalkingToursScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember(context) { RepositoryProvider.getRepository(context) }
    val site by produceState<com.example.virasat.data.model.HeritageSite?>(null, siteId) {
        value = try { repo.getSiteById(siteId) } catch (_: Exception) { null }
    }

    val siteLatLng = remember(site) {
        site?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(15.3350, 76.4600)
    }

    var language by remember { mutableStateOf("English") }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }

    var currentStopIndex by remember { mutableIntStateOf(0) }
    var showUI by remember { mutableStateOf(true) }

    // Snapshot state
    var isSnapshotLoading by remember { mutableStateOf(false) }
    var showSnapshot by remember { mutableStateOf(false) }
    var snapshotText by remember { mutableStateOf("") }
    var snapshotQuestions by remember { mutableStateOf(listOf<TriviaQuestion>()) }
    var answeredQuestions by remember { mutableStateOf(setOf<Int>()) }

    val tourStops = remember(site) {
        val s = site ?: return@remember emptyList()
        buildList {
            add(TalkingTourStop("Welcome", Icons.Default.Tour, s.shortDescription, s.nameLocal.takeIf { it.isNotBlank() }))
            if (s.description.isNotBlank()) add(TalkingTourStop("About", Icons.Default.Info, s.description))
            if (s.history.isNotBlank()) add(TalkingTourStop("History", Icons.Default.HistoryEdu, s.history))
            if (s.architecture.isNotBlank()) add(TalkingTourStop("Architecture", Icons.Default.AccountBalance, s.architecture))
            if (s.legends.isNotBlank()) add(TalkingTourStop("Legends", Icons.Default.AutoStories, s.legends))
            s.facts.take(3).forEach { f ->
                add(TalkingTourStop("Did You Know?", Icons.Default.Lightbulb, f.description, f.title))
            }
        }
    }

    fun speakText(text: String, utteranceId: String = "stop") {
        if (!ttsReady) return
        isSpeaking = true
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun speakCurrentStop() {
        val stop = tourStops.getOrNull(currentStopIndex) ?: return
        val text = buildString {
            append(stop.title); append(". ")
            stop.highlight?.let { append(it); append(". ") }
            append(stop.bodyText)
        }
        speakText(text, "stop_$currentStopIndex")
    }

    // TTS init
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
                val loc = if (language == "Kannada") Locale("kn", "IN") else Locale.ENGLISH
                tts?.language = loc
                tts?.setSpeechRate(0.85f)
                tts?.setPitch(0.95f)
                tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) { isSpeaking = true }
                    override fun onDone(utteranceId: String?) { isSpeaking = false }
                    override fun onError(utteranceId: String?) { isSpeaking = false }
                })
            }
        }
    }

    LaunchedEffect(language) {
        if (ttsReady) {
            tts?.language = if (language == "Kannada") Locale("kn", "IN") else Locale.ENGLISH
        }
    }

    // Auto-narrate when stop changes and TTS is ready
    LaunchedEffect(currentStopIndex, ttsReady) {
        if (ttsReady && tourStops.isNotEmpty()) {
            delay(500)
            speakCurrentStop()
        }
    }

    DisposableEffect(Unit) {
        onDispose { tts?.stop(); tts?.shutdown() }
    }

    // Street View — use verified panorama ID when available, else fall back to coords
    val streetViewView = remember(context, siteId, siteLatLng) {
        val panoId = StreetViewData.panoramaIds[siteId]
        val options = if (panoId != null) {
            StreetViewPanoramaOptions().panoramaId(panoId)
        } else {
            StreetViewPanoramaOptions().position(siteLatLng, 5000)
        }
        StreetViewPanoramaView(context, options).apply {
            onCreate(null)
            onStart()
            onResume()   // full lifecycle so panorama loads immediately
            getStreetViewPanoramaAsync { p ->
                p.isStreetNamesEnabled = true
                p.isUserNavigationEnabled = true
                p.isZoomGesturesEnabled = true
                p.isPanningGesturesEnabled = true
            }
        }
    }

    if (site == null) {
        Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val s = site!!
    val cs = MaterialTheme.colorScheme
    val currentStop = tourStops.getOrNull(currentStopIndex)

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { if (!showSnapshot) showUI = !showUI }
    ) {
        // ======================================================
        // BACKGROUND: Street View fills the entire screen
        // ======================================================
        AndroidView(
            factory = { streetViewView },
            modifier = Modifier.fillMaxSize()
        )
        DisposableEffect(Unit) {
            onDispose { streetViewView.onPause(); streetViewView.onStop(); streetViewView.onDestroy() }
        }

        // ======================================================
        // TOP BAR
        // ======================================================
        AnimatedVisibility(
            visible = showUI,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(.5f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { tts?.stop(); onBack() }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                }

                // Site name pill
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.Black.copy(.5f),
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .padding(horizontal = 8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = s.name,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (s.nameLocal.isNotBlank()) {
                            Text(
                                text = s.nameLocal,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(.65f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Language toggle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(.5f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { language = if (language == "English") "Kannada" else "English" }
                ) {
                    Text(
                        if (language == "English") "EN" else "ಕ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }

        // ======================================================
        // STOP PROGRESS DOTS (below top bar)
        // ======================================================
        AnimatedVisibility(
            visible = showUI && tourStops.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 88.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tourStops.forEachIndexed { i, stop ->
                    val isActive = i == currentStopIndex
                    val isPassed = i < currentStopIndex
                    val dotWidth by animateDpAsState(
                        targetValue = if (isActive) 22.dp else 7.dp,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "dot_width_$i"
                    )
                    Box(
                        Modifier
                            .height(4.dp)
                            .width(dotWidth)
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                when {
                                    isActive -> cs.primary
                                    isPassed -> cs.primary.copy(.45f)
                                    else -> Color.White.copy(.28f)
                                }
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                currentStopIndex = i
                                answeredQuestions = emptySet()
                                showSnapshot = false
                                tts?.stop()
                            }
                    )
                }
            }
        }

        // ======================================================
        // BOTTOM NARRATION BAR + CONTROLS
        // ======================================================
        AnimatedVisibility(
            visible = showUI && currentStop != null,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val stop = tourStops.getOrNull(currentStopIndex)
            if (stop != null) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(.92f))
                            )
                        )
                        .navigationBarsPadding()
                        .padding(bottom = 20.dp, start = 22.dp, end = 22.dp, top = 48.dp)
                ) {
                    // Chapter label row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = stop.icon,
                            contentDescription = null,
                            tint = cs.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = stop.title.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.primary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "${currentStopIndex + 1} of ${tourStops.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(.45f)
                        )
                    }

                    Spacer(Modifier.height(5.dp))

                    // Title
                    Text(
                        text = stop.highlight ?: stop.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(3.dp))

                    // Body preview (2 lines)
                    Text(
                        text = stop.bodyText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(.68f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(18.dp))

                    // Controls row
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Prev
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (currentStopIndex > 0) Color.White.copy(.14f) else Color.White.copy(.05f)
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    enabled = currentStopIndex > 0
                                ) {
                                    currentStopIndex--
                                    answeredQuestions = emptySet()
                                    showSnapshot = false
                                    tts?.stop()
                                }
                        ) {
                            Icon(
                                Icons.Default.SkipPrevious, null,
                                tint = if (currentStopIndex > 0) Color.White else Color.White.copy(.3f),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Center cluster: waveform + SNAPSHOT + play-pause
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Waveform visualizer
                            AudioWaveform(isActive = isSpeaking, primaryColor = cs.primary)

                            // SNAPSHOT button (hero action)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(cs.primary)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        enabled = !isSnapshotLoading
                                    ) {
                                        isSnapshotLoading = true
                                        showSnapshot = false
                                        answeredQuestions = emptySet()
                                        tts?.stop()
                                        isSpeaking = false
                                        scope.launch {
                                            val focus = when (stop.title.lowercase()) {
                                                "history" -> "history"
                                                "architecture" -> "architecture"
                                                "legends" -> "legends"
                                                else -> "overview"
                                            }
                                            val aiText = GeminiHeritageService.describeView(s, focus, language)
                                            val questions = GeminiHeritageService.generateTrivia(s, 3, language)
                                            snapshotText = aiText.ifBlank { stop.bodyText.take(300) }
                                            snapshotQuestions = questions
                                            isSnapshotLoading = false
                                            showSnapshot = true
                                            speakText(snapshotText, "snapshot")
                                        }
                                    }
                            ) {
                                if (isSnapshotLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(26.dp),
                                        color = cs.onPrimary,
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.CameraAlt,
                                        "AI Snapshot",
                                        tint = cs.onPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            // Play / Pause
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(.14f))
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        if (isSpeaking) {
                                            tts?.stop()
                                            isSpeaking = false
                                        } else {
                                            speakCurrentStop()
                                        }
                                    }
                            ) {
                                Icon(
                                    if (isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Next
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (currentStopIndex < tourStops.size - 1) Color.White.copy(.14f) else Color.White.copy(.05f)
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    enabled = currentStopIndex < tourStops.size - 1
                                ) {
                                    currentStopIndex++
                                    answeredQuestions = emptySet()
                                    showSnapshot = false
                                    tts?.stop()
                                }
                        ) {
                            Icon(
                                Icons.Default.SkipNext, null,
                                tint = if (currentStopIndex < tourStops.size - 1) Color.White else Color.White.copy(.3f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Progress bar
                    val progress = (currentStopIndex + 1).toFloat() / tourStops.size.coerceAtLeast(1)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .clip(RoundedCornerShape(999.dp)),
                        color = cs.primary,
                        trackColor = Color.White.copy(.18f)
                    )
                }
            }
        }

        // ======================================================
        // SNAPSHOT OVERLAY (slides up from bottom)
        // ======================================================
        AnimatedVisibility(
            visible = showSnapshot,
            enter = slideInVertically { it } + fadeIn(tween(300)),
            exit = slideOutVertically { it } + fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            SnapshotOverlay(
                snapshotText = snapshotText,
                questions = snapshotQuestions,
                answeredQuestions = answeredQuestions,
                onAnswerQuestion = { idx -> answeredQuestions = answeredQuestions + idx },
                onClose = {
                    showSnapshot = false
                    tts?.stop()
                    isSpeaking = false
                },
                cs = cs
            )
        }

        // Snapshot hint label (above snapshot button when overlay is closed)
        AnimatedVisibility(
            visible = showUI && !showSnapshot && !isSnapshotLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 148.dp)
        ) {
            Text(
                text = "Tap to get AI insights",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(.45f),
                fontSize = 10.sp
            )
        }
    }
}

// ======================================================
// AUDIO WAVEFORM VISUALIZER
// ======================================================
@Composable
private fun AudioWaveform(isActive: Boolean, primaryColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val peakHeights = listOf(10f, 20f, 8f, 22f, 12f)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier
            .height(28.dp)
            .padding(horizontal = 2.dp)
    ) {
        peakHeights.forEachIndexed { i, peak ->
            val animHeight by infiniteTransition.animateFloat(
                initialValue = 4f,
                targetValue = peak,
                animationSpec = infiniteRepeatable(
                    animation = tween(420 + i * 70, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(i * 90)
                ),
                label = "bar_$i"
            )
            Box(
                Modifier
                    .width(3.dp)
                    .height((if (isActive) animHeight else 4f).dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (isActive) primaryColor else Color.White.copy(.28f))
            )
        }
    }
}

// ======================================================
// SNAPSHOT OVERLAY SHEET
// ======================================================
@Composable
private fun SnapshotOverlay(
    snapshotText: String,
    questions: List<TriviaQuestion>,
    answeredQuestions: Set<Int>,
    onAnswerQuestion: (Int) -> Unit,
    onClose: () -> Unit,
    cs: ColorScheme
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.68f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = Color(0xFF0E0E0E).copy(.97f)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // Drag handle
            Box(
                Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(.2f))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            // Header
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = cs.primaryContainer,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                null,
                                tint = cs.onPrimaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            "AI Snapshot",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Powered by Gemini",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(.45f)
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(.08f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onClose() }
                ) {
                    Icon(Icons.Default.Close, "Close", tint = Color.White.copy(.7f), modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(.08f))
            Spacer(Modifier.height(16.dp))

            // AI description
            Text(
                text = snapshotText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(.85f),
                lineHeight = 23.sp
            )

            if (questions.isNotEmpty()) {
                Spacer(Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Quiz, null, tint = cs.primary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Questions to Consider",
                        style = MaterialTheme.typography.labelLarge,
                        color = cs.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(Modifier.height(12.dp))
                questions.forEachIndexed { idx, q ->
                    SnapshotQuestionCard(
                        question = q,
                        isAnswered = idx in answeredQuestions,
                        onReveal = { onAnswerQuestion(idx) },
                        cs = cs
                    )
                    if (idx < questions.size - 1) Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ======================================================
// INDIVIDUAL QUESTION CARD
// ======================================================
@Composable
private fun SnapshotQuestionCard(
    question: TriviaQuestion,
    isAnswered: Boolean,
    onReveal: () -> Unit,
    cs: ColorScheme
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(.05f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(Modifier.height(10.dp))
            if (!isAnswered) {
                // Options — tap any to reveal
                question.options.take(4).forEach { option ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(.07f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onReveal() }
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(.78f),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "Tap any option to reveal answer",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(.3f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                // Revealed with correct answer highlighted
                question.options.forEachIndexed { i, option ->
                    val isCorrect = i == question.correctAnswer
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCorrect) Color(0xFF4CAF50).copy(.15f) else Color.White.copy(.04f),
                        border = if (isCorrect) BorderStroke(1.dp, Color(0xFF4CAF50).copy(.4f)) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCorrect) Color(0xFF81C784) else Color.White.copy(.42f),
                                modifier = Modifier.weight(1f)
                            )
                            if (isCorrect) {
                                Icon(
                                    Icons.Default.Check,
                                    null,
                                    tint = Color(0xFF81C784),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
                if (question.explanation.isNotBlank()) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(.55f),
                        fontStyle = FontStyle.Italic,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
