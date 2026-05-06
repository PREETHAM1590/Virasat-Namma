package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
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
fun ErrorScreen(
    title: String = "Something Went Wrong",
    message: String = "An unexpected error occurred. Please try again.",
    onRetry: (() -> Unit)? = null
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
            Icons.Default.ErrorOutline,
            null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFFB00020).copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
        Text(
            message,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        if (onRetry != null) {
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Retry")
            }
        }
    }
}
