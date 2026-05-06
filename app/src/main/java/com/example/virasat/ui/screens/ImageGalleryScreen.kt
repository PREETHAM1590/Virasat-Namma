package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatMaroon

@Composable
fun ImageGalleryScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val site = KarnatakaSites.allSites.find { it.id == siteId }
    val images = remember(site) { listOf(site?.imageUrl ?: "") + (site?.galleryImages ?: emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VirasatCream)
            .padding(16.dp)
    ) {
        Text(
            "${site?.name ?: "Site"} Gallery",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VirasatMaroon,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(images) { img ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    AsyncImage(
                        model = img,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                }
            }
        }
    }
}
