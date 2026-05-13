package com.example.virasat.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurePreferences {

    private const val PREFS_NAME = "virasat_secure_prefs"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_ONBOARDING_SEEN = "onboarding_seen"
    private const val KEY_LOCALE = "app_locale"

    private fun getEncryptedPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // User credentials (encrypted)
    fun saveUserCredentials(context: Context, name: String, email: String) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit()
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    fun getUserName(context: Context): String? {
        return getEncryptedPrefs(context).getString(KEY_USER_NAME, null)
    }

    fun getUserEmail(context: Context): String? {
        return getEncryptedPrefs(context).getString(KEY_USER_EMAIL, null)
    }

    fun clearUserCredentials(context: Context) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit()
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_AUTH_TOKEN)
            .apply()
    }

    // Auth token (encrypted)
    fun saveAuthToken(context: Context, token: String) {
        getEncryptedPrefs(context).edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }

    fun getAuthToken(context: Context): String? {
        return getEncryptedPrefs(context).getString(KEY_AUTH_TOKEN, null)
    }

    fun clearAuthToken(context: Context) {
        getEncryptedPrefs(context).edit()
            .remove(KEY_AUTH_TOKEN)
            .apply()
    }

    // Onboarding (can stay unencrypted as it's not sensitive)
    fun setOnboardingSeen(context: Context, seen: Boolean) {
        context.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ONBOARDING_SEEN, seen)
            .apply()
    }

    fun isOnboardingSeen(context: Context): Boolean {
        return context.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
            .getBoolean(KEY_ONBOARDING_SEEN, false)
    }

    // Locale (can stay unencrypted)
    fun setLocale(context: Context, languageCode: String) {
        context.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LOCALE, languageCode)
            .apply()
    }

    fun getLocale(context: Context): String {
        return context.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
            .getString(KEY_LOCALE, "en") ?: "en"
    }

    // Clear all secure data on logout
    fun clearAll(context: Context) {
        clearUserCredentials(context)
        clearAuthToken(context)
    }
}