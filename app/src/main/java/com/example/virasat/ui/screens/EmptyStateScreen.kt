package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.virasat.ui.theme.BeVietnamPro
import com.example.virasat.ui.theme.PlusJakartaSans

@Composable
fun EmptyStateScreen(
    title: String = "Nothing here yet",
    message: String = "Explore heritage sites to add items here.",
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowForward,
    onAction: (() -> Unit)? = null,
    actionLabel: String = "Explore"
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
                .offset(x = 120.dp, y = 400.dp)
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
            // Icon container with soft neumorphic feel
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(48.dp))
                    .background(scheme.surfaceContainerLowest)
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    scheme.surfaceContainerLow.copy(alpha = 0.5f),
                                    scheme.surfaceContainerLowest
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = scheme.primary.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold
                ),
                color = scheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = BeVietnamPro,
                    fontWeight = FontWeight.Normal
                ),
                color = scheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            if (onAction != null) {
                Spacer(modifier = Modifier.height(48.dp))

                // Primary-container pill button
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .background(
                            scheme.primaryContainer,
                            RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = actionLabel,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = BeVietnamPro,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = (0.05).em
                            ),
                            color = scheme.onPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = scheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
