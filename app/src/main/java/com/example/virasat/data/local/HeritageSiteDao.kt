package com.example.virasat.data.local

import androidx.room.*
import com.example.virasat.data.model.HeritageSiteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeritageSiteDao {

    @Query("SELECT * FROM heritage_sites ORDER BY name ASC")
    fun getAllSites(): Flow<List<HeritageSiteEntity>>

    @Query("SELECT * FROM heritage_sites ORDER BY name ASC")
    suspend fun getAllSitesList(): List<HeritageSiteEntity>

    @Query("SELECT * FROM heritage_sites WHERE id = :id LIMIT 1")
    suspend fun getSiteById(id: String): HeritageSiteEntity?

    @Query("SELECT * FROM heritage_sites WHERE qrCodeId = :qrCodeId LIMIT 1")
    suspend fun getSiteByQrCode(qrCodeId: String): HeritageSiteEntity?

    @Query("SELECT * FROM heritage_sites WHERE type = :type ORDER BY name ASC")
    fun getSitesByType(type: String): Flow<List<HeritageSiteEntity>>

    @Query("""
        SELECT * FROM heritage_sites 
        WHERE name LIKE '%' || :query || '%' 
           OR nameLocal LIKE '%' || :query || '%'
           OR location LIKE '%' || :query || '%'
           OR district LIKE '%' || :query || '%'
        ORDER BY name ASC
    """)
    fun searchSites(query: String): Flow<List<HeritageSiteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSites(sites: List<HeritageSiteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSite(site: HeritageSiteEntity)

    @Query("SELECT COUNT(*) FROM heritage_sites")
    suspend fun getSiteCount(): Int

    @Query("DELETE FROM heritage_sites")
    suspend fun clearAllSites()
}
