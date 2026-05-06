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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit,
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onRateUsClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(VirasatMaroon, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("V", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = VirasatGold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Virasat", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
            Text("Namma Karnataka Heritage Guide", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Version 1.0.0", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Virasat is your companion to exploring Karnataka's rich heritage. From the ruins of Hampi to the grandeur of Mysore Palace, discover UNESCO sites, scan QR codes for hidden facts, collect digital passport stamps, and listen to immersive audio guides — all in one app.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Links", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = onPrivacyPolicyClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Privacy Policy", color = VirasatMaroon)
                    }
                    HorizontalDivider(color = Color.LightGray)
                    TextButton(onClick = onTermsClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Terms of Service", color = VirasatMaroon)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRateUsClick,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Star, null, tint = VirasatGold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rate Us", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Developed with love in Karnataka", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
