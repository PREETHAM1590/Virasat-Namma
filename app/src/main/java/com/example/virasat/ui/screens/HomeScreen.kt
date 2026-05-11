package com.example.virasat.ui.screens

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.SiteType
import com.example.virasat.ui.components.AnimatedWeatherWidget
import com.example.virasat.ui.components.BottomNavItem
import com.example.virasat.ui.theme.Background
import com.example.virasat.ui.theme.NavBackground
import com.example.virasat.ui.theme.OnPrimaryContainer
import com.example.virasat.ui.theme.OnSurface
import com.example.virasat.ui.theme.OnSurfaceVariant
import com.example.virasat.ui.theme.Primary
import com.example.virasat.ui.theme.PrimaryContainer
import com.example.virasat.ui.theme.responsiveHorizontalPadding
import com.example.virasat.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(
    onSiteClick: (String) -> Unit,
    onQrScan: () -> Unit,
    onPassport: () -> Unit,
    onFavourites: () -> Unit,
    onNavItemClick: (BottomNavItem) -> Unit,
    onBadges: () -> Unit,
    onQuiz: () -> Unit,
    onAiAssistant: () -> Unit,
    onGuides: () -> Unit,
    onItinerary: () -> Unit,
    onSearch: () -> Unit,
    onSitesList: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {}
) {
    val weatherViewModel: WeatherViewModel = viewModel()
    val weather by weatherViewModel.weather.collectAsState()
    val viewModel: com.example.virasat.viewmodel.HomeViewModel = viewModel()
    val context = LocalContext.current

    val hPadding = responsiveHorizontalPadding()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            weatherViewModel.refreshWithLocation(context)
        }
    }

    val selectedType by viewModel.selectedType.collectAsState()
    val allSites by viewModel.allSites.collectAsState()
    val filteredSites by viewModel.filteredSites.collectAsState()

    val displaySites = if (selectedType == null) allSites else filteredSites
    val heroSite = allSites.firstOrNull()
    val popularSites = allSites.take(6)

    val userName = remember {
        val prefs = context.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
        prefs.getString("user_name", null)?.split(" ")?.firstOrNull() ?: "Explorer"
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // ── Header ──────────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = hPadding, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Hi, $userName \uD83D\uDC4B",
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurfaceVariant
                        )
                        Text(
                            "Heritage\nis Power",
                            style = MaterialTheme.typography.displayLarge.copy(lineHeight = 50.sp),
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        AnimatedWeatherWidget(
                            weather = weather,
                            modifier = Modifier.clickable {
                                locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(PrimaryContainer.copy(alpha = 0.4f))
                                .clickable { onSearch() }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "Search sites",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Primary
                                )
                            }
                        }
                    }
                }
            }

            // ── Location label ───────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.padding(horizontal = hPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocationOn, null, tint = Primary, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "KARNATAKA, INDIA",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                        color = Primary
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Category chips ──────────────────────────────────────────────
            item {
                val categories = listOf("All", "Temple", "Palace", "Fort", "Monument", "UNESCO", "Jain")
                val siteTypeMap = mapOf(
                    "Temple" to "TEMPLE", "Palace" to "PALACE", "Fort" to "FORT",
                    "Monument" to "MONUMENT", "UNESCO" to "UNESCO", "Jain" to "JAIN"
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = hPadding),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = if (cat == "All") selectedType == null
                        else selectedType?.name == siteTypeMap[cat]
                        Box(
                            modifier = Modifier
                                .shadow(
                                    if (isSelected) 6.dp else 2.dp,
                                    RoundedCornerShape(999.dp),
                                    spotColor = Primary.copy(alpha = if (isSelected) 0.18f else 0.05f)
                                )
                                .clip(RoundedCornerShape(999.dp))
                                .background(if (isSelected) Primary else Color.White)
                                .clickable {
                                    if (cat == "All") {
                                        viewModel.setTypeFilter(null)
                                    } else {
                                        val typeName = siteTypeMap[cat]
                                        val newType = if (isSelected) null else SiteType.valueOf(typeName!!)
                                        viewModel.setTypeFilter(newType)
                                        onCategoryClick(cat)
                                    }
                                }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                cat,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) Color.White else Primary
                            )
                        }
                    }
                }
            }

            // ── Hero card ────────────────────────────────────────────────────
            heroSite?.let { site ->
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = hPadding)
                            .height(340.dp)
                            .shadow(20.dp, RoundedCornerShape(36.dp), spotColor = Color.Black.copy(alpha = 0.12f))
                            .clip(RoundedCornerShape(36.dp))
                            .clickable { onSiteClick(site.id) }
                    ) {
                        AsyncImage(
                            model = site.imageUrl,
                            contentDescription = site.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.15f),
                                            Color.Black.copy(alpha = 0.65f)
                                        ),
                                        startY = 100f
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(28.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color.White.copy(alpha = 0.22f))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    "Featured",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                site.name,
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                site.shortDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f),
                                maxLines = 2
                            )
                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = { onSiteClick(site.id) },
                                shape = RoundedCornerShape(999.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Primary
                                ),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    "Explore Now",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(40.dp))
                }
            }

            // ── Popular Sites ────────────────────────────────────────────────
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = hPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Popular Sites",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        "View All",
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary,
                        modifier = Modifier.clickable { onSitesList() }
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = hPadding),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 40.dp)
                ) {
                    items(popularSites) { site ->
                        PopularSiteCard(site = site, onClick = { onSiteClick(site.id) })
                    }
                }
            }

            // ── Curated Trails ───────────────────────────────────────────────
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = hPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Curated Trails",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        "View All",
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary,
                        modifier = Modifier.clickable { onGuides() }
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.padding(horizontal = hPadding),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TrailCard(
                        "Hampi Heritage Walk",
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer,
                        "Explore the Vijayanagara ruins — stone chariot, musical pillars, and boulder landscapes.",
                        Modifier.weight(1f)
                    )
                    TrailCard(
                        "Temple Town Circuit",
                        PrimaryContainer,
                        OnPrimaryContainer,
                        "Belur & Halebidu — 12th century Hoysala temples with sculptures that defy imagination.",
                        Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(40.dp))
            }
        }

        // ── Bottom Nav ───────────────────────────────────────────────────────
        Box(Modifier.align(Alignment.BottomCenter)) {
            com.example.virasat.ui.components.VirasatBottomNavBar(currentRoute = "home", onItemClick = onNavItemClick)
        }
    }
}

@Composable
private fun PopularSiteCard(site: HeritageSite, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = site.imageUrl,
                    contentDescription = site.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f))
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            site.rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    site.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = OnSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        site.district,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(PrimaryContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        site.type.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = Primary
                    )
                }
            }
        }
    }
}

@Composable
private fun TrailCard(
    title: String,
    iconBg: Color,
    iconColor: Color,
    desc: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.06f))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column {
            Box(
                Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text("◆", fontSize = 18.sp, color = iconColor)
            }
            Spacer(Modifier.height(14.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = OnSurface,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                desc,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant
            )
        }
    }
}
