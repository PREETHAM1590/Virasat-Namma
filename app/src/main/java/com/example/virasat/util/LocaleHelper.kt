package com.example.virasat.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {

    fun wrap(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        // On older APIs, also update resources directly
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return context.createConfigurationContext(config)
    }

    fun getSavedLocale(context: Context): String {
        return context.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
            .getString("app_locale", "en") ?: "en"
    }

    fun setLocale(context: Context, languageCode: String) {
        context.getSharedPreferences("virasat_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("app_locale", languageCode)
            .apply()
        // Apply immediately to default locale
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
    }

    fun siteName(name: String, nameLocal: String, context: Context): String {
        return if (getSavedLocale(context) == "kn") nameLocal.ifBlank { name } else name
    }

    fun siteDescription(site: com.example.virasat.data.model.HeritageSite, context: Context): String {
        if (getSavedLocale(context) != "kn") return site.description
        return com.example.virasat.data.source.KannadaContentProvider.getCached(context, site.id, "description") ?: site.description
    }

    fun siteShortDescription(site: com.example.virasat.data.model.HeritageSite, context: Context): String {
        if (getSavedLocale(context) != "kn") return site.shortDescription
        return com.example.virasat.data.source.KannadaContentProvider.getCached(context, site.id, "shortDescription") ?: site.shortDescription
    }

    fun siteHistory(site: com.example.virasat.data.model.HeritageSite, context: Context): String {
        if (getSavedLocale(context) != "kn") return site.history
        return com.example.virasat.data.source.KannadaContentProvider.getCached(context, site.id, "history") ?: site.history
    }

    fun siteArchitecture(site: com.example.virasat.data.model.HeritageSite, context: Context): String {
        if (getSavedLocale(context) != "kn") return site.architecture
        return com.example.virasat.data.source.KannadaContentProvider.getCached(context, site.id, "architecture") ?: site.architecture
    }

    fun siteLegends(site: com.example.virasat.data.model.HeritageSite, context: Context): String {
        if (getSavedLocale(context) != "kn") return site.legends
        return com.example.virasat.data.source.KannadaContentProvider.getCached(context, site.id, "legends") ?: site.legends
    }
}
