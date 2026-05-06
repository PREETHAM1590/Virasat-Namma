package com.example.virasat.data.repository

import android.content.Context
import com.example.virasat.data.local.VirasatDatabase
import com.example.virasat.data.model.*
import com.example.virasat.data.source.KarnatakaSites
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomHeritageRepository(context: Context) : HeritageRepository {
    private val database = VirasatDatabase.getDatabase(context)
    private val checkInDao = database.checkInDao()
    private val unlockedFactDao = database.unlockedFactDao()

    override fun getAllSites(): Flow<List<HeritageSite>> = flow {
        emit(KarnatakaSites.allSites)
    }

    override fun getAllSitesList(): List<HeritageSite> = KarnatakaSites.allSites

    override fun getSiteById(id: String): HeritageSite? {
        return KarnatakaSites.allSites.find { it.id == id }
    }

    override fun getSitesByType(type: String): Flow<List<HeritageSite>> = flow {
        emit(KarnatakaSites.allSites.filter { it.type.name == type })
    }

    override fun searchSites(query: String): Flow<List<HeritageSite>> = flow {
        val q = query.lowercase()
        emit(KarnatakaSites.allSites.filter {
            it.name.lowercase().contains(q) ||
            it.nameLocal.lowercase().contains(q) ||
            it.location.lowercase().contains(q) ||
            it.district.lowercase().contains(q)
        })
    }

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
}
