package com.example.virasat.data.source

import android.content.Context
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.service.GeminiHeritageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Provides Kannada translations for site content.
 * Uses Gemini AI to translate on first access, then caches in SharedPreferences.
 * Falls back to English if translation unavailable.
 */
object KannadaContentProvider {

    private const val PREFS_NAME = "kannada_translations"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getCached(context: Context, siteId: String, field: String): String? {
        return prefs(context).getString("${siteId}_$field", null)
    }

    private fun cache(context: Context, siteId: String, field: String, value: String) {
        prefs(context).edit().putString("${siteId}_$field", value).apply()
    }

    /**
     * Get Kannada translation for a site field. Returns cached if available,
     * otherwise translates via Gemini and caches.
     */
    suspend fun getTranslation(
        context: Context,
        site: HeritageSite,
        field: String
    ): String = withContext(Dispatchers.IO) {
        val cached = getCached(context, site.id, field)
        if (!cached.isNullOrBlank()) return@withContext cached

        val englishText = when (field) {
            "description" -> site.description
            "shortDescription" -> site.shortDescription
            "history" -> site.history
            "architecture" -> site.architecture
            "legends" -> site.legends
            else -> return@withContext ""
        }

        if (englishText.isBlank()) return@withContext ""

        if (!GeminiHeritageService.isInitialized()) return@withContext ""

        try {
            val prompt = """Translate the following text about the heritage site "${site.name}" to Kannada (ಕನ್ನಡ). 
Provide ONLY the Kannada translation, no English, no explanations:

$englishText"""
            val translation = GeminiHeritageService.callGemini(prompt) ?: ""
            if (translation.isNotBlank()) {
                cache(context, site.id, field, translation)
            }
            translation.ifBlank { englishText }
        } catch (_: Exception) {
            englishText
        }
    }

    /**
     * Pre-translate all fields for a site (call when user views site detail).
     */
    suspend fun preloadSite(context: Context, site: HeritageSite) {
        listOf("description", "shortDescription", "history", "architecture", "legends").forEach { field ->
            getTranslation(context, site, field)
        }
    }
}
