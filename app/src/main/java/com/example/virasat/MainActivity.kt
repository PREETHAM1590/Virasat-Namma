package com.example.virasat

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virasat.ui.screens.*
import com.example.virasat.ui.theme.VirasatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFormat(PixelFormat.OPAQUE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.setBackgroundColor(android.graphics.Color.parseColor("#FFF5E6"))
        setContent {
            VirasatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen(
                                onNavigateToLanguage = {
                                    navController.navigate("language") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("language") {
                            LanguageScreen(
                                onContinue = {
                                    navController.navigate("onboarding") {
                                        popUpTo("language") { inclusive = true }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("onboarding") {
                            OnboardingScreen(
                                onFinish = {
                                    navController.navigate("home") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                onSiteClick = { siteId ->
                                    navController.navigate("site_detail/$siteId")
                                },
                                onQrScan = {
                                    navController.navigate("qr_scan")
                                },
                                onPassport = {
                                    navController.navigate("passport")
                                },
                                onFavourites = {
                                    navController.navigate("favourites")
                                }
                            )
                        }
                        composable("site_detail/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            SiteDetailScreen(
                                siteId = siteId,
                                onBack = { navController.popBackStack() },
                                onAudioGuide = { sId ->
                                    navController.navigate("audio_guide/$sId")
                                },
                                onCheckIn = {
                                    navController.navigate("qr_scan")
                                }
                            )
                        }
                        composable("qr_scan") {
                            QrScannerScreen(
                                onBack = { navController.popBackStack() },
                                onNavigateToSite = { siteId ->
                                    navController.navigate("site_detail/$siteId") {
                                        popUpTo("qr_scan") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("audio_guide/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            AudioGuideScreen(
                                siteId = siteId,
                                siteName = siteId.replace("-", " ").replaceFirstChar { it.uppercase() },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("passport") {
                            TravelPassportScreen(
                                onBack = { navController.popBackStack() },
                                onSiteClick = { siteId ->
                                    navController.navigate("site_detail/$siteId")
                                }
                            )
                        }
                        composable("favourites") {
                            // Reuse HomeScreen with favourites filter
                            HomeScreen(
                                onSiteClick = { siteId ->
                                    navController.navigate("site_detail/$siteId")
                                },
                                onQrScan = {
                                    navController.navigate("qr_scan")
                                },
                                onPassport = {
                                    navController.navigate("passport")
                                },
                                onFavourites = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
