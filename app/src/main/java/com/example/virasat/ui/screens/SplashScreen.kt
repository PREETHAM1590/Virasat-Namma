package com.example.virasat.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.virasat.R
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

/**
 * SplashScreen — displayed for 1–3 seconds on every launch.
 *
 * Navigation logic (Requirements 1.1–1.5):
 *  - If onboarding has NOT been seen → navigate to LanguageScreen (first-launch flow).
 *  - If onboarding HAS been seen:
 *      - Wait up to 3 s for [authStateProvider] to emit a non-null or null user.
 *      - Authenticated within 3 s  → navigate to HomeScreen.
 *      - Unauthenticated within 3 s → navigate to LoginScreen.
 *      - Auth did not resolve within 3 s (timeout) → fallback to LoginScreen (Req 1.5).
 *
 * The minimum display time is 1 second (Req 1.1).
 */
@Composable
fun SplashScreen(
    onboardingSeen: Boolean,
    authStateProvider: suspend () -> FirebaseUser?,
    onNavigateToLanguage: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    LaunchedEffect(Unit) {
        // Always show splash for at least 1 second (Req 1.1)
        val minDisplayMs = 1_000L
        val maxWaitMs = 3_000L

        if (!onboardingSeen) {
            // First-launch path: splash → language → onboarding → login (Req 1.2)
            delay(minDisplayMs)
            onNavigateToLanguage()
        } else {
            // Returning user: wait up to 3 s for auth to resolve (Req 1.3, 1.4, 1.5)
            val startTime = System.currentTimeMillis()
            val authUser = withTimeoutOrNull(maxWaitMs) { authStateProvider() }
            val elapsed = System.currentTimeMillis() - startTime
            // Ensure minimum 1 s display time
            val remaining = minDisplayMs - elapsed
            if (remaining > 0) delay(remaining)

            if (authUser != null) {
                // Authenticated within 3 s → home (Req 1.4)
                onNavigateToHome()
            } else {
                // Unauthenticated or timed out → login (Req 1.3, 1.5)
                onNavigateToLogin()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
