package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.source.ImageUrls
import com.example.virasat.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onBookmarks: () -> Unit,
    onPassport: () -> Unit,
    onLogout: () -> Unit,
    onBadges: () -> Unit = {},
    onCheckIns: () -> Unit = {},
    onGuides: () -> Unit = {},
    onCommunity: () -> Unit = {},
    onFeedback: () -> Unit = {},
    onHelp: () -> Unit = {},
    onSitesList: () -> Unit = {},
    onLeaderboard: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = VirasatMaroon) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = VirasatMaroon)
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
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with edit button
            Box(
                modifier = Modifier.size(108.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(VirasatMaroon),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageUrls.PROFILE_AVATAR,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                FloatingActionButton(
                    onClick = onEditProfile,
                    modifier = Modifier.size(32.dp),
                    containerColor = VirasatGold,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Edit, null, tint = VirasatMaroon, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Heritage Explorer", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
            Text("explorer@virasat.in", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Level 3: Heritage Hunter", fontSize = 13.sp, color = VirasatGold, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(20.dp))

            // Stats cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon = Icons.Default.Place,
                    value = "12",
                    label = "Sites Visited",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Default.Lightbulb,
                    value = "24",
                    label = "Facts Unlocked",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Default.EmojiEvents,
                    value = "5",
                    label = "Badges Earned",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu items
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileMenuItem("My Passport", Icons.Default.MenuBook, onPassport)
                ProfileMenuItem("My Check-ins", Icons.Default.CheckCircle, onCheckIns)
                ProfileMenuItem("Saved Sites", Icons.Default.Favorite, onBookmarks)
                ProfileMenuItem("Badges", Icons.Default.EmojiEvents, onBadges)
                ProfileMenuItem("Heritage Guides", Icons.Default.Map, onGuides)
                ProfileMenuItem("Community", Icons.Default.Groups, onCommunity)
                ProfileMenuItem("Feedback", Icons.Default.RateReview, onFeedback)
                ProfileMenuItem("All Heritage Sites", Icons.Default.AccountBalance, onSitesList)
                ProfileMenuItem("Leaderboard", Icons.Default.EmojiEvents, onLeaderboard)
                ProfileMenuItem("Help & Support", Icons.Default.Help, onHelp)
                ProfileMenuItem("Settings", Icons.Default.Settings, onSettings)
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = VirasatMaroon,
                    containerColor = Color.Transparent
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(VirasatMaroon.copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Logout, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out")
            }
        }
    }
}

@Composable
fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = VirasatMaroon, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
            Text(label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ProfileMenuItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = VirasatMaroon, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, fontSize = 16.sp, color = VirasatMaroon, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
    }
}
