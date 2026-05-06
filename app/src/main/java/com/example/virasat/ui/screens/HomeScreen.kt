package com.example.virasat.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.SiteType
import com.example.virasat.ui.components.BottomNavItem
import com.example.virasat.ui.components.VirasatBottomNavBar
import com.example.virasat.ui.theme.*
import com.example.virasat.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSiteClick: (String) -> Unit,
    onQrScan: () -> Unit,
    onPassport: () -> Unit,
    onFavourites: () -> Unit,
    onNavItemClick: (BottomNavItem) -> Unit,
    onBadges: () -> Unit = {},
    onQuiz: () -> Unit = {},
    onAiAssistant: () -> Unit = {},
    onGuides: () -> Unit = {},
    onItinerary: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val sites by viewModel.filteredSites.collectAsState()
    val allSites by viewModel.allSites.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val selectedDistrict by viewModel.selectedDistrict.collectAsState()
    val checkInCount by viewModel.checkInCount.collectAsState()
    val uniqueSites by viewModel.uniqueSiteCount.collectAsState()
    val isLoading = sites.isEmpty() && allSites.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Namaste!", style = MaterialTheme.typography.titleMedium, color = VirasatGold)
                        Text("Explore Karnataka", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF2D2D2D))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream),
                actions = {
                    IconButton(onClick = onFavourites) {
                        Icon(Icons.Outlined.FavoriteBorder, "Favourites", tint = VirasatMaroon)
                    }
                    IconButton(onClick = onPassport) {
                        Icon(Icons.Outlined.MenuBook, "Passport", tint = VirasatMaroon)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onQrScan,
                containerColor = VirasatMaroon,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.QrCodeScanner, null) },
                text = { Text("Scan QR") }
            )
        },
        bottomBar = {
            VirasatBottomNavBar(
                currentRoute = "home",
                onItemClick = onNavItemClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
        ) {
            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search heritage sites...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, null)
                            }
                        } else {
                            IconButton(onClick = { viewModel.clearFilters() }) {
                                Icon(Icons.Default.Tune, null)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = VirasatGold,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )
            }

            // Hero Stats Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = VirasatMaroon),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        HeroStat(
                            icon = Icons.Default.TempleHindu,
                            value = "$uniqueSites",
                            label = "Sites Visited"
                        )
                        Divider(
                            modifier = Modifier.height(40.dp).width(1.dp),
                            color = Color.White.copy(alpha = 0.3f)
                        )
                        HeroStat(
                            icon = Icons.Default.CheckCircle,
                            value = "$checkInCount",
                            label = "Check-ins"
                        )
                        Divider(
                            modifier = Modifier.height(40.dp).width(1.dp),
                            color = Color.White.copy(alpha = 0.3f)
                        )
                        HeroStat(
                            icon = Icons.Default.EmojiEvents,
                            value = "${allSites.size}",
                            label = "Total Sites"
                        )
                    }
                }
            }

            // Quick Access Row
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { QuickAccessChip("Badges", Icons.Default.EmojiEvents, VirasatGold) { onBadges() } }
                    item { QuickAccessChip("Quiz", Icons.Default.Quiz, Color(0xFF7B1FA2)) { onQuiz() } }
                    item { QuickAccessChip("AI Guide", Icons.Default.SmartToy, Color(0xFF1976D2)) { onAiAssistant() } }
                    item { QuickAccessChip("Guides", Icons.Default.Map, Color(0xFF388E3C)) { onGuides() } }
                    item { QuickAccessChip("Plan", Icons.Default.Schedule, VirasatMaroon) { onItinerary() } }
                }
            }

            // Type Filter Chips
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedType == null && selectedDistrict == null,
                            onClick = { viewModel.clearFilters() },
                            label = { Text("All") },
                            leadingIcon = if (selectedType == null && selectedDistrict == null) {
                                { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VirasatMaroon,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    items(SiteType.values().toList()) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = {
                                viewModel.setTypeFilter(if (selectedType == type) null else type)
                            },
                            label = { Text(type.name.replaceFirstChar { it.uppercase() }) },
                            leadingIcon = {
                                Icon(
                                    when (type) {
                                        SiteType.TEMPLE -> Icons.Outlined.TempleHindu
                                        SiteType.PALACE -> Icons.Outlined.AccountBalance
                                        SiteType.FORT -> Icons.Outlined.Castle
                                        SiteType.MONUMENT -> Icons.Outlined.AccountBalance
                                        SiteType.CAVE -> Icons.Outlined.Landscape
                                        SiteType.UNESCO -> Icons.Outlined.Public
                                    },
                                    null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VirasatMaroon,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Site count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${sites.size} Heritage Sites",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D2D2D)
                    )
                    if (searchQuery.isNotEmpty()) {
                        TextButton(onClick = { viewModel.setSearchQuery("") }) {
                            Text("Clear", color = VirasatMaroon)
                        }
                    }
                }
            }

            // Sites List
            if (sites.isEmpty() && allSites.isNotEmpty()) {
                item {
                    EmptySearchResult(searchQuery)
                }
            } else if (allSites.isEmpty()) {
                item {
                    LoadingShimmer()
                }
            }

            items(sites) { site ->
                SiteCard(
                    site = site,
                    onClick = { onSiteClick(site.id) }
                )
            }

            // Bottom spacing for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun HeroStat(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = VirasatGold, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
        Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
    }
}

@Composable
private fun EmptySearchResult(query: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Outlined.SearchOff, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("No sites found", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Gray)
            Text("No results for \"$query\"", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Try adjusting your search or filters", fontSize = 13.sp, color = Color.LightGray)
        }
    }
}

@Composable
private fun LoadingShimmer() {
    repeat(3) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .height(120.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFE0E0E0))
                )
                Column(modifier = Modifier.padding(12.dp)) {
                    Box(modifier = Modifier.width(140.dp).height(16.dp).background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.width(80.dp).height(12.dp).background(Color(0xFFEEEEEE), RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.width(100.dp).height(12.dp).background(Color(0xFFEEEEEE), RoundedCornerShape(4.dp)))
                }
            }
        }
    }
}

@Composable
fun SiteCard(site: HeritageSite, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(130.dp)) {
            AsyncImage(
                model = site.imageUrl,
                contentDescription = site.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            site.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = VirasatMaroon,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        if (site.isFavourite) {
                            Icon(Icons.Default.Favorite, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                        }
                    }
                    Text(
                        site.nameLocal,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            site.location,
                            fontSize = 11.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = VirasatGold, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${site.rating}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                    }
                    AssistChip(
                        onClick = {},
                        label = { Text(site.type.name.replaceFirstChar { it.uppercase() }, fontSize = 10.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when (site.type) {
                                SiteType.UNESCO -> VirasatGold.copy(alpha = 0.15f)
                                SiteType.TEMPLE -> Color(0xFFFFE0B2)
                                SiteType.PALACE -> Color(0xFFE1BEE7)
                                SiteType.FORT -> Color(0xFFFFCDD2)
                                SiteType.CAVE -> Color(0xFFB2DFDB)
                                SiteType.MONUMENT -> Color(0xFFBBDEFB)
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickAccessChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Card(onClick = onClick, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        }
    }
}