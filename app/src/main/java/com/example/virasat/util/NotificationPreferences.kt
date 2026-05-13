package com.example.virasat.util

import android.content.Context

object NotificationPreferences {
    private const val PREFS = "notification_prefs"

    fun isEnabled(context: Context, category: String): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(category, true)

    fun setEnabled(context: Context, category: String, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(category, enabled).apply()
    }
}
