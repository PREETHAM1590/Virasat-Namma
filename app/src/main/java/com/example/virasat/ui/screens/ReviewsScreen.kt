package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val site = KarnatakaSites.allSites.find { it.id == siteId }
    val reviews = listOf(
        ReviewItem("Heritage Explorer", "Absolutely stunning architecture. The stone carvings are breathtaking!", 5, "2 days ago"),
        ReviewItem("TravelBug", "Great experience but gets crowded on weekends. Visit early morning.", 4, "1 week ago"),
        ReviewItem("HistoryBuff", "A must-visit for anyone interested in South Indian history.", 5, "2 weeks ago"),
        ReviewItem("WeekendWanderer", "Good maintenance and helpful audio guide.", 4, "3 weeks ago")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${site?.name ?: "Site"} Reviews") },
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
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${site?.rating ?: 4.5f}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row {
                            repeat(5) { i ->
                                Icon(Icons.Default.Star, null, tint = if (i < (site?.rating?.toInt() ?: 4)) VirasatGold else Color.LightGray, modifier = Modifier.size(16.dp))
                            }
                        }
                        Text("${site?.reviews ?: 0} reviews", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(reviews.size) { index ->
                ReviewCard(review = reviews[index])
            }
        }
    }
}

@Composable
fun ReviewCard(review: ReviewItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(VirasatMaroon, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(review.author.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.author, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = VirasatMaroon)
                    Text(review.time, fontSize = 12.sp, color = Color.Gray)
                }
                Row {
                    repeat(5) { i ->
                        Icon(Icons.Default.Star, null, tint = if (i < review.rating) VirasatGold else Color.LightGray, modifier = Modifier.size(14.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.body, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

data class ReviewItem(val author: String, val body: String, val rating: Int, val time: String)
