package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
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
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkedSitesScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    val bookmarked = remember { KarnatakaSites.allSites.filter { it.isFavourite } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Sites") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        }
    ) { padding ->
        if (bookmarked.isEmpty()) {
            EmptyStateScreen(
                title = "No Saved Sites",
                message = "Bookmark your favourite heritage sites to see them here.",
                onAction = onBack,
                actionLabel = "Explore Sites"
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VirasatCream)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bookmarked) { site ->
                    BookmarkCard(site = site, onClick = { onSiteClick(site.id) })
                }
            }
        }
    }
}

@Composable
fun BookmarkCard(site: HeritageSite, onClick: () -> Unit) {
    Card(
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
                    Text(site.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = VirasatMaroon)
                    Text(site.location, fontSize = 12.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bookmark, null, tint = VirasatGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Saved", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
