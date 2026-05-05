package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.example.virasat.data.model.AudioChapter
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon
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

    LaunchedEffect(isPlaying) {
        while (isPlaying && progress < 1f) {
            delay(100)
            progress += 0.001f
        }
        if (progress >= 1f) {
            isPlaying = false
            progress = 0f
            if (currentChapter < sampleChapters.lastIndex) {
                currentChapter++
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Guide") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Site Title
            Text(siteName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = VirasatMaroon)
            Spacer(modifier = Modifier.height(24.dp))

            // Audio visualizer area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(VirasatMaroon.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Headphones,
                    null,
                    modifier = Modifier.size(80.dp),
                    tint = VirasatMaroon.copy(alpha = 0.3f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Chapter info
            Text("Chapter ${currentChapter + 1} of ${sampleChapters.size}", color = Color.Gray, fontSize = 14.sp)
            Text(chapter.title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = VirasatMaroon)
            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = VirasatMaroon,
                trackColor = Color.LightGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(formatTime((progress * chapter.durationSeconds).toInt()), fontSize = 12.sp, color = Color.Gray)
                Text(formatTime(chapter.durationSeconds), fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (currentChapter > 0) { currentChapter--; progress = 0f; isPlaying = false } },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous", tint = VirasatMaroon)
                }

                FilledIconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = VirasatMaroon)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = { if (currentChapter < sampleChapters.lastIndex) { currentChapter++; progress = 0f; isPlaying = false } },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next", tint = VirasatMaroon)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Transcript
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Transcript", fontWeight = FontWeight.Bold, color = VirasatMaroon)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(chapter.transcript, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chapter list
            Text("All Chapters", fontWeight = FontWeight.Bold, color = VirasatMaroon)
            Spacer(modifier = Modifier.height(8.dp))
            sampleChapters.forEachIndexed { index, ch ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { currentChapter = index; progress = 0f; isPlaying = false }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (index == currentChapter) VirasatMaroon else Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${index + 1}",
                            color = if (index == currentChapter) Color.White else Color.DarkGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ch.title, fontWeight = if (index == currentChapter) FontWeight.Bold else FontWeight.Normal)
                        Text(formatTime(ch.durationSeconds), fontSize = 12.sp, color = Color.Gray)
                    }
                    if (index == currentChapter && isPlaying) {
                        Icon(Icons.Default.VolumeUp, null, tint = VirasatMaroon)
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}
