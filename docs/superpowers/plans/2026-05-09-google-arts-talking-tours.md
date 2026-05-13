# Google Arts-Style Talking Tours Rewrite

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:executing-plans` to implement task-by-task.

**Goal:** Rewrite `TalkingToursScreen` to match the Google Arts & Culture AI Audio Guide pattern: user navigates immersive photo views and taps a Snapshot button to get AI-generated narration + 3 curiosity questions per view.

**Architecture:** Replace auto-playing narration with user-triggered "Snapshot" flow. Each photo becomes a "view" Gemini can describe in context. Bottom panel replaces quiz sheet with narration + curiosity chips. Dark immersive theme throughout.

**Tech Stack:** Jetpack Compose, Material 3, google-genai, Android TTS, Coil

---

## File Structure

| File | Action | Purpose |
|------|--------|---------|
| `TalkingToursScreen.kt` | Modify full rewrite | New immersive Snapshot pattern, dark theme |
| `GeminiHeritageService.kt` | Add methods | `generateSnapshotNarration()`, `generateCuriosityQuestions()` |

---

## Data Model Changes

`ImageTourStop` gains a `viewLabel` field that describes what the image depicts (this becomes Gemini's prompt context):

```kotlin
private data class ImageTourStop(
    val imageUrl: String,
    val viewLabel: String,   // NEW: what the user sees, e.g. "Stone Chariot"
    val title: String
)
```

`stopData` maps each image to a specific view label per site. Hampi example:

```kotlin
val images = mutableListOf<GalleryImage>().apply {
    add(GalleryImage(site.imageUrl, "Overview"))
    site.galleryImages?.forEachIndexed { i, url ->
        val label = when (i) {
            0 -> "Stone Chariot"       // Cave 1 facade
            1 -> "Musical Pillars"     // Cave interior
            2 -> "Virupaksha Temple"   // Temple tower view
            else -> "Heritage"
        }
        add(GalleryImage(url, label))
    }
}
```

---

## Task 1: Add Gemini Methods for Snapshot Flow

**Files:**
- Modify: `GeminiHeritageService.kt:63-end`

**[ ] Step 1.1: Add `generateSnapshotNarration()`**

Insert after `generateTrivia()` method (after line 76). This method takes a `viewLabel` (what image shows) and generates contextual narration:

```kotlin
suspend fun generateSnapshotNarration(
    siteId: String,
    viewLabel: String,
    language: String = "English"
): String = withContext(Dispatchers.IO) {
    if (!isInitialized()) return@withContext "Take a closer look at the intricate details in this view."
    try {
        val site = KarnatakaSites.allSites.find { it.id == siteId }
        val context = getSiteContext(siteId)
        val prompt = """You are an expert heritage guide at ${site?.name ?: "a Karnataka heritage site"}. The visitor is looking at this specific view: "$viewLabel".

Site context:
$context

In a warm, conversational tone in $language, describe what the visitor is seeing in 3-4 sentences. Point out one fascinating detail they might miss. Keep it to about 30 seconds of spoken time. Start with an engaging observation."""
        val response = client!!.models.generateContent("gemini-2.5-flash-preview", prompt, null)
        response.text() ?: "Notice the remarkable details captured in this heritage view."
    } catch (e: Exception) {
        "Each view here holds centuries of stories waiting to be discovered."
    }
}
```

**[ ] Step 1.2: Add `generateCuriosityQuestions()`**

Insert after the narration method. Returns 3 curiosity-driving questions tied to the view:

```kotlin
suspend fun generateCuriosityQuestions(
    siteId: String,
    viewLabel: String,
    language: String = "English"
): List<String> = withContext(Dispatchers.IO) {
    if (!isInitialized()) return@withContext curiosityFallback(siteId)
    try {
        val context = getSiteContext(siteId)
        val prompt = """You are a curious heritage guide at $viewLabel. The visitor has just heard about this view.

Site context:
$context

Generate exactly 3 short, thought-provoking curiosity questions in $language that encourage the visitor to look closer at this view. Each question should be 1 sentence, no longer than 15 words. Return as a plain numbered list, one per line. No markdown, no quotes."""
        val response = client!!.models.generateContent("gemini-2.5-flash-preview", prompt, null)
        val text = response.text() ?: return@withContext curiosityFallback(siteId)
        text.lines()
            .map { it.replace(Regex("^\\d+[.)]\\s*"), "").trim() }
            .filter { it.isNotBlank() && it.endsWith("?") }
            .take(3)
    } catch (e: Exception) {
        curiosityFallback(siteId)
    }
}

private fun curiosityFallback(siteId: String): List<String> {
    val site = KarnatakaSites.allSites.find { it.id == siteId }
    return listOf(
        "What stories do these stone walls hold?",
        "Can you spot the craftsmanship detail in this view?",
        "How many generations walked this very spot?"
    )
}
```

**[ ] Step 1.3: Add helper data class `GalleryImage`**

No new file needed; use pair or local class. In `GeminiHeritageService.kt`, keep `ImageTourStop` inside `TalkingToursScreen.kt` as before but rename + add field.

---

## Task 2: Full TalkingToursScreen Rewrite

**Files:**
- Modify: `TalkingToursScreen.kt` (entire file, ~770 lines)

**[ ] Step 2.1: Replace imports and data model**

Top of file becomes:

```kotlin
package com.example.virasat.ui.screens

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.service.GeminiHeritageService
import com.example.virasat.data.source.KarnatakaSites
import kotlinx.coroutines.launch
import java.util.Locale

private data class ImageTourStop(val imageUrl: String, val viewLabel: String, val title: String)
```

**[ ] Step 2.2: Rewrite stateful Composable**

Replace full `TalkingToursScreen` function. New signature and state:

```kotlin
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TalkingToursScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val site = KarnatakaSites.allSites.find { it.id == siteId }
    val scope = rememberCoroutineScope()

    // === Stops with view labels for Gemini context ===
    val stopData = remember(siteId) {
        buildStopData(site)
    }
    val pagerState = rememberPagerState(pageCount = { stopData.size })

    var language by remember { mutableStateOf("English") }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    // Snapshot state
    var isSnapping by remember { mutableStateOf(false) }
    var snapshotNarration by remember { mutableStateOf("") }
    var curiosityQuestions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoadingSnapshot by remember { mutableStateOf(false) }
    var showSnapshotPanel by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }

    // TTS init
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
                tts?.language = if (language == "Kannada") Locale("kn", "IN") else Locale.ENGLISH
                tts?.setSpeechRate(0.85f)
            }
        }
    }

    LaunchedEffect(language) {
        if (ttsReady) {
            tts?.language = if (language == "Kannada") Locale("kn", "IN") else Locale.ENGLISH
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    // Utterance listener for isSpeaking
    DisposableEffect(tts) {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(id: String?) { isSpeaking = true }
            override fun onDone(id: String?) { isSpeaking = false }
            override fun onError(id: String?) { isSpeaking = false }
        })
        onDispose { tts?.setOnUtteranceProgressListener(null) }
    }

    fun takeSnapshot() {
        val currentStop = stopData.getOrNull(pagerState.currentPage) ?: return
        isLoadingSnapshot = true
        showSnapshotPanel = true
        isSnapping = true
        snapshotNarration = ""
        curiosityQuestions = emptyList()
        scope.launch {
            val narration = GeminiHeritageService.generateSnapshotNarration(
                siteId, currentStop.viewLabel, language
            )
            snapshotNarration = narration
            val questions = GeminiHeritageService.generateCuriosityQuestions(
                siteId, currentStop.viewLabel, language
            )
            curiosityQuestions = questions
            isLoadingSnapshot = false
            // Auto-speak if TTS ready
            if (ttsReady) {
                tts?.speak(narration, TextToSpeech.QUEUE_FLUSH, null, "snapshot_${pagerState.currentPage}")
            }
        }
    }

    fun toggleSpeech() {
        if (isSpeaking) {
            tts?.stop()
            isSpeaking = false
        } else if (snapshotNarration.isNotBlank() && ttsReady) {
            tts?.speak(snapshotNarration, TextToSpeech.QUEUE_FLUSH, null, "snapshot_speak")
        }
    }

    val cs = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // === Full-bleed immersive gallery ===
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val stop = stopData[page]
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = stop.imageUrl,
                    contentDescription = stop.viewLabel,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Dark overlay for readability when panel shows
                val overlayAlpha by animateFloatAsState(
                    targetValue = if (showSnapshotPanel) 0.6f else 0.2f,
                    label = "overlay"
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = overlayAlpha * 0.4f),
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = overlayAlpha),
                                    Color.Black.copy(alpha = overlayAlpha * 1.3f)
                                )
                            )
                        )
                )
            }
        }

        // === Top Bar ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(44.dp)
                    .clickable { tts?.stop(); onBack() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Back",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            site?.let {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            it.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            it.district,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.clickable {
                    language = if (language == "English") "Kannada" else "English"
                }
            ) {
                Text(
                    if (language == "English") "EN" else "K",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }

        // === Carousel dots ===
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (showSnapshotPanel) 340.dp else 100.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            stopData.forEachIndexed { index, _ ->
                val active = index == pagerState.currentPage
                val animWidth by animateDpAsState(
                    targetValue = if (active) 24.dp else 6.dp,
                    label = "dot_$index"
                )
                Box(
                    modifier = Modifier
                        .width(animWidth)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(
                            if (active) Color.White else Color.White.copy(alpha = 0.3f)
                        )
                )
            }
        }

        // === Prev/Next arrows ===
        if (pagerState.currentPage > 0) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.4f),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp)
                    .size(40.dp)
                    .clickable {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        if (pagerState.currentPage < stopData.lastIndex) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.4f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .size(40.dp)
                    .clickable {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // === Snapshot FAB (center-bottom when no panel) ===
        AnimatedVisibility(
            visible = !showSnapshotPanel,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            Column(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingActionButton(
                    onClick = { takeSnapshot() },
                    shape = CircleShape,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        "Take Snapshot",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Tap to explore",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // === Snapshot Panel ===
        AnimatedVisibility(
            visible = showSnapshotPanel,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1A1A).copy(alpha = 0.96f)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Header row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF2D2D2D),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.AutoAwesome,
                                            null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    "AI Guide",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                            IconButton(
                                onClick = {
                                    showSnapshotPanel = false
                                    tts?.stop()
                                    isSpeaking = false
                                }
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    null,
                                    tint = Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        if (isLoadingSnapshot) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth().height(2.dp),
                                color = Color.White.copy(alpha = 0.5f),
                                trackColor = Color(0xFF2D2D2D)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Analyzing this view...",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        } else {
                            // Narration text
                            if (snapshotNarration.isNotBlank()) {
                                Text(
                                    snapshotNarration,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 15.sp,
                                    lineHeight = 24.sp,
                                    fontWeight = FontWeight.Normal
                                )
                                Spacer(Modifier.height(12.dp))
                            }

                            // Curiosity questions
                            if (curiosityQuestions.isNotEmpty()) {
                                Text(
                                    "Look closer:",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    curiosityQuestions.forEach { q ->
                                        Surface(
                                            shape = RoundedCornerShape(999.dp),
                                            color = Color(0xFF2D2D2D),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                q,
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 13.sp,
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(14.dp))

                            // Replay / Play button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Take another snapshot button
                                OutlinedButton(
                                    onClick = { takeSnapshot() },
                                    shape = RoundedCornerShape(999.dp),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.3f))
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.White.copy(alpha = 0.8f)
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text("New insight", fontSize = 13.sp)
                                }

                                Spacer(Modifier.weight(1f))

                                // Play / Stop narration
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSpeaking) Color(0xFF3A3A3A) else Color.White,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clickable { toggleSpeech() }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            if (isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                                            null,
                                            tint = if (isSpeaking) Color.White else Color.Black,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
```

**[ ] Step 2.3: Add `buildStopData()` helper**

Insert after the Composable, before closing brace of file. Maps each site’s images to view labels:

```kotlin
private fun buildStopData(site: com.example.virasat.data.model.HeritageSite?): List<ImageTourStop> {
    if (site == null) return emptyList()
    val result = mutableListOf<ImageTourStop>()
    result.add(ImageTourStop(site.imageUrl, "Overview", "Overview"))
    site.galleryImages?.forEachIndexed { index, url ->
        val (label, title) = viewLabelForSite(site.id, index)
        result.add(ImageTourStop(url, label, title))
    }
    return result.distinctBy { it.imageUrl }
}

private fun viewLabelForSite(siteId: String, galleryIndex: Int): Pair<String, String> {
    return when (siteId) {
        "hampi" -> when (galleryIndex) {
            0 -> Pair("Stone Chariot", "Vittala Temple")
            1 -> Pair("Musical Pillars", "Temple Interior")
            2 -> Pair("Virupaksha Temple Tower", "Sacred Complex")
            else -> Pair("Heritage View", "Explore")
        }
        "mysore-palace" -> when (galleryIndex) {
            0 -> Pair("Palace Facade", "Grand Entrance")
            1 -> Pair("Durbar Hall", "Royal Court")
            2 -> Pair("Golden Throne", "Regal Seat")
            else -> Pair("Palace View", "Explore")
        }
        "badami" -> when (galleryIndex) {
            0 -> Pair("Cave Temple Facade", "Rock-cut Caves")
            1 -> Pair("Nataraja Sculpture", "Divine Dance")
            2 -> Pair("Agastya Lake", "Sacred Waters")
            else -> Pair("Cave View", "Explore")
        }
        "belur-halebidu" -> when (galleryIndex) {
            0 -> Pair("Chennakeshava Temple", "Hoysala Masterpiece")
            1 -> Pair("Intricate Carvings", "Sculpted Walls")
            2 -> Pair("Dancers and Musicians", "Living Stone")
            else -> Pair("Temple View", "Explore")
        }
        "gol-gumbaz" -> when (galleryIndex) {
            0 -> Pair("Gol Gumbaz Dome", "World's Second Largest")
            1 -> Pair("Whispering Gallery", "Acoustic Marvel")
            2 -> Pair("Tomb Chamber", "Final Resting Place")
            else -> Pair("Monument View", "Explore")
        }
        "madikeri-fort" -> when (galleryIndex) {
            0 -> Pair("Fort Entrance", "Historic Gates")
            1 -> Pair("Coorg Landscape", "Coffee Country")
            2 -> Pair("Museum Artifacts", "Royal Heritage")
            else -> Pair("Fort View", "Explore")
        }
        else -> Pair("Heritage View", "Explore")
    }
}
```

---

## Task 3: Remove Old Quiz Dependency

**Files:**
- Modify: `TalkingToursScreen.kt` (already handled in rewrite)

The new screen does not use `TriviaQuestion` or quiz bottom sheet. Remove all quiz-related imports (`TriviaQuestion`, quiz state vars, quiz sheet). Code in Task 2 already excludes quiz.

---

## Task 4: Build Verification

**Files:** None (compile check)

**[ ] Step 4.1: Compile check**

Run:
```bash
cd "D:\Android PROJECTS\Virasat"
.\gradlew.bat :app:compileDebugKotlin
```

Expected: `BUILD SUCCESSFUL`. Fix any import errors or type mismatches.

---

## Spec Coverage Check

| Requirement | Task |
|-------------|------|
| Full-bleed immersive photo gallery | Task 2 (HorizontalPager fillMaxSize) |
| No auto-play; user navigates freely | Task 2 (manual swipe + arrows) |
| Snapshot button triggers AI analysis | Task 2 (FAB + `takeSnapshot()`) |
| AI narration per view using `viewLabel` | Task 1 (`generateSnapshotNarration`) |
| 3 curiosity questions per view | Task 1 (`generateCuriosityQuestions`) |
| TTS for spoken narration | Task 2 (toggleSpeech + TTS) |
| Re-snapshot same view for new insight | Task 2 ("New insight" button) |
| Dark immersive theme | Task 2 (Color.Black backgrounds, dark cards) |
| Language toggle EN/Kannada | Task 2 (top bar toggle) |

No placeholders. No TBDs. All code provided.
