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
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScreen(
    onBack: () -> Unit,
    cameraGranted: Boolean = false,
    locationGranted: Boolean = false,
    microphoneGranted: Boolean = false,
    onRequestCamera: () -> Unit = {},
    onRequestLocation: () -> Unit = {},
    onRequestMicrophone: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Permissions") },
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
            Text(
                "Virasat needs the following permissions to provide the best experience. You can manage these anytime in your device settings.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            PermissionCard(
                icon = Icons.Default.PhotoCamera,
                title = "Camera",
                description = "Required to scan QR codes at heritage sites for check-ins and unlock hidden facts.",
                granted = cameraGranted,
                onRequest = onRequestCamera
            )
            PermissionCard(
                icon = Icons.Default.LocationOn,
                title = "Location",
                description = "Used to show nearby heritage sites, provide directions, and verify check-in locations.",
                granted = locationGranted,
                onRequest = onRequestLocation
            )
            PermissionCard(
                icon = Icons.Default.Mic,
                title = "Microphone",
                description = "Needed for voice interaction with the AI heritage assistant feature.",
                granted = microphoneGranted,
                onRequest = onRequestMicrophone
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = VirasatMaroon, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "We never access permissions without your consent. Denying a permission may limit certain features.",
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PermissionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    granted: Boolean,
    onRequest: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = VirasatMaroon, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
                Text(description, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (granted) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(28.dp))
            } else {
                Button(
                    onClick = onRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Allow", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
