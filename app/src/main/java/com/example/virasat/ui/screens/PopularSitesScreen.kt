package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.ui.theme.OnSecondaryFixed
import com.example.virasat.ui.theme.SecondaryFixed

data class RegionCard(
    val name: String,
    val subtitle: String,
    val imageUrl: String
)

@Composable
fun PopularSitesScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    val ctx = LocalContext.current
    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val sites by produceState<List<HeritageSite>>(emptyList(), ctx) {
        value = repo.getAllSitesList()
    }
    val listState = rememberLazyListState()

    val regions = remember(sites) {
        listOf(
            RegionCard("Vijayanagara Circuit", "Ballari, Karnataka", sites.find { it.id == "hampi" }?.imageUrl ?: "https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=400"),
            RegionCard("Hoysala Trail", "Hassan, Karnataka", sites.find { it.id == "belur-halebidu" }?.imageUrl ?: "https://images.unsplash.com/photo-1673779376455-b203cef903d1?w=400"),
            RegionCard("Royal Mysore", "Mysuru, Karnataka", sites.find { it.id == "mysore-palace" }?.imageUrl ?: "https://images.unsplash.com/photo-1600112356915-089abb8fc71a?w=400")
        )
    }

    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
    ) {
        // TopAppBar (per design)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = cs.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Virasat",
                style = type.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
                color = cs.primary
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = cs.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 128.dp)
        ) {
            // "Popular" header
            item {
                Text(
                    text = "Popular",
                    style = type.displayLarge.copy(fontWeight = FontWeight.Bold),
                    color = cs.onBackground,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            // Horizontal scroll cards
            item {
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    items(sites.take(3)) { site ->
                        Box(
                            modifier = Modifier
                                .width(280.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(OnSecondaryFixed)
                                .clickable { onSiteClick(site.id) }
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(224.dp)
                                ) {
                                    AsyncImage(
                                        model = site.imageUrl,
                                        contentDescription = site.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        OnSecondaryFixed.copy(alpha = 0.4f),
                                                        OnSecondaryFixed.copy(alpha = 0.95f)
                                                    ),
                                                    startY = 0f,
                                                    endY = Float.POSITIVE_INFINITY
                                                )
                                            )
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp, vertical = 16.dp)
                                        .offset(y = (-64).dp)
                                ) {
                                    Text(
                                        text = site.name,
                                        style = type.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = cs.surfaceContainerLowest
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = SecondaryFixed.copy(alpha = 0.9f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = site.location,
                                            style = type.bodyMedium,
                                            color = SecondaryFixed.copy(alpha = 0.9f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // "Discover Regions" header
            item {
                Text(
                    text = "Discover Regions",
                    style = type.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.onBackground,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
                )
            }

            // Overlapping region cards
            items(regions) { region ->
                val overlapOffset = if (regions.indexOf(region) > 0) (-48).dp else 0.dp
                val zIndex = regions.indexOf(region).toFloat()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(288.dp)
                        .padding(horizontal = 24.dp)
                        .offset(y = overlapOffset)
                        .zIndex(zIndex)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    AsyncImage(
                        model = region.imageUrl,
                        contentDescription = region.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    ),
                                    startY = 50f
                                )
                            )
                    )
                    // Glass pill at bottom
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(cs.surfaceContainerLowest.copy(alpha = 0.2f))
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = region.name,
                                style = type.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White
                            )
                            Text(
                                text = region.subtitle,
                                style = type.labelMedium.copy(letterSpacing = 0.05.sp),
                                color = SecondaryFixed,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(cs.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Go",
                                tint = cs.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}
