package com.example.virasat.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.virasat.R
import java.io.File

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val ctx = LocalContext.current
    val prefs = remember { ctx.getSharedPreferences("virasat_prefs", android.content.Context.MODE_PRIVATE) }
    var name by remember { mutableStateOf(prefs.getString("user_name", "") ?: "") }
    var email by remember { mutableStateOf(prefs.getString("user_email", "") ?: "") }
    var phone by remember { mutableStateOf(prefs.getString("user_phone", "") ?: "") }
    var bio by remember { mutableStateOf(prefs.getString("user_bio", "") ?: "") }
    var profileImageUri by remember { mutableStateOf(prefs.getString("user_profile_image", null)?.let { Uri.parse(it) }) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Copy to app-private storage for persistence
            try {
                val inputStream = ctx.contentResolver.openInputStream(uri)
                val file = File(ctx.filesDir, "profile_image.jpg")
                inputStream?.use { input -> file.outputStream().use { output -> input.copyTo(output) } }
                profileImageUri = Uri.fromFile(file)
            } catch (_: Exception) {
                profileImageUri = uri
            }
        }
    }

    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(scrollState)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilledIconButton(
                onClick = onBack,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = cs.surfaceContainerLowest.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(999.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = cs.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = stringResource(R.string.edit_profile_title),
                style = type.headlineMedium,
                color = cs.onSurface
            )
            FilledIconButton(
                onClick = {
                    prefs.edit()
                        .putString("user_name", name.trim())
                        .putString("user_email", email.trim())
                        .putString("user_phone", phone.trim())
                        .putString("user_bio", bio.trim())
                        .putString("user_profile_image", profileImageUri?.toString())
                        .apply()
                    onSaved()
                },
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = cs.primary
                ),
                shape = RoundedCornerShape(999.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(R.string.save),
                    tint = cs.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Avatar with image picker
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(cs.primaryContainer)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val initials = name.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")
                    Text(
                        text = initials.ifBlank { "?" },
                        style = type.headlineLarge,
                        color = cs.onPrimaryContainer,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
            // Camera icon badge
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 40.dp, y = 40.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(cs.primary)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = cs.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Form fields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.edit_profile_name)) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = cs.onSurfaceVariant) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cs.surfaceContainerLowest,
                    unfocusedContainerColor = cs.surfaceContainerLowest
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.edit_profile_email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cs.surfaceContainerLowest,
                    unfocusedContainerColor = cs.surfaceContainerLowest
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(stringResource(R.string.edit_profile_phone)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cs.surfaceContainerLowest,
                    unfocusedContainerColor = cs.surfaceContainerLowest
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text(stringResource(R.string.edit_profile_bio)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cs.surfaceContainerLowest,
                    unfocusedContainerColor = cs.surfaceContainerLowest
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
