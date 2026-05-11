package com.example.virasat

import android.app.Application
import com.google.firebase.FirebaseApp

class VirasatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase (ensures SDK ready before any usage)
        FirebaseApp.initializeApp(this)
        // NOTE: Firestore seeding (KarnatakaSites) is triggered from MainActivity
        // after the user is authenticated, since write rules require auth.
    }
}
