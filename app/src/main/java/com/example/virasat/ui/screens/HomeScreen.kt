package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.components.BottomNavItem
import com.example.virasat.ui.theme.Primary
import com.example.virasat.ui.theme.OnSurfaceVariant
import com.example.virasat.ui.theme.PrimaryContainer
import com.example.virasat.ui.theme.OnPrimaryContainer
import com.example.virasat.ui.theme.SurfaceContainerLowest
import com.example.virasat.ui.theme.OnSurface
import com.example.virasat.ui.theme.SecondaryContainer
import com.example.virasat.ui.theme.OutlineVariant
import com.example.virasat.ui.theme.NavBackground

@Composable
fun HomeScreen(
    onSiteClick: (String) -> Unit,
    onQrScan: () -> Unit,
    onPassport: () -> Unit,
    onFavourites: () -> Unit,
    onNavItemClick: (BottomNavItem) -> Unit,
    onBadges: () -> Unit,
    onQuiz: () -> Unit,
    onAiAssistant: () -> Unit,
    onGuides: () -> Unit,
    onItinerary: () -> Unit,
    onSearch: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(com.example.virasat.ui.theme.Background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                // Top Navigation
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = onSearch,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Eco, 
                            contentDescription = "Search",
                            tint = Primary
                        )
                    }
                }
            }

            item {
                // Header Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            "Hi, Ananya 👋",
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurfaceVariant
                        )
                        Text(
                            "Heritage\nPower",
                            style = MaterialTheme.typography.displayLarge.copy(lineHeight = 48.sp),
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    // Weather Widget
                    Row(
                        modifier = Modifier
                            .shadow(4.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                            .clip(RoundedCornerShape(999.dp))
                            .background(SurfaceContainerLowest)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("☀️", fontSize = 20.sp)
                        Text(
                            "15°C",
                            style = MaterialTheme.typography.labelLarge,
                            color = OnSurface
                        )
                    }
                }
            }

            item {
                // Categories
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    item {
                        Text(
                            "Hiking",
                            modifier = Modifier
                                .shadow(8.dp, RoundedCornerShape(999.dp), spotColor = Primary.copy(alpha = 0.2f))
                                .clip(RoundedCornerShape(999.dp))
                                .background(PrimaryContainer)
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = OnPrimaryContainer
                        )
                    }
                    item {
                        Text(
                            "Rivers",
                            modifier = Modifier
                                .shadow(4.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                                .clip(RoundedCornerShape(999.dp))
                                .background(SurfaceContainerLowest)
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = OnSurface
                        )
                    }
                }
            }

            item {
                // Main Hero
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .aspectRatio(4f/5f)
                        .shadow(24.dp, RoundedCornerShape(48.dp), spotColor = Color.Black.copy(alpha = 0.15f))
                        .clip(RoundedCornerShape(48.dp))
                ) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCJP-Loh4tzULL7Ziu8cjoozV-a5KLW1IxRMKhKdpQnsUSDaXKfbx8WlZRbmdoXTUx8E2D6Sja-O2kzZYP_s9bprszI_abspgCUsaJoJ5GG1MKlC8iqiidPaaeV2isVmASXpDiofVZ_X2IVGKkkc2j9jsISeZa6aQqDfbcWoh6EPhX2RZGgBG0YgwwqtQNrOFbfufUBeBJhUu36ZEcT9VPmHJC0yjN-yAvMCSrb115mLzPCk2aJrV0TvPYwkLsJ1RX0Akw2tltdycw",
                        contentDescription = "Lush forest canopy",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.6f)),
                                    startY = 0f
                                )
                            )
                    )
                    
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(32.dp)
                    ) {
                        Text(
                            "Featured Audio",
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "The Sounds of Nature",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Immerse yourself in the ancient whispers of the forest and the flowing rhythms of hidden rivers.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Curated Trails
            item {
                Spacer(Modifier.height(48.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Curated Trails",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "View All",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onGuides() }
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TrailCard(
                        "Ancient Temple Route",
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer,
                        "Discover ruins dating back to the 12th century along this scenic 5km trek.",
                        Modifier.weight(1f)
                    )
                    TrailCard(
                        "Valley of Flowers",
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.onPrimaryContainer,
                        "A vibrant seasonal hike showcasing endemic flora and breathtaking mountain views.",
                        Modifier.weight(1f)
                    )
                }
            }

            // Sites
            item {
                Spacer(Modifier.height(48.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Nearby Heritage Sites",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "View All",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onFavourites() }
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
            
            items(KarnatakaSites.allSites) { site ->
                Box(
                    Modifier
                        .padding(horizontal = 24.dp, vertical = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .clickable { onSiteClick(site.id) }
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = site.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(999.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                site.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                site.location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }

        // Floating Nav
        Box(Modifier.align(Alignment.BottomCenter)) {
            com.example.virasat.ui.components.VirasatBottomNavBar(currentRoute = "home", onItemClick = onNavItemClick)
        }
    }
}

@Composable
private fun QuickAction(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TrailCard(
    title: String,
    iconBg: androidx.compose.ui.graphics.Color,
    iconColor: androidx.compose.ui.graphics.Color,
    desc: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(24.dp)
    ) {
        Column {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "◆",
                    fontSize = 20.sp,
                    color = iconColor
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(
                desc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
