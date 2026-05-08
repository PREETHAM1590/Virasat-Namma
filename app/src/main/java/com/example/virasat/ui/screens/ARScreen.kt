package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.source.ImageUrls
import com.example.virasat.data.source.KarnatakaSites

@Composable
fun ARScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val site = KarnatakaSites.allSites.find { it.id == siteId }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Camera preview placeholder (full screen)
        AsyncImage(
            model = site?.imageUrl ?: ImageUrls.STONE_CARVING,
            contentDescription = site?.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Scan frame overlay (center)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 160.dp),
            contentAlignment = Alignment.Center
        ) {
            ScanFrame()
        }

        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .statusBarsPadding()
                .padding(top = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f),
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onBack)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CropFree,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "Point at monument",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        // Bottom floating bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = MaterialTheme.colorScheme.inverseSurface,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(64.dp)
                ) {
                    // Gallery button (left)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable(onClick = { }),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Gallery",
                            tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Scan button (center, elevated)
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.TopCenter)
                            .offset(y = (-24).dp)
                            .clickable(onClick = { })
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Scan",
                                tint = MaterialTheme.colorScheme.inverseSurface,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    // Flash button (right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable(onClick = { }),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "Flash",
                            tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScanFrame() {
    val cornerSize = 32.dp
    val borderWidth = 3.dp
    val frameSize = 256.dp

    Box(
        modifier = Modifier.size(frameSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top-left corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .width(cornerSize)
                    .height(cornerSize)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(topStart = 16.dp)
                    )
            )
            // Top-right corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(cornerSize)
                    .height(cornerSize)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(topEnd = 16.dp)
                    )
            )
            // Bottom-left corner
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .width(cornerSize)
                    .height(cornerSize)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(bottomStart = 16.dp)
                    )
            )
            // Bottom-right corner
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(cornerSize)
                    .height(cornerSize)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(bottomEnd = 16.dp)
                    )
            )
        }
    }
}
