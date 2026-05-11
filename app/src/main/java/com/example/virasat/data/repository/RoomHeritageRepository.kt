package com.example.virasat.data.repository

import android.content.Context
import com.example.virasat.data.local.VirasatDatabase
import com.example.virasat.data.model.*
import com.example.virasat.data.source.KarnatakaSites
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RoomHeritageRepository(context: Context) : HeritageRepository {
    private val database = VirasatDatabase.getDatabase(context)
    private val siteDao = database.heritageSiteDao()
    private val checkInDao = database.checkInDao()
    private val unlockedFactDao = database.unlockedFactDao()

    init {
        // Seed the database on first launch
        CoroutineScope(Dispatchers.IO).launch {
            if (siteDao.getSiteCount() == 0) {
                siteDao.insertSites(KarnatakaSites.allSites.map { it.toEntity() })
            }
        }
    }

    override fun getAllSites(): Flow<List<HeritageSite>> =
        siteDao.getAllSites().map { list -> list.map { it.toModel() } }

    override suspend fun getAllSitesList(): List<HeritageSite> =
        siteDao.getAllSitesList().map { it.toModel() }

    override suspend fun getSiteById(id: String): HeritageSite? {
        return siteDao.getSiteById(id)?.toModel()
    }

    override fun getSitesByType(type: String): Flow<List<HeritageSite>> =
        siteDao.getSitesByType(type).map { list -> list.map { it.toModel() } }

    override fun searchSites(query: String): Flow<List<HeritageSite>> =
        siteDao.searchSites(query.lowercase()).map { list -> list.map { it.toModel() } }

    override fun getAllCheckIns(): Flow<List<CheckIn>> = checkInDao.getAllCheckIns()

    override fun getCheckInCount(): Flow<Int> = checkInDao.getCheckInCount()

    override fun getUniqueSiteCount(): Flow<Int> = checkInDao.getUniqueSiteCount()

    override suspend fun hasCheckedIn(siteId: String): Boolean {
        return checkInDao.getCheckInForSite(siteId) != null
    }

    override suspend fun checkIn(site: HeritageSite) {
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

    override fun getAllUnlockedFacts(): Flow<List<UnlockedFact>> = unlockedFactDao.getAllUnlockedFacts()

    override fun getUnlockedFactsForSite(siteId: String): Flow<List<UnlockedFact>> =
        unlockedFactDao.getUnlockedFactsForSite(siteId)

    override fun getUnlockedFactCount(): Flow<Int> = unlockedFactDao.getUnlockedFactCount()

    override suspend fun unlockFact(siteId: String, fact: Fact) {
        val unlocked = UnlockedFact(
            factId = fact.id,
            siteId = siteId,
            title = fact.title,
            description = fact.description
        )
        unlockedFactDao.insertUnlockedFact(unlocked)
    }

    override suspend fun isFactUnlocked(factId: String): Boolean {
        return unlockedFactDao.isFactUnlocked(factId)
    }

    // Bookmark stubs - Room fallback has no bookmark support
    override suspend fun toggleBookmark(siteId: String): Boolean = false
    override suspend fun isBookmarked(siteId: String): Boolean = false
    override fun observeBookmarks(): kotlinx.coroutines.flow.Flow<List<String>> =
        kotlinx.coroutines.flow.flowOf(emptyList())
}
