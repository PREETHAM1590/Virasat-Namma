package com.example.virasat.data.repository

import com.example.virasat.data.model.*
import com.example.virasat.data.source.FirestoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import com.example.virasat.data.source.KarnatakaSites

class FirebaseHeritageRepository : HeritageRepository {

    private val firestore = FirestoreDataSource()

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
        firestore.hasCheckedIn(siteId)

    override suspend fun checkIn(site: HeritageSite) {
        firestore.checkIn(site)
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
