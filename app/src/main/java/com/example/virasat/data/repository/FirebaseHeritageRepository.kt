package com.example.virasat.data.repository

import android.content.Context
import com.example.virasat.data.local.VirasatDatabase
import com.example.virasat.data.model.*
import com.example.virasat.data.source.FirestoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import com.example.virasat.data.source.KarnatakaSites

class FirebaseHeritageRepository(context: Context) : HeritageRepository {

    private val firestore = FirestoreDataSource()
    private val db = VirasatDatabase.getDatabase(context)
    private val checkInDao = db.checkInDao()

    override fun getAllSites(): Flow<List<HeritageSite>> =
        firestore.observeAllSites().map { it.ifEmpty { KarnatakaSites.allSites } }

    override suspend fun getAllSitesList(): List<HeritageSite> =
        firestore.getAllSitesList().ifEmpty { KarnatakaSites.allSites }

    override suspend fun getSiteById(id: String): HeritageSite? =
        firestore.getSiteById(id) ?: KarnatakaSites.allSites.find { it.id == id }

    override fun getSitesByType(type: String): Flow<List<HeritageSite>> =
        firestore.observeSitesByType(type)

    override fun searchSites(query: String): Flow<List<HeritageSite>> = flow {
        val results = firestore.searchSites(query)
        emit(
            results.ifEmpty {
                KarnatakaSites.allSites.filter {
                    it.name.contains(query, ignoreCase = true) ||
                        it.nameLocal.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
                }
            }
        )
    }

    override fun getAllCheckIns(): Flow<List<CheckIn>> =
        firestore.observeCheckIns()

    override fun getCheckInCount(): Flow<Int> =
        firestore.observeCheckInCount()

    override fun getUniqueSiteCount(): Flow<Int> =
        firestore.observeUniqueSiteCount()

    override suspend fun hasCheckedIn(siteId: String): Boolean =
        checkInDao.getCheckInForSite(siteId) != null || firestore.hasCheckedIn(siteId)

    override suspend fun checkIn(site: HeritageSite) {
        // Write to Firebase
        firestore.checkIn(site)
        // Also persist locally in Room
        val checkIn = CheckIn(
            id = java.util.UUID.randomUUID().toString(),
            siteId = site.id,
            siteName = site.name,
            siteLocation = site.location,
            qrCodeId = site.qrCodeId,
            stampIcon = site.type.name.lowercase()
        )
        checkInDao.insertCheckIn(checkIn)
    }

    override fun getAllUnlockedFacts(): Flow<List<UnlockedFact>> =
        firestore.observeUnlockedFacts()

    override fun getUnlockedFactsForSite(siteId: String): Flow<List<UnlockedFact>> =
        firestore.observeUnlockedFactsForSite(siteId)

    override fun getUnlockedFactCount(): Flow<Int> =
        firestore.observeUnlockedFactCount()

    override suspend fun unlockFact(siteId: String, fact: Fact) {
        firestore.unlockFact(siteId, fact)
    }

    override suspend fun isFactUnlocked(factId: String): Boolean =
        firestore.isFactUnlocked(factId)

    override suspend fun toggleBookmark(siteId: String): Boolean =
        firestore.toggleBookmark(siteId)

    override suspend fun isBookmarked(siteId: String): Boolean =
        firestore.isBookmarked(siteId)

    override fun observeBookmarks(): kotlinx.coroutines.flow.Flow<List<String>> =
        firestore.observeBookmarks()
}
