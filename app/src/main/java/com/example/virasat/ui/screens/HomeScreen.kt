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
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.example.virasat.R
import com.example.virasat.util.LocaleHelper
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
import com.example.virasat.util.LocationUtils
import com.example.virasat.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

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

    // Always use filteredSites so search query is respected even when no type filter is set (#9)
    val displaySites = filteredSites
    val heroSite = allSites.firstOrNull()
    val popularSites = displaySites.take(6)

    var userLocation by remember { mutableStateOf<android.location.Location?>(null) }
    LaunchedEffect(Unit) {
        if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            try {
                userLocation = LocationUtils.fetchCurrentLocation(context)
            } catch (_: Exception) { }
        }
    }

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
                            stringResource(R.string.home_greeting, userName),
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurfaceVariant
                        )
                        Text(
                            stringResource(R.string.home_tagline),
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
                                    stringResource(R.string.home_search_hint),
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
                        stringResource(R.string.home_location),
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                        color = Primary
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Category chips ──────────────────────────────────────────────
            item {
                val allType = stringResource(R.string.category_all)
                val categories = listOf(allType, stringResource(R.string.category_temple), stringResource(R.string.category_palace), stringResource(R.string.category_fort), stringResource(R.string.category_monument), stringResource(R.string.category_unesco), stringResource(R.string.category_jain))
                val siteTypeMap = mapOf(
                    stringResource(R.string.category_temple) to "TEMPLE",
                    stringResource(R.string.category_palace) to "PALACE",
                    stringResource(R.string.category_fort) to "FORT",
                    stringResource(R.string.category_monument) to "MONUMENT",
                    stringResource(R.string.category_unesco) to "UNESCO",
                    stringResource(R.string.category_jain) to "JAIN"
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = hPadding),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = if (cat == allType) selectedType == null
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
                                    if (cat == allType) {
                                        viewModel.setTypeFilter(null)
                                    } else {
                                        val typeName = siteTypeMap[cat]
                                        val newType = if (isSelected) null else SiteType.valueOf(typeName!!)
                                        viewModel.setTypeFilter(newType)
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
                                    stringResource(R.string.home_featured),
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
                                    stringResource(R.string.home_explore_now),
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
                        stringResource(R.string.home_popular_sites),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        stringResource(R.string.view_all),
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
                        val distanceText = remember(userLocation) {
                            userLocation?.let {
                                val km = LocationUtils.distanceKm(it.latitude, it.longitude, site.latitude, site.longitude)
                                LocationUtils.formatDistance(km)
                            } ?: site.district
                        }
                        PopularSiteCard(site = site, distanceText = distanceText, onClick = { onSiteClick(site.id) })
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
                        stringResource(R.string.home_curated_trails),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        stringResource(R.string.view_all),
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
                        stringResource(R.string.home_hampi_walk),
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer,
                        stringResource(R.string.home_hampi_desc),
                        Modifier.weight(1f)
                    )
                    TrailCard(
                        stringResource(R.string.home_temple_circuit),
                        PrimaryContainer,
                        OnPrimaryContainer,
                        stringResource(R.string.home_temple_desc),
                        Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(32.dp))

                // ── Itinerary Planner Card ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = hPadding)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable { onItinerary() }
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Route,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                stringResource(R.string.home_plan_itinerary),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                stringResource(R.string.home_plan_itinerary_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
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
private fun PopularSiteCard(site: HeritageSite, distanceText: String = site.district, onClick: () -> Unit) {
    val context = LocalContext.current
    val displayName = LocaleHelper.siteName(site.name, site.nameLocal, context)
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
                    contentDescription = displayName,
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
                    displayName,
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
                        distanceText,
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
