package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.virasat.ui.theme.BeVietnamPro
import com.example.virasat.ui.theme.PlusJakartaSans

@Composable
fun PermissionScreen(
    onRequestPermission: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scheme.background)
    ) {
        // Ambient background blobs
        Box(
            modifier = Modifier
                .size(500.dp)
                .offset(x = (-120).dp, y = (-160).dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            scheme.secondaryContainer.copy(alpha = 0.4f),
                            scheme.secondaryContainer.copy(alpha = 0f)
                        )
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(600.dp)
                .offset(x = 180.dp, y = 350.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            scheme.tertiaryContainer.copy(alpha = 0.3f),
                            scheme.tertiaryContainer.copy(alpha = 0f)
                        )
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        scheme.surfaceContainerLowest,
                        RoundedCornerShape(48.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = scheme.primary.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Camera Access",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold
                ),
                color = scheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Virasat needs camera access to scan QR codes at heritage sites for check-ins and to unlock hidden facts during your visit.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = BeVietnamPro,
                    fontWeight = FontWeight.Normal
                ),
                color = scheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Allow pill button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        scheme.primaryContainer,
                        RoundedCornerShape(999.dp)
                    )
                    .clickable(onClick = onRequestPermission)
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Allow",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = BeVietnamPro,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (0.05).em
                    ),
                    color = scheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Maybe Later",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = BeVietnamPro,
                    fontWeight = FontWeight.SemiBold
                ),
                color = scheme.primary,
                modifier = Modifier.clickable(onClick = {}),
                textAlign = TextAlign.Center
            )
        }
    }
}
