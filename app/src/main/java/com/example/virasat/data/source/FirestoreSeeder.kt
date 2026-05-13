package com.example.virasat.data.source

import android.content.Context
import com.example.virasat.data.source.KarnatakaSites
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FirestoreSeeder {

    private const val PREFS_NAME = "virasat_prefs"
    private const val KEY_FIRESTORE_SEEDED = "firestore_seeded"

    fun seedIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_FIRESTORE_SEEDED, false)) return

        val dataSource = FirestoreDataSource()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataSource.seedSites(KarnatakaSites.allSites)
                prefs.edit().putBoolean(KEY_FIRESTORE_SEEDED, true).apply()
            } catch (_: Exception) {
                // Fail silently — next app launch will retry
            }
        }
    }

    fun resetSeedFlag(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_FIRESTORE_SEEDED, false).apply()
    }
}
