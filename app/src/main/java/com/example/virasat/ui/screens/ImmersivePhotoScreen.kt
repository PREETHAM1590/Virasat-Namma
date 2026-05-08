package com.example.virasat.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ImmersivePhotoScreen(
    siteId: String,
    siteName: String,
    galleryImages: List<String>,
    onBack: () -> Unit
) {
    var currentImageIndex by remember { mutableIntStateOf(0) }
    var autoRotate by remember { mutableStateOf(true) }

    val images = galleryImages.ifEmpty {
        listOf(
            "https://images.unsplash.com/photo-1561361513-2d000a50f0dc?w=1200",
            "https://images.unsplash.com/photo-1548013146-72479768bada?w=1200",
            "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=1200"
        )
    }

    val panoramaHtml = remember(images, currentImageIndex, autoRotate) {
        val imgUrl = images[currentImageIndex]
        """
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.css">
<script src="https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.js"></script>
<style>
  body { margin: 0; padding: 0; overflow: hidden; background: #000; }
  #panorama { width: 100vw; height: 100vh; }
</style>
</head>
<body>
<div id="panorama"></div>
<script>
pannellum.viewer('panorama', {
    "type": "equirectangular",
    "panorama": "$imgUrl",
    "autoLoad": true,
    "autoRotate": ${if (autoRotate) "-2" else "0"},
    "compass": false,
    "showZoomCtrl": true,
    "showFullscreenCtrl": false,
    "hotSpotDebug": false
});
</script>
</body>
</html>
        """.trimIndent()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Main 360 content
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.domStorageEnabled = true
                    setBackgroundColor(android.graphics.Color.BLACK)
                    loadDataWithBaseURL(null, panoramaHtml, "text/html", "UTF-8", null)
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { webView ->
                webView.loadDataWithBaseURL(null, panoramaHtml, "text/html", "UTF-8", null)
            }
        )

        // Semi-transparent top bar with back arrow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 8.dp)
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Title at bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp, start = 24.dp, end = 24.dp)
        ) {
            Text(
                siteName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.surfaceContainerLowest
            )
        }

        // Floating control pills (center-right)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
                tonalElevation = 2.dp,
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = { })
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = "Zoom In",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
                tonalElevation = 2.dp,
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = { })
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ZoomOut,
                        contentDescription = "Zoom Out",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = if (autoRotate)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                else
                    MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
                tonalElevation = 2.dp,
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = { autoRotate = !autoRotate })
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.RotateRight,
                        contentDescription = "Auto Rotate",
                        tint = if (autoRotate)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
