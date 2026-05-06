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
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
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
            Text("Privacy Policy", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = VirasatMaroon)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Last updated: May 6, 2026", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            Text("1. Introduction", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Virasat (\"we,\" \"our,\" or \"us\") is committed to protecting your privacy. This Privacy Policy explains how we collect, use, disclose, and safeguard your information when you use our mobile application.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("2. Data Collection", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("We collect the following types of information:", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("   - Personal Information: name, email address, and phone number (only when you contact us).", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Usage Data: check-in history, visited sites, badges earned, and app preferences.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Device Data: device type, operating system, app version, and unique device identifiers.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Location Data: approximate location when you check in at heritage sites (with your permission).", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("3. Data Usage", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("We use your data to:", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("   - Personalize recommendations and content based on your interests and visit history.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Power leaderboards and achievement systems within the app.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Sync your data across devices using your account.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Improve app performance and user experience through analytics.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Respond to your inquiries and provide customer support.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("4. Data Security", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("We use Firebase Firestore with industry-standard encryption to store your data. Access is limited to authorized personnel only. We conduct regular security audits to protect your information from unauthorized access, alteration, disclosure, or destruction. We never sell your personal data to third parties.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("5. Cookies and Tracking Technologies", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("We use analytics tools to understand app usage patterns. These tools may use cookies or similar technologies to collect anonymous usage statistics. You can disable analytics tracking in your device settings. We do not use tracking for advertising purposes.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("6. Third-Party Services", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Our app integrates with the following third-party services:", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("   - Google Firebase: for authentication, database, and analytics.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Google Maps: for displaying heritage site locations.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - These services have their own privacy policies which we encourage you to review.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("7. Data Retention and Deletion", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("We retain your data as long as your account is active. You may request deletion of your account and associated data at any time by contacting us. We will delete your data within 30 days of receiving a valid deletion request, except where retention is required by law.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("8. Your Rights", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("You have the right to:", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("   - Access the personal data we hold about you.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Correct inaccurate or incomplete data.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Request deletion of your personal data.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Object to or restrict certain processing activities.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Text("   - Withdraw consent where processing is based on consent.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("9. Children's Privacy", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Our app is not intended for children under 13. We do not knowingly collect personal information from children under 13. If you believe we have collected information from a child under 13, please contact us immediately.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("10. Changes to This Policy", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("We may update this Privacy Policy from time to time. We will notify you of any material changes by posting the updated policy in the app. Your continued use of the app after changes constitutes acceptance of the revised policy.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("11. Contact Us", fontWeight = FontWeight.Bold, color = VirasatMaroon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("If you have any questions about this Privacy Policy or our data practices, please contact us at privacy@virasat.app.", fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
