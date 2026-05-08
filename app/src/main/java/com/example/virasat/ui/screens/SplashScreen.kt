package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.virasat.ui.theme.PlusJakartaSans
import com.example.virasat.ui.theme.SecondaryFixed
import com.example.virasat.ui.theme.OnSecondaryFixed
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLanguage: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onNavigateToLanguage()
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(SecondaryFixed),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .size(192.dp)
                    .clip(androidx.compose.foundation.shape.GenericShape { size, _ ->
                        val w = size.width
                        val h = size.height
                        moveTo(w * 0.4f, 0f)
                        cubicTo(w * 0.7f, 0f, w * 1f, h * 0.3f, w * 0.95f, h * 0.5f)
                        cubicTo(w * 1f, h * 0.7f, w * 0.7f, h * 1f, w * 0.5f, h * 0.95f)
                        cubicTo(w * 0.3f, h * 1f, 0f, h * 0.7f, 0.05f, h * 0.5f)
                        cubicTo(0f, h * 0.3f, w * 0.1f, 0f, w * 0.4f, 0f)
                        close()
                    })
                    .background(OnSecondaryFixed),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = SecondaryFixed
                )
            }
            Spacer(Modifier.height(48.dp))
            Text(
                text = "Virasat",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold
                ),
                color = OnSecondaryFixed
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Nature & Heritage",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}
