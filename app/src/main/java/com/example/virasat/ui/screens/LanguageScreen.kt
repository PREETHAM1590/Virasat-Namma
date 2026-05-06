package com.example.virasat.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.*
import java.util.Locale

data class LanguageOption(val id: String, val name: String, val englishName: String, val locale: Locale)

val languages = listOf(
    LanguageOption("en", "English", "", Locale.ENGLISH),
    LanguageOption("kn", "ಕನ್ನಡ", "(Kannada)", Locale("kn")),
    LanguageOption("hi", "हिंदी", "(Hindi)", Locale("hi")),
    LanguageOption("te", "తెలుగు", "(Telugu)", Locale("te")),
    LanguageOption("ta", "தமிழ்", "(Tamil)", Locale("ta"))
)

fun Context.applyLocale(locale: Locale) {
    val prefs = getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("selected_language", locale.language).apply()
    Locale.setDefault(locale)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun Context.getSavedLocale(): Locale {
    val prefs = getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
    val langCode = prefs.getString("selected_language", "kn") ?: "kn"
    return languages.find { it.id == langCode }?.locale ?: Locale("kn")
}

@Composable
fun LanguageScreen(onContinue: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val savedLang = remember { context.getSavedLocale().language }
    var selectedLang by remember { mutableStateOf(savedLang) }
    var previewText by remember { mutableStateOf("") }

    LaunchedEffect(selectedLang) {
        previewText = when (selectedLang) {
            "kn" -> "ಹಂಪಿ, ಮೈಸೂರು ಅರಮನೆ, ಬಾದಾಮಿ ಗವಿಗಳು..."
            "hi" -> "हम्पी, मैसूर पैलेस, बादामी गुफाएं..."
            "te" -> "హంపి, మైసూర్ ప్యాలెస్, బాదామి గుహలు..."
            "ta" -> "ஹம்பி, மைசூர் அரண்மனை, பாதாமி குகைகள்..."
            else -> "Hampi, Mysore Palace, Badami Caves..."
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = VirasatCream) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.padding(bottom = 16.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back", tint = VirasatMaroon, modifier = Modifier.size(28.dp))
            }
            Text("Choose Language", style = MaterialTheme.typography.headlineMedium, fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = VirasatMaroon, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Select your preferred language to continue", style = MaterialTheme.typography.bodyLarge, fontSize = 17.sp, color = Color.DarkGray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, lineHeight = 24.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), colors = CardDefaults.cardColors(containerColor = GoldContainer), shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Preview", style = MaterialTheme.typography.labelMedium, color = GoldOnContainer)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(previewText, style = MaterialTheme.typography.bodyMedium, color = VirasatText)
                }
            }
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(languages) { lang ->
                    LanguageCard(language = lang, isSelected = selectedLang == lang.id, onClick = { selectedLang = lang.id })
                }
            }
            Button(onClick = {
                val locale = languages.find { it.id == selectedLang }?.locale ?: Locale.ENGLISH
                context.applyLocale(locale)
                onContinue()
            }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon), shape = MaterialTheme.shapes.medium) {
                Text("Continue", style = MaterialTheme.typography.labelLarge, fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun LanguageCard(language: LanguageOption, isSelected: Boolean, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = Color.White), border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, VirasatGold) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2D8CE)), shape = MaterialTheme.shapes.medium, elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = isSelected, onClick = null, colors = RadioButtonDefaults.colors(selectedColor = VirasatMaroon, unselectedColor = VirasatMaroon))
                Spacer(modifier = Modifier.width(12.dp))
                Text(language.name, style = MaterialTheme.typography.bodyLarge, fontSize = 18.sp, color = if (isSelected) VirasatMaroon else VirasatText)
                if (language.englishName.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(language.englishName, style = MaterialTheme.typography.bodyMedium, fontSize = 17.sp, color = VirasatText)
                }
            }
            if (isSelected) {
                Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(VirasatMaroon), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}