package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
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
import coil.compose.AsyncImage
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit,
    onNavigateToResults: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val recentSearches = remember { mutableStateListOf("Hampi", "Mysore Palace", "Temples", "UNESCO sites") }

    val results by remember(query) {
        derivedStateOf {
            if (query.trim().isEmpty()) emptyList()
            else KarnatakaSites.allSites.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.location.contains(query, ignoreCase = true) ||
                it.district.contains(query, ignoreCase = true) ||
                it.type.name.contains(query, ignoreCase = true) ||
                it.shortDescription.contains(query, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Heritage Sites", color = VirasatMaroon) },
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
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search sites, districts, types...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = VirasatMaroon) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Close, null, tint = Color.Gray)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = VirasatMaroon,
                    unfocusedBorderColor = VirasatMaroon.copy(alpha = 0.3f)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            when {
                query.trim().isEmpty() -> {
                    // Recent searches
                    if (recentSearches.isNotEmpty()) {
                        Text("Recent Searches", fontWeight = FontWeight.Bold, color = VirasatMaroon)
                        Spacer(modifier = Modifier.height(8.dp))
                        recentSearches.forEach { term ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        query = term
                                        onNavigateToResults(term)
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.History, null, tint = VirasatGold, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(term, fontSize = 16.sp, color = Color.DarkGray, modifier = Modifier.weight(1f))
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray)
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 36.dp),
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Popular Categories", fontWeight = FontWeight.Bold, color = VirasatMaroon)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CategoryChip("UNESCO", VirasatMaroon) { query = "UNESCO" }
                        CategoryChip("Temples", VirasatGold) { query = "TEMPLE" }
                        CategoryChip("Forts", VirasatMaroon) { query = "FORT" }
                        CategoryChip("Palaces", VirasatGold) { query = "PALACE" }
                    }
                }
                results.isEmpty() -> {
                    // Empty state
                    Spacer(modifier = Modifier.height(48.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.SearchOff,
                            null,
                            tint = VirasatGold,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No results found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = VirasatMaroon
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Try searching for \"Hampi\", \"Temple\", or \"Fort\".",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                else -> {
                    // Search results
                    Text(
                        "${results.size} result${if (results.size != 1) "s" else ""} for \"$query\"",
                        fontWeight = FontWeight.SemiBold,
                        color = VirasatMaroon,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(results, key = { it.id }) { site ->
                            SearchResultCard(site = site, onClick = { onSiteClick(site.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(label: String, color: Color, onClick: () -> Unit = {}) {
    AssistChip(
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp, color = Color.White) },
        colors = AssistChipDefaults.assistChipColors(containerColor = color),
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun SearchResultCard(site: HeritageSite, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(100.dp)) {
            AsyncImage(
                model = site.imageUrl,
                contentDescription = site.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
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
                    Text(site.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = VirasatMaroon)
                    Text(
                        site.location,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = VirasatGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("${site.rating}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        site.type.name.lowercase().replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        color = VirasatGold,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
