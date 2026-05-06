package com.example.virasat.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.virasat.data.model.Fact
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.ui.theme.*
import com.example.virasat.viewmodel.DetailViewModel

enum class DetailTab { OVERVIEW, HISTORY, ARCHITECTURE, LEGENDS, FACTS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteDetailScreen(
    siteId: String,
    onBack: () -> Unit,
    onAudioGuide: (String) -> Unit,
    onCheckIn: () -> Unit,
    onImmersiveView: (String) -> Unit,
    onGallery: (String) -> Unit = {},
    onReviews: (String) -> Unit = {},
    viewModel: DetailViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(siteId) { viewModel.loadSite(siteId) }

    val site by viewModel.site.collectAsState()
    val hasCheckedIn by viewModel.hasCheckedIn.collectAsState()
    val unlockedFacts by viewModel.unlockedFacts.collectAsState()
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
                // Hero Image with immersive button overlay
                item {
                    Box {
                        AsyncImage(
                            model = s.imageUrl,
                            contentDescription = s.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                        )
                        // Gradient overlay for readability
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .background(
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                                        startY = 200f
                                    )
                                )
                                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                        )
                        // Immersive view button
                        IconButton(
                            onClick = { onImmersiveView(s.id) },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(Icons.Default.ViewInAr, "360 View", tint = Color.White)
                        }
                    }
                }

                // Title Section
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(s.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = VirasatMaroon)
                                Text(s.nameLocal, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                            }
                            AssistChip(
                                onClick = {},
                                label = { Text(s.type.name.replaceFirstChar { it.uppercase() }) },
                                colors = AssistChipDefaults.assistChipColors(containerColor = VirasatGold.copy(alpha = 0.2f))
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Info chips row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            InfoChip(Icons.Default.LocationOn, s.location)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoChip(Icons.Default.AccessTime, s.visitingHours)
                            InfoChip(Icons.Default.AttachMoney, s.entryFee)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = VirasatGold, modifier = Modifier.size(16.dp))
                                Text("${s.rating} (${formatReviews(s.reviews)})", fontSize = 14.sp, color = Color.Gray)
                            }
                            if (hasCheckedIn) {
                                Spacer(modifier = Modifier.width(8.dp))
                                AssistChip(
                                    onClick = {},
                                    label = { Text("Checked In", fontSize = 11.sp) },
                                    leadingIcon = { Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(14.dp)) },
                                    colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFC8E6C9))
                                )
                            }
                        }
                    }
                }

                // Action Buttons Row
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionButton("Audio Guide", Icons.Outlined.Headset) { onAudioGuide(s.id) }
                        ActionButton("Directions", Icons.Outlined.Directions) {
                            val uri = "google.navigation:q=${s.latitude},${s.longitude}"
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                        }
                        ActionButton("Gallery", Icons.Outlined.PhotoLibrary) { onGallery(s.id) }
                        ActionButton("Reviews", Icons.Outlined.RateReview) { onReviews(s.id) }
                        ActionButton("Share", Icons.Outlined.Share) {
                            val text = "Check out ${s.name} — a magnificent heritage site in Karnataka!\n${s.shortDescription}"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, text)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                        }
                        ActionButton("Listen", Icons.Outlined.VolumeUp) { onAudioGuide(s.id) }
                    }
                }

                // Gallery
                if (s.galleryImages.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Gallery", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = VirasatMaroon)
                            TextButton(onClick = { onImmersiveView(s.id) }) {
                                Text("View All", color = VirasatGold)
                            }
                        }
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

                // Bottom padding
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = VirasatMaroon, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 13.sp, color = Color.Gray, maxLines = 1)
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun TabContent(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
        )
    }
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(fact.title, fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
                            Icon(Icons.Default.LockOpen, null, tint = VirasatGold, modifier = Modifier.size(20.dp))
                        }
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
                            Icon(Icons.Default.FlashOn, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Unlock")
                        }
                    }
                }
            }
        }
    }
}

private fun formatReviews(count: Int): String = when {
    count >= 1000 -> "${count / 1000}k"
    else -> count.toString()
}