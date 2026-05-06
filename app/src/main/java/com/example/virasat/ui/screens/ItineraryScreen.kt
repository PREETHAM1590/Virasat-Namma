package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    val days = listOf(
        ItineraryDay("Day 1: Hampi", listOf(
            ItineraryItem("Virupaksha Temple", "08:00 AM", "temple"),
            ItineraryItem("Vittala Temple", "11:00 AM", "temple"),
            ItineraryItem("Hemakuta Hill Sunset", "05:30 PM", "monument")
        )),
        ItineraryDay("Day 2: Badami & Aihole", listOf(
            ItineraryItem("Badami Cave Temples", "09:00 AM", "cave"),
            ItineraryItem("Aihole Durga Temple", "02:00 PM", "temple")
        )),
        ItineraryDay("Day 3: Belur & Halebidu", listOf(
            ItineraryItem("Chennakeshava Temple", "09:00 AM", "temple"),
            ItineraryItem("Hoysaleswara Temple", "01:00 PM", "temple")
        ))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Itinerary") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(days) { day ->
                DayCard(day = day, onSiteClick = onSiteClick)
            }
        }
    }
}

@Composable
fun DayCard(day: ItineraryDay, onSiteClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(day.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VirasatMaroon)
            Spacer(modifier = Modifier.height(12.dp))
            day.items.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(VirasatGold, CircleShape)
                        )
                        if (index < day.items.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(40.dp)
                                    .background(Color.LightGray)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Card(
                        onClick = { onSiteClick(item.name.lowercase().replace(" ", "-")) },
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = VirasatCream),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(item.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = VirasatMaroon)
                            Text(item.time, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

data class ItineraryDay(val title: String, val items: List<ItineraryItem>)
data class ItineraryItem(val name: String, val time: String, val type: String)
