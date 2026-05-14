@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.virasat.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.virasat.util.LocaleHelper
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.ui.theme.Primary
import com.example.virasat.ui.theme.PrimaryContainer
import com.example.virasat.ui.theme.OnPrimaryContainer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.example.virasat.data.model.SiteType
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    val ctx = LocalContext.current
    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val allSites by produceState<List<HeritageSite>>(emptyList(), ctx) {
        value = repo.getAllSitesList()
    }
    val cs = MaterialTheme.colorScheme
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedSite by remember { mutableStateOf<HeritageSite?>(null) }

    val filters = listOf("All", "Temple", "Palace", "Fort", "Monument", "UNESCO", "Jain")
    val displaySites = remember(allSites, selectedFilter) {
        if (selectedFilter == "All") allSites
        else allSites.filter { it.type.name.equals(selectedFilter, ignoreCase = true) }
    }

    val karnatakaCenter = LatLng(15.0, 76.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(karnatakaCenter, 6.5f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
    ) {
        // Real Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = false
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = false,
                mapToolbarEnabled = false
            )
        ) {
            displaySites.forEach { site ->
                val icon = rememberMarkerIcon(ctx, site.type)
                Marker(
                    state = MarkerState(position = LatLng(site.latitude, site.longitude)),
                    title = LocaleHelper.siteName(site.name, site.nameLocal, ctx),
                    snippet = site.district,
                    icon = icon,
                    onClick = {
                        selectedSite = site
                        true
                    }
                )
            }
        }

        // Top overlay: back button + search pill + filters
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(6.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.1f))
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = cs.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .shadow(6.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.08f))
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = cs.outline,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Explore Karnataka sites...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.outline
                        )
                    }
                }
            }

            // Filter chips row
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .shadow(
                                if (isSelected) 6.dp else 2.dp,
                                RoundedCornerShape(999.dp),
                                spotColor = Primary.copy(alpha = if (isSelected) 0.2f else 0.05f)
                            )
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (isSelected) Primary else Color.White)
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            filter,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Color.White else Primary
                        )
                    }
                }
            }
        }

        // Bottom overlay: scrolling site cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            // Sites count badge
            Box(
                modifier = Modifier
                    .padding(start = 24.dp, bottom = 12.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    "${displaySites.size} site${if (displaySites.size != 1) "s" else ""} found",
                    style = MaterialTheme.typography.labelMedium,
                    color = Primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(displaySites) { site ->
                    MapBottomCard(
                        site = site,
                        isFavourite = site.isFavourite,
                        onClick = {
                            selectedSite = site
                            cameraPositionState.move(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(site.latitude, site.longitude), 12f
                                )
                            )
                            onSiteClick(site.id)
                        },
                        onFavouriteClick = { }
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun MapBottomCard(
    site: HeritageSite,
    isFavourite: Boolean,
    onClick: () -> Unit,
    onFavouriteClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .width(260.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.1f))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
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
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.25f))
                            )
                        )
                )
                // Bookmark icon top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { onFavouriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isFavourite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = if (isFavourite) Primary else cs.outline,
                        modifier = Modifier.size(16.dp)
                    )
                }
                // Type badge bottom-left
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Primary.copy(alpha = 0.85f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        site.type.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = site.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = cs.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            site.rating.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = cs.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = cs.outline,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = site.district,
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun TypeBadge(type: String) {
    val display = type.replaceFirstChar { it.uppercase() }
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            display,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


@Composable
private fun rememberMarkerIcon(context: android.content.Context, type: SiteType): com.google.android.gms.maps.model.BitmapDescriptor {
    // #15: hold raw Bitmap reference so it can be recycled when the composable leaves (#bitmap-leak fix)
    var rawBitmap: Bitmap? = null
    val descriptor = remember(type) {
        val (bmp, desc) = createMarkerBitmapPair(context, type)
        rawBitmap = bmp
        desc
    }
    androidx.compose.runtime.DisposableEffect(type) {
        onDispose { rawBitmap?.recycle() }
    }
    return descriptor
}

private fun createMarkerBitmapPair(context: android.content.Context, type: SiteType): Pair<Bitmap, com.google.android.gms.maps.model.BitmapDescriptor> {
    val size = 96
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val bgColor = when (type) {
        SiteType.TEMPLE -> 0xFFFF6F00.toInt()
        SiteType.PALACE -> 0xFF7B1FA2.toInt()
        SiteType.FORT -> 0xFFE65100.toInt()
        SiteType.MONUMENT -> 0xFF00838F.toInt()
        SiteType.CAVE -> 0xFF4E342E.toInt()
        SiteType.UNESCO -> 0xFF1565C0.toInt()
        SiteType.JAIN -> 0xFFC2185B.toInt()
        SiteType.MUSEUM -> 0xFF00695C.toInt()
        SiteType.NATURE -> 0xFF2E7D32.toInt()
        SiteType.TREK -> 0xFF33691E.toInt()
        SiteType.LAKE -> 0xFF0277BD.toInt()
        SiteType.MISC -> 0xFF5D4037.toInt()
    }

    val drawableRes = when (type) {
        SiteType.TEMPLE -> com.example.virasat.R.drawable.ic_marker_temple
        SiteType.PALACE -> com.example.virasat.R.drawable.ic_marker_palace
        SiteType.FORT -> com.example.virasat.R.drawable.ic_marker_fort
        SiteType.MONUMENT -> com.example.virasat.R.drawable.ic_marker_monument
        SiteType.CAVE -> com.example.virasat.R.drawable.ic_marker_cave
        SiteType.UNESCO -> com.example.virasat.R.drawable.ic_marker_unesco
        SiteType.JAIN -> com.example.virasat.R.drawable.ic_marker_temple
        SiteType.MUSEUM -> com.example.virasat.R.drawable.ic_marker_museum
        SiteType.NATURE -> com.example.virasat.R.drawable.ic_marker_nature
        SiteType.TREK -> com.example.virasat.R.drawable.ic_marker_nature
        SiteType.LAKE -> com.example.virasat.R.drawable.ic_marker_lake
        SiteType.MISC -> com.example.virasat.R.drawable.ic_marker_monument
    }

    val cx = size / 2f
    val circleR = 36f

    // Pin background
    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = bgColor }
    canvas.drawCircle(cx, cx - 10f, circleR, bgPaint)
    val path = Path().apply {
        moveTo(cx - 14f, cx + 20f)
        lineTo(cx, size.toFloat() - 2f)
        lineTo(cx + 14f, cx + 20f)
        close()
    }
    canvas.drawPath(path, bgPaint)

    // White border
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    canvas.drawCircle(cx, cx - 10f, circleR - 2f, borderPaint)

    // Draw vector icon inside circle
    val drawable = androidx.core.content.ContextCompat.getDrawable(context, drawableRes)
    drawable?.let {
        val iconSize = 36
        val left = (cx - iconSize / 2f).toInt()
        val top = (cx - 10f - iconSize / 2f).toInt()
        it.setBounds(left, top, left + iconSize, top + iconSize)
        it.draw(canvas)
    }

    val descriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
    return Pair(bitmap, descriptor)
}
