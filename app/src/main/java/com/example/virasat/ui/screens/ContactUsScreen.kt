package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.virasat.R
import com.example.virasat.ui.theme.PlusJakartaSans

@Composable
fun ContactUsScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
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
                .size(400.dp)
                .background(scheme.secondaryContainer.copy(alpha = 0.2f), CircleShape)
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 48.dp)
        )
        Box(
            modifier = Modifier
                .size(500.dp)
                .background(scheme.tertiaryContainer.copy(alpha = 0.15f), CircleShape)
                .align(Alignment.BottomStart)
                .padding(bottom = 48.dp, start = 48.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .background(
                            scheme.surfaceContainerLowest,
                            RoundedCornerShape(999.dp)
                        )
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = scheme.onSurface
                    )
                }
            }

            // Header
            if (!sent) {
                Text(
                    text = stringResource(R.string.contact_get_in_touch),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold
                    ),
                    color = scheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (sent) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = scheme.primaryContainer.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = scheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Message sent! We will get back to you soon.",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = scheme.onSurface
                            )
                        }
                    }
                } else {
                    // Neumorphic card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = scheme.surfaceContainerLowest
                        ),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "How would you rate your recent journey with us?",
                                style = MaterialTheme.typography.bodyLarge,
                                color = scheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Name field - pill shape
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text(stringResource(com.example.virasat.R.string.contact_name), style = MaterialTheme.typography.bodyMedium) },
                                placeholder = { Text(stringResource(R.string.contact_name_hint), style = MaterialTheme.typography.bodyMedium) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(999.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = scheme.surfaceContainerHigh,
                                    unfocusedContainerColor = scheme.surfaceContainerHigh,
                                    focusedBorderColor = scheme.primaryContainer.copy(alpha = 0.5f),
                                    unfocusedBorderColor = scheme.outlineVariant,
                                    focusedTextColor = scheme.onSurface,
                                    unfocusedTextColor = scheme.onSurface
                                ),
                                textStyle = MaterialTheme.typography.bodyMedium
                            )

                            // Email field - pill shape
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text(stringResource(com.example.virasat.R.string.contact_email), style = MaterialTheme.typography.bodyMedium) },
                                placeholder = { Text(stringResource(com.example.virasat.R.string.contact_email_hint), style = MaterialTheme.typography.bodyMedium) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(999.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = scheme.surfaceContainerHigh,
                                    unfocusedContainerColor = scheme.surfaceContainerHigh,
                                    focusedBorderColor = scheme.primaryContainer.copy(alpha = 0.5f),
                                    unfocusedBorderColor = scheme.outlineVariant,
                                    focusedTextColor = scheme.onSurface,
                                    unfocusedTextColor = scheme.onSurface
                                ),
                                textStyle = MaterialTheme.typography.bodyMedium
                            )

                            // Message field - rounded large, inset style
                            OutlinedTextField(
                                value = message,
                                onValueChange = { message = it },
                                label = { Text(stringResource(com.example.virasat.R.string.contact_message), style = MaterialTheme.typography.bodyMedium) },
                                placeholder = { Text(stringResource(com.example.virasat.R.string.contact_message_hint), style = MaterialTheme.typography.bodyMedium) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = scheme.surfaceContainerHigh,
                                    unfocusedContainerColor = scheme.surfaceContainerHigh,
                                    focusedBorderColor = scheme.primaryContainer.copy(alpha = 0.5f),
                                    unfocusedBorderColor = scheme.outlineVariant,
                                    focusedTextColor = scheme.onSurface,
                                    unfocusedTextColor = scheme.onSurface
                                ),
                                textStyle = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Submit pill button
                    Button(
                        onClick = {
                            isLoading = true
                            onSubmit()
                            isLoading = false
                            sent = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = scheme.primaryContainer,
                            contentColor = scheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(999.dp),
                        enabled = name.isNotBlank() && email.isNotBlank() && message.isNotBlank() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = scheme.onPrimaryContainer,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.contact_send_message),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
