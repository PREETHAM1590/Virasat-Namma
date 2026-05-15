package com.example.virasat.ui.screens

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val CHAPTER_KEYS = listOf("introduction", "history", "architecture", "legends", "facts")

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
    var selectedLanguage by remember {
        mutableStateOf(when (savedLocale) { "kn" -> "Kannada"; "hi" -> "Hindi"; else -> "English" })
    }
    // Gemini transcripts: chapterKey -> generated text
    var geminiTranscripts by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isGenerating by remember { mutableStateOf(false) }
    var generatingChapter by remember { mutableStateOf("") }
    // TTS state
    var ttsUnsupported by remember { mutableStateOf(false) }
    var showTtsHint by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val site by produceState<com.example.virasat.data.model.HeritageSite?>(null, siteId) {
        value = try {
            repo.getSiteById(siteId) ?: KarnatakaSites.allSites.find { it.id == siteId }
        } catch (_: Exception) {
            KarnatakaSites.allSites.find { it.id == siteId }
        }
    }

    fun estimateDuration(text: String): Int =
        (text.split(" ").size.coerceAtLeast(20) * 60 / 130).coerceAtLeast(30)

    // Chapters: use Gemini transcripts if available, else site data
    val chapters = remember(siteId, geminiTranscripts, site, ctx) {
        fun transcript(key: String, fallback: String): String =
            geminiTranscripts[key]?.ifBlank { fallback } ?: fallback
        val introText = transcript("introduction", site?.shortDescription ?: "")
        val historyText = transcript("history", site?.history ?: "")
        val archText = transcript("architecture", site?.architecture ?: "")
        val legendsText = transcript("legends", site?.legends ?: "")
        val factsText = transcript("facts",
            site?.facts?.take(3)?.joinToString("\n") { "${it.title}: ${it.description}" } ?: "")
        listOf(
            AudioChapter("1", ctx.getString(com.example.virasat.R.string.audio_chapter_introduction),
                estimateDuration(introText), "", introText),
            AudioChapter("2", ctx.getString(com.example.virasat.R.string.audio_chapter_history),
                estimateDuration(historyText), "", historyText),
            AudioChapter("3", ctx.getString(com.example.virasat.R.string.audio_chapter_architecture),
                estimateDuration(archText), "", archText),
            AudioChapter("4", ctx.getString(com.example.virasat.R.string.audio_chapter_legends),
                estimateDuration(legendsText), "", legendsText),
            AudioChapter("5", ctx.getString(com.example.virasat.R.string.audio_chapter_facts),
                estimateDuration(factsText), "", factsText)
        ).filter { it.transcript.isNotBlank() }
    }
    val safeChapterIdx = currentChapter.coerceIn(0, (chapters.size - 1).coerceAtLeast(0))
    val chapter = chapters.getOrNull(safeChapterIdx)

    val context = LocalContext.current
    val mainHandler = remember { Handler(Looper.getMainLooper()) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    fun setTtsLanguage(): Boolean {
        val locale = when (selectedLanguage) {
            "Kannada" -> Locale("kn", "IN")
            "Hindi"   -> Locale("hi", "IN")
            "Tamil"   -> Locale("ta", "IN")
            "Telugu"  -> Locale("te", "IN")
            else      -> Locale.ENGLISH
        }
        val result = tts?.setLanguage(locale)
        return if (result == TextToSpeech.LANG_NOT_SUPPORTED ||
                   result == TextToSpeech.LANG_MISSING_DATA) {
            tts?.setLanguage(Locale.ENGLISH)  // fallback so speech still works
            true  // unsupported
        } else false
    }

    var pendingStart by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (tts != null) return@LaunchedEffect
        // Use a holder so the init callback can reference the instance safely
        // (capturing `tts` state directly can race; holder is set before callback fires)
        val holder = arrayOfNulls<TextToSpeech>(1)
        val engine = TextToSpeech(context) { status ->
            val t = holder[0] ?: return@TextToSpeech
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
                val unsupported = setTtsLanguage()
                if (unsupported && selectedLanguage != "English") {
                    ttsUnsupported = true
                    showTtsHint = true
                }
                t.setSpeechRate(0.88f)
                t.setPitch(0.95f)
                // Callbacks fire on TTS thread — post to main to safely mutate Compose state (#3)
                t.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        mainHandler.post { if (chapters.isNotEmpty()) isPlaying = true }
                    }
                    override fun onDone(utteranceId: String?) {
                        mainHandler.post {
                            if (chapters.isEmpty()) return@post
                            isPlaying = false
                            progress = 0f
                            if (currentChapter < chapters.lastIndex) currentChapter++
                        }
                    }
                    override fun onError(utteranceId: String?) {
                        mainHandler.post { isPlaying = false }
                    }
                })
                if (pendingStart && chapters.isNotEmpty()) {
                    pendingStart = false
                    t.speak(chapters.getOrNull(currentChapter)?.transcript ?: "",
                        TextToSpeech.QUEUE_FLUSH, null, "chapter_$currentChapter")
                }
            }
        }
        holder[0] = engine
        tts = engine
    }

    // When TTS becomes ready and chapters are available, auto-play if pendingStart
    LaunchedEffect(ttsReady, chapters.size) {
        if (ttsReady && pendingStart && chapters.isNotEmpty()) {
            pendingStart = false
            tts?.speak(
                chapters.getOrNull(currentChapter)?.transcript ?: "",
                android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, "chapter_$currentChapter"
            )
        }
    }

    // Show snackbar when TTS language unsupported
    LaunchedEffect(showTtsHint) {
        if (showTtsHint) {
            val msg = if (selectedLanguage == "Kannada")
                "\u0c95\u0ca8\u0ccd\u0ca8\u0ca1 \u0ca7\u0ccd\u0cb5\u0ca8\u0cbf \u0ca1\u0cc7\u0c9f\u0cbe \u0cbf\u0cb2\u0ccd\u0cb2 \u2014 \u0c87\u0c82\u0c97\u0ccd\u0cb2\u0cbf\u0cb7\u0ccd \u0ca7\u0ccd\u0cb5\u0ca8\u0cbf \u0cac\u0cb3\u0cb8\u0cb2\u0cbe\u0c97\u0cc1\u0ca4\u0ccd\u0ca4\u0cbf\u0ca6\u0cc6. \u0cb8\u0cc6\u0c9f\u0ccd\u0c9f\u0cbf\u0c82\u0c97\u0ccd\u0cb8\u0ccd \u0ca8\u0cb2\u0ccd\u0cb2\u0cbf Kannada TTS \u0ca1\u0ccc\u0ca8\u0ccd\u0cb2\u0ccb\u0ca1\u0ccd \u0cae\u0cbe\u0ca1\u0cbf."
                else "Kannada voice not installed on this device — using English TTS. Download Kannada TTS in Settings."
            val result = snackbarHostState.showSnackbar(
                message = msg,
                actionLabel = "Settings",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                ctx.startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
            showTtsHint = false
        }
    }

    // When language changes: update TTS locale + fetch all chapter narrations from Gemini
    // TTS locale update is guarded by ttsReady; Gemini fetch is independent — no guard needed (#22)
    LaunchedEffect(selectedLanguage, site) {
        if (site == null) return@LaunchedEffect
        if (ttsReady) {
            val unsupported = setTtsLanguage()
            if (unsupported && selectedLanguage != "English") {
                ttsUnsupported = true; showTtsHint = true
            } else {
                ttsUnsupported = false
            }
        }
        // Gemini transcript fetch happens regardless of ttsReady so language switch
        // doesn't silently skip when TTS init hasn't finished yet.
        if (!GeminiHeritageService.isInitialized()) return@LaunchedEffect
        isGenerating = true
        generatingChapter = "all"
        // Fetch all 5 chapter narrations in parallel — reduces wait from ~25s to ~5s
        val newTranscripts = coroutineScope {
            CHAPTER_KEYS
                .map { key -> async { key to GeminiHeritageService.generateChapterNarration(site, key, selectedLanguage) } }
                .awaitAll()
                .filter { (_, text) -> text.isNotBlank() }
                .toMap()
        }
        geminiTranscripts = newTranscripts
        isGenerating = false
        generatingChapter = ""
    }

    DisposableEffect(Unit) {
        onDispose {
            val old = tts
            tts = null
            ttsReady = false
            old?.stop()
            old?.shutdown()
        }
    }

    LaunchedEffect(currentChapter) {
        progress = 0f
        tts?.stop()
        if (ttsReady && chapters.isNotEmpty() && isPlaying) {
            val idx = currentChapter.coerceIn(0, chapters.lastIndex)
            val transcript = chapters[idx].transcript
            if (transcript.isNotBlank()) {
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .padding(paddingValues)
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
                        selectedLanguage = when (selectedLanguage) {
                            "English" -> "Kannada"
                            "Kannada" -> "Hindi"
                            else -> "English"
                        }
                    }
                ) {
                    Text(
                        when (selectedLanguage) { "English" -> "EN"; "Hindi" -> "HI"; else -> "\u0c95" },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = cs.onPrimaryContainer
                    )
                }
            }

            // Gemini generating status bar
            if (isGenerating) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cs.secondaryContainer)
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = cs.onSecondaryContainer,
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = if (selectedLanguage == "Kannada")
                            "\u0c97\u0cc6\u0cae\u0cbf\u0ca8\u0cbf AI \u0cb5\u0cbf\u0cb5\u0cb0\u0ca3 \u0ca4\u0caf\u0cbe\u0cb0\u0cbf\u0cb8\u0cc1\u0ca4\u0ccd\u0ca4\u0cbf\u0ca6\u0cc6..."
                            else "Gemini AI generating audio descriptions...",
                        style = type.labelMedium,
                        color = cs.onSecondaryContainer
                    )
                }
            }

            // TTS unsupported warning banner
            if (ttsUnsupported && selectedLanguage != "English") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cs.errorContainer)
                        .clickable {
                            ctx.startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            })
                        }
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        null,
                        tint = cs.onErrorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (selectedLanguage == "Kannada")
                            "\u0c95\u0ca8\u0ccd\u0ca8\u0ca1 TTS \u0cac\u0cc7\u0c95\u0cbe\u0c97\u0cc1\u0ca4\u0ccd\u0ca4\u0cc6 \u2014 Settings \u0ca4\u0cc6\u0cb0\u0cc6\u0caf\u0cb2\u0cc1 \u0c92\u0ca4\u0ccd\u0ccd\u0ccc\u0ca6\u0cbf \u0c95\u0ccd\u0cb2\u0cbf\u0c95\u0ccd \u0cae\u0cbe\u0ca1\u0cbf"
                            else "Kannada voice not installed — tap to open Settings and download Kannada TTS",
                        style = type.labelSmall,
                        color = cs.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Hero Section
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
                            model = site?.imageUrl
                                ?: "https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=800",
                            contentDescription = siteName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.2f))
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // Playback Controls
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(cs.inverseSurface)
                            .padding(24.dp)
                    ) {
                        Column {
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
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        siteName,
                                        style = type.titleMedium,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                    Text(
                                        chapter?.title ?: "",
                                        style = type.labelSmall,
                                        color = Color.White.copy(alpha = 0.7f),
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    formatTime(chapter?.durationSeconds ?: 60),
                                    style = type.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Spacer(Modifier.height(16.dp))
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
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    if (currentChapter > 0) {
                                        tts?.stop(); progress = 0f; currentChapter--
                                    }
                                }) {
                                    Icon(Icons.Default.Replay10, "Rewind",
                                        modifier = Modifier.size(32.dp), tint = cs.inverseOnSurface)
                                }
                                Button(
                                    onClick = {
                                        if (chapters.isEmpty()) return@Button
                                        isPlaying = !isPlaying
                                        if (isPlaying) {
                                            if (ttsReady) {
                                                tts?.speak(chapter?.transcript ?: "",
                                                    TextToSpeech.QUEUE_FLUSH, null, "chapter_$currentChapter")
                                            } else { pendingStart = true }
                                        } else { tts?.stop() }
                                    },
                                    modifier = Modifier.size(64.dp),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = cs.primaryContainer)
                                ) {
                                    Icon(
                                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        if (isPlaying) "Pause" else "Play",
                                        modifier = Modifier.size(32.dp),
                                        tint = cs.onPrimaryContainer
                                    )
                                }
                                IconButton(onClick = {
                                    if (currentChapter < chapters.lastIndex) {
                                        currentChapter++; progress = 0f; isPlaying = false
                                    }
                                }) {
                                    Icon(Icons.Default.Forward10, "Forward",
                                        modifier = Modifier.size(32.dp), tint = cs.inverseOnSurface)
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
                    val chKey = CHAPTER_KEYS.getOrNull(index) ?: ""
                    val chGenerating = isGenerating && generatingChapter == chKey
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cs.surfaceContainerLowest)
                            .clickable {
                                if (chapters.isEmpty()) return@clickable
                                tts?.stop()
                                currentChapter = index
                                progress = 0f
                                isPlaying = true
                                if (ttsReady) {
                                    tts?.speak(ch.transcript, TextToSpeech.QUEUE_FLUSH, null, "chapter_$index")
                                } else { pendingStart = true }
                            }
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp).height(48.dp)
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(cs.primaryContainer)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(48.dp).clip(CircleShape)
                                        .background(cs.primaryContainer.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        @Suppress("DEPRECATION")
                                        Icons.Default.VolumeUp, null,
                                        modifier = Modifier.size(24.dp),
                                        tint = cs.primaryContainer
                                    )
                                }
                            } else {
                                Spacer(Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(48.dp).clip(CircleShape)
                                        .background(cs.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (chGenerating) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = cs.primary,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Text(
                                            String.format("%02d", index + 1),
                                            style = type.labelMedium,
                                            color = cs.onSurfaceVariant
                                        )
                                    }
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
                                    if (chGenerating) (if (selectedLanguage == "Kannada") "\u0c97\u0cc6\u0cae\u0cbf\u0ca8\u0cbf \u0ca4\u0caf\u0cbe\u0cb0\u0cbf\u0cb8\u0cc1\u0ca4\u0ccd\u0ca4\u0cbf\u0ca6\u0cc6..." else "Generating with Gemini AI...")
                                    else ch.transcript.take(55) + "\u2026",
                                    style = type.bodyMedium,
                                    color = cs.onSurfaceVariant
                                )
                            }
                            Text(
                                if (isCurrent) stringResource(com.example.virasat.R.string.audio_now_playing)
                                else formatTime(ch.durationSeconds),
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
    } // end Scaffold
}

fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}
