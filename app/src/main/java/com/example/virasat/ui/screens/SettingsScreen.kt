package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
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
    onDataSync: () -> Unit = {}
) {
    var darkModeEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoDownload by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = VirasatMaroon) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = VirasatMaroon)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsGroup("Preferences") {
                SettingsToggleItem(
                    label = "Dark Mode",
                    icon = Icons.Default.DarkMode,
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it; onDarkMode() }
                )
                SettingsToggleItem(
                    label = "Notifications",
                    icon = Icons.Default.Notifications,
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it; onNotifications() }
                )
                SettingsNavItem(
                    label = "Language",
                    icon = Icons.Default.Language,
                    trailing = "English",
                    onClick = onLanguageSettings
                )
            }

            SettingsGroup("Offline") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Download, null, tint = VirasatMaroon)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Auto-Download Audio", fontSize = 16.sp, color = VirasatMaroon)
                        Text("Download guides over Wi-Fi", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = autoDownload,
                        onCheckedChange = { autoDownload = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = VirasatMaroon,
                            checkedTrackColor = VirasatMaroon.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            SettingsGroup("App Info") {
                SettingsNavItem("About Virasat", Icons.Default.Info, null, onAbout)
                SettingsNavItem("Privacy Policy", Icons.Default.Lock, null, onPrivacy)
                SettingsNavItem("Terms of Service", Icons.Default.Description, null, onTerms)
                SettingsNavItem("Data Sync", Icons.Default.Sync, null, onDataSync)
                SettingsNavItem("Help & Support", Icons.Default.HelpOutline, null, onHelp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Version 1.0.0",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            title,
            fontSize = 14.sp,
            color = VirasatGold,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun SettingsToggleItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = VirasatMaroon, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, fontSize = 16.sp, color = VirasatMaroon, modifier = Modifier.weight(1f))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = VirasatMaroon,
                    checkedTrackColor = VirasatMaroon.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun SettingsNavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    trailing: String?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = VirasatMaroon, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, fontSize = 16.sp, color = VirasatMaroon, modifier = Modifier.weight(1f))
            if (trailing != null) {
                Text(trailing, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
    }
}
