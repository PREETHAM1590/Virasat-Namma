package com.example.virasat.data.repository

import com.example.virasat.data.model.Itinerary
import com.example.virasat.data.service.FirebaseAuthService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ItineraryRepository {
    private val db = FirebaseFirestore.getInstance()

    private fun collection() = db.collection("users")
        .document(FirebaseAuthService.uid ?: "anon")
        .collection("itineraries")

    suspend fun save(itinerary: Itinerary): String {
        val doc = if (itinerary.id.isBlank()) collection().document()
                  else collection().document(itinerary.id)
        doc.set(mapOf(
            "name" to itinerary.name,
            "siteIds" to itinerary.siteIds,
            "createdAt" to itinerary.createdAt
        )).await()
        return doc.id
    }

    fun observeAll(): Flow<List<Itinerary>> = callbackFlow {
        val listener = collection()
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.mapNotNull { doc ->
                    Itinerary(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        siteIds = (doc.get("siteIds") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                        createdAt = doc.getLong("createdAt") ?: 0L
                    )
                }
                trySend(list ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun getById(id: String): Itinerary? {
        val doc = collection().document(id).get().await()
        if (!doc.exists()) return null
        return Itinerary(
            id = doc.id,
            name = doc.getString("name") ?: "",
            siteIds = (doc.get("siteIds") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
            createdAt = doc.getLong("createdAt") ?: 0L
        )
    }
}
