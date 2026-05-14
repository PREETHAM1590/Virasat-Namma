package com.example.virasat.data.repository

import com.example.virasat.data.model.CheckIn
import com.example.virasat.data.model.Fact
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.UnlockedFact
import kotlinx.coroutines.flow.Flow

interface HeritageRepository {
    fun getAllSites(): Flow<List<HeritageSite>>
    suspend fun getAllSitesList(): List<HeritageSite>
    suspend fun getSiteById(id: String): HeritageSite?
    suspend fun getSiteByQrCode(qrCodeId: String): HeritageSite?
    fun getSitesByType(type: String): Flow<List<HeritageSite>>
    fun searchSites(query: String): Flow<List<HeritageSite>>
    fun getAllCheckIns(): Flow<List<CheckIn>>
    fun getCheckInCount(): Flow<Int>
    fun getUniqueSiteCount(): Flow<Int>
    suspend fun hasCheckedIn(siteId: String): Boolean
    suspend fun checkIn(site: HeritageSite)
    fun getAllUnlockedFacts(): Flow<List<UnlockedFact>>
    fun getUnlockedFactsForSite(siteId: String): Flow<List<UnlockedFact>>
    fun getUnlockedFactCount(): Flow<Int>
    suspend fun unlockFact(siteId: String, fact: Fact)
    suspend fun isFactUnlocked(factId: String): Boolean
    suspend fun toggleBookmark(siteId: String): Boolean
    suspend fun isBookmarked(siteId: String): Boolean
    fun observeBookmarks(): kotlinx.coroutines.flow.Flow<List<String>>
    /** Clear all local user data on logout so next user does not inherit prior data (#12) */
    suspend fun clearLocalUserData()
}
