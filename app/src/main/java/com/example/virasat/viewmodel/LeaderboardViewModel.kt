package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.LeaderboardEntry
import com.example.virasat.data.service.FirebaseAuthService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()

    val leaderboard: StateFlow<List<LeaderboardEntry>> = callbackFlow {
        val listener = db.collection("users")
            .orderBy("checkInCount", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snap, _ ->
                val entries = snap?.documents?.mapNotNull { doc ->
                    LeaderboardEntry(
                        uid = doc.id,
                        name = doc.getString("name") ?: "",
                        checkIns = (doc.getLong("checkInCount") ?: 0).toInt(),
                        badges = (doc.getLong("badgesEarned") ?: 0).toInt()
                    )
                }
                trySend(entries ?: emptyList())
            }
        awaitClose { listener.remove() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val currentUid: String? get() = FirebaseAuthService.uid
}
