package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.virasat.ui.theme.BeVietnamPro
import com.example.virasat.ui.theme.PlusJakartaSans
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import com.example.virasat.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    onSendResetLink: (String) -> Unit,
    onBack: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val resetState by authViewModel.resetState.collectAsState()
    var email by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }
    val scheme = MaterialTheme.colorScheme
    
    LaunchedEffect(resetState.success) {
        if (resetState.success) sent = true
    }

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
                            scheme.surfaceContainerLowest.copy(alpha = 0.4f),
                            scheme.surfaceContainerLowest.copy(alpha = 0f)
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
                            scheme.primaryContainer.copy(alpha = 0.2f),
                            scheme.primaryContainer.copy(alpha = 0f)
                        )
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 8.dp),
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = scheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (sent) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(120.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                scheme.primaryContainer,
                                RoundedCornerShape(24.dp)
                            )
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Reset link sent! Check your email.",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = BeVietnamPro,
                                fontWeight = FontWeight.Medium
                            ),
                            color = scheme.onPrimaryContainer
                        )
                    }
                }
            } else {
                // Headline
                Text(
                    text = "Forgot\nPassword",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold
                    ),
                    color = scheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter the email address associated with your account to receive a secure reset link.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = BeVietnamPro
                    ),
                    color = scheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Floating white pill input with mail icon
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            "Email address",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = BeVietnamPro
                            ),
                            color = scheme.outlineVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = scheme.outlineVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(999.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = scheme.surfaceContainerLowest,
                        focusedContainerColor = scheme.surfaceContainerLowest,
                        unfocusedBorderColor = scheme.surfaceContainerLowest,
                        focusedBorderColor = scheme.primaryContainer.copy(alpha = 0.5f),
                        unfocusedTrailingIconColor = scheme.outlineVariant,
                        focusedTrailingIconColor = scheme.primary
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = BeVietnamPro
                    )
                )
            }
        }

        if (!sent) {
            // Massive bottom lime green pill button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            scheme.primaryContainer,
                            RoundedCornerShape(999.dp)
                        )
                        .clickable(
                            enabled = email.isNotBlank() && !resetState.isLoading,
                            onClick = { authViewModel.sendPasswordReset(email); onSendResetLink(email) }
                        )
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Send Reset Link",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = BeVietnamPro,
                                fontWeight = FontWeight.Bold,
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
