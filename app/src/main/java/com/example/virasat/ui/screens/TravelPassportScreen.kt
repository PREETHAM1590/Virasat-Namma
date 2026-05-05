package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virasat.data.model.CheckIn
import com.example.virasat.ui.theme.*
import com.example.virasat.viewmodel.PassportViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TravelPassportScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit,
    viewModel: PassportViewModel = viewModel()
) {
    val checkIns by viewModel.checkIns.collectAsStateWithLifecycle()
    val checkInCount by viewModel.checkInCount.collectAsStateWithLifecycle()
    val uniqueSites by viewModel.uniqueSiteCount.collectAsStateWithLifecycle()
    val factCount by viewModel.unlockedFactCount.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Travel Passport") },
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Passport Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E7)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, VirasatGold)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "KARNATAKA",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = VirasatMaroon,
                            letterSpacing = 4.sp
                        )
                        Text(
                            "HERITAGE PASSPORT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = VirasatMaroon
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Profile Circle
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(VirasatMaroon),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                null,
                                modifier = Modifier.size(40.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Heritage Explorer", fontWeight = FontWeight.Medium, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            PassportStat(uniqueSites.toString(), "Sites")
                            PassportStat(checkInCount.toString(), "Check-ins")
                            PassportStat(factCount.toString(), "Facts")
                        }
                    }
                }
            }

            // Level Progress
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Explorer Level", fontWeight = FontWeight.Bold, color = VirasatMaroon)
                            Text(getLevelName(uniqueSites), fontWeight = FontWeight.Bold, color = VirasatGold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (uniqueSites.coerceAtMost(20)) / 20f },
                            modifier = Modifier.fillMaxWidth(),
                            color = VirasatMaroon,
                            trackColor = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${20 - uniqueSites.coerceAtMost(20)} more sites to reach Heritage Master", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            // Stamps
            if (checkIns.isNotEmpty()) {
                item {
                    Text("Your Stamps", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VirasatMaroon, modifier = Modifier.padding(top = 8.dp))
                }
                items(checkIns) { checkIn ->
                    StampCard(checkIn = checkIn, onClick = { onSiteClick(checkIn.siteId) })
                }
            } else {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.QrCodeScanner,
                                null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.LightGray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No stamps yet",
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                "Visit Karnataka's heritage sites and scan QR codes to collect stamps!",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PassportStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = VirasatMaroon)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

fun getLevelName(sites: Int): String = when {
    sites >= 20 -> "Heritage Master"
    sites >= 15 -> "Explorer Elite"
    sites >= 10 -> "Seasoned Traveler"
    sites >= 5 -> "Heritage Hunter"
    sites >= 1 -> "Beginner"
    else -> "Newcomer"
}

@Composable
fun StampCard(checkIn: CheckIn, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stamp Circle
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(2.dp, VirasatGold, CircleShape)
                    .background(VirasatGold.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    null,
                    tint = VirasatMaroon,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(checkIn.siteName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = VirasatMaroon)
                Text(checkIn.siteLocation, fontSize = 13.sp, color = Color.Gray)
                Text(dateFormat.format(Date(checkIn.timestamp)), fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}
