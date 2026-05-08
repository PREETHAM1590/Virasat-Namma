package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BadgesScreen(onBack: () -> Unit) {
    val earnedBadges = listOf(
        BadgeItem("First Sprout", Icons.Default.Eco, true),
        BadgeItem("Pilgrim", Icons.Default.TempleHindu, true),
        BadgeItem("Pathfinder", Icons.Default.Map, true)
    )

    val lockedBadges = listOf(
        BadgeItem("Curator", Icons.Default.Museum, false),
        BadgeItem("Botanist", Icons.Default.LocalFlorist, false),
        BadgeItem("Elder", Icons.Default.Groups, false),
        BadgeItem("UNESCO Explorer", Icons.Default.Public, false),
        BadgeItem("Audio Aficionado", Icons.Default.Headset, false),
        BadgeItem("Master Explorer", Icons.Default.Star, false)
    )

    val earned = earnedBadges.size
    val total = earnedBadges.size + lockedBadges.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .verticalScroll(rememberScrollState())
    ) {
        // TopAppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .statusBarsPadding()
                .padding(top = 4.dp),
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
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
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

        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Custodian Badges",
                style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "You have earned $earned of $total badges",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

        // Grid of badges
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 1200.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false
        ) {
            items(earnedBadges) { badge ->
                BadgeGridCard(badge = badge)
            }
            items(lockedBadges) { badge ->
                BadgeGridCard(badge = badge)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BadgeGridCard(badge: BadgeItem) {
    val isEarned = badge.unlocked
    val blobShape = when (badge.name) {
        "First Sprout", "Curator" -> RoundedCornerShape(
            topStartPercent = 43, topEndPercent = 57,
            bottomStartPercent = 30, bottomEndPercent = 70
        )
        "Pilgrim", "Botanist" -> RoundedCornerShape(
            topStartPercent = 50, topEndPercent = 50,
            bottomStartPercent = 30, bottomEndPercent = 70
        )
        else -> RoundedCornerShape(
            topStartPercent = 30, topEndPercent = 70,
            bottomStartPercent = 50, bottomEndPercent = 50
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isEarned)
            MaterialTheme.colorScheme.surfaceContainerLowest
        else
            MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f),
        shadowElevation = 2.dp,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Organic blob icon container
            Surface(
                shape = blobShape,
                color = if (isEarned)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(96.dp),
                border = if (!isEarned) BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                ) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = badge.icon,
                        contentDescription = badge.name,
                        tint = if (isEarned)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                badge.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (isEarned)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                if (isEarned) "Earned" else "Locked",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = if (isEarned)
                    MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

data class BadgeItem(val name: String, val icon: ImageVector, val unlocked: Boolean)
