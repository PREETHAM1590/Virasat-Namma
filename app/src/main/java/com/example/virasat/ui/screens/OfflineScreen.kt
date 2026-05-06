package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatMaroon

@Composable
fun OfflineScreen(
    onRetry: () -> Unit,
    onBrowseOffline: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VirasatCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.WifiOff,
            null,
            modifier = Modifier.size(80.dp),
            tint = VirasatMaroon.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("No Internet Connection", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
        Text(
            "You're offline. Some features may not be available.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Retry", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onBrowseOffline,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Browse Saved Sites")
        }
    }
}
