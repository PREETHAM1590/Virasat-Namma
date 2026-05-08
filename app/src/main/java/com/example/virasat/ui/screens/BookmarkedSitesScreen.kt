package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.BeVietnamPro
import com.example.virasat.ui.theme.PlusJakartaSans

@Composable
fun BookmarkedSitesScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    val bookmarked = remember { KarnatakaSites.allSites.filter { it.isFavourite } }
    val sampleSites = remember {
        listOf(
            SampleBookmarkedSite(
                "mossy-ziggurat",
                "The Mossy Ziggurat",
                "A forgotten architectural marvel slowly being reclaimed by the ancient forest. Explore the terraced levels where rare flora now blooms amidst carved stone.",
                "Eastern Highlands",
                "HERITAGE RESERVE",
                KarnatakaSites.allSites.firstOrNull()?.imageUrl ?: ""
            ),
            SampleBookmarkedSite(
                "veridian-glasshouse",
                "Veridian Glasshouse",
                "Step into a living archive of endangered plant species housed within an elegant Victorian-era glass structure.",
                "Royal Gardens District",
                "BOTANICAL SANCTUARY",
                KarnatakaSites.allSites.drop(1).firstOrNull()?.imageUrl ?: ""
            ),
            SampleBookmarkedSite(
                "whispering-cascades",
                "The Whispering Cascades",
                "A sacred water site known for its unique acoustic properties. The polished stones here have been smoothed by centuries.",
                "Valley of Stones",
                "NATURAL WONDER",
                KarnatakaSites.allSites.drop(2).firstOrNull()?.imageUrl ?: ""
            )
        )
    }

    val displaySites = if (bookmarked.isNotEmpty()) bookmarked else emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Fixed top bar
        Surface(
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .statusBarsPadding()
                    .padding(top = 4.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onBack)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    "Virasat",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = { })
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Page heading
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            Text(
                text = "Saved Journeys",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Revisit the sanctuaries and heritage sites you've curated for your next expedition.",
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = BeVietnamPro),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (displaySites.isEmpty()) {
            // Show sample cards from HTML design when no real bookmarks
            sampleSites.forEach { site ->
                SavedJourneyCard(
                    site = site,
                    onClick = { onSiteClick(site.id) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            displaySites.forEach { site ->
                val sampleSite = SampleBookmarkedSite(
                    id = site.id,
                    name = site.name,
                    description = "${site.type.name.replaceFirstChar { it.uppercase() }} site in ${site.district}.",
                    location = site.location,
                    tag = site.type.name.uppercase(),
                    imageUrl = site.imageUrl
                )
                SavedJourneyCard(
                    site = sampleSite,
                    onClick = { onSiteClick(site.id) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

data class SampleBookmarkedSite(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val tag: String,
    val imageUrl: String
)

@Composable
private fun SavedJourneyCard(site: SampleBookmarkedSite, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(180.dp)) {
            // Image side
            AsyncImage(
                model = site.imageUrl,
                contentDescription = site.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )

            // Content side
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box {
                    // Heart icon (top right)
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Saved",
                            tint = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text(
                                site.tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = BeVietnamPro,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            site.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            site.description,
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = BeVietnamPro),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                }

                // Bottom row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            site.location,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = BeVietnamPro,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(onClick = onClick)
                    ) {
                        Text(
                            "View Details",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = BeVietnamPro,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(14.dp)
                                .graphicsLayer(rotationZ = 180f)
                        )
                    }
                }
            }
        }
    }
}
