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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.virasat.R
import com.example.virasat.data.di.RepositoryProvider
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
    val userName = androidx.compose.runtime.remember { userPrefs.getString("user_name", null) ?: context.getString(R.string.profile_heritage_explorer) }
    val userEmail = androidx.compose.runtime.remember { userPrefs.getString("user_email", null) ?: "" }
    val repo = remember(context) { RepositoryProvider.getRepository(context) }
    val allSites by produceState(0, context) { value = repo.getAllSitesList().size }
    val checkInCount by produceState(0, context) { repo.getCheckInCount().collect { value = it } }
    val unlockedFacts by produceState(0, context) { repo.getUnlockedFactCount().collect { value = it } }
    val scrollState = rememberScrollState()
    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
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
                text = stringResource(R.string.app_name),
                style = type.headlineLarge,
                color = cs.primary
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_title),
                tint = cs.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Profile Card with overlapping avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                shape = RoundedCornerShape(16.dp),
                color = cs.surfaceContainerLowest,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 64.dp, bottom = 24.dp)
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
                        text = stringResource(R.string.profile_karnataka_passport),
                        style = type.bodyMedium,
                        color = cs.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Overlapping avatar + camera badge (centered on card top edge)
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.Center
            ) {
                // Avatar circle
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(cs.primary),
                    contentAlignment = Alignment.Center
                ) {
                    val profileImagePath = userPrefs.getString("user_profile_image", null)
                    if (profileImagePath != null) {
                        AsyncImage(
                            model = profileImagePath,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        val initials = userName.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")
                        Text(
                            text = initials.ifBlank { "HE" },
                            style = type.headlineLarge,
                            color = cs.onPrimary,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                }
                // Camera edit badge
                SmallFloatingActionButton(
                    onClick = onEditProfile,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp),
                    containerColor = cs.primaryContainer,
                    contentColor = cs.onPrimaryContainer,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Edit photo",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Edit profile button
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Surface(
                onClick = onEditProfile,
                shape = RoundedCornerShape(999.dp),
                color = cs.primaryContainer,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = cs.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = stringResource(R.string.profile_edit_profile),
                        style = type.labelLarge,
                        color = cs.onPrimaryContainer
                    )
                }
            }
        }

        // Floating Profile Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
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
                            text = allSites.toString(),
                            style = type.headlineMedium,
                            color = cs.onPrimaryContainer
                        )
                        Text(
                            text = stringResource(R.string.profile_heritage_sites),
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
                            text = checkInCount.toString(),
                            style = type.headlineMedium,
                            color = cs.onSurface
                        )
                        Text(
                            text = stringResource(R.string.profile_checkins),
                            style = type.labelMedium,
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menu buttons (rounded-full white containers)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuRow(
                icon = Icons.Default.Bookmark,
                label = stringResource(R.string.profile_saved_locations),
                onClick = onBookmarks
            )
            MenuRow(
                icon = Icons.Default.History,
                label = stringResource(R.string.profile_exploration_history),
                onClick = onCheckIns
            )
            MenuRow(
                icon = Icons.Default.Settings,
                label = stringResource(R.string.profile_preferences),
                onClick = onSettings
            )
            MenuRow(
                icon = Icons.Default.CardTravel,
                label = stringResource(R.string.passport_title),
                onClick = onPassport
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
                    text = stringResource(R.string.profile_sign_out),
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
