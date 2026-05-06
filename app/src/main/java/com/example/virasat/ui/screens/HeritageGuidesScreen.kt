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
import com.example.virasat.data.source.ImageUrls
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeritageGuidesScreen(
    onBack: () -> Unit,
    onGuideClick: (String) -> Unit = {},
    onBookGuide: (String) -> Unit = {}
) {
    val guides = remember {
        listOf(
            GuideData("Raju Gowda", "Hampi Expert", ImageUrls.HAMPI, "Kannada, English, Hindi", 4.8f, "15 years experience"),
            GuideData("Lakshmi Devi", "Palace Historian", ImageUrls.MYSORE_PALACE, "English, Kannada", 4.9f, "20 years experience"),
            GuideData("Krishna Rao", "Architecture Specialist", ImageUrls.BELUR_HALEBIDU, "Kannada, English", 4.7f, "12 years experience"),
            GuideData("Anita Sharma", "Archaeology PhD", ImageUrls.BADAMI, "English, Hindi", 4.6f, "8 years experience"),
            GuideData("Mohammed Ali", "Cultural Expert", ImageUrls.GOL_GUMBAZ, "Kannada, Urdu, English", 4.9f, "18 years experience")
        )
    }
    var searchQuery by remember { mutableStateOf("") }
    val filtered = guides.filter { it.name.contains(searchQuery, true) || it.specialty.contains(searchQuery, true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heritage Guides") },
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search guides...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = VirasatMaroon) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PersonSearch, null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No guides found", fontSize = 16.sp, color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filtered) { guide ->
                        GuideCard(guide = guide, onClick = { onGuideClick(guide.name) }, onBook = { onBookGuide(guide.name) })
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

data class GuideData(
    val name: String,
    val specialty: String,
    val photoUrl: String,
    val languages: String,
    val rating: Float,
    val experience: String
)

@Composable
fun GuideCard(guide: GuideData, onClick: () -> Unit, onBook: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AsyncImage(
                model = guide.photoUrl,
                contentDescription = guide.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(guide.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VirasatMaroon)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = VirasatGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${guide.rating}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(guide.specialty, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, null, tint = VirasatMaroon, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(guide.languages, fontSize = 13.sp, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Work, null, tint = VirasatMaroon, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(guide.experience, fontSize = 13.sp, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onClick,
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("View Profile", fontSize = 13.sp)
                    }
                    Button(
                        onClick = onBook,
                        modifier = Modifier.weight(1f).height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Book", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
