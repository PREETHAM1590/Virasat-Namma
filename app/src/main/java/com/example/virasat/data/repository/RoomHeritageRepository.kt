package com.example.virasat.data.repository

import android.content.Context
import com.example.virasat.data.local.VirasatDatabase
import com.example.virasat.data.model.*
import com.example.virasat.data.source.KarnatakaSites
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.room.withTransaction

class RoomHeritageRepository(context: Context) : HeritageRepository {
    private val database = VirasatDatabase.getDatabase(context)
    private val siteDao = database.heritageSiteDao()
    private val checkInDao = database.checkInDao()
    private val unlockedFactDao = database.unlockedFactDao()

    // Scoped to the lifetime of the singleton repository (app lifetime).
    private val repoScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // Reseed only when site count differs from bundled data (avoids clear+insert every launch).
        repoScope.launch {
            val expectedCount = KarnatakaSites.allSites.size
            val currentCount = siteDao.getSiteCount()
            if (currentCount != expectedCount) {
                // Atomic: no window where DB is empty.
                database.withTransaction {
                    siteDao.clearAllSites()
                    siteDao.insertSites(KarnatakaSites.allSites.map { it.toEntity() })
                }
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
            stampIcon = site.type.name.lowercase(),
            imageUrl = site.imageUrl
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

    private val bookmarkDao = database.bookmarkDao()

    // @Transaction prevents TOCTOU race between exists-check and insert/delete
    @androidx.room.Transaction
    override suspend fun toggleBookmark(siteId: String): Boolean {
        return if (bookmarkDao.exists(siteId)) {
            bookmarkDao.delete(siteId)
            false
        } else {
            bookmarkDao.insert(com.example.virasat.data.local.BookmarkEntity(siteId))
            true
        }
    }

    override suspend fun isBookmarked(siteId: String): Boolean =
        bookmarkDao.exists(siteId)

    override fun observeBookmarks(): kotlinx.coroutines.flow.Flow<List<String>> =
        bookmarkDao.observeAll()

    override suspend fun clearLocalUserData() {
        // Clear all user-specific local data so next user starts fresh (#12)
        database.withTransaction {
            checkInDao.clearAll()
            unlockedFactDao.clearAllUnlockedFacts()
            bookmarkDao.clearAll()
        }
    }
}
