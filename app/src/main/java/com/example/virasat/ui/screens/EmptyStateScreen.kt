package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@Composable
fun EmptyStateScreen(
    title: String = "Nothing Found",
    message: String = "We couldn't find anything matching your search.",
    icon: ImageVector = Icons.Default.SearchOff,
    onAction: (() -> Unit)? = null,
    actionLabel: String = "Explore Sites"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VirasatCream)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(VirasatMaroon.copy(alpha = 0.08f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                null,
                modifier = Modifier.size(56.dp),
                tint = VirasatMaroon.copy(alpha = 0.5f)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VirasatMaroon,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            message,
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        if (onAction != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(actionLabel, fontWeight = FontWeight.Medium)
            }
        }
    }
}
