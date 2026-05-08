package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
fun FeedbackScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit = {}
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }
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
                .offset(x = 200.dp, y = (-100).dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            scheme.secondaryContainer.copy(alpha = 0.2f),
                            scheme.secondaryContainer.copy(alpha = 0f)
                        )
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(600.dp)
                .offset(x = (-160).dp, y = 400.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            scheme.tertiaryContainer.copy(alpha = 0.15f),
                            scheme.tertiaryContainer.copy(alpha = 0f)
                        )
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            scheme.surfaceContainerLowest,
                            CircleShape
                        )
                        .clip(CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Back",
                        tint = scheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (sent) {
                Spacer(modifier = Modifier.height(120.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = scheme.onPrimaryContainer,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Thank You!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold
                        ),
                        color = scheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Thanks for your feedback! It helps us improve the app.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = BeVietnamPro
                        ),
                        color = scheme.onPrimaryContainer.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Headline
                Text(
                    text = "Your Experience",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold
                    ),
                    color = scheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
                )

                // Floating white card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            scheme.surfaceContainerLowest,
                            RoundedCornerShape(48.dp)
                        )
                        .padding(horizontal = 32.dp, vertical = 40.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            text = "How would you rate your recent journey with us?",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = BeVietnamPro
                            ),
                            color = scheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        // 5 lime green stars
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { index ->
                                val filled = index < rating
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "${index + 1} star",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clickable { rating = index + 1 },
                                    tint = if (filled) scheme.primaryContainer else scheme.outlineVariant
                                )
                            }
                        }

                        // Gray pill input
                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            placeholder = {
                                Text(
                                    "Share more details (optional)...",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = BeVietnamPro
                                    ),
                                    color = scheme.outlineVariant
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(999.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = scheme.surfaceContainerHigh,
                                focusedContainerColor = scheme.surfaceContainerHigh,
                                unfocusedBorderColor = scheme.surfaceContainerHigh,
                                focusedBorderColor = scheme.primaryContainer.copy(alpha = 0.5f)
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = BeVietnamPro
                            ),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Bottom lime green submit pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            scheme.primaryContainer,
                            RoundedCornerShape(999.dp)
                        )
                        .clickable(
                            enabled = rating > 0,
                            onClick = { sent = true; onSubmit() }
                        )
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Submit Feedback",
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

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
