package com.example.virasat.data.source

import android.content.Context

/**
 * FirestoreSeeder — disabled.
 *
 * Seeding heritage-site data from the client app is intentionally removed:
 * Firestore security rules allow only authenticated users to read `sites/`,
 * not write. Site data must be seeded from a trusted backend / Firebase Admin SDK.
 *
 * All functions are kept as no-ops so no call-site breaks.
 */
object FirestoreSeeder {

    @Suppress("UNUSED_PARAMETER")
    fun seedIfNeeded(context: Context) {
        // No-op — client must never write to the global `sites` collection.
    }

    @Suppress("UNUSED_PARAMETER")
    fun resetSeedFlag(context: Context) {
        // No-op
    }
}
