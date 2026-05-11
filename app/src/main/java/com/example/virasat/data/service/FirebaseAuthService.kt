package com.example.virasat.data.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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

            // Create user profile in Firestore
            db.collection("users").document(user.uid)
                .set(mapOf(
                    "uid" to user.uid,
                    "name" to name,
                    "email" to email,
                    "createdAt" to System.currentTimeMillis(),
                    "checkInCount" to 0,
                    "badgesEarned" to 0
                )).await()

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
    fun friendlyError(e: Exception): String {
        val msg = e.message ?: "Unknown error"
        return when {
            "email address is already in use" in msg -> "This email is already registered. Try logging in."
            "password is invalid" in msg || "INVALID_LOGIN_CREDENTIALS" in msg -> "Incorrect email or password."
            "no user record" in msg || "USER_NOT_FOUND" in msg -> "No account found with this email."
            "badly formatted" in msg -> "Invalid email address."
            "weak-password" in msg || "WEAK_PASSWORD" in msg -> "Password must be at least 6 characters."
            "network error" in msg.lowercase() -> "Network error. Check your connection."
            "TOO_MANY_REQUESTS" in msg -> "Too many attempts. Try again later."
            else -> "Authentication failed. Please try again."
        }
    }
}
