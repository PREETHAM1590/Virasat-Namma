package com.example.virasat.ui.screens

import android.content.Intent
import com.example.virasat.util.LocaleHelper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.virasat.R
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.util.LocationUtils

@Composable
fun SiteDetailScreen(
    siteId: String,
    onBack: () -> Unit,
    onAudioGuide: (String) -> Unit,
    onCheckIn: () -> Unit,
    onImmersiveView: (String) -> Unit,
    onGallery: (String) -> Unit,
    onReviews: (String) -> Unit,
    onTalkingTour: (String) -> Unit,
    onQuiz: (String) -> Unit
) {
    val ctx = LocalContext.current
    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val scope = rememberCoroutineScope()
    var isLoading by remember(siteId) { mutableStateOf(true) }
    val site by produceState<com.example.virasat.data.model.HeritageSite?>(null, siteId) {
        value = try {
            repo.getSiteById(siteId) ?: KarnatakaSites.allSites.find { it.id == siteId }
        } catch (_: Exception) {
            KarnatakaSites.allSites.find { it.id == siteId }
        }
        isLoading = false
    }
    var isFav by remember { mutableStateOf(false) }
    LaunchedEffect(siteId) {
        isFav = try { repo.isBookmarked(siteId) } catch (_: Exception) { false }
    }

    // Preload Kannada translations if locale is Kannada
    LaunchedEffect(site) {
        if (site != null && LocaleHelper.getSavedLocale(ctx) == "kn") {
            com.example.virasat.data.source.KannadaContentProvider.preloadSite(ctx, site!!)
        }
    }

    var userLocation by remember { mutableStateOf<android.location.Location?>(null) }
    LaunchedEffect(Unit) {
        if (ctx.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED ||
            ctx.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            try {
                userLocation = LocationUtils.fetchCurrentLocation(ctx)
            } catch (_: Exception) { }
        }
    }
    val distanceText = remember(userLocation, site) {
        userLocation?.let { loc ->
            site?.let { s ->
                val km = LocationUtils.distanceKm(loc.latitude, loc.longitude, s.latitude, s.longitude)
                LocationUtils.formatDistance(km)
            }
        } ?: site?.district ?: ""
    }

    if (isLoading) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }
    if (site == null) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(R.string.detail_site_not_found),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }
    val s = site!!

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            // Hero Section
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(442.dp)
            ) {
                val fallbackUrl = com.example.virasat.data.source.ImageUrls.SOUTH_INDIAN_TEMPLE
                AsyncImage(
                    model = s.imageUrl.ifBlank { fallbackUrl },
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                            )
                        )
                )
                // Top nav
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        val shareText = "${s.name} - ${s.location}\n${s.shortDescription}\nhttps://virasat.app/site/${s.id}"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        ctx.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    }) {
                        Icon(
                            Icons.Default.Share,
                            stringResource(R.string.share),
                            tint = Color.White
                        )
                    }
                }
                // Floating Fav FAB overlapping bottom right
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 32.dp)
                        .offset(y = 32.dp)
                        .size(64.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .clickable {
                            scope.launch { isFav = repo.toggleBookmark(siteId) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        if (isFav) stringResource(R.string.saved) else stringResource(R.string.save),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(48.dp))
            Column(Modifier.padding(horizontal = 24.dp)) {
                // Location tag
                Box(
                    Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        s.district.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(R.string.detail_title, s.name),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(24.dp))

                // Stats Pill
                Row(
                    Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            distanceText,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        Modifier
                            .width(1.dp)
                            .height(16.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFF59E0B)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "${s.rating} (${formatCount(s.reviews)} ${stringResource(R.string.reviews)})",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionChip(stringResource(R.string.detail_gallery), Icons.Default.Image) { onGallery(siteId) }
                    ActionChip(stringResource(R.string.detail_check_in), Icons.Default.QrCodeScanner) { onCheckIn() }
                    ActionChip(stringResource(R.string.detail_talking_tour), Icons.Default.Mic) { onTalkingTour(siteId) }
                    ActionChip(stringResource(R.string.detail_quiz), Icons.Default.Quiz) { onQuiz(siteId) }
                }
                Spacer(Modifier.height(32.dp))

                // Action Area
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            stringResource(R.string.detail_audio_guide),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            stringResource(R.string.detail_free),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            stringResource(R.string.detail_narrated),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = { onAudioGuide(siteId) },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(stringResource(R.string.detail_listen), style = MaterialTheme.typography.labelLarge)
                        Icon(
                            Icons.Default.PlayArrow,
                            null,
                            modifier = Modifier.padding(start = 4.dp).size(18.dp)
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
                Spacer(Modifier.height(32.dp))

                // Description Card
                Box(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(32.dp)
                ) {
                    Column {
                        Text(
                            stringResource(R.string.detail_about),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            LocaleHelper.siteDescription(s, ctx),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            LocaleHelper.siteHistory(s, ctx),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> "${count / 1000}.${(count % 1000) / 100}k"
        else -> count.toString()
    }
}

@Composable
private fun ActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
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
