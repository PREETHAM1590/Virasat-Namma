package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.virasat.R
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLanguage: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(2500L)
        onNavigateToLanguage()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VirasatCream)
    ) {
        // Top subtle gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.4f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ornate Circular Logo Frame
            Box(
                modifier = Modifier.size(220.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer decorative ring
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.dp, VirasatGold.copy(alpha = 0.4f), CircleShape)
                )
                // Middle ring
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .border(3.dp, VirasatGold, CircleShape)
                        .padding(6.dp)
                        .border(1.dp, VirasatGold.copy(alpha = 0.6f), CircleShape)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8E0D0))
                ) {
                    AsyncImage(
                        model = R.drawable.logo_main,
                        contentDescription = "Virasat Karnataka Heritage Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Top ornamental flourish
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-4).dp)
                        .size(20.dp, 12.dp)
                        .background(VirasatGold, RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Title
            Text(
                text = "VIRASAT",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = VirasatMaroon,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "NAMMA GUIDE",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = VirasatMaroon,
                letterSpacing = 5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Divider with diamond
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(180.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(VirasatGold)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(6.dp)
                        .background(VirasatGold, RoundedCornerShape(1.dp))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(VirasatGold)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Karnataka Heritage Explorer",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5A5A5A),
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        }

        // Bottom Karnataka Temple Silhouette
        AsyncImage(
            model = "https://images.unsplash.com/photo-1684830235389-60a88c4e626e?w=800",
            contentDescription = "Karnataka Temple",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .align(Alignment.BottomCenter)
        )
    }
}
