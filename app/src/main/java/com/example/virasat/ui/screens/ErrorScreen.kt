package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.virasat.ui.theme.BeVietnamPro
import com.example.virasat.ui.theme.PlusJakartaSans

@Composable
fun ErrorScreen(
    title: String = "Something went wrong",
    message: String = "An unexpected error occurred. Please try again.",
    onRetry: (() -> Unit)? = null
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
                            scheme.surfaceBright.copy(alpha = 0.8f),
                            scheme.surfaceBright.copy(alpha = 0f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Organic blob with error icon
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(
                        topStartPercent = 43,
                        topEndPercent = 57,
                        bottomEndPercent = 35,
                        bottomStartPercent = 65
                    ))
                    .background(scheme.error),
                contentAlignment = Alignment.Center
            ) {
                // Subtle border overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(
                            topStartPercent = 43,
                            topEndPercent = 57,
                            bottomEndPercent = 35,
                            bottomStartPercent = 65
                        ))
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    scheme.onError.copy(alpha = 0.1f),
                                    scheme.onError.copy(alpha = 0f)
                                )
                            )
                        )
                )
                Icon(
                    imageVector = Icons.Default.PriorityHigh,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = scheme.onError
                )
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

            if (onRetry != null) {
                Spacer(modifier = Modifier.height(48.dp))

                // Dark pill button
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .background(
                            scheme.inverseSurface,
                            RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 40.dp, vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Try Again",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = BeVietnamPro,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = (0.05).em
                        ),
                        color = scheme.inverseOnSurface
                    )
                }
            }
        }
    }
}
