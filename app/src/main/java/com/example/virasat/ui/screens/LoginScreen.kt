package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.source.ImageUrls
import com.example.virasat.ui.theme.*

private fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() &&
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val emailError by remember(email) {
        derivedStateOf {
            if (email.isEmpty()) null
            else if (!isValidEmail(email)) "Invalid email format"
            else null
        }
    }
    val passwordError by remember(password) {
        derivedStateOf {
            if (password.isEmpty()) null
            else if (password.length < 6) "Password must be at least 6 characters"
            else null
        }
    }
    val fieldsValid = isValidEmail(email) && password.length >= 6

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageUrls.AUTH_BACKGROUND,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatMaroon.copy(alpha = 0.7f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = VirasatCream)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Welcome Back",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = VirasatCream,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Sign in to continue your Karnataka journey",
                    fontSize = 14.sp,
                    color = VirasatGold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = VirasatCream) },
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = VirasatGold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError != null,
                    supportingText = {
                        if (emailError != null) {
                            Text(emailError!!, color = Color.Red.copy(alpha = 0.8f))
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                        focusedTextColor = VirasatCream,
                        unfocusedTextColor = VirasatCream,
                        focusedBorderColor = VirasatGold,
                        unfocusedBorderColor = VirasatCream.copy(alpha = 0.5f),
                        focusedLabelColor = VirasatGold,
                        unfocusedLabelColor = VirasatCream.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = VirasatCream) },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = VirasatGold) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                null,
                                tint = VirasatGold
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError != null,
                    supportingText = {
                        if (passwordError != null) {
                            Text(passwordError!!, color = Color.Red.copy(alpha = 0.8f))
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                        focusedTextColor = VirasatCream,
                        unfocusedTextColor = VirasatCream,
                        focusedBorderColor = VirasatGold,
                        unfocusedBorderColor = VirasatCream.copy(alpha = 0.5f),
                        focusedLabelColor = VirasatGold,
                        unfocusedLabelColor = VirasatCream.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                TextButton(onClick = onNavigateToForgotPassword, modifier = Modifier.align(Alignment.End)) {
                    Text("Forgot Password?", color = VirasatGold)
                }
                Button(
                    onClick = {
                        isLoading = true
                        onLogin(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VirasatGold,
                        disabledContainerColor = VirasatGold.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = fieldsValid && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = VirasatMaroon, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?", color = VirasatCream.copy(alpha = 0.8f))
                TextButton(onClick = onNavigateToSignUp) {
                    Text("Sign Up", color = VirasatGold, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
