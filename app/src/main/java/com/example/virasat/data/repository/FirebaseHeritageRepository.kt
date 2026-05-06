package com.example.virasat.data.repository

import com.example.virasat.data.model.*
import com.example.virasat.data.source.KarnatakaSites
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseHeritageRepository : HeritageRepository {
    private val db = FirebaseFirestore.getInstance()
    private val sitesCollection = db.collection("sites")
    private val checkInsCollection = db.collection("checkins")
    private val unlockedFactsCollection = db.collection("unlockedFacts")

    private var sitesCache: List<HeritageSite> = emptyList()

    override fun getAllSites(): Flow<List<HeritageSite>> = callbackFlow {
        if (sitesCache.isNotEmpty()) { trySend(sitesCache) }
        val listener = sitesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            val sites = snapshot?.documents?.mapNotNull { doc ->
                try {
                    HeritageSite(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        nameLocal = doc.getString("nameLocal") ?: "",
                        location = doc.getString("location") ?: "",
                        district = doc.getString("district") ?: "",
                        type = SiteType.valueOf(doc.getString("type") ?: "MONUMENT"),
                        description = doc.getString("description") ?: "",
                        shortDescription = doc.getString("shortDescription") ?: "",
                        history = doc.getString("history") ?: "",
                        architecture = doc.getString("architecture") ?: "",
                        legends = doc.getString("legends") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: "",
                        galleryImages = (doc.get("galleryImages") as? List<String>) ?: emptyList(),
                        facts = emptyList(),
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        visitingHours = doc.getString("visitingHours") ?: "",
                        entryFee = doc.getString("entryFee") ?: "",
                        qrCodeId = doc.getString("qrCodeId") ?: "",
                        audioGuideUrl = doc.getString("audioGuideUrl"),
                        rating = (doc.getDouble("rating") ?: 4.5f).toFloat(),
                        reviews = (doc.getLong("reviews") ?: 0).toInt()
                    )
                } catch (_: Exception) { null }
            } ?: emptyList()
            sitesCache = sites
            trySend(sites)
        }
        awaitClose { listener.remove() }
    }

    override fun getAllSitesList(): List<HeritageSite> {
        return if (sitesCache.isNotEmpty()) sitesCache else KarnatakaSites.allSites
    }

    override fun getSiteById(id: String): HeritageSite? {
        return sitesCache.find { it.id == id } ?: KarnatakaSites.allSites.find { it.id == id }
    }

    override fun getSitesByType(type: String): Flow<List<HeritageSite>> = flow {
        emit(getAllSitesList().filter { it.type.name == type })
    }

    override fun searchSites(query: String): Flow<List<HeritageSite>> = flow {
        val q = query.lowercase()
        emit(getAllSitesList().filter {
            it.name.lowercase().contains(q) ||
            it.nameLocal.lowercase().contains(q) ||
            it.location.lowercase().contains(q)
        })
    }

    override fun getAllCheckIns(): Flow<List<CheckIn>> = callbackFlow {
        val listener = checkInsCollection.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
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

    override fun getCheckInCount(): Flow<Int> = callbackFlow {
        val listener = checkInsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snapshot?.size() ?: 0)
        }
        awaitClose { listener.remove() }
    }

    override fun getUniqueSiteCount(): Flow<Int> = callbackFlow {
        val listener = checkInsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            val unique = snapshot?.documents?.map { it.getString("siteId") }?.toSet()?.size ?: 0
            trySend(unique)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun hasCheckedIn(siteId: String): Boolean {
        val snapshot = checkInsCollection.whereEqualTo("siteId", siteId).get().await()
        return !snapshot.isEmpty
    }

    override suspend fun checkIn(site: HeritageSite) {
        val data = hashMapOf(
            "siteId" to site.id,
            "siteName" to site.name,
            "siteLocation" to site.location,
            "timestamp" to System.currentTimeMillis(),
            "qrCodeId" to site.qrCodeId,
            "stampIcon" to site.type.name.lowercase()
        )
        checkInsCollection.add(data).await()
    }

    override fun getAllUnlockedFacts(): Flow<List<UnlockedFact>> = callbackFlow {
        val listener = unlockedFactsCollection.orderBy("unlockedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
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

    override fun getUnlockedFactsForSite(siteId: String): Flow<List<UnlockedFact>> = callbackFlow {
        val listener = unlockedFactsCollection.whereEqualTo("siteId", siteId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
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

    override fun getUnlockedFactCount(): Flow<Int> = callbackFlow {
        val listener = unlockedFactsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snapshot?.size() ?: 0)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun unlockFact(siteId: String, fact: Fact) {
        val data = hashMapOf(
            "factId" to fact.id,
            "siteId" to siteId,
            "title" to fact.title,
            "description" to fact.description,
            "unlockedAt" to System.currentTimeMillis()
        )
        unlockedFactsCollection.add(data).await()
    }

    override suspend fun isFactUnlocked(factId: String): Boolean {
        val snapshot = unlockedFactsCollection.whereEqualTo("factId", factId).get().await()
        return !snapshot.isEmpty
    }
}
