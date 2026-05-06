package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.source.ImageUrls
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val site = KarnatakaSites.allSites.find { it.id == siteId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${site?.name ?: "Site"} AR View") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = site?.imageUrl ?: ImageUrls.STONE_CARVING,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(24.dp))
            Icon(Icons.Default.CameraEnhance, null, modifier = Modifier.size(64.dp), tint = VirasatMaroon)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Augmented Reality", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
            Text("Point your camera at artifacts to see history come alive.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
