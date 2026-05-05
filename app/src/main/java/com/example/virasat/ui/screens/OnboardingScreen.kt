package com.example.virasat.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.R
import com.example.virasat.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
    ) {
        // Top Skip Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, end = 24.dp, bottom = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Skip",
                color = AppMaroon,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onFinish() }
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 120.dp)
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    title = "Discover\nKarnataka\nHeritage",
                    description = "From Hampi's ruins to Mysore Palace grandeur. Explore 750+ heritage sites across Karnataka.",
                    imageUrl = "https://images.unsplash.com/photo-1587135941948-3b6394eb6ae9?q=80&w=800&auto=format&fit=crop"
                )
                1 -> OnboardingPage(
                    title = "Scan QR at\nHeritage Sites",
                    description = "At Hampi, Badami, Belur or Gol Gumbaz — scan QR codes to unlock hidden stories and history.",
                    imageUrl = "https://images.unsplash.com/photo-1606293926075-69a00febf780?q=80&w=800&auto=format&fit=crop"
                )
                2 -> PassportPage()
            }
        }

        // Bottom Navigation Area
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(56.dp))

            // Page Indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val isActive = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = if (isActive) AppMaroon else Color(0xFFE0CCA9),
                                shape = CircleShape
                            )
                    )
                }
            }

            // Next / Finish Button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(AppMaroon, CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        if (pagerState.currentPage < 2) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinish()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(title: String, description: String, imageUrl: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = title,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            color = AppMaroon,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(6.dp)
                .background(VirasatGold, shape = RoundedCornerShape(2.dp))
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            fontSize = 15.sp,
            color = AppText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 48.dp),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp))
                .border(
                    width = 2.dp,
                    color = VirasatGold,
                    shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.logo_hampi)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PassportPage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Travel Passport /\nCheck-ins",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = AppMaroon,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Check-in at Karnataka's UNESCO sites and collect heritage stamps from Hampi to Mysore.",
            fontSize = 16.sp,
            color = AppText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Simulated Passport UI
        Row(
            modifier = Modifier
                .height(280.dp)
                .width(320.dp)
        ) {
            // Passport Cover
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF6B1B22), RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    .border(1.dp, Color(0xFF4A0D15), RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TRAVEL\nPASSPORT",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                    
                    Box(modifier = Modifier.size(60.dp).background(Color(0xFFD4AF37).copy(alpha = 0.2f), CircleShape))
                    
                    Text(
                        text = "VIRASAT",
                        color = Color(0xFFD4AF37),
                        fontSize = 12.sp,
                        letterSpacing = 2.sp
                    )
                }
            }
            
            // Passport Pages
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(top = 4.dp, bottom = 4.dp)
                    .background(Color(0xFFFDFBF7), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Stamp(name = "HAMPI")
                    Stamp(name = "BADAMI")
                    Stamp(name = "MYSORE")
                    Stamp(name = "BELUR")
                    Stamp(name = "BIJAPUR")
                }
            }
        }
    }
}

@Composable
fun Stamp(name: String) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color(0xFFFCF8F2), CircleShape)
            .border(1.dp, VirasatGold, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = VirasatGold)
    }
}
