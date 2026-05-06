package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatMaroon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms of Service") },
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
                .padding(24.dp)
        ) {
            Text("Terms of Service", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Last updated: May 6, 2026", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            Text("1. Acceptance of Terms", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("By downloading, installing, or using Virasat (the \"App\"), you agree to be bound by these Terms of Service. If you do not agree to these terms, please do not use the App. These terms constitute a legally binding agreement between you and Virasat regarding your use of the App.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("2. Description of Service", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Virasat is a heritage exploration application that provides information about Karnataka heritage sites, QR code check-ins, digital passport stamps, audio guides, and related features. The App is provided \"as is\" and we reserve the right to modify, suspend, or discontinue any part of the service at any time.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("3. User Accounts", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("To access certain features, you may be required to create an account. You agree to provide accurate, current, and complete information during registration. You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account. You must notify us immediately of any unauthorized use of your account.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("4. User Responsibilities", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("As a user of the App, you agree to:", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("   - Use the App only for lawful purposes and in accordance with these Terms.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Respect all heritage sites, cultural monuments, and local rules and regulations.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Not misuse QR check-ins by scanning codes without physically visiting sites.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Not attempt to gain unauthorized access to any part of the App or its systems.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Not upload, transmit, or distribute any viruses, malware, or harmful code.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Not engage in any activity that interferes with or disrupts the App's functionality.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("5. Intellectual Property", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("All content in the App, including but not limited to text, graphics, logos, images, audio guides, and software, is the property of Virasat or its licensors and is protected by copyright, trademark, and other intellectual property laws. You may not reproduce, modify, distribute, or create derivative works from any content without our express written permission. Heritage site images are sourced from Unsplash and other licensed sources. The Virasat name, logo, and branding are trademarks of Virasat.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("6. Content and Accuracy", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("All heritage content in the App is provided for educational and informational purposes only. While we strive for accuracy, we do not guarantee that all information is complete, accurate, or current. Historical details, visiting hours, and entry fees may change. Always verify critical information independently before visiting a site.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("7. Limitation of Liability", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("To the maximum extent permitted by law, Virasat shall not be liable for any indirect, incidental, special, consequential, or punitive damages arising out of or related to your use of the App. Virasat is not liable for incidents, injuries, or losses that may occur during site visits. You assume all risks associated with visiting heritage sites. Always follow safety guidelines, respect local authorities, and exercise caution.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("8. Termination", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("We reserve the right to suspend or terminate your access to the App at any time, with or without notice, for any reason, including but not limited to violation of these Terms. Upon termination, all licenses granted to you will immediately cease. You may also delete your account at any time through the App settings. Provisions regarding intellectual property, limitation of liability, and dispute resolution shall survive termination.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("9. Disclaimer of Warranties", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("The App is provided on an \"as is\" and \"as available\" basis without warranties of any kind, either express or implied. We do not warrant that the App will be uninterrupted, error-free, secure, or free from viruses. Your use of the App is at your sole risk.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("10. Governing Law and Dispute Resolution", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("These Terms shall be governed by and construed in accordance with the laws of India. Any dispute arising out of or relating to these Terms shall be subject to the exclusive jurisdiction of the courts in Bangalore, Karnataka. Before initiating any legal proceedings, you agree to attempt to resolve disputes informally by contacting us.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("11. Changes to Terms", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("We may revise these Terms from time to time. The most current version will always be posted in the App. By continuing to use the App after changes become effective, you agree to be bound by the revised Terms. If you do not agree to the revised Terms, you must stop using the App.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("12. Contact Information", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("If you have any questions about these Terms of Service, please contact us at legal@virasat.app.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
