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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.virasat.R
import coil.compose.AsyncImage
import com.example.virasat.data.model.CheckIn
import com.example.virasat.data.source.ImageUrls
import com.example.virasat.viewmodel.PassportViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TravelPassportScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit,
    viewModel: PassportViewModel = viewModel()
) {
    val checkIns by viewModel.checkIns.collectAsState()
    val uniqueSites by viewModel.uniqueSiteCount.collectAsState()
    // #19: level derived from unique site count (every 5 unique sites = 1 level, min 1)
    val passportLevel = maxOf(1, uniqueSites / 5 + 1)
    val scrollState = rememberScrollState()
    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(scrollState)
    ) {
        // Header section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    cs.onSecondaryContainer,
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(bottom = 64.dp)
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = cs.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable(onClick = onBack)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = type.headlineLarge,
                    color = cs.onPrimaryContainer
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_title),
                    tint = cs.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Avatar + name + level
            val context = androidx.compose.ui.platform.LocalContext.current
            val userPrefs = remember { context.getSharedPreferences("virasat_prefs", android.content.Context.MODE_PRIVATE) }
            val userName = remember { userPrefs.getString("user_name", null) ?: context.getString(R.string.profile_heritage_explorer) }
            val initials = userName.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("").ifBlank { "HE" }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .shadow(8.dp, CircleShape)
                        .background(cs.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        style = type.headlineLarge,
                        color = cs.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.passport_title),
                    style = type.headlineLarge,
                    color = cs.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = cs.primaryContainer,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = cs.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(R.string.passport_level, passportLevel),
                            style = type.labelLarge,
                            color = cs.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // Passport Badges neumorphic card (overlapping header)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-32).dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = cs.surfaceContainerLowest,
                shadowElevation = 4.dp,
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.passport_badges),
                            style = type.headlineMedium,
                            color = cs.onSurface
                        )
                        Text(
                            text = stringResource(R.string.view_all),
                            style = type.labelLarge,
                            color = cs.primary
                        )
                    }
                    // Badge grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        BadgeItem(
                            icon = { modifier ->
                                Surface(
                                    shape = CircleShape,
                                    color = cs.secondaryContainer,
                                    modifier = modifier
                                        .size(64.dp)
                                        .shadow(2.dp, CircleShape),
                                    contentColor = cs.onSecondaryContainer
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "",
                                            style = type.displayLarge,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        ) // landscape
                                    }
                                }
                            },
                            label = "Temples"
                        )
                        BadgeItem(
                            icon = { modifier ->
                                Surface(
                                    shape = CircleShape,
                                    color = cs.tertiaryContainer,
                                    modifier = modifier
                                        .size(64.dp)
                                        .shadow(2.dp, CircleShape),
                                    contentColor = cs.onTertiaryContainer
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "🕉️",
                                            style = type.displayLarge,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                }
                            },
                            label = "Hoysala"
                        )
                        BadgeItem(
                            icon = { modifier ->
                                Surface(
                                    shape = CircleShape,
                                    color = cs.errorContainer,
                                    modifier = modifier
                                        .size(64.dp)
                                        .shadow(2.dp, CircleShape),
                                    contentColor = cs.onErrorContainer
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "🏛️",
                                            style = type.displayLarge,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                }
                            },
                            label = "Palaces"
                        )
                        BadgeItem(
                            icon = { modifier ->
                                Surface(
                                    shape = CircleShape,
                                    color = cs.surfaceDim,
                                    modifier = modifier
                                        .size(64.dp)
                                        .shadow(0.dp, CircleShape),
                                    contentColor = cs.outline
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            },
                            label = "Locked",
                            isLocked = true
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.passport_recent_journeys),
            style = type.headlineMedium,
            color = cs.onBackground,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
        )

        if (checkIns.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                checkIns.take(4).forEach { checkIn ->
                    JourneyItem(checkIn = checkIn, onClick = { onSiteClick(checkIn.siteId) })
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = cs.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.passport_no_checkins),
                        style = type.bodyLarge,
                        color = cs.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.passport_no_checkins_hint),
                        style = type.bodyMedium,
                        color = cs.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BadgeItem(
    icon: @Composable (Modifier) -> Unit,
    label: String,
    isLocked: Boolean = false
) {
    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon(Modifier)
        Text(
            text = label,
            style = type.labelSmall,
            color = if (isLocked) cs.outline else cs.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun JourneyItem(checkIn: CheckIn, onClick: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = cs.surfaceContainerLowest,
        shadowElevation = 4.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.size(112.dp)
            ) {
                AsyncImage(
                    model = ImageUrls.TEMPLE_SILHOUETTE,
                    contentDescription = checkIn.siteName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = cs.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = stringResource(R.string.passport_completed),
                            style = type.labelMedium,
                            color = cs.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    Icon(
                        Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = cs.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = checkIn.siteName,
                    style = type.bodyLarge,
                    color = cs.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${dateFormat.format(Date(checkIn.timestamp))} • ${checkIn.siteLocation}",
                    style = type.bodyMedium,
                    color = cs.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SampleJourneyItem(
    title: String,
    date: String,
    location: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = cs.surfaceContainerLowest,
        shadowElevation = 4.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.size(112.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = cs.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = stringResource(R.string.passport_completed),
                            style = type.labelMedium,
                            color = cs.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    Icon(
                        Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = cs.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = type.bodyLarge,
                    color = cs.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$date • $location",
                    style = type.bodyMedium,
                    color = cs.onSurfaceVariant
                )
            }
        }
    }
}
