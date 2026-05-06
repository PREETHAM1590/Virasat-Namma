package com.example.virasat.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
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
    val images = if (galleryImages.isNotEmpty()) galleryImages else emptyList()

    val panoramaHtml = remember(images, currentImageIndex, autoRotate) {
        val imgUrl = if (images.isNotEmpty()) images[currentImageIndex] else ""
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
    "showFullscreenCtrl": true,
    "hotSpotDebug": false,
    "showControls": true,
    "showToolbar": true,
    "strings": {
        "loadButtonLabel": "Tap to Load 360° View"
    }
});
</script>
</body>
</html>
        """.trimIndent()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(siteName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        },
        bottomBar = {
            if (images.isNotEmpty()) {
                Surface(
                    color = VirasatCream,
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous
                        IconButton(
                            onClick = {
                                if (currentImageIndex > 0) currentImageIndex--
                            },
                            enabled = currentImageIndex > 0
                        ) {
                            Icon(Icons.Default.ChevronLeft, "Previous", tint = if (currentImageIndex > 0) VirasatMaroon else Color.Gray)
                        }

                        // Image counter
                        Text(
                            "${currentImageIndex + 1} / ${images.size}",
                            fontWeight = FontWeight.Bold,
                            color = VirasatMaroon
                        )

                        // Auto-rotate toggle
                        IconButton(onClick = { autoRotate = !autoRotate }) {
                            Icon(
                                if (autoRotate) Icons.Default.RotateRight else Icons.Default.RotateLeft,
                                "Auto-rotate",
                                tint = if (autoRotate) VirasatGold else Color.Gray
                            )
                        }

                        // Next
                        IconButton(
                            onClick = {
                                if (currentImageIndex < images.size - 1) currentImageIndex++
                            },
                            enabled = currentImageIndex < images.size - 1
                        ) {
                            Icon(Icons.Default.ChevronRight, "Next", tint = if (currentImageIndex < images.size - 1) VirasatMaroon else Color.Gray)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
        ) {
            if (images.isEmpty()) {
                // No images fallback
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Image, null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No images available for 360° view", color = Color.Gray)
                }
            } else {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = false
                            settings.allowContentAccess = false
                            setBackgroundColor(android.graphics.Color.BLACK)
                            loadDataWithBaseURL(null, panoramaHtml, "text/html", "UTF-8", null)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { webView ->
                        webView.loadDataWithBaseURL(null, panoramaHtml, "text/html", "UTF-8", null)
                    }
                )

                // Loading overlay
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VirasatGold)
                }
            }
        }
    }
}