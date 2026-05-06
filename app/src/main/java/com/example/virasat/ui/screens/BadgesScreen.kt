package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgesScreen(onBack: () -> Unit) {
    val badges = listOf(
        BadgeData("First Steps", "Check in at your first site", true, Icons.Default.Place, 1, 1),
        BadgeData("Temple Trekker", "Visit 3 temples", true, Icons.Default.TempleBuddhist, 3, 3),
        BadgeData("Fort Finder", "Visit 2 forts", false, Icons.Default.Fort, 1, 2),
        BadgeData("UNESCO Explorer", "Visit all UNESCO sites", false, Icons.Default.Public, 1, 1),
        BadgeData("Heritage Hunter", "Visit 5 different sites", true, Icons.Default.EmojiEvents, 5, 5),
        BadgeData("Audio Aficionado", "Listen to 3 audio guides", false, Icons.Default.Headset, 1, 3),
        BadgeData("Fact Finder", "Unlock 10 hidden facts", false, Icons.Default.Lightbulb, 4, 10),
        BadgeData("Master Explorer", "Visit all 6 sites", false, Icons.Default.Star, 3, 6)
    )
    val earned = badges.count { it.unlocked }
    val total = badges.size
    val progress = earned.toFloat() / total

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Badges & Achievements") },
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
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "$earned of $total Badges Unlocked",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = VirasatMaroon
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = VirasatGold,
                        trackColor = Color(0xFFEEEEEE)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "${(progress * 100).toInt()}% complete",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(badges) { badge ->
                    BadgeGridCard(badge)
                }
            }
        }
    }
}

@Composable
fun BadgeGridCard(badge: BadgeData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.unlocked) Color.White else Color(0xFFEEEEEE)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge.unlocked) VirasatGold.copy(alpha = 0.2f)
                        else Color.LightGray.copy(alpha = 0.3f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    badge.icon,
                    null,
                    tint = if (badge.unlocked) VirasatGold else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                badge.title,
                fontWeight = if (badge.unlocked) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp,
                color = if (badge.unlocked) VirasatMaroon else Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                badge.description,
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (!badge.unlocked) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { badge.progress.toFloat() / badge.target },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color.LightGray,
                    trackColor = Color(0xFFE0E0E0)
                )
                Text(
                    "${badge.progress}/${badge.target}",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Earned",
                        fontSize = 11.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

data class BadgeData(
    val title: String,
    val description: String,
    val unlocked: Boolean,
    val icon: ImageVector,
    val progress: Int = 0,
    val target: Int = 1
)
