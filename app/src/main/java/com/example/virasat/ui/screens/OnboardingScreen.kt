package com.example.virasat.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.virasat.ui.theme.Surface
import com.example.virasat.ui.theme.Primary
import com.example.virasat.ui.theme.OnSurfaceVariant
import com.example.virasat.ui.theme.OnSecondaryFixed
import com.example.virasat.ui.theme.PrimaryContainer
import com.example.virasat.ui.theme.OnPrimaryContainer

data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

val onboardingPages = listOf(
    OnboardingPage(
        "Discover Karnataka",
        "Explore 6 UNESCO World Heritage sites, ancient temples, palaces, and monuments across Karnataka.",
        "https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=800"
    ),
    OnboardingPage(
        "Scan. Learn. Collect.",
        "Scan QR codes at heritage sites, unlock audio guides, earn check-in stamps, and build your travel passport.",
        "https://images.unsplash.com/photo-1600112356915-089abb8fc71a?w=800"
    ),
    OnboardingPage(
        "Step Into Time",
        "From the ruins of Hampi to the whispering domes of Gol Gumbaz — Karnataka's heritage awaits.",
        "https://images.unsplash.com/photo-1673779376455-b203cef903d1?w=800"
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .background(Surface)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            Column(Modifier.fillMaxSize()) {
                // Top 60%: Hero Image with rounded bottom
                Box(Modifier.height(530.dp)) {
                    AsyncImage(
                        model = onboardingPages[pageIndex].imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = 64.dp, bottomEnd = 64.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = 64.dp, bottomEnd = 64.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.2f))
                                )
                            )
                    )
                }

                // Content area
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val page = onboardingPages[pageIndex]
                    Text(
                        page.title,
                        style = MaterialTheme.typography.displayLarge,
                        color = if (pageIndex == 0) {
                            Primary
                        } else {
                            OnSecondaryFixed
                        },
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        page.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }

        // Floating Navigation Pill at bottom
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(OnSecondaryFixed)
                    .padding(start = 20.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(onboardingPages.size) { i ->
                        Box(
                            Modifier
                                .then(
                                    if (i == pagerState.currentPage) {
                                        Modifier.width(32.dp)
                                    } else {
                                        Modifier.size(8.dp)
                                    }
                                )
                                .height(8.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    if (i == pagerState.currentPage) {
                                        PrimaryContainer
                                    } else {
                                        com.example.virasat.ui.theme.TertiaryContainer.copy(alpha = 0.3f)
                                    }
                                )
                        )
                    }
                }

                // Seamless action pill
                val isLastPage = pagerState.currentPage == onboardingPages.size - 1
                val actionLabel = if (isLastPage) "Get Started" else "Next"

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(PrimaryContainer)
                        .clickable {
                            if (pagerState.currentPage < onboardingPages.size - 1) {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            } else {
                                onFinish()
                            }
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            actionLabel,
                            style = MaterialTheme.typography.labelLarge,
                            color = OnPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = OnPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
