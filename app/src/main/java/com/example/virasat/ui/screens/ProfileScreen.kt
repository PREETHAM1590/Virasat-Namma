package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onBookmarks: () -> Unit,
    onPassport: () -> Unit,
    onLogout: () -> Unit,
    onBadges: () -> Unit = {},
    onCheckIns: () -> Unit = {},
    onGuides: () -> Unit = {},
    onCommunity: () -> Unit = {},
    onFeedback: () -> Unit = {},
    onHelp: () -> Unit = {},
    onSitesList: () -> Unit = {},
    onLeaderboard: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefs = androidx.compose.runtime.remember { context.getSharedPreferences("virasat_prefs", android.content.Context.MODE_PRIVATE) }
    val userName = androidx.compose.runtime.remember { userPrefs.getString("user_name", null) ?: "Heritage Explorer" }
    val userEmail = androidx.compose.runtime.remember { userPrefs.getString("user_email", null) ?: "" }
    val scrollState = rememberScrollState()
    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.secondaryContainer)
            .verticalScroll(scrollState)
    ) {
        // Top bar: eco icon, title, search
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                tint = cs.primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "Virasat",
                style = type.headlineLarge,
                color = cs.primary
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = cs.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Hero Profile Card with overlapping avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp),
                shape = RoundedCornerShape(16.dp),
                color = cs.surfaceContainerLowest,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp, bottom = 32.dp)
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = userName,
                        style = type.headlineMedium,
                        color = cs.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Karnataka Heritage Passport\nSites Visited & Facts Unlocked",
                        style = type.bodyMedium,
                        color = cs.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Overlapping avatar (centered, half in card half above)
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-32).dp)
                    .clip(CircleShape)
                    .shadow(8.dp, CircleShape)
                    .background(cs.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                val initials = userName.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")
                Text(
                    text = initials.ifBlank { "HE" },
                    style = type.headlineLarge,
                    color = cs.onPrimaryContainer,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }

        // Floating Profile Stats (overlapping card)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-20).dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Trails pill
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = cs.primaryContainer,
                shadowElevation = 4.dp,
                tonalElevation = 2.dp,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        tint = cs.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "6",
                            style = type.headlineMedium,
                            color = cs.onPrimaryContainer
                        )
                        Text(
                            text = "Heritage Sites",
                            style = type.labelMedium,
                            color = cs.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Sites pill
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = cs.surfaceContainerLowest,
                shadowElevation = 4.dp,
                tonalElevation = 2.dp,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = cs.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "---",
                            style = type.headlineMedium,
                            color = cs.onSurface
                        )
                        Text(
                            text = "Check-ins",
                            style = type.labelMedium,
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Menu buttons (rounded-full white containers)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuRow(
                icon = Icons.Default.Bookmark,
                label = "Saved Locations",
                onClick = onBookmarks
            )
            MenuRow(
                icon = Icons.Default.History,
                label = "Exploration History",
                onClick = onCheckIns
            )
            MenuRow(
                icon = Icons.Default.Settings,
                label = "Preferences",
                onClick = onSettings
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign out button
        Surface(
            onClick = onLogout,
            shape = RoundedCornerShape(999.dp),
            color = cs.errorContainer.copy(alpha = 0.5f),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(cs.errorContainer.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = null,
                        tint = cs.onErrorContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Sign Out",
                    style = type.headlineMedium,
                    color = cs.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = cs.surfaceContainerLowest,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(cs.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = cs.onSecondaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = type.headlineMedium,
                color = cs.onSecondaryContainer,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = cs.onSecondaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
