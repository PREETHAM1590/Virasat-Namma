package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.virasat.ui.theme.BeVietnamPro
import com.example.virasat.ui.theme.PlusJakartaSans
import androidx.compose.foundation.layout.ColumnScope

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(999.dp)
                )
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Last updated: May 6, 2026",
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = BeVietnamPro),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp)
        ) {
            PolicySection("1. Introduction") {
                StyledBody("Virasat (\"we,\" \"our,\" or \"us\") is committed to protecting your privacy. This Privacy Policy explains how we collect, use, disclose, and safeguard your information when you use our mobile application.")
            }
            PolicySection("2. Data Collection") {
                StyledBody("We collect the following types of information:")
                Spacer(modifier = Modifier.height(4.dp))
                StyledBullet("Personal Information: name, email address, and phone number.")
                StyledBullet("Usage Data: check-in history, visited sites, badges earned.")
                StyledBullet("Device Data: device type, operating system, app version.")
                StyledBullet("Location Data: approximate location when you check in (with permission).")
            }
            PolicySection("3. Data Usage") {
                StyledBody("We use your data to personalize recommendations, power leaderboards, sync across devices, improve app performance, and provide customer support.")
            }
            PolicySection("4. Data Security") {
                StyledBody("We use Firebase Firestore with industry-standard encryption. Access is limited to authorized personnel. We never sell your personal data to third parties.")
            }
            PolicySection("5. Cookies and Tracking") {
                StyledBody("We use analytics tools to understand app usage patterns. You can disable analytics tracking in device settings. We do not use tracking for advertising.")
            }
            PolicySection("6. Third-Party Services") {
                StyledBody("Our app integrates with Google Firebase and Google Maps. These services have their own privacy policies.")
            }
            PolicySection("7. Data Retention") {
                StyledBody("We retain your data as long as your account is active. You may request deletion at any time. We will delete within 30 days of a valid request.")
            }
            PolicySection("8. Your Rights") {
                StyledBody("You have the right to access, correct, delete, or restrict processing of your personal data. You may also withdraw consent where applicable.")
            }
            PolicySection("9. Children's Privacy") {
                StyledBody("Our app is not intended for children under 13. We do not knowingly collect personal information from children under 13.")
            }
            PolicySection("10. Changes to This Policy") {
                StyledBody("We may update this Privacy Policy from time to time. Your continued use constitutes acceptance of the revised policy.")
            }
            PolicySection("11. Contact Us") {
                StyledBody("If you have any questions, please contact us at privacy@virasat.app.")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StyledBody(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = BeVietnamPro,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun PolicySection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun StyledBullet(text: String) {
    Text(
        text = "   • $text",
        style = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = BeVietnamPro,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(4.dp))
}
