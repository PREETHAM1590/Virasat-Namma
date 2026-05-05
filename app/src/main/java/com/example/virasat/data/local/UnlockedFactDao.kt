package com.example.virasat.data.local

import androidx.room.*
import com.example.virasat.data.model.UnlockedFact
import kotlinx.coroutines.flow.Flow

@Dao
interface UnlockedFactDao {
    @Query("SELECT * FROM unlocked_facts ORDER BY unlockedAt DESC")
    fun getAllUnlockedFacts(): Flow<List<UnlockedFact>>

    @Query("SELECT * FROM unlocked_facts WHERE siteId = :siteId")
    fun getUnlockedFactsForSite(siteId: String): Flow<List<UnlockedFact>>

    @Query("SELECT COUNT(*) FROM unlocked_facts")
    fun getUnlockedFactCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnlockedFact(fact: UnlockedFact)

    @Query("SELECT EXISTS(SELECT 1 FROM unlocked_facts WHERE factId = :factId)")
    suspend fun isFactUnlocked(factId: String): Boolean
}
