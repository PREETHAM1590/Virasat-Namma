package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virasat.ui.theme.Background
import com.example.virasat.ui.theme.OnPrimaryContainer
import com.example.virasat.ui.theme.OnSurface
import com.example.virasat.ui.theme.OnSurfaceVariant
import com.example.virasat.ui.theme.Primary
import com.example.virasat.ui.theme.PrimaryContainer
import com.example.virasat.ui.theme.SecondaryContainer
import com.example.virasat.ui.theme.SurfaceContainerLow
import com.example.virasat.ui.theme.SurfaceContainerLowest
import com.example.virasat.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onBack: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val loginState by authViewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Trigger navigation on success
    LaunchedEffect(loginState.success) {
        if (loginState.success) {
            val displayName = authViewModel.currentUser?.displayName
                ?: email.substringBefore("@")
            onLogin(displayName, email.trim())
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Decorative blobs
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(256.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(SecondaryContainer.copy(alpha = 0.4f))
                .blur(48.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(192.dp)
                .clip(RoundedCornerShape(80.dp))
                .background(PrimaryContainer.copy(alpha = 0.3f))
                .blur(40.dp)
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .shadow(4.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                    .clip(RoundedCornerShape(999.dp))
                    .background(SurfaceContainerLowest)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Primary)
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "Hi, Welcome \uD83D\uDC4B",
                style = MaterialTheme.typography.displayLarge.copy(lineHeight = 48.sp),
                color = Primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Sign in to continue your heritage journey.",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant
            )

            Spacer(Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    authViewModel.clearLoginError()
                },
                placeholder = { Text("Email Address", color = MaterialTheme.colorScheme.outline) },
                leadingIcon = { Icon(Icons.Default.Mail, null, tint = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = loginState.error != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryContainer,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedContainerColor = SurfaceContainerLow,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    authViewModel.clearLoginError()
                },
                placeholder = { Text("Password", color = MaterialTheme.colorScheme.outline) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.outline) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = loginState.error != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryContainer,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedContainerColor = SurfaceContainerLow,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                )
            )

            // Error message
            if (loginState.error != null) {
                Spacer(Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                ) {
                    Text(
                        loginState.error!!,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Forgot Password?",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onNavigateToForgotPassword() },
                style = MaterialTheme.typography.labelLarge,
                color = Primary
            )

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    authViewModel.login(email, password) { name, em ->
                        onLogin(name, em)
                    }
                },
                enabled = !loginState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryContainer,
                    contentColor = OnPrimaryContainer
                )
            ) {
                if (loginState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = OnPrimaryContainer,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Sign In", style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(24.dp))

            // Divider
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)))
                Text("  or  ", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Box(Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)))
            }

            Spacer(Modifier.height(24.dp))

            // Google sign-in placeholder (no Firebase Google Sign-In without SHA-1 in console)
            Surface(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(999.dp),
                color = SurfaceContainerLowest,
                tonalElevation = 1.dp
            ) {
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("G", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF4285F4), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(12.dp))
                    Text("Continue with Google", style = MaterialTheme.typography.bodyMedium, color = OnSurface)
                }
            }

            Spacer(Modifier.height(32.dp))
            Row(
                Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ", style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
                Text(
                    "Sign Up",
                    modifier = Modifier.clickable { onNavigateToSignUp() },
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Primary
                )
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
