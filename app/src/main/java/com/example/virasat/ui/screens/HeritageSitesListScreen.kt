package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.SiteType
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeritageSitesListScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<SiteType?>(null) }
    var isGrid by remember { mutableStateOf(false) }

    val allSites = KarnatakaSites.allSites
    val filtered = remember(query, selectedType) {
        allSites.filter { site ->
            val matchesQuery = query.isBlank() ||
                site.name.contains(query, ignoreCase = true) ||
                site.district.contains(query, ignoreCase = true) ||
                site.location.contains(query, ignoreCase = true)
            val matchesType = selectedType == null || site.type == selectedType
            matchesQuery && matchesType
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Heritage Sites") },
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search sites...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TypeFilterChips(
                    selected = selectedType,
                    onSelect = { selectedType = it }
                )
                IconButton(onClick = { isGrid = !isGrid }) {
                    Icon(
                        if (isGrid) Icons.Default.List else Icons.Default.GridView,
                        if (isGrid) "List View" else "Grid View",
                        tint = VirasatMaroon
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${filtered.size} site${if (filtered.size != 1) "s" else ""} found",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (filtered.isEmpty()) {
                EmptyStateScreen(
                    title = "No Sites Found",
                    message = "Try adjusting your search or filter criteria.",
                    onAction = {
                        query = ""
                        selectedType = null
                    },
                    actionLabel = "Clear Filters"
                )
            } else if (isGrid) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtered, key = { it.id }) { site ->
                        GridSiteCard(site = site, onClick = { onSiteClick(site.id) })
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filtered, key = { it.id }) { site ->
                        DirectorySiteCard(site = site, onClick = { onSiteClick(site.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun TypeFilterChips(
    selected: SiteType?,
    onSelect: (SiteType?) -> Unit
) {
    val types = listOf(null) + SiteType.values().toList()
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        types.forEach { type ->
            val label = type?.name?.replaceFirstChar { it.uppercase() } ?: "All"
            val isSelected = selected == type
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(type) },
                label = { Text(label, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VirasatMaroon,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = VirasatMaroon
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = VirasatMaroon.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
fun DirectorySiteCard(site: HeritageSite, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
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
                    Text(
                        site.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = VirasatMaroon
                    )
                    Text(
                        "${site.district}, ${site.location}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = VirasatGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("${site.rating}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    SiteTypeBadge(type = site.type.name)
                }
            }
        }
    }
}

@Composable
fun GridSiteCard(site: HeritageSite, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AsyncImage(
                model = site.imageUrl,
                contentDescription = site.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    site.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = VirasatMaroon,
                    maxLines = 1
                )
                Text(
                    site.district,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = VirasatGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("${site.rating}", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(6.dp))
                    SiteTypeBadge(type = site.type.name)
                }
            }
        }
    }
}

@Composable
fun SiteTypeBadge(type: String) {
    Surface(
        color = VirasatGold.copy(alpha = 0.12f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            type.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = VirasatMaroon
        )
    }
}
