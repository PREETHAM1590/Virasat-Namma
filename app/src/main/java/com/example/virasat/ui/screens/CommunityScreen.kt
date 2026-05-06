package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun CommunityScreen(onBack: () -> Unit) {
    val posts = listOf(
        CommunityPost("Priya Sharma", "Just returned from Hampi! The sunset at Hemakuta Hill is magical.", ImageUrls.HAMPI, "3h ago", 24),
        CommunityPost("Ravi Kumar", "Badami caves are underrated. The climb is worth every step.", ImageUrls.BADAMI, "5h ago", 18),
        CommunityPost("Ananya Rao", "Loved the audio guide at Mysore Palace. Learned so much!", ImageUrls.MYSORE_PALACE, "1d ago", 42),
        CommunityPost("Vikram Patil", "Quiz challenge was fun. Scored 4/5 on Hampi trivia!", ImageUrls.QUIZ_THUMBNAIL, "2d ago", 12)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Community") },
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
            items(posts) { post ->
                PostCard(post = post)
            }
        }
    }
}

@Composable
fun PostCard(post: CommunityPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(VirasatMaroon, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(post.author.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(post.author, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = VirasatMaroon)
                    Text(post.time, fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(post.body, fontSize = 14.sp, color = Color.DarkGray)
            if (post.imageUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${post.likes}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

data class CommunityPost(val author: String, val body: String, val imageUrl: String, val time: String, val likes: Int)
