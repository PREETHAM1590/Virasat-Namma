package com.example.virasat.data.local

import androidx.room.*
import com.example.virasat.data.model.CheckIn
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Query("SELECT * FROM check_ins ORDER BY timestamp DESC")
    fun getAllCheckIns(): Flow<List<CheckIn>>

    @Query("SELECT * FROM check_ins WHERE siteId = :siteId LIMIT 1")
    suspend fun getCheckInForSite(siteId: String): CheckIn?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckIn)

    @Query("SELECT COUNT(*) FROM check_ins")
    fun getCheckInCount(): Flow<Int>

    @Query("SELECT COUNT(DISTINCT siteId) FROM check_ins")
    fun getUniqueSiteCount(): Flow<Int>

    @Delete
    suspend fun deleteCheckIn(checkIn: CheckIn)

    @Query("DELETE FROM check_ins")
    suspend fun clearAll()
}
