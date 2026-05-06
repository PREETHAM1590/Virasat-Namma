package com.example.virasat

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virasat.ui.components.BottomNavItem
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
                    val prefs = LocalContext.current.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
                    val onboarded = remember { prefs.getBoolean("onboarding_complete", false) }
                    val startDest = if (onboarded) "home" else "splash"
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = startDest
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
                            val isSettingsFlow = navController.previousBackStackEntry?.destination?.route == "settings"
                            LanguageScreen(
                                onContinue = {
                                    if (isSettingsFlow) {
                                        navController.popBackStack()
                                    } else {
                                        navController.navigate("login") {
                                            popUpTo("language") { inclusive = true }
                                        }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                onLogin = { _, _ ->
                                    prefs.edit().putBoolean("onboarding_complete", true).apply()
                                    navController.navigate("onboarding") { popUpTo("login") { inclusive = true } }
                                },
                                onNavigateToSignUp = { navController.navigate("signup") },
                                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("signup") {
                            SignUpScreen(
                                onSignUp = { _, _, _ ->
                                    prefs.edit().putBoolean("onboarding_complete", true).apply()
                                    navController.navigate("onboarding") { popUpTo("signup") { inclusive = true } }
                                },
                                onNavigateToLogin = { navController.popBackStack() },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                onSendResetLink = { },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("onboarding") {
                            OnboardingScreen(
                                onFinish = {
                                    prefs.edit().putBoolean("onboarding_complete", true).apply()
                                    navController.navigate("home") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                onSiteClick = { siteId ->
                                    navController.navigate("site_detail/$siteId") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                },
                                onQrScan = { navController.navigate("qr_scan") },
                                onPassport = { navController.navigate("passport") },
                                onFavourites = { navController.navigate("favourites") },
                                onNavItemClick = { item ->
                                    navController.navigate(item.route) {
                                        popUpTo("home") { inclusive = false }
                                    }
                                },
                                onBadges = { navController.navigate("badges") },
                                onQuiz = { navController.navigate("quiz") },
                                onAiAssistant = { navController.navigate("ai_assistant") },
                                onGuides = { navController.navigate("guides") },
                                onItinerary = { navController.navigate("itinerary") }
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
                                },
                                onImmersiveView = { sId ->
                                    navController.navigate("immersive/$sId")
                                },
                                onGallery = { sId ->
                                    navController.navigate("gallery/$sId")
                                },
                                onReviews = { sId ->
                                    navController.navigate("reviews/$sId")
                                }
                            )
                        }
                        composable("qr_scan") {
                            QrScannerScreen(
                                onBack = { navController.popBackStack() },
                                onNavigateToSite = { siteId ->
                                    navController.navigate("check_in_success/$siteId") {
                                        popUpTo("qr_scan") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("audio_guide/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            val site = com.example.virasat.data.source.KarnatakaSites.allSites.find { it.id == siteId }
                            AudioGuideScreen(
                                siteId = siteId,
                                siteName = site?.name ?: siteId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("immersive/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            val site = com.example.virasat.data.source.KarnatakaSites.allSites.find { it.id == siteId }
                            ImmersivePhotoScreen(
                                siteId = siteId,
                                siteName = site?.name ?: siteId,
                                galleryImages = site?.galleryImages ?: listOf(site?.imageUrl ?: ""),
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
                            BookmarkedSitesScreen(
                                onBack = { navController.popBackStack() },
                                onSiteClick = { siteId ->
                                    navController.navigate("site_detail/$siteId")
                                }
                            )
                        }
                        composable("offline") {
                            OfflineScreen(
                                onRetry = { navController.popBackStack() },
                                onBrowseOffline = { navController.navigate("bookmarks") }
                            )
                        }
                        composable("profile") {
                            Scaffold(bottomBar = { com.example.virasat.ui.components.VirasatBottomNavBar("profile") { navController.navigate(it.route) { popUpTo("home") { inclusive = false } } } }) { p ->
                                Box(modifier = Modifier.padding(p)) {
                                    ProfileScreen(
                                        onBack = { navController.popBackStack() },
                                        onEditProfile = { },
                                        onSettings = { navController.navigate("settings") },
                                        onBookmarks = { navController.navigate("bookmarks") },
                                        onPassport = { navController.navigate("passport") },
                                        onLogout = { navController.navigate("login") },
                                        onBadges = { navController.navigate("badges") },
                                        onCheckIns = { navController.navigate("my_checkins") },
                                        onGuides = { navController.navigate("guides") },
                                        onCommunity = { navController.navigate("community") },
                                        onFeedback = { navController.navigate("feedback") },
                                        onHelp = { navController.navigate("help_support") },
                                        onSitesList = { navController.navigate("sites_list") },
                                        onLeaderboard = { navController.navigate("leaderboard") }
                                    )
                                }
                            }
                        }
                        composable("settings") {
                            SettingsScreen(
                                onBack = { navController.popBackStack() },
                                onLanguageSettings = { navController.navigate("language") },
                                onNotifications = { navController.navigate("notifications") },
                                onDarkMode = { },
                                onAbout = { navController.navigate("about") },
                                onHelp = { navController.navigate("help_support") },
                                onPrivacy = { navController.navigate("privacy_policy") },
                                onTerms = { navController.navigate("terms") },
                                onDataSync = { navController.navigate("data_sync") }
                            )
                        }
                        composable("about") {
                            AboutScreen(onBack = { navController.popBackStack() })
                        }
                        composable("help_support") {
                            HelpSupportScreen(
                                onBack = { navController.popBackStack() },
                                onContactSupportClick = { navController.navigate("contact_us") }
                            )
                        }
                        composable("search") {
                            Scaffold(bottomBar = { com.example.virasat.ui.components.VirasatBottomNavBar("search") { navController.navigate(it.route) { popUpTo("home") { inclusive = false } } } }) { p ->
                                Box(modifier = Modifier.padding(p)) {
                                    SearchScreen(
                                        onBack = { navController.popBackStack() },
                                        onSiteClick = { siteId -> navController.navigate("site_detail/$siteId") },
                                        onNavigateToResults = {}
                                    )
                                }
                            }
                        }
                        composable("bookmarks") {
                            BookmarkedSitesScreen(
                                onBack = { navController.popBackStack() },
                                onSiteClick = { siteId -> navController.navigate("site_detail/$siteId") }
                            )
                        }
                        composable("gallery/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            ImageGalleryScreen(
                                siteId = siteId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("check_in_success/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            CheckInSuccessScreen(
                                siteId = siteId,
                                onViewSite = { navController.popBackStack() },
                                onViewPassport = { navController.navigate("passport") },
                                onShare = { }
                            )
                        }
                        composable("badges") {
                            BadgesScreen(onBack = { navController.popBackStack() })
                        }
                        composable("guides") {
                            HeritageGuidesScreen(
                                onBack = { navController.popBackStack() },
                                onGuideClick = { }
                            )
                        }
                        composable("ai_assistant") {
                            AIAssistantScreen(onBack = { navController.popBackStack() })
                        }
                        composable("quiz") {
                            QuizScreen(onBack = { navController.popBackStack() })
                        }
                        composable("notifications") {
                            Scaffold(bottomBar = { com.example.virasat.ui.components.VirasatBottomNavBar("notifications") { navController.navigate(it.route) { popUpTo("home") { inclusive = false } } } }) { p ->
                                Box(modifier = Modifier.padding(p)) {
                                    NotificationsScreen(
                                        onBack = { navController.popBackStack() },
                                        onNotificationClick = { }
                                    )
                                }
                            }
                        }
                        composable("map") {
                            Scaffold(bottomBar = { com.example.virasat.ui.components.VirasatBottomNavBar("map") { navController.navigate(it.route) { popUpTo("home") { inclusive = false } } } }) { p ->
                                Box(modifier = Modifier.padding(p)) {
                                    MapScreen(
                                        onBack = { navController.popBackStack() },
                                        onSiteClick = { siteId -> navController.navigate("site_detail/$siteId") }
                                    )
                                }
                            }
                        }
                        composable("sites_list") {
                            HeritageSitesListScreen(
                                onBack = { navController.popBackStack() },
                                onSiteClick = { siteId -> navController.navigate("site_detail/$siteId") }
                            )
                        }
                        composable("itinerary") {
                            ItineraryScreen(
                                onBack = { navController.popBackStack() },
                                onSiteClick = { siteId -> navController.navigate("site_detail/$siteId") }
                            )
                        }
                        composable("reviews/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            ReviewsScreen(
                                siteId = siteId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("community") {
                            CommunityScreen(onBack = { navController.popBackStack() })
                        }
                        composable("leaderboard") {
                            LeaderboardScreen(onBack = { navController.popBackStack() })
                        }
                        composable("my_checkins") {
                            MyCheckInsScreen(
                                onBack = { navController.popBackStack() },
                                onSiteClick = { siteId -> navController.navigate("site_detail/$siteId") }
                            )
                        }
                        composable("virtual_tour/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            VirtualTourScreen(siteId = siteId, onBack = { navController.popBackStack() })
                        }
                        composable("ar/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            ARScreen(siteId = siteId, onBack = { navController.popBackStack() })
                        }
                        composable("contact_us") {
                            ContactUsScreen(
                                onBack = { navController.popBackStack() },
                                onSubmit = { navController.popBackStack() }
                            )
                        }
                        composable("privacy_policy") {
                            PrivacyPolicyScreen(onBack = { navController.popBackStack() })
                        }
                        composable("terms") {
                            TermsOfServiceScreen(onBack = { navController.popBackStack() })
                        }
                        composable("feedback") {
                            FeedbackScreen(
                                onBack = { navController.popBackStack() },
                                onSubmit = { navController.popBackStack() }
                            )
                        }
                        composable("data_sync") {
                            DataSyncScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
