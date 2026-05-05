package com.example.virasat.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.ui.theme.*
import com.example.virasat.viewmodel.DetailViewModel

enum class DetailTab { OVERVIEW, HISTORY, ARCHITECTURE, LEGENDS, FACTS }

@Composable
fun SiteDetailScreen(
    siteId: String,
    onBack: () -> Unit,
    onAudioGuide: (String) -> Unit,
    onCheckIn: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    LaunchedEffect(siteId) { viewModel.loadSite(siteId) }

    val site by viewModel.site.collectAsStateWithLifecycle()
    val hasCheckedIn by viewModel.hasCheckedIn.collectAsStateWithLifecycle()
    val unlockedFacts by viewModel.unlockedFacts.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(DetailTab.OVERVIEW) }

    site?.let { s ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(s.name) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
                )
            },
            floatingActionButton = {
                if (!hasCheckedIn) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            viewModel.checkIn()
                            onCheckIn()
                        },
                        containerColor = VirasatMaroon,
                        contentColor = Color.White,
                        icon = { Icon(Icons.Default.QrCodeScanner, null) },
                        text = { Text("Check In") }
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VirasatCream)
                    .padding(padding)
            ) {
                // Hero Image
                item {
                    AsyncImage(
                        model = s.imageUrl,
                        contentDescription = s.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    )
                }

                // Title Section
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(s.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = VirasatMaroon)
                        Text(s.nameLocal, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = VirasatMaroon, modifier = Modifier.size(16.dp))
                            Text(s.location, fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.AccessTime, null, tint = VirasatMaroon, modifier = Modifier.size(16.dp))
                            Text(s.visitingHours, fontSize = 14.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = VirasatGold, modifier = Modifier.size(16.dp))
                            Text("${s.rating} (${s.reviews} reviews)", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(s.entryFee, fontSize = 14.sp, color = VirasatMaroon, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                // Action Buttons
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionButton("Audio Guide", Icons.Outlined.Headphones) { onAudioGuide(s.id) }
                        ActionButton("Directions", Icons.Outlined.LocationOn) { }
                        ActionButton("Share", Icons.Outlined.Share) { }
                        if (hasCheckedIn) {
                            ActionButton("Checked In", Icons.Default.CheckCircle) { }
                        }
                    }
                }

                // Gallery
                if (s.galleryImages.isNotEmpty()) {
                    item {
                        Text("Gallery", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp), color = VirasatMaroon)
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(s.galleryImages) { img ->
                                AsyncImage(
                                    model = img,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }
                        }
                    }
                }

                // Tabs
                item {
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab.ordinal,
                        containerColor = Color.Transparent,
                        contentColor = VirasatMaroon,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        DetailTab.values().forEach { tab ->
                            Tab(
                                selected = selectedTab == tab,
                                onClick = { selectedTab = tab },
                                text = { Text(tab.name.replaceFirstChar { it.uppercase() }) }
                            )
                        }
                    }
                }

                // Tab Content
                item {
                    when (selectedTab) {
                        DetailTab.OVERVIEW -> TabContent(s.shortDescription + "\n\n" + s.description)
                        DetailTab.HISTORY -> TabContent(s.history)
                        DetailTab.ARCHITECTURE -> TabContent(s.architecture)
                        DetailTab.LEGENDS -> TabContent(s.legends)
                        DetailTab.FACTS -> FactsContent(s, unlockedFacts, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FilledTonalIconButton(
            onClick = onClick,
            colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = Color.White)
        ) {
            Icon(icon, null, tint = VirasatMaroon)
        }
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun TabContent(text: String) {
    Text(
        text,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyLarge,
        lineHeight = 24.sp
    )
}

@Composable
fun FactsContent(site: HeritageSite, unlockedFacts: List<String>, viewModel: DetailViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        site.facts.forEach { fact ->
            val isUnlocked = unlockedFacts.contains(fact.id)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUnlocked) Color.White else Color(0xFFEEEEEE)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isUnlocked) {
                        Text(fact.title, fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(fact.description, style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hidden Fact", fontWeight = FontWeight.Bold, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Check in at this site to unlock this secret fact!", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.unlockFact(fact.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon)
                        ) {
                            Text("Unlock")
                        }
                    }
                }
            }
        }
    }
}
