package com.example.virasat.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    fun wrap(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
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
    }

    /** Returns site name in selected language */
    fun siteName(name: String, nameLocal: String, context: Context): String {
        return if (getSavedLocale(context) == "kn") nameLocal.ifBlank { name } else name
    }
}
