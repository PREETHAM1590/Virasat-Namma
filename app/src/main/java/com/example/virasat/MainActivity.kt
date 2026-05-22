package com.example.virasat

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.service.FirebaseAnalyticsHelper
import com.example.virasat.data.service.AIHeritageService
import com.example.virasat.data.service.FirebaseAuthService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import com.example.virasat.util.LocaleHelper
import com.google.firebase.auth.FirebaseAuth
import com.example.virasat.ui.components.BottomNavItem
import com.example.virasat.ui.screens.*
import com.example.virasat.ui.theme.VirasatTheme

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: android.content.Context) {
        val lang = LocaleHelper.getSavedLocale(newBase)
        super.attachBaseContext(LocaleHelper.wrap(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        // Locale is applied in attachBaseContext; no need to repeat here.
        window.setFormat(PixelFormat.OPAQUE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.setBackgroundColor(android.graphics.Color.parseColor("#FFF5E6"))
        // Defer non-critical init off main thread
        Thread {
            AIHeritageService.initialize(BuildConfig.GEMINI_API_KEY)
            AIHeritageService.initializeDeepSeek(BuildConfig.DEEPSEEK_API_KEY)
            AIHeritageService.initializeNova(BuildConfig.NOVA_API_URL, BuildConfig.NOVA_API_KEY)
            FirebaseAnalyticsHelper.init(this)
        }.start()
        setContent {
            VirasatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val prefs = LocalContext.current.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
                    val onboardingSeen = remember { prefs.getBoolean("onboarding_seen", false) }
                    val languageSelected = remember { prefs.getBoolean("language_selected", false) }

                    // Resolve start destination once — wait up to 3s for auth if returning user
                    val startDest by produceState<String?>(null) {
                        value = when {
                            !onboardingSeen && !languageSelected -> "language"
                            !onboardingSeen && languageSelected -> "onboarding"
                            else -> {
                                val user = withTimeoutOrNull(3_000) {
                                    FirebaseAuthService.authStateFlow().first()
                                }
                                if (user != null) "home" else "login"
                            }
                        }
                    }

                    val authUser by FirebaseAuthService.authStateFlow()
                        .collectAsState(initial = FirebaseAuthService.currentUser)
                    // Wait until start destination is resolved before showing NavHost
                    val resolvedDest = startDest ?: return@Surface
                    // Track whether NavHost has rendered at least once
                    var navGraphReady by remember { mutableStateOf(false) }
                    val navController = androidx.navigation.compose.rememberNavController()
                    // Re-run whenever authUser changes OR when navGraphReady flips to true,
                    // so a sign-out that occurs before the NavHost is ready is not missed.
                    LaunchedEffect(authUser, navGraphReady) {
                        if (!navGraphReady) return@LaunchedEffect
                        if (authUser == null && onboardingSeen) {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                    // Handle deep links
                    val activity = LocalContext.current as? android.app.Activity
                    LaunchedEffect(Unit) {
                        activity?.intent?.data?.let { uri ->
                            when (uri.host) {
                                "site" -> uri.lastPathSegment?.let { id -> navController.navigate("site_detail/$id") }
                                "badges" -> navController.navigate("badges")
                                "itinerary" -> navController.navigate("itinerary")
                            }
                        }
                    }
                    // Defer NavHost composition to second frame to avoid 1s+ first onMeasure
                    var ready by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { ready = true }
                    if (ready) {
                    LaunchedEffect(Unit) { navGraphReady = true }
                    NavHost(
                        navController = navController,
                        startDestination = resolvedDest,
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    ) {
                        composable("language") {
                            val isSettingsFlow = navController.previousBackStackEntry?.destination?.route == "settings"
                            val activity = LocalContext.current as? android.app.Activity
                            LanguageScreen(
                                onContinue = {
                                    if (isSettingsFlow) {
                                        navController.popBackStack()
                                        // LanguageScreen already calls recreate() so the new
                                        // locale is applied to all subsequently composed screens.
                                    } else {
                                        // First-time flow: LanguageScreen already set
                                        // language_selected=true and called recreate().
                                        // The recreated activity will navigate to onboarding.
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                onLogin = { name, email ->
                                    prefs.edit()
                                        .putString("user_name", name)
                                        .putString("user_email", email)
                                        .apply()
                                    navController.navigate("home") { popUpTo(0) { inclusive = true } }
                                },
                                onNavigateToSignUp = { navController.navigate("signup") },
                                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("signup") {
                            SignUpScreen(
                                onSignUp = { name, email, _ ->
                                    prefs.edit()
                                        .putString("user_name", name)
                                        .putString("user_email", email)
                                        .apply()
                                    navController.navigate("home") { popUpTo(0) { inclusive = true } }
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
                                    prefs.edit().putBoolean("onboarding_seen", true).apply()
                                    navController.navigate("login") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("home") {
                            val homeViewModel: com.example.virasat.viewmodel.HomeViewModel = viewModel()
                            FirebaseAnalyticsHelper.logScreenView("home")
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
                                    onItinerary = { navController.navigate("itinerary") },
                                    onSearch = { navController.navigate("search") },
                                    onSitesList = { navController.navigate("sites_list") },
                                    onCategoryClick = { category ->
                                        val siteTypeMap = mapOf(
                                            "Temple" to "TEMPLE", "Palace" to "PALACE", "Fort" to "FORT",
                                            "Monument" to "MONUMENT", "UNESCO" to "UNESCO", "Jain" to "JAIN",
                                            "Museum" to "MUSEUM", "Nature" to "NATURE"
                                        )
                                        val typeName = siteTypeMap[category]
                                        if (typeName != null) {
                                            homeViewModel.setTypeFilter(com.example.virasat.data.model.SiteType.valueOf(typeName))
                                        }
                                    }
                                )
                            }
                        composable("site_detail/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            FirebaseAnalyticsHelper.logScreenView("site_detail")
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
                                },
                                onTalkingTour = { sId ->
                                    navController.navigate("talking_tour/$sId")
                                },
                                onQuiz = { sId ->
                                    navController.navigate("quiz/$sId")
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
                            val ctx = LocalContext.current
                            val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
                            var siteName by remember { mutableStateOf(siteId) }
                            LaunchedEffect(siteId) {
                                siteName = repo.getSiteById(siteId)?.name ?: siteId
                            }
                            AudioGuideScreen(
                                siteId = siteId,
                                siteName = siteName,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("immersive/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            val ctx = LocalContext.current
                            val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
                            val site by produceState<com.example.virasat.data.model.HeritageSite?>(null, siteId) {
                                value = repo.getSiteById(siteId)
                            }
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
                                onBack = { navController.popBackStack() },
                                onSiteClick = { siteId -> navController.navigate("site_detail/$siteId") }
                            )
                        }
                        composable("profile") {
                            FirebaseAnalyticsHelper.logScreenView("profile")
                            Scaffold(bottomBar = { com.example.virasat.ui.components.VirasatBottomNavBar("profile") { navController.navigate(it.route) { popUpTo("home") { inclusive = false } } } }) { p ->
                                Box(modifier = Modifier.padding(p)) {
                                    ProfileScreen(
                                        onBack = { navController.popBackStack() },
                                        onEditProfile = { navController.navigate("edit_profile") },
                                        onSettings = { navController.navigate("settings") },
                                        onBookmarks = { navController.navigate("bookmarks") },
                                        onPassport = { navController.navigate("passport") },
                                        onLogout = {
                                        FirebaseAuthService.signOut()
                                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                                    },
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
                        composable("edit_profile") {
                            EditProfileScreen(
                                onBack = { navController.popBackStack() },
                                onSaved = { navController.popBackStack() }
                            )
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
                                onDataSync = { navController.navigate("data_sync") },
                                onLogout = {
                                    FirebaseAuthService.signOut()
                                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                                }
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
                            FirebaseAnalyticsHelper.logScreenView("search")
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
                        composable("check_in_success/{siteId}/{factId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            val factId = backStackEntry.arguments?.getString("factId") ?: ""
                            CheckInSuccessScreen(
                                siteId = siteId,
                                factId = factId,
                                onViewSite = { siteId ->
                                    navController.navigate("site_detail/$siteId") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                },
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
                            FirebaseAnalyticsHelper.logScreenView("ai_assistant")
                            AIAssistantScreen(onBack = { navController.popBackStack() })
                        }
                        composable("quiz") {
                            FirebaseAnalyticsHelper.logScreenView("quiz")
                            QuizScreen(onBack = { navController.popBackStack() })
                        }
                        composable("quiz/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            FirebaseAnalyticsHelper.logScreenView("quiz")
                            QuizScreen(siteId = siteId, onBack = { navController.popBackStack() })
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
                            FirebaseAnalyticsHelper.logScreenView("map")
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
                            FirebaseAnalyticsHelper.logScreenView("itinerary")
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
                        composable("ai_tour/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            AINarratedTourScreen(siteId = siteId, onBack = { navController.popBackStack() })
                        }
                        composable("talking_tour/{siteId}") { backStackEntry ->
                            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
                            FirebaseAnalyticsHelper.logScreenView("talking_tour")
                            TalkingToursScreen(siteId = siteId, onBack = { navController.popBackStack() })
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
                    } // end if (ready)
                }
            }
        }
    }
}
