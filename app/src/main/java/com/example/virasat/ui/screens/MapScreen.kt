package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
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
import coil.compose.AsyncImage
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.source.ImageUrls
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    val sites = remember { KarnatakaSites.allSites }
    var selectedSite by remember { mutableStateOf<HeritageSite?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heritage Map") },
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
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0))
            ) {
                AsyncImage(
                    model = ImageUrls.KARNATAKA_MAP_STYLE,
                    contentDescription = "Karnataka Map",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.1f))
                )
                sites.forEach { site ->
                    val (xOffset, yOffset) = site.toMapOffset()
                    MapMarker(
                        site = site,
                        modifier = Modifier.offset(x = xOffset, y = yOffset),
                        onClick = { selectedSite = site }
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sites) { site ->
                    MapSiteChip(site = site, onClick = { onSiteClick(site.id) })
                }
            }
        }
    }

    selectedSite?.let { site ->
        ModalBottomSheet(
            onDismissRequest = { selectedSite = null },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(VirasatMaroon),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            site.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = VirasatMaroon
                        )
                        Text(
                            "${site.district}, Karnataka",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    site.shortDescription,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                TypeBadge(type = site.type.name)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        selectedSite = null
                        onSiteClick(site.id)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View Details", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MapMarker(
    site: HeritageSite,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(VirasatMaroon.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.LocationOn,
                null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(8.dp)
                .offset(y = 14.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.3f))
        )
    }
}

@Composable
fun TypeBadge(type: String) {
    val display = type.replaceFirstChar { it.uppercase() }
    Surface(
        color = VirasatGold.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            display,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = VirasatMaroon
        )
    }
}

private fun HeritageSite.toMapOffset(): Pair<androidx.compose.ui.unit.Dp, androidx.compose.ui.unit.Dp> {
    val minLat = 11.5
    val maxLat = 17.5
    val minLon = 73.5
    val maxLon = 78.5
    val xPercent = ((longitude - minLon) / (maxLon - minLon)).coerceIn(0.0, 1.0)
    val yPercent = 1.0 - ((latitude - minLat) / (maxLat - minLat)).coerceIn(0.0, 1.0)
    return (xPercent * 280).dp to (yPercent * 380).dp
}

@Composable
fun MapSiteChip(site: HeritageSite, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VirasatMaroon),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocationOn, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(site.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = VirasatMaroon)
                Text(site.district, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
