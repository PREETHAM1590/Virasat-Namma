package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCheckInsScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    val checkIns = listOf(
        CheckInEntry("Hampi", "Bellary", "02 May 2026"),
        CheckInEntry("Mysore Palace", "Mysuru", "28 Apr 2026"),
        CheckInEntry("Badami Cave Temples", "Bagalkot", "15 Apr 2026"),
        CheckInEntry("Belur Temples", "Hassan", "10 Apr 2026")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Check-Ins") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(checkIns) { entry ->
                CheckInCard(entry = entry, onClick = { onSiteClick(entry.name.lowercase().replace(" ", "-")) })
            }
        }
    }
}

@Composable
fun CheckInCard(entry: CheckInEntry, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.QrCodeScanner, null, tint = VirasatMaroon, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = VirasatMaroon)
                Text(entry.location, fontSize = 13.sp, color = Color.Gray)
            }
            Text(entry.date, fontSize = 12.sp, color = VirasatGold, fontWeight = FontWeight.Medium)
        }
    }
}

data class CheckInEntry(val name: String, val location: String, val date: String)
