package com.example.virasat.data.source

import android.content.Context
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.service.GeminiHeritageService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Provides Kannada translations for site content.
 * Read strategy: local SharedPreferences cache → Firestore shared read → Gemini AI translation.
 * Write strategy: local cache only. Firestore write-back removed — `translations` collection
 * requires admin write access; client writes were causing PERMISSION_DENIED errors.
 */
object KannadaContentProvider {

    private val db = FirebaseFirestore.getInstance()
    private const val PREFS_NAME = "kannada_translations"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getCached(context: Context, siteId: String, field: String): String? {
        return prefs(context).getString("${siteId}_$field", null)
    }

    private fun cacheLocally(context: Context, siteId: String, field: String, value: String) {
        prefs(context).edit().putString("${siteId}_$field", value).apply()
    }

    /**
     * Get translation: local cache → Firestore shared read → Gemini translate & cache locally.
     */
    suspend fun getTranslation(
        context: Context,
        site: HeritageSite,
        field: String
    ): String = withContext(Dispatchers.IO) {
        // 1. Check local cache first (fastest)
        val local = getCached(context, site.id, field)
        if (!local.isNullOrBlank()) return@withContext local

        // 2. Check Firestore shared translations (read-only)
        try {
            val doc = db.collection("translations").document(site.id).get().await()
            if (doc.exists()) {
                val translation = doc.getString(field)
                if (!translation.isNullOrBlank()) {
                    cacheLocally(context, site.id, field, translation)
                    return@withContext translation
                }
            }
        } catch (_: Exception) { }

        // 3. Translate via Gemini and cache locally (no Firestore write — requires admin access)
        val englishText = when (field) {
            "description" -> site.description
            "shortDescription" -> site.shortDescription
            "history" -> site.history
            "architecture" -> site.architecture
            "legends" -> site.legends
            else -> return@withContext ""
        }
        if (englishText.isBlank() || !GeminiHeritageService.isInitialized()) return@withContext ""

        return@withContext try {
            val prompt = """Translate to Kannada (ಕನ್ನಡ). Output ONLY the Kannada text:

$englishText"""
            val translation = GeminiHeritageService.callGemini(prompt) ?: ""
            if (translation.isNotBlank()) {
                cacheLocally(context, site.id, field, translation)
            }
            translation.ifBlank { englishText }
        } catch (_: Exception) {
            englishText
        }
    }

    /** Pre-translate all fields for a site. */
    suspend fun preloadSite(context: Context, site: HeritageSite) {
        listOf("description", "shortDescription", "history", "architecture", "legends").forEach {
            getTranslation(context, site, it)
        }
    }
}
