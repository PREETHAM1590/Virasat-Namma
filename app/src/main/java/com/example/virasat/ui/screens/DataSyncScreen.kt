package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataSyncScreen(onBack: () -> Unit) {
    var syncing by remember { mutableStateOf(false) }
    var lastSync by remember { mutableStateOf("May 6, 2026 at 10:30 AM") }
    var autoSync by remember { mutableStateOf(true) }
    var synced by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Sync & Backup") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(if (synced) Color(0xFFE8F5E9) else Color(0xFFFFF3E0), RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (synced) Icons.Default.CheckCircle else Icons.Default.SyncProblem,
                            null,
                            tint = if (synced) Color(0xFF2E7D32) else Color(0xFFE65100),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            if (synced) "All Data Synced" else "Sync Pending",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (synced) Color(0xFF2E7D32) else Color(0xFFE65100)
                        )
                        Text("Last synced: $lastSync", fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Auto-Sync", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = VirasatMaroon)
                            Text("Sync automatically over Wi-Fi", fontSize = 13.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = autoSync,
                            onCheckedChange = { autoSync = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = VirasatMaroon)
                        )
                    }
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Check-ins", fontSize = 14.sp, color = Color.DarkGray)
                            Text("12 items", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text("Synced", fontSize = 13.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Audio Guides", fontSize = 14.sp, color = Color.DarkGray)
                            Text("5 downloaded", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text("Synced", fontSize = 13.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Passport Stamps", fontSize = 14.sp, color = Color.DarkGray)
                            Text("8 collected", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text("Synced", fontSize = 13.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
                    }
                }
            }
            Button(
                onClick = {
                    syncing = true
                    synced = false
                    scope.launch {
                        delay(2000)
                        syncing = false
                        synced = true
                        lastSync = "Just now"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                shape = RoundedCornerShape(12.dp),
                enabled = !syncing
            ) {
                if (syncing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Syncing...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.CloudSync, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sync Now", fontWeight = FontWeight.Bold)
                }
            }
            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Download, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export Data")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your data is stored securely in the cloud. Total data usage: 2.4 MB",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
