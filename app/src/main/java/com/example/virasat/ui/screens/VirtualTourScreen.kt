package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.source.ImageUrls
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualTourScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val site = KarnatakaSites.allSites.find { it.id == siteId }
    val tourStops = remember {
        listOf(
            TourStop("Entrance & Welcome", "Begin your journey at the main entrance with an overview of the site history."),
            TourStop("Main Monument", "Explore the central structure with detailed architectural commentary."),
            TourStop("Inner Sanctum", "Walk through the most sacred or historically significant areas."),
            TourStop("Artifacts Gallery", "View preserved artifacts and learn their stories."),
            TourStop("Panoramic Viewpoint", "End at the best vantage point for photos and reflection.")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${site?.name ?: "Site"} Virtual Tour") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = site?.imageUrl ?: ImageUrls.HERITAGE_LAKE,
                        contentDescription = site?.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Videocam, null, modifier = Modifier.size(40.dp), tint = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "360 Virtual Tour",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "Coming Soon",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Experience ${site?.name ?: "this heritage site"} like never before with our upcoming immersive virtual tour. Walk through every corner, zoom into intricate details, and listen to guided narration from the comfort of your home.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text("Planned Tour Stops", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                tourStops.forEachIndexed { index, stop ->
                    TourStopItem(index + 1, stop)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

data class TourStop(val title: String, val description: String)

@Composable
fun TourStopItem(number: Int, stop: TourStop) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(VirasatMaroon, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("$number", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(stop.title, fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 15.sp)
                Text(stop.description, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
            }
        }
    }
}
