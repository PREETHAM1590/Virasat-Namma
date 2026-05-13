package com.example.virasat

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.virasat.data.local.BookmarkEntity
import com.example.virasat.data.local.VirasatDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertFalse

// Feature: virasat-heritage-app, Property 3: Bookmark toggle invariant
class BookmarkTogglePropertyTest {

    private lateinit var db: VirasatDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, VirasatDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun toggleBookmarkEvenTimesReturnsToUnbookmarked() = runTest {
        val dao = db.bookmarkDao()
        val testCases = listOf(
            "site_alpha" to 2,
            "site_beta" to 4,
            "site_gamma" to 6,
            "site_delta" to 8,
            "site_epsilon" to 10
        )
        for ((siteId, evenN) in testCases) {
            repeat(evenN) {
                if (dao.exists(siteId)) dao.delete(siteId)
                else dao.insert(BookmarkEntity(siteId))
            }
            assertFalse("After $evenN toggles, $siteId should not be bookmarked", dao.exists(siteId))
        }
    }
}
