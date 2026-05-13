package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.virasat.R

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

        // Avatar placeholder
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(cs.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                val initials = name.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")
                Text(
                    text = initials.ifBlank { "?" },
                    style = type.headlineLarge,
                    color = cs.onPrimaryContainer,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
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
