package com.example.virasat.data.repository

import android.content.Context
import com.example.virasat.data.local.CheckInDao
import com.example.virasat.data.local.UnlockedFactDao
import com.example.virasat.data.local.VirasatDatabase
import com.example.virasat.data.model.*
import com.example.virasat.data.source.KarnatakaSites
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HeritageRepository(context: Context) {
    private val database = VirasatDatabase.getDatabase(context)
    private val checkInDao = database.checkInDao()
    private val unlockedFactDao = database.unlockedFactDao()

    // --- Sites (in-memory from JSON/local) ---
    fun getAllSites(): Flow<List<HeritageSite>> = flow {
        emit(KarnatakaSites.allSites)
    }

    fun getSiteById(id: String): HeritageSite? {
        return KarnatakaSites.allSites.find { it.id == id }
    }

    fun getSitesByType(type: SiteType): Flow<List<HeritageSite>> = flow {
        emit(KarnatakaSites.allSites.filter { it.type == type })
    }

    fun getSitesByDistrict(district: String): Flow<List<HeritageSite>> = flow {
        emit(KarnatakaSites.allSites.filter { it.district == district })
    }

    fun searchSites(query: String): Flow<List<HeritageSite>> = flow {
        val q = query.lowercase()
        emit(KarnatakaSites.allSites.filter {
            it.name.lowercase().contains(q) ||
            it.nameLocal.lowercase().contains(q) ||
            it.location.lowercase().contains(q) ||
            it.district.lowercase().contains(q)
        })
    }

    fun getNearbySites(lat: Double, lng: Double, radiusKm: Double = 50.0): Flow<List<HeritageSite>> = flow {
        emit(KarnatakaSites.allSites.filter { site ->
            val dLat = Math.toRadians(site.latitude - lat)
            val dLng = Math.toRadians(site.longitude - lng)
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(site.latitude)) *
                    Math.sin(dLng / 2) * Math.sin(dLng / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            val distance = 6371 * c // Earth's radius in km
            distance <= radiusKm
        })
    }

    fun getFavouriteSites(): Flow<List<HeritageSite>> = flow {
        emit(KarnatakaSites.allSites.filter { it.isFavourite })
    }

    // --- Check-ins (Room) ---
    fun getAllCheckIns(): Flow<List<CheckIn>> = checkInDao.getAllCheckIns()
    fun getCheckInCount(): Flow<Int> = checkInDao.getCheckInCount()
    fun getUniqueSiteCount(): Flow<Int> = checkInDao.getUniqueSiteCount()

    suspend fun hasCheckedIn(siteId: String): Boolean {
        return checkInDao.getCheckInForSite(siteId) != null
    }

    suspend fun checkIn(site: HeritageSite) {
        val checkIn = CheckIn(
            siteId = site.id,
            siteName = site.name,
            siteLocation = site.location,
            qrCodeId = site.qrCodeId,
            stampIcon = site.type.name.lowercase()
        )
        checkInDao.insertCheckIn(checkIn)
    }

    // --- Unlocked Facts (Room) ---
    fun getAllUnlockedFacts(): Flow<List<UnlockedFact>> = unlockedFactDao.getAllUnlockedFacts()
    fun getUnlockedFactsForSite(siteId: String): Flow<List<UnlockedFact>> =
        unlockedFactDao.getUnlockedFactsForSite(siteId)
    fun getUnlockedFactCount(): Flow<Int> = unlockedFactDao.getUnlockedFactCount()

    suspend fun unlockFact(siteId: String, fact: Fact) {
        val unlocked = UnlockedFact(
            factId = fact.id,
            siteId = siteId,
            title = fact.title,
            description = fact.description
        )
        unlockedFactDao.insertUnlockedFact(unlocked)
    }

    suspend fun isFactUnlocked(factId: String): Boolean {
        return unlockedFactDao.isFactUnlocked(factId)
    }
}
