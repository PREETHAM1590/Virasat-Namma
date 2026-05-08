package com.example.virasat.ui.screens

import android.annotation.SuppressLint
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.virasat.data.service.GeminiHeritageService
import com.example.virasat.data.service.TriviaQuestion
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AINarratedTourScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val site = KarnatakaSites.allSites.find { it.id == siteId }
    val images = site?.galleryImages?.ifEmpty { listOf(site?.imageUrl ?: "") } ?: listOf("")
    val coroutineScope = rememberCoroutineScope()

    var isNarrating by remember { mutableStateOf(false) }
    var narrationText by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("English") }
    var showTrivia by remember { mutableStateOf(false) }
    var triviaQuestions by remember { mutableStateOf<List<TriviaQuestion>>(emptyList()) }
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }
    var snapshotFact by remember { mutableStateOf("") }
    var showSnapshot by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var currentImageIndex by remember { mutableIntStateOf(0) }
    var autoRotate by remember { mutableStateOf(true) }
    var focusMode by remember { mutableStateOf("overview") }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = if (language == "Kannada") Locale("kn") else Locale.ENGLISH
            }
        }
    }

    LaunchedEffect(language) {
        tts?.language = if (language == "Kannada") Locale("kn") else Locale.ENGLISH
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val panoramaHtml = remember(images, currentImageIndex, autoRotate) {
        val imgUrl = if (images.isNotEmpty()) images[currentImageIndex] else ""
        """
<!DOCTYPE html>
<html><head><meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.css">
<script src="https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.js"></script>
<style>body{margin:0;padding:0;overflow:hidden;background:#000}#panorama{width:100vw;height:100vh}</style>
</head><body><div id="panorama"></div>
<script>
pannellum.viewer('panorama',{"type":"equirectangular","panorama":"$imgUrl","autoLoad":true,"autoRotate":${if(autoRotate)"-2" else "0"},"compass":false,"showZoomCtrl":true,"showFullscreenCtrl":false,"showControls":true,"strings":{"loadButtonLabel":"Tap to Load 360°"}});
</script></body></html>
        """.trimIndent()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(site?.name ?: "AI Tour", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { tts?.stop(); onBack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Color.Black)) {
            // 360° viewer
            if (images.isNotEmpty()) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = false
                            setBackgroundColor(android.graphics.Color.BLACK)
                            loadDataWithBaseURL(null, panoramaHtml, "text/html", "UTF-8", null)
                        }
                    },
                    update = { it.loadDataWithBaseURL(null, panoramaHtml, "text/html", "UTF-8", null) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Overlay controls
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Loading indicator
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        trackColor = Color.Transparent
                    )
                }

                // Snapshot result
                if (showSnapshot && snapshotFact.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, null, tint = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI Snapshot", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(snapshotFact, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Trivia overlay
                if (showTrivia && triviaQuestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .heightIn(max = 400.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.97f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Quiz, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI Trivia", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { showTrivia = false }) {
                                    Icon(Icons.Default.Close, null, tint  = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            triviaQuestions.forEachIndexed { qi, q ->
                                Text("Q${qi + 1}: ${q.question}", fontWeight = FontWeight.Medium, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(4.dp))
                                q.options.forEachIndexed { oi, opt ->
                                    val isSelected = selectedAnswers[qi] == oi
                                    val isCorrect = q.correctAnswer == oi
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                            .background(
                                                if (isSelected && isCorrect) Color(0xFFC8E6C9)
                                                else if (isSelected && !isCorrect) Color(0xFFFFCDD2)
                                                else Color.Transparent,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                selectedAnswers = mutableMapOf<Int, Int>().apply { putAll(selectedAnswers); put(qi, oi) }
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(opt, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                if (selectedAnswers.containsKey(qi)) {
                                    Text(q.explanation, fontSize = 12.sp, color  = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                // Narration text
                if (narrationText.isNotEmpty()) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            narrationText,
                            modifier = Modifier.padding(12.dp),
                            color = Color.White,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            maxLines = 3,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Bottom controls
                    Column {
                        // Navigation row
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { if (currentImageIndex > 0) { currentImageIndex--; snapshotFact = ""; showSnapshot = false } }) {
                                Icon(Icons.Default.SkipPrevious, "Previous", tint = if (currentImageIndex > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                            }
                            Text("${currentImageIndex + 1}/${images.size}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = { autoRotate = !autoRotate }) {
                                Icon(if (autoRotate) Icons.Default.RotateRight else Icons.Default.Panorama, null, tint = if (autoRotate) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                            }
                            IconButton(onClick = { if (currentImageIndex < images.size - 1) { currentImageIndex++; snapshotFact = ""; showSnapshot = false } }) {
                                Icon(Icons.Default.SkipNext, "Next", tint = if (currentImageIndex < images.size - 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                            }
                        }

                        // Action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Narration toggle
                            Button(
                                onClick = {
                                    if (isNarrating) {
                                        isNarrating = false
                                        tts?.stop()
                                    } else {
                                        isNarrating = true
                                        isLoading = true
                                        coroutineScope.launch {
                                            val text = GeminiHeritageService.generateNarration(siteId, language)
                                            narrationText = text
                                            isLoading = false
                                            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                                                override fun onDone(uttId: String?) { isNarrating = false }
                                                override fun onError(uttId: String?) { isNarrating = false }
                                                override fun onStart(uttId: String?) {}
                                            })
                                            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "narration")
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isNarrating) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary),
                                shape = CircleShape,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(if (isNarrating) Icons.Default.Stop else Icons.Default.PlayArrow, "Narrate", tint = Color.White)
                            }

                            // Snapshot
                            Button(
                                onClick = {
                                    isLoading = true; showSnapshot = true
                                    coroutineScope.launch {
                                        snapshotFact = GeminiHeritageService.describeView(siteId, focusMode, language)
                                        isLoading = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                shape = CircleShape,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, "Snapshot", tint = Color.White)
                            }

                            // Trivia
                            Button(
                                onClick = {
                                    showTrivia = !showTrivia
                                    if (showTrivia && triviaQuestions.isEmpty()) {
                                        isLoading = true
                                        coroutineScope.launch {
                                            triviaQuestions = GeminiHeritageService.generateTrivia(siteId, 3, language)
                                            isLoading = false
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
                                shape = CircleShape,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(Icons.Default.Quiz, "Trivia", tint = Color.White)
                            }

                            // Language toggle
                            OutlinedButton(
                                onClick = { language = if (language == "English") "Kannada" else "English" },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(56.dp)
                            ) {
                                Text(if (language == "English") "EN" else "ಕ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }

                            // Focus selector
                            var showFocusMenu by remember { mutableStateOf(false) }
                            Box {
                                OutlinedButton(
                                    onClick = { showFocusMenu = true },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(56.dp)
                                ) {
                                    Text(focusMode.take(4), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                DropdownMenu(expanded = showFocusMenu, onDismissRequest = { showFocusMenu = false }) {
                                    listOf("overview", "history", "architecture", "legends").forEach { mode ->
                                        DropdownMenuItem(
                                            text = { Text(mode.replaceFirstChar { it.uppercase() }) },
                                            onClick = { focusMode = mode; showFocusMenu = false }
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
            }
        }
    }
}