package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(onBack: () -> Unit) {
    val leaders = listOf(
        Leader("Heritage Hunter", 48, 12),
        Leader("Temple Trekker", 42, 10),
        Leader("Fort Finder", 38, 9),
        Leader("History Buff", 35, 8),
        Leader("Wanderlust", 30, 7),
        Leader("You", 22, 5)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(leaders) { index, leader ->
                LeaderCard(rank = index + 1, leader = leader)
            }
        }
    }
}

@Composable
fun LeaderCard(rank: Int, leader: Leader) {
    val bg = when (rank) {
        1 -> Color(0xFFFFF8E1)
        2 -> Color(0xFFF5F5F5)
        3 -> Color(0xFFFFF3E0)
        else -> Color.White
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (rank <= 3) VirasatGold else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("$rank", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(leader.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = VirasatMaroon)
                Text("${leader.sites} sites visited", fontSize = 12.sp, color = Color.Gray)
            }
            Text("${leader.badges} badges", fontSize = 14.sp, color = VirasatGold, fontWeight = FontWeight.Medium)
        }
    }
}

data class Leader(val name: String, val sites: Int, val badges: Int)
