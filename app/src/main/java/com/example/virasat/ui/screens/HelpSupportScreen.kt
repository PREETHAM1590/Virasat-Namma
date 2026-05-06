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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
    onBack: () -> Unit,
    onContactSupportClick: () -> Unit = {}
) {
    val faqItems = listOf(
        "How to check in?" to "Navigate to a heritage site and tap the QR scanner button. Scan the QR code displayed at the site to check in and unlock hidden facts. Your check-in is automatically saved to your Travel Passport.",
        "How does the Travel Passport work?" to "Your Travel Passport tracks every heritage site you visit. Each check-in adds a digital stamp. Collect stamps from different types of sites (temples, forts, palaces) to unlock achievement badges.",
        "Audio guide not working?" to "Ensure you have downloaded the audio guide while connected to Wi-Fi. Check your volume settings. If the issue persists, try clearing the app cache or reinstalling the app.",
        "Can I use the app offline?" to "Yes! Downloaded audio guides work offline. Site information and images may require internet. We recommend downloading guides before your visit.",
        "How do I earn badges?" to "Visit different types of heritage sites and scan QR codes to unlock achievement badges. Rare badges are awarded for visiting UNESCO World Heritage Sites.",
        "Is my data safe?" to "Yes. We store your check-ins and unlocked facts securely using Firebase Firestore with industry-standard encryption. We never share or sell your personal data."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support") },
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Frequently Asked Questions", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            faqItems.forEach { (question, answer) ->
                FaqCard(question = question, answer = answer)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Help, null, tint = VirasatMaroon, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Still need help?", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Our support team is here to help.", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onContactSupportClick,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Email, null, tint = VirasatGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contact Support", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun FaqCard(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(question, fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 15.sp, modifier = Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        null,
                        tint = VirasatMaroon
                    )
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(answer, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            }
        }
    }
}
