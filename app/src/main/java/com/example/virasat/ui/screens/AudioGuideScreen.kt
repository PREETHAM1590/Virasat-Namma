package com.example.virasat.ui.screens

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.model.AudioChapter
import com.example.virasat.data.service.GeminiHeritageService
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.util.LocaleHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun AudioGuideScreen(
    siteId: String,
    siteName: String,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    val savedLocale = LocaleHelper.getSavedLocale(ctx)
    var currentChapter by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var selectedLanguage by remember { mutableStateOf(when(savedLocale) { "kn" -> "Kannada"; "hi" -> "Hindi"; else -> "English" }) }
    var generatedNarration by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val site by produceState<com.example.virasat.data.model.HeritageSite?>(null, siteId) {
        value = try {
            repo.getSiteById(siteId) ?: KarnatakaSites.allSites.find { it.id == siteId }
        } catch (_: Exception) {
            KarnatakaSites.allSites.find { it.id == siteId }
        }
    }

    val chapters = remember(siteId, generatedNarration, site, ctx) {
        val intro = site?.shortDescription ?: ""
        val narration = generatedNarration.ifBlank { intro }
        fun estimateDuration(text: String): Int = (text.split(" ").size.coerceAtLeast(20) * 60 / 130).coerceAtLeast(30)
        listOf(
            AudioChapter("1", ctx.getString(com.example.virasat.R.string.audio_chapter_introduction), estimateDuration(narration), "", narration),
            AudioChapter("2", ctx.getString(com.example.virasat.R.string.audio_chapter_history), estimateDuration(site?.history ?: ""), "", site?.history ?: ""),
            AudioChapter("3", ctx.getString(com.example.virasat.R.string.audio_chapter_architecture), estimateDuration(site?.architecture ?: ""), "", site?.architecture ?: ""),
            AudioChapter("4", ctx.getString(com.example.virasat.R.string.audio_chapter_legends), estimateDuration(site?.legends ?: ""), "", site?.legends ?: ""),
            AudioChapter("5", ctx.getString(com.example.virasat.R.string.audio_chapter_facts), 60, "", site?.facts?.take(3)?.joinToString("\n") { "${it.title}: ${it.description}" } ?: "")
        ).filter { it.transcript.isNotBlank() }
    }
    val safeChapterIdx = currentChapter.coerceIn(0, (chapters.size - 1).coerceAtLeast(0))
    val chapter = chapters.getOrNull(safeChapterIdx)

    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    fun setTtsLanguage() {
        val locale = when (selectedLanguage) {
            "Kannada" -> Locale("kn", "IN")
            "Hindi" -> Locale("hi", "IN")
            "Tamil" -> Locale("ta", "IN")
            "Telugu" -> Locale("te", "IN")
            else -> Locale.ENGLISH
        }
        val result = tts?.setLanguage(locale)
        // Fallback to English if language not supported by device TTS engine
        if (result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED ||
            result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA) {
            tts?.setLanguage(Locale.ENGLISH)
        }
    }

    var pendingStart by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
                setTtsLanguage()
                tts?.setSpeechRate(0.88f)
                tts?.setPitch(0.95f)
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) { isPlaying = true }
                    override fun onDone(utteranceId: String?) {
                        isPlaying = false
                        progress = 0f
                        if (currentChapter < chapters.lastIndex) {
                            currentChapter++
                        }
                    }
                    override fun onError(utteranceId: String?) { isPlaying = false }
                })
                if (pendingStart) {
                    pendingStart = false
                    tts?.speak(chapters[currentChapter].transcript, TextToSpeech.QUEUE_FLUSH, null, "chapter_${currentChapter}")
                }
            }
        }
    }

    LaunchedEffect(selectedLanguage) {
        if (ttsReady) setTtsLanguage()
        if (GeminiHeritageService.isInitialized()) {
            isGenerating = true
            generatedNarration = GeminiHeritageService.generateNarration(site, selectedLanguage)
            isGenerating = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    LaunchedEffect(currentChapter) {
        progress = 0f
        tts?.stop()
        if (ttsReady && chapters.isNotEmpty()) {
            val idx = currentChapter.coerceIn(0, chapters.lastIndex)
            val transcript = chapters[idx].transcript
            if (transcript.isNotBlank() && isPlaying) {
                tts?.speak(transcript, TextToSpeech.QUEUE_FLUSH, null, "chapter_$idx")
            }
        }
    }

    LaunchedEffect(isPlaying, currentChapter) {
        if (isPlaying) {
            val durationMs = ((chapter?.durationSeconds ?: 60).toLong() * 1000L).coerceAtLeast(5000L)
            val stepMs = 200L
            val stepSize = stepMs.toFloat() / durationMs.toFloat()
            while (isPlaying && progress < 1f) {
                delay(stepMs)
                progress = (progress + stepSize).coerceAtMost(1f)
            }
            if (progress >= 1f) progress = 0f
        }
    }

    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE2EFE1))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top nav bar
            Row(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Back",
                        tint = cs.onSurface,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        siteName,
                        style = type.headlineMedium,
                        color = cs.onSurface,
                        maxLines = 1
                    )
                }
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = cs.primaryContainer,
                    onClick = {
                        selectedLanguage = if (selectedLanguage == "English") "Kannada" else "English"
                    }
                ) {
                    Text(
                        if (selectedLanguage == "English") "EN" else "ಕ",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = cs.onPrimaryContainer
                    )
                }
            }

            // Hero Section: Massive Circle with floating controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .aspectRatio(1f)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = site?.imageUrl ?: "https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=800",
                            contentDescription = siteName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        // Neumorphic inner shadow overlay
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.2f)))
                    }

                    Spacer(Modifier.height(24.dp))

                    // Floating Playback Controls
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color(0xFF0B2211))
                            .padding(24.dp)
                    ) {
                        Column {
                            // Time + Title row
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    formatTime((progress * (chapter?.durationSeconds ?: 60).toFloat()).toInt()),
                                    style = type.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    siteName,
                                    style = type.headlineMedium,
                                    color = Color.White
                                )
                                Text(
                                    formatTime(chapter?.durationSeconds ?: 60),
                                    style = type.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            // Progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(cs.primaryContainer)
                                )
                            }
                            Spacer(Modifier.height(20.dp))
                            // Controls
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        if (currentChapter > 0) {
                                            tts?.stop()
                                            progress = 0f
                                            currentChapter--
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Replay10,
                                        "Rewind",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color(0xFFE2EFE1)
                                    )
                                }
                                Button(
                                    onClick = {
                                        isPlaying = !isPlaying
                                        if (isPlaying) {
                                            if (ttsReady) {
                                                tts?.speak(chapter?.transcript ?: "", TextToSpeech.QUEUE_FLUSH, null, "chapter_$currentChapter")
                                            } else {
                                                pendingStart = true
                                            }
                                        } else {
                                            tts?.stop()
                                        }
                                    },
                                    modifier = Modifier.size(64.dp),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = cs.primaryContainer
                                    )
                                ) {
                                    Icon(
                                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        if (isPlaying) "Pause" else "Play",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color(0xFF0B2211)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        if (currentChapter < chapters.lastIndex) {
                                            currentChapter++
                                            progress = 0f
                                            isPlaying = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Forward10,
                                        "Forward",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color(0xFFE2EFE1)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Chapter Cards
            Spacer(Modifier.height(32.dp))
            Text(
                stringResource(com.example.virasat.R.string.audio_chapters),
                style = type.headlineMedium,
                color = cs.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                chapters.forEachIndexed { index, ch ->
                    val isCurrent = index == currentChapter
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cs.surfaceContainerLowest)
                            .clickable {
                                tts?.stop()
                                currentChapter = index
                                progress = 0f
                                isPlaying = true
                                if (ttsReady) {
                                    tts?.speak(ch.transcript, TextToSpeech.QUEUE_FLUSH, null, "chapter_$index")
                                } else {
                                    pendingStart = true
                                }
                            }
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (isCurrent) {
                                // Active card: left accent bar within card
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(cs.primaryContainer)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(cs.primaryContainer.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        @Suppress("DEPRECATION")
                                    Icons.Default.VolumeUp,
                                        null,
                                        modifier = Modifier.size(24.dp),
                                        tint = cs.primaryContainer
                                    )
                                }
                            } else {
                                Spacer(Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(cs.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        String.format("%02d", index + 1),
                                        style = type.labelMedium,
                                        color = cs.onSurfaceVariant
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    ch.title,
                                    style = type.bodyLarge,
                                    color = cs.onSurface,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    ch.transcript.take(50) + "...",
                                    style = type.bodyMedium,
                                    color = cs.onSurfaceVariant
                                )
                            }
                            Text(
                                if (isCurrent) stringResource(com.example.virasat.R.string.audio_now_playing) else formatTime(ch.durationSeconds),
                                style = type.labelMedium,
                                color = cs.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}
