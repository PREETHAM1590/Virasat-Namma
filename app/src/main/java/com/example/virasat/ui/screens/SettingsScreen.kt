package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.R
import com.example.virasat.util.BiometricHelper
import com.example.virasat.util.NotificationPreferences

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLanguageSettings: () -> Unit,
    onNotifications: () -> Unit,
    onDarkMode: () -> Unit,
    onAbout: () -> Unit,
    onHelp: () -> Unit,
    onPrivacy: () -> Unit = {},
    onTerms: () -> Unit = {},
    onDataSync: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefs = remember { context.getSharedPreferences("virasat_prefs", android.content.Context.MODE_PRIVATE) }
    val userName = remember { userPrefs.getString("user_name", null) ?: context.getString(R.string.profile_heritage_explorer) }
    val userEmail = remember { userPrefs.getString("user_email", null) ?: "" }
    val currentLangCode = remember { userPrefs.getString("app_locale", "en") ?: "en" }
    val currentLangName = remember { when (currentLangCode) { "kn" -> "Kannada"; "hi" -> "Hindi"; "te" -> "Telugu"; "ta" -> "Tamil"; "ml" -> "Malayalam"; else -> "English" } }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var locationEnabled by remember { mutableStateOf(true) }
    var offlineEnabled by remember { mutableStateOf(false) }
    val biometricStatus = remember { BiometricHelper.checkBiometricAvailability(context) }
    var biometricEnabled by remember {
        mutableStateOf(BiometricHelper.isBiometricEnabled(context))
    }
    var showBiometricEnrollDialog by remember { mutableStateOf(false) }

    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(rememberScrollState())
    ) {
        // TopAppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Eco",
                tint = cs.primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = stringResource(R.string.app_name),
                style = type.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
                color = cs.primary
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = cs.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // Page Title
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_title),
                style = type.displayLarge.copy(fontWeight = FontWeight.Bold),
                color = cs.onBackground
            )
            Text(
                text = stringResource(R.string.settings_subtitle),
                style = type.bodyLarge,
                color = cs.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Account Section
            SettingsSectionCard {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .background(cs.primaryContainer.copy(alpha = 0.1f), CircleShape)
                            .offset(x = 32.dp, y = (-32).dp)
                    )
                }
                Text(
                    text = stringResource(R.string.settings_account),
                    style = type.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Profile row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(cs.tertiaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = cs.onTertiaryContainer,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userName,
                            style = type.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = cs.onBackground
                        )
                        Text(
                            text = if (userEmail.isNotBlank()) userEmail else stringResource(R.string.settings_tap_edit),
                            style = type.bodyMedium,
                            color = cs.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = { onLogout() },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primaryContainer,
                            contentColor = cs.onPrimaryContainer
                        )
                    ) {
                        Text(stringResource(R.string.settings_edit), style = type.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                    }
                }

                HorizontalDivider(color = cs.surfaceVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                // Privacy & Security
                SettingsNavRow(
                    icon = Icons.Default.Lock,
                    iconBg = cs.secondaryContainer.copy(alpha = 0.5f),
                    iconTint = cs.secondary,
                    label = stringResource(R.string.settings_privacy),
                    onClick = onPrivacy
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Payment Methods
                SettingsNavRow(
                    icon = Icons.Default.CreditCard,
                    iconBg = cs.secondaryContainer.copy(alpha = 0.5f),
                    iconTint = cs.secondary,
                    label = stringResource(R.string.settings_payments),
                    onClick = onTerms
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Preferences Section
            SettingsSectionCard {
                Text(
                    text = stringResource(R.string.settings_preferences),
                    style = type.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                SettingsNavRow(
                    icon = Icons.Default.Language,
                    iconBg = cs.secondaryContainer.copy(alpha = 0.5f),
                    iconTint = cs.secondary,
                    label = stringResource(R.string.settings_language, currentLangName),
                    onClick = onLanguageSettings
                )
                Spacer(modifier = Modifier.height(16.dp))
                SettingsToggleRow(
                    icon = Icons.Default.NotificationsActive,
                    iconBg = cs.surfaceVariant.copy(alpha = 0.5f),
                    iconTint = cs.tertiary,
                    label = stringResource(R.string.settings_notifications),
                    subtitle = stringResource(R.string.settings_notification_subtitle),
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        notificationsEnabled = it
                        onNotifications()
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                SettingsToggleRow(
                    icon = Icons.Default.LocationOn,
                    iconBg = cs.surfaceVariant.copy(alpha = 0.5f),
                    iconTint = cs.tertiary,
                    label = stringResource(R.string.settings_location),
                    subtitle = stringResource(R.string.settings_location_subtitle),
                    checked = locationEnabled,
                    onCheckedChange = { locationEnabled = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                SettingsToggleRow(
                    icon = Icons.Default.DarkMode,
                    iconBg = cs.surfaceVariant.copy(alpha = 0.5f),
                    iconTint = cs.tertiary,
                    label = stringResource(R.string.settings_offline),
                    subtitle = stringResource(R.string.settings_offline_subtitle),
                    checked = offlineEnabled,
                    onCheckedChange = {
                        offlineEnabled = it
                        onDarkMode()
                    }
                )

                // Biometric toggle — only shown when hardware is present
                if (biometricStatus != BiometricHelper.BiometricStatus.NOT_AVAILABLE) {
                    Spacer(modifier = Modifier.height(24.dp))
                    if (biometricStatus == BiometricHelper.BiometricStatus.NOT_ENROLLED) {
                        // Hardware present but no biometrics enrolled — show info row, no toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showBiometricEnrollDialog = true },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(cs.surfaceVariant.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    tint = cs.tertiary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.settings_biometric_login),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = cs.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.settings_biometric_enroll),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = cs.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        // Hardware present and biometrics enrolled — show toggle
                        SettingsToggleRow(
                            icon = Icons.Default.Fingerprint,
                            iconBg = cs.surfaceVariant.copy(alpha = 0.5f),
                            iconTint = cs.tertiary,
                            label = stringResource(R.string.settings_biometric_login),
                            subtitle = stringResource(R.string.settings_biometric_subtitle),
                            checked = biometricEnabled,
                            onCheckedChange = { enabled ->
                                biometricEnabled = enabled
                                BiometricHelper.setBiometricEnabled(context, enabled)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Notification Categories Section
            SettingsSectionCard {
                Text(
                    text = stringResource(R.string.settings_notification_categories),
                    style = type.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                var proximityEnabled by remember { mutableStateOf(NotificationPreferences.isEnabled(context, "proximity")) }
                var weeklyFactsEnabled by remember { mutableStateOf(NotificationPreferences.isEnabled(context, "weekly_facts")) }
                var badgeUnlocksEnabled by remember { mutableStateOf(NotificationPreferences.isEnabled(context, "badge_unlocks")) }

                SettingsToggleRow(
                    icon = Icons.Default.NearMe,
                    iconBg = cs.surfaceVariant.copy(alpha = 0.5f),
                    iconTint = cs.tertiary,
                    label = stringResource(R.string.settings_proximity_alerts),
                    subtitle = stringResource(R.string.settings_proximity_subtitle),
                    checked = proximityEnabled,
                    onCheckedChange = {
                        proximityEnabled = it
                        NotificationPreferences.setEnabled(context, "proximity", it)
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                SettingsToggleRow(
                    icon = Icons.Default.AutoStories,
                    iconBg = cs.surfaceVariant.copy(alpha = 0.5f),
                    iconTint = cs.tertiary,
                    label = stringResource(R.string.settings_weekly_facts),
                    subtitle = stringResource(R.string.settings_weekly_facts_subtitle),
                    checked = weeklyFactsEnabled,
                    onCheckedChange = {
                        weeklyFactsEnabled = it
                        NotificationPreferences.setEnabled(context, "weekly_facts", it)
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                SettingsToggleRow(
                    icon = Icons.Default.EmojiEvents,
                    iconBg = cs.surfaceVariant.copy(alpha = 0.5f),
                    iconTint = cs.tertiary,
                    label = stringResource(R.string.settings_badge_unlocks),
                    subtitle = stringResource(R.string.settings_badge_unlocks_subtitle),
                    checked = badgeUnlocksEnabled,
                    onCheckedChange = {
                        badgeUnlocksEnabled = it
                        NotificationPreferences.setEnabled(context, "badge_unlocks", it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Support & About Section
            SettingsSectionCard {
                Text(
                    text = stringResource(R.string.settings_support),
                    style = type.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                SettingsNavRow(
                    icon = Icons.Default.Help,
                    iconBg = cs.tertiaryContainer.copy(alpha = 0.3f),
                    iconTint = cs.tertiary,
                    label = stringResource(R.string.settings_help),
                    onClick = onHelp
                )
                Spacer(modifier = Modifier.height(16.dp))
                SettingsNavRow(
                    icon = Icons.Default.Info,
                    iconBg = cs.tertiaryContainer.copy(alpha = 0.3f),
                    iconTint = cs.tertiary,
                    label = stringResource(R.string.settings_about),
                    onClick = onAbout
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Log Out Button
            Button(
                onClick = { onLogout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.errorContainer,
                    contentColor = cs.onErrorContainer
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.settings_log_out),
                    style = type.labelMedium.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 0.05.sp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Biometric enroll dialog — shown when hardware present but no biometrics enrolled
    if (showBiometricEnrollDialog) {
        AlertDialog(
            onDismissRequest = { showBiometricEnrollDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(stringResource(R.string.settings_biometric_login))
            },
            text = {
                Text(stringResource(R.string.settings_biometric_enroll))
            },
            confirmButton = {
                TextButton(onClick = { showBiometricEnrollDialog = false }) {
                    Text(stringResource(android.R.string.ok))
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(24.dp),
        content = content
    )
}

@Composable
private fun SettingsNavRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = cs.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = cs.outline,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SettingsToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = cs.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = cs.surfaceContainerLowest,
                checkedTrackColor = cs.primaryContainer,
                uncheckedThumbColor = cs.surfaceContainerLowest,
                uncheckedTrackColor = cs.surfaceVariant
            )
        )
    }
}
