package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    onNotificationClick: (String) -> Unit
) {
    var notifications by remember {
        mutableStateOf(
            listOf(
                NotificationItem("Check-In Reminder", "You are near Hampi! Don't forget to check in and earn XP.", "5h ago", false, Icons.Default.LocationOn, NotificationType.REMINDER),
                NotificationItem("Badge Unlocked!", "You earned the 'Temple Trekker' badge for visiting 3 temples!", "1d ago", false, Icons.Default.EmojiEvents, NotificationType.ACHIEVEMENT),
                NotificationItem("Dussehra Festival at Mysore", "The palace will be illuminated with 97,000 bulbs starting tomorrow.", "2h ago", false, Icons.Default.Event, NotificationType.EVENT),
                NotificationItem("New Heritage Site Added", "Explore the newly added Bidar Fort — a hidden gem of Karnataka.", "2d ago", true, Icons.Default.Place, NotificationType.UPDATE),
                NotificationItem("Audio Guide Ready", "Your downloaded audio guide for Belur is ready for offline listening.", "3d ago", true, Icons.Default.Headset, NotificationType.REMINDER),
                NotificationItem("Quiz Challenge", "Test your knowledge with this week's heritage quiz. Win badges!", "4d ago", true, Icons.Default.Lightbulb, NotificationType.EVENT),
                NotificationItem("Master Explorer Badge Earned!", "Congratulations! You have visited all 6 heritage sites in Karnataka.", "1w ago", true, Icons.Default.Star, NotificationType.ACHIEVEMENT),
                NotificationItem("Sync Complete", "Your passport data has been synced successfully across devices.", "1w ago", true, Icons.Default.CloudDone, NotificationType.UPDATE)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
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
            if (notifications.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.NotificationsNone, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No Notifications", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Gray)
                    Text("You're all caught up!", fontSize = 14.sp, color = Color.LightGray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        val unread = notifications.count { !it.read }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("$unread unread", fontSize = 14.sp, color = VirasatMaroon, fontWeight = FontWeight.Medium)
                            if (unread > 0) {
                                TextButton(onClick = {
                                    notifications = notifications.map { it.copy(read = true) }
                                }) {
                                    Text("Mark all read", color = VirasatMaroon)
                                }
                            }
                        }
                    }
                    items(notifications, key = { it.title + it.time }) { item ->
                        NotificationCard(item = item, onClick = {
                            notifications = notifications.map {
                                if (it.title == item.title && it.time == item.time) it.copy(read = true) else it
                            }
                            onNotificationClick(item.title)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(item: NotificationItem, onClick: () -> Unit) {
    val iconBg = when (item.type) {
        NotificationType.REMINDER -> VirasatMaroon.copy(alpha = 0.1f)
        NotificationType.ACHIEVEMENT -> VirasatGold.copy(alpha = 0.15f)
        NotificationType.EVENT -> Color(0xFFE3F2FD)
        NotificationType.UPDATE -> Color(0xFFF3E5F5)
    }
    val iconTint = when (item.type) {
        NotificationType.REMINDER -> VirasatMaroon
        NotificationType.ACHIEVEMENT -> VirasatGold
        NotificationType.EVENT -> Color(0xFF1976D2)
        NotificationType.UPDATE -> Color(0xFF7B1FA2)
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (item.read) Color(0xFFF5F5F5) else Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).background(iconBg, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(item.icon, null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontWeight = if (item.read) FontWeight.Medium else FontWeight.Bold, fontSize = 15.sp, color = VirasatMaroon)
                Text(item.body, fontSize = 13.sp, color = Color.Gray, maxLines = 2)
                Text(item.time, fontSize = 11.sp, color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
            }
            if (!item.read) {
                Box(modifier = Modifier.size(10.dp).background(VirasatGold, RoundedCornerShape(50)))
            }
        }
    }
}

data class NotificationItem(
    val title: String, val body: String, val time: String, val read: Boolean,
    val icon: ImageVector, val type: NotificationType
)

enum class NotificationType { REMINDER, ACHIEVEMENT, EVENT, UPDATE }