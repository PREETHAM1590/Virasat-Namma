package com.example.virasat.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.R
import com.example.virasat.ui.theme.Primary
import com.example.virasat.ui.theme.PrimaryContainer
import com.example.virasat.ui.theme.OnPrimaryContainer
import com.example.virasat.ui.theme.SurfaceContainerLow
import com.example.virasat.ui.theme.SurfaceContainerLowest
import com.example.virasat.ui.theme.OnSurface
import com.example.virasat.ui.theme.OnSurfaceVariant
import com.example.virasat.ui.theme.OutlineVariant
import com.example.virasat.util.LocaleHelper

data class AppLanguage(val code: String, val displayName: String, val nativeName: String)

val languages = listOf(
    AppLanguage("en", "English", "English"),
    AppLanguage("kn", "Kannada", "\u0C95\u0CA8\u0CCD\u0CA8\u0CA1")
)

@Composable
fun LanguageScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedCode by remember { mutableStateOf(LocaleHelper.getSavedLocale(context)) }

    Box(
        Modifier
            .fillMaxSize()
            .background(SurfaceContainerLow)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(4.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                        .clip(RoundedCornerShape(999.dp))
                        .background(SurfaceContainerLowest)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = OnSurfaceVariant)
                }

                Text(
                    "Virasat",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.size(48.dp))
            }

            Spacer(Modifier.height(48.dp))

            Text(
                stringResource(R.string.language_select_title),
                style = MaterialTheme.typography.displayLarge.copy(lineHeight = 48.sp),
                color = Primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.language_select_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            languages.forEach { lang ->
                val isSelected = selectedCode == lang.code

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .shadow(
                            if (isSelected) 8.dp else 4.dp,
                            RoundedCornerShape(12.dp),
                            spotColor = Color.Black.copy(alpha = if (isSelected) 0.1f else 0.05f)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLowest)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) PrimaryContainer else OutlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedCode = lang.code }
                        .padding(24.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                lang.nativeName,
                                style = MaterialTheme.typography.headlineMedium,
                                color = OnSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                lang.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(if (isSelected) PrimaryContainer else SurfaceContainerLowest)
                                .border(
                                    width = 2.dp,
                                    color = if (isSelected) PrimaryContainer else OutlineVariant,
                                    shape = RoundedCornerShape(999.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = OnPrimaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(120.dp))
        }

        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Button(
                onClick = {
                    LocaleHelper.setLocale(context, selectedCode)
                    // Mark language as selected before recreating so the
                    // recreated activity navigates to onboarding (not language).
                    context.getSharedPreferences("virasat_prefs", android.content.Context.MODE_PRIVATE)
                        .edit().putBoolean("language_selected", true).apply()
                    // Recreate so stringResource() picks up the new locale immediately.
                    (context as? android.app.Activity)?.recreate() ?: onContinue()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryContainer,
                    contentColor = OnPrimaryContainer
                )
            ) {
                Text(stringResource(R.string.language_continue), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
