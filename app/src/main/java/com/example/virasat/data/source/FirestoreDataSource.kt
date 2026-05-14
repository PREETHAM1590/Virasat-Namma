package com.example.virasat.data.source

import com.example.virasat.data.model.CheckIn
import com.example.virasat.data.model.Fact
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.UnlockedFact
import com.example.virasat.data.service.FirebaseAuthService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class FirestoreDataSource {

    private val db = FirebaseFirestore.getInstance()

    // Global collections (public data)
    private val sitesCollection = db.collection("sites")

    // User-scoped collections — always access via uid
    private fun uid(): String? = FirebaseAuthService.uid

    private fun userCheckIns() = uid()?.let {
        db.collection("users").document(it).collection("checkins")
    }

    private fun userUnlockedFacts() = uid()?.let {
        db.collection("users").document(it).collection("unlockedFacts")
    }

    private fun userBookmarks() = uid()?.let {
        db.collection("users").document(it).collection("bookmarks")
    }

    // ── Sites (global, read-only for clients) ─────────────────────────────

    suspend fun seedSites(sites: List<HeritageSite>) {
        for (site in sites) {
            sitesCollection.document(site.id).set(site.toMap(), SetOptions.merge()).await()
        }
    }

    fun observeAllSites(): Flow<List<HeritageSite>> = callbackFlow {
        val listener = sitesCollection
            .addSnapshotListener { snapshot, error ->
                // #23: emit empty list on error rather than closing the flow — keeps collectors alive
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val sites = snapshot?.documents?.mapNotNull { it.toHeritageSite() } ?: emptyList()
                trySend(sites)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getAllSitesList(): List<HeritageSite> {
        val bookmarkedIds = getBookmarkedSiteIds()
        return sitesCollection.get().await().documents.mapNotNull { doc ->
            doc.toHeritageSite()?.copy(isFavourite = doc.id in bookmarkedIds)
        }
    }

    suspend fun getSiteById(id: String): HeritageSite? {
        val bookmarkedIds = getBookmarkedSiteIds()
        return sitesCollection.document(id).get().await().toHeritageSite()
            ?.copy(isFavourite = id in bookmarkedIds)
    }

    fun observeSitesByType(type: String): Flow<List<HeritageSite>> = callbackFlow {
        val listener = sitesCollection
            .whereEqualTo("type", type)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val sites = snapshot?.documents?.mapNotNull { it.toHeritageSite() } ?: emptyList()
                trySend(sites)
            }
        awaitClose { listener.remove() }
    }

    suspend fun searchSites(query: String): List<HeritageSite> {
        val q = query.lowercase()
        return getAllSitesList().filter {
            it.name.lowercase().contains(q) ||
            it.nameLocal.lowercase().contains(q) ||
            it.location.lowercase().contains(q) ||
            it.district.lowercase().contains(q)
        }
    }

    // ── Bookmarks (user-scoped) ────────────────────────────────────────────

    private suspend fun getBookmarkedSiteIds(): Set<String> {
        val col = userBookmarks() ?: return emptySet()
        return try {
            col.get().await().documents.map { it.id }.toSet()
        } catch (_: Exception) {
            emptySet()
        }
    }

    suspend fun toggleBookmark(siteId: String): Boolean {
        val col = userBookmarks() ?: return false
        val doc = col.document(siteId)
        return try {
            val exists = doc.get().await().exists()
            if (exists) {
                doc.delete().await()
                false
            } else {
                doc.set(mapOf("siteId" to siteId, "savedAt" to System.currentTimeMillis())).await()
                true
            }
        } catch (_: Exception) {
            false
        }
    }

    suspend fun isBookmarked(siteId: String): Boolean {
        val col = userBookmarks() ?: return false
        return try {
            col.document(siteId).get().await().exists()
        } catch (_: Exception) {
            false
        }
    }

    fun observeBookmarks(): Flow<List<String>> {
        val col = userBookmarks() ?: return flowOf(emptyList())
        return callbackFlow {
            val listener = col.addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                trySend(snapshot?.documents?.map { it.id } ?: emptyList())
            }
            awaitClose { listener.remove() }
        }
    }

    // ── Check-ins (user-scoped) ────────────────────────────────────────────

    fun observeCheckIns(): Flow<List<CheckIn>> {
        val col = userCheckIns() ?: return flowOf(emptyList())
        return callbackFlow {
            val listener = col
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                    val checkIns = snapshot?.documents?.mapNotNull { doc ->
                        CheckIn(
                            id = doc.id,
                            siteId = doc.getString("siteId") ?: "",
                            siteName = doc.getString("siteName") ?: "",
                            siteLocation = doc.getString("siteLocation") ?: "",
                            timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
                            qrCodeId = doc.getString("qrCodeId") ?: "",
                            stampIcon = doc.getString("stampIcon") ?: "default"
                        )
                    } ?: emptyList()
                    trySend(checkIns)
                }
            awaitClose { listener.remove() }
        }
    }

    fun observeCheckInCount(): Flow<Int> {
        val col = userCheckIns() ?: return flowOf(0)
        return callbackFlow {
            val listener = col.addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(0); return@addSnapshotListener }
                trySend(snapshot?.size() ?: 0)
            }
            awaitClose { listener.remove() }
        }
    }

    fun observeUniqueSiteCount(): Flow<Int> {
        val col = userCheckIns() ?: return flowOf(0)
        return callbackFlow {
            val listener = col.addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(0); return@addSnapshotListener }
                val unique = snapshot?.documents?.map { it.getString("siteId") }?.toSet()?.size ?: 0
                trySend(unique)
            }
            awaitClose { listener.remove() }
        }
    }

    suspend fun hasCheckedIn(siteId: String): Boolean {
        val col = userCheckIns() ?: return false
        return try {
            !col.whereEqualTo("siteId", siteId).get().await().isEmpty
        } catch (_: Exception) {
            false
        }
    }

    suspend fun checkIn(site: HeritageSite) {
        val col = userCheckIns() ?: return
        val data = hashMapOf(
            "siteId" to site.id,
            "siteName" to site.name,
            "siteLocation" to site.location,
            "timestamp" to System.currentTimeMillis(),
            "qrCodeId" to site.qrCodeId,
            "stampIcon" to site.type.name.lowercase()
        )
        col.add(data).await()
        // Update user's checkInCount in profile
        uid()?.let { uid ->
            db.collection("users").document(uid)
                .update("checkInCount", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
        }
    }

    // ── Unlocked Facts (user-scoped) ───────────────────────────────────────

    fun observeUnlockedFacts(): Flow<List<UnlockedFact>> {
        val col = userUnlockedFacts() ?: return flowOf(emptyList())
        return callbackFlow {
            val listener = col
                .orderBy("unlockedAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                    val facts = snapshot?.documents?.mapNotNull { doc ->
                        UnlockedFact(
                            factId = doc.getString("factId") ?: "",
                            siteId = doc.getString("siteId") ?: "",
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            unlockedAt = doc.getLong("unlockedAt") ?: System.currentTimeMillis()
                        )
                    } ?: emptyList()
                    trySend(facts)
                }
            awaitClose { listener.remove() }
        }
    }

    fun observeUnlockedFactsForSite(siteId: String): Flow<List<UnlockedFact>> {
        val col = userUnlockedFacts() ?: return flowOf(emptyList())
        return callbackFlow {
            val listener = col
                .whereEqualTo("siteId", siteId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                    val facts = snapshot?.documents?.mapNotNull { doc ->
                        UnlockedFact(
                            factId = doc.getString("factId") ?: "",
                            siteId = doc.getString("siteId") ?: "",
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            unlockedAt = doc.getLong("unlockedAt") ?: System.currentTimeMillis()
                        )
                    } ?: emptyList()
                    trySend(facts)
                }
            awaitClose { listener.remove() }
        }
    }

    fun observeUnlockedFactCount(): Flow<Int> {
        val col = userUnlockedFacts() ?: return flowOf(0)
        return callbackFlow {
            val listener = col.addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(0); return@addSnapshotListener }
                trySend(snapshot?.size() ?: 0)
            }
            awaitClose { listener.remove() }
        }
    }

    suspend fun unlockFact(siteId: String, fact: Fact) {
        val col = userUnlockedFacts() ?: return
        val data = hashMapOf(
            "factId" to fact.id,
            "siteId" to siteId,
            "title" to fact.title,
            "description" to fact.description,
            "unlockedAt" to System.currentTimeMillis()
        )
        col.add(data).await()
    }

    suspend fun isFactUnlocked(factId: String): Boolean {
        val col = userUnlockedFacts() ?: return false
        return try {
            !col.whereEqualTo("factId", factId).get().await().isEmpty
        } catch (_: Exception) {
            false
        }
    }
}
