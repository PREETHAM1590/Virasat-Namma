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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.model.AudioChapter
import kotlinx.coroutines.delay

val sampleChapters = listOf(
    AudioChapter("1", "Introduction to the Site", 120, "", "Welcome to this magnificent heritage site. As you walk through these ancient corridors, imagine the centuries of history embedded in every stone."),
    AudioChapter("2", "Historical Significance", 180, "", "Built during the peak of the Vijayanagara Empire, this structure represents the pinnacle of Dravidian architecture..."),
    AudioChapter("3", "Architectural Marvels", 240, "", "Notice the intricate carvings on the pillars. Each sculpture tells a story from mythology..."),
    AudioChapter("4", "Hidden Legends", 150, "", "Local folklore speaks of secret passages and mystical events that occurred here..."),
    AudioChapter("5", "Preservation Efforts", 90, "", "Today, conservationists work tirelessly to preserve these structures for future generations...")
)

@Composable
fun AudioGuideScreen(
    siteId: String,
    siteName: String,
    onBack: () -> Unit
) {
    var currentChapter by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    val chapter = sampleChapters[currentChapter]

    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) { isPlaying = true }
                    override fun onDone(utteranceId: String?) {
                        isPlaying = false
                        progress = 0f
                        if (currentChapter < sampleChapters.lastIndex) {
                            currentChapter++
                        }
                    }
                    override fun onError(utteranceId: String?) { isPlaying = false }
                })
            }
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
        if (isPlaying) {
            tts?.speak(chapter.transcript, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying && progress < 1f) {
            delay(100)
            progress += 0.001f
        }
        if (progress >= 1f) {
            progress = 0f
        }
    }

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
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    "Virasat",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = { }) {
                    Icon(
                        Icons.Default.Search,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
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
                            model = "https://images.unsplash.com/photo-1501854140801-50d01698950b?w=600",
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        // Inner shadow overlay
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
                            .background(Color(0xFF050505))
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
                                    formatTime((progress * chapter.durationSeconds).toInt()),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    "The Sacred Grove",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.White
                                )
                                Text(
                                    formatTime(chapter.durationSeconds),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            // Progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer)
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
                                            currentChapter--
                                            progress = 0f
                                            isPlaying = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Replay10,
                                        "Rewind",
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                }
                                Button(
                                    onClick = {
                                        isPlaying = !isPlaying
                                        if (isPlaying) {
                                            tts?.speak(chapter.transcript, TextToSpeech.QUEUE_FLUSH, null, null)
                                        } else {
                                            tts?.stop()
                                        }
                                    },
                                    modifier = Modifier.size(64.dp),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Icon(
                                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        if (isPlaying) "Pause" else "Play",
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        if (currentChapter < sampleChapters.lastIndex) {
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
                                        tint = MaterialTheme.colorScheme.secondaryContainer
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
                "Chapters",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                sampleChapters.forEachIndexed { index, ch ->
                    val isCurrent = index == currentChapter
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                            .clickable {
                                tts?.stop()
                                currentChapter = index
                                progress = 0f
                                isPlaying = true
                                tts?.speak(ch.transcript, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                            .padding(20.dp)
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
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.VolumeUp,
                                        null,
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.primaryContainer
                                    )
                                }
                            } else {
                                Spacer(Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        String.format("%02d", index + 1),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    ch.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    ch.transcript.take(50) + "...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                if (isCurrent) "Now Playing" else formatTime(ch.durationSeconds),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
