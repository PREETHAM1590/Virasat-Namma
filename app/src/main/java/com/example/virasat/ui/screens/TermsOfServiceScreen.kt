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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.virasat.ui.theme.BeVietnamPro
import com.example.virasat.ui.theme.PlusJakartaSans
import androidx.compose.foundation.layout.ColumnScope

@Composable
fun TermsOfServiceScreen(onBack: () -> Unit) {
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
            text = "Terms of Service",
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
            PolicySection("1. Acceptance of Terms") {
                StyledBody("By downloading, installing, or using Virasat (the \"App\"), you agree to be bound by these Terms of Service. If you do not agree to these terms, please do not use the App.")
            }
            PolicySection("2. Description of Service") {
                StyledBody("Virasat is a heritage exploration application that provides information about Karnataka heritage sites, QR code check-ins, digital passport stamps, audio guides, and related features.")
            }
            PolicySection("3. User Accounts") {
                StyledBody("To access certain features, you may be required to create an account. You agree to provide accurate information during registration and maintain the confidentiality of your credentials.")
            }
            PolicySection("4. User Responsibilities") {
                StyledBody("As a user of the App, you agree to:")
                Spacer(modifier = Modifier.height(4.dp))
                StyledBullet("Use the App only for lawful purposes.")
                StyledBullet("Respect all heritage sites and local regulations.")
                StyledBullet("Not misuse QR check-ins without visiting sites.")
                StyledBullet("Not attempt unauthorized access to App systems.")
            }
            PolicySection("5. Intellectual Property") {
                StyledBody("All content in the App is the property of Virasat or its licensors and is protected by intellectual property laws. You may not reproduce or distribute content without permission.")
            }
            PolicySection("6. Limitation of Liability") {
                StyledBody("To the maximum extent permitted by law, Virasat shall not be liable for any indirect or consequential damages arising from your use of the App.")
            }
            PolicySection("7. Termination") {
                StyledBody("We reserve the right to suspend or terminate your access to the App at any time for violation of these Terms.")
            }
            PolicySection("8. Governing Law") {
                StyledBody("These Terms shall be governed by the laws of India. Any dispute shall be subject to the exclusive jurisdiction of courts in Bangalore, Karnataka.")
            }
            PolicySection("9. Changes to Terms") {
                StyledBody("We may revise these Terms from time to time. Your continued use constitutes acceptance of the revised Terms.")
            }
            PolicySection("10. Contact Information") {
                StyledBody("If you have any questions, please contact us at legal@virasat.app.")
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
