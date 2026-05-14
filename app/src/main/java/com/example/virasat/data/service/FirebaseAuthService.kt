package com.example.virasat.data.service

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseAuthService {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser? get() = auth.currentUser
    val uid: String? get() = auth.currentUser?.uid
    val isLoggedIn: Boolean get() = auth.currentUser != null

    // ── Auth state stream ──────────────────────────────────────────────────
    fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    // ── Sign Up ────────────────────────────────────────────────────────────
    suspend fun signUp(name: String, email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("User creation failed"))

            // Set display name
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdate).await()

            // Create user profile in Firestore — if this fails, delete the auth account
            // to avoid partial state (Requirement 3.2)
            try {
                db.collection("users").document(user.uid)
                    .set(mapOf(
                        "uid" to user.uid,
                        "name" to name,
                        "email" to email,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "checkInCount" to 0,
                        "badgesEarned" to 0
                    )).await()
            } catch (firestoreEx: Exception) {
                // Roll back: delete the Firebase Auth account so no partial state exists
                try { user.delete().await() } catch (_: Exception) { /* best-effort */ }
                return Result.failure(firestoreEx)
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Sign In ────────────────────────────────────────────────────────────
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user ?: throw Exception("Login failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Password Reset ─────────────────────────────────────────────────────
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Sign Out ───────────────────────────────────────────────────────────
    fun signOut() {
        auth.signOut()
    }

    // ── Google Sign-In ─────────────────────────────────────────────────────
    fun getGoogleSignInIntent(context: Context, webClientId: String): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(context, gso)
        return client.signInIntent
    }

    suspend fun handleGoogleSignInResult(data: Intent?): Result<FirebaseUser> {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken ?: return Result.failure(Exception("Google ID token is null"))
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user ?: return Result.failure(Exception("Firebase auth failed"))

            // Create/update user profile in Firestore if new user
            if (result.additionalUserInfo?.isNewUser == true) {
                db.collection("users").document(user.uid)
                    .set(mapOf(
                        "uid" to user.uid,
                        "name" to (user.displayName ?: ""),
                        "email" to (user.email ?: ""),
                        "createdAt" to FieldValue.serverTimestamp(),
                        "checkInCount" to 0,
                        "badgesEarned" to 0
                    )).await()
            }
            Result.success(user)
        } catch (e: ApiException) {
            Result.failure(Exception("Google sign-in failed: ${e.statusCode}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Fetch user profile from Firestore ──────────────────────────────────
    suspend fun getUserProfile(): Map<String, Any?> {
        val uid = uid ?: return emptyMap()
        return try {
            db.collection("users").document(uid).get().await().data ?: emptyMap()
        } catch (_: Exception) {
            emptyMap()
        }
    }

    // ── Friendly error messages ────────────────────────────────────────────
    // Returns a human-readable string with NO raw Firebase error code constants
    // (no substring matching [A-Z_]{5,}) — Requirement 3.4, 3.11
    fun friendlyError(e: Exception): String {
        val msg = e.message ?: "Unknown error"
        val msgLower = msg.lowercase()
        return when {
            "email address is already in use" in msgLower -> "This email is already registered. Try logging in."
            "password is invalid" in msgLower || "invalid login credentials" in msgLower ||
                "invalid credential" in msgLower -> "Incorrect email or password."
            "no user record" in msgLower || "user not found" in msgLower -> "No account found with this email."
            "badly formatted" in msgLower || "invalid email" in msgLower -> "Invalid email address."
            "weak-password" in msgLower || "weak password" in msgLower ||
                "password should be at least" in msgLower -> "Password must be at least 6 characters."
            "network error" in msgLower || "unable to resolve host" in msgLower ||
                "failed to connect" in msgLower -> "Network error. Check your connection."
            "too many requests" in msgLower || "too many attempts" in msgLower -> "Too many attempts. Try again later."
            "sign-in failed" in msgLower -> "Sign-in failed. Please try again."
            else -> "Authentication failed. Please try again."
        }
    }
}
