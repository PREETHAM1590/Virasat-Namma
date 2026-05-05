package com.example.virasat.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.SiteType
import com.example.virasat.ui.theme.*
import com.example.virasat.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onSiteClick: (String) -> Unit,
    onQrScan: () -> Unit,
    onPassport: () -> Unit,
    onFavourites: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val sites by viewModel.filteredSites.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    val checkInCount by viewModel.checkInCount.collectAsStateWithLifecycle()
    val uniqueSites by viewModel.uniqueSiteCount.collectAsStateWithLifecycle()
    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Namaste!", style = MaterialTheme.typography.titleMedium, color = VirasatGold)
                        Text("Explore Karnataka", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream),
                actions = {
                    IconButton(onClick = onFavourites) {
                        Icon(Icons.Outlined.FavoriteBorder, "Favourites", tint = VirasatMaroon)
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::setSearchQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search heritage sites...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, null)
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Sites Visited", uniqueSites.toString(), Icons.Default.Place)
                StatCard("Check-ins", checkInCount.toString(), Icons.Default.CheckCircle)
            }

            // Type Filters
            if (showFilters) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SiteType.values().toList()) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = {
                                viewModel.setTypeFilter(if (selectedType == type) null else type)
                            },
                            label = { Text(type.name.replaceFirstChar { it.uppercase() }) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VirasatMaroon,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Site List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sites) { site ->
                    SiteCard(site = site, onClick = { onSiteClick(site.id) })
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = VirasatMaroon, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VirasatMaroon)
                Text(title, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun SiteCard(site: HeritageSite, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(120.dp)) {
            AsyncImage(
                model = site.imageUrl,
                contentDescription = site.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        site.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = VirasatMaroon,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        site.nameLocal,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Text(
                        site.location,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = VirasatGold, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${site.rating}", fontSize = 12.sp, color = Color.Gray)
                    }
                    AssistChip(
                        onClick = {},
                        label = { Text(site.type.name.replaceFirstChar { it.uppercase() }, fontSize = 11.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = VirasatGold.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    }
}
