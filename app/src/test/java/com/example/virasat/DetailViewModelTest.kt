package com.example.virasat

import android.app.Application
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.model.CheckIn
import com.example.virasat.data.model.Fact
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.SiteType
import com.example.virasat.data.model.UnlockedFact
import com.example.virasat.data.repository.HeritageRepository
import com.example.virasat.viewmodel.DetailViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private lateinit var mockRepository: HeritageRepository
    private lateinit var mockApplication: Application
    private val testDispatcher = StandardTestDispatcher()

    private val testSite = HeritageSite(
        id = "site1",
        name = "Test Temple",
        nameLocal = "ಪರೀಕ್ಷಾ ದೇವಾಲಯ",
        location = "Bangalore",
        district = "Bangalore Urban",
        type = SiteType.TEMPLE,
        description = "A test temple",
        shortDescription = "Test",
        history = "Ancient history",
        architecture = "Dravidian",
        legends = "Many legends",
        imageUrl = "https://example.com/image.jpg",
        galleryImages = listOf(),
        facts = listOf(
            Fact("fact1", "Fact 1", "Description 1", false),
            Fact("fact2", "Fact 2", "Description 2", false)
        ),
        latitude = 12.9716,
        longitude = 77.5946,
        visitingHours = "6 AM - 8 PM",
        entryFee = "Free",
        qrCodeId = "QR-TEST-001",
        audioGuideUrl = null,
        isFavourite = false,
        rating = 4.5f,
        reviews = 100
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        mockApplication = mockk(relaxed = true)
        mockRepository = mockk(relaxed = true)
        
        // Mock RepositoryProvider
        mockkObject(RepositoryProvider)
        every { RepositoryProvider.getRepository(any()) } returns mockRepository
        
        // Setup default repository responses
        coEvery { mockRepository.getAllCheckIns() } returns flowOf(emptyList())
        coEvery { mockRepository.getAllUnlockedFacts() } returns flowOf(emptyList())
        
        viewModel = DetailViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `loadSite should load site details and update state`() = runTest {
        // Given
        coEvery { mockRepository.getSiteById("site1") } returns testSite
        coEvery { mockRepository.hasCheckedIn("site1") } returns false
        coEvery { mockRepository.isBookmarked("site1") } returns false
        coEvery { mockRepository.isFactUnlocked(any()) } returns false

        // When
        viewModel.loadSite("site1")
        advanceUntilIdle()

        // Then
        assertEquals(testSite, viewModel.site.value)
        assertFalse(viewModel.hasCheckedIn.value)
        assertFalse(viewModel.isBookmarked.value)
        coVerify { mockRepository.getSiteById("site1") }
        coVerify { mockRepository.hasCheckedIn("site1") }
        coVerify { mockRepository.isBookmarked("site1") }
    }

    @Test
    fun `loadSite should load unlocked facts`() = runTest {
        // Given
        coEvery { mockRepository.getSiteById("site1") } returns testSite
        coEvery { mockRepository.hasCheckedIn("site1") } returns false
        coEvery { mockRepository.isBookmarked("site1") } returns false
        coEvery { mockRepository.isFactUnlocked("fact1") } returns true
        coEvery { mockRepository.isFactUnlocked("fact2") } returns false

        // When
        viewModel.loadSite("site1")
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.unlockedFacts.value.contains("fact1"))
        assertFalse(viewModel.unlockedFacts.value.contains("fact2"))
    }

    @Test
    fun `checkIn should update hasCheckedIn state`() = runTest {
        // Given
        coEvery { mockRepository.getSiteById("site1") } returns testSite
        coEvery { mockRepository.hasCheckedIn("site1") } returns false
        coEvery { mockRepository.isBookmarked("site1") } returns false
        coEvery { mockRepository.isFactUnlocked(any()) } returns false
        coEvery { mockRepository.checkIn(any()) } just Runs

        viewModel.loadSite("site1")
        advanceUntilIdle()

        // When
        viewModel.checkIn()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.hasCheckedIn.value)
        coVerify { mockRepository.checkIn(testSite) }
    }

    @Test
    fun `unlockFact should update unlockedFacts state`() = runTest {
        // Given
        coEvery { mockRepository.getSiteById("site1") } returns testSite
        coEvery { mockRepository.hasCheckedIn("site1") } returns false
        coEvery { mockRepository.isBookmarked("site1") } returns false
        coEvery { mockRepository.isFactUnlocked(any()) } returns false
        coEvery { mockRepository.unlockFact(any(), any()) } just Runs

        viewModel.loadSite("site1")
        advanceUntilIdle()

        // When
        viewModel.unlockFact("fact1")
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isFactUnlocked("fact1"))
        assertTrue(viewModel.unlockedFacts.value.contains("fact1"))
        coVerify { mockRepository.unlockFact("site1", testSite.facts[0]) }
    }

    @Test
    fun `toggleBookmark should update isBookmarked state`() = runTest {
        // Given
        coEvery { mockRepository.getSiteById("site1") } returns testSite
        coEvery { mockRepository.hasCheckedIn("site1") } returns false
        coEvery { mockRepository.isBookmarked("site1") } returns false
        coEvery { mockRepository.isFactUnlocked(any()) } returns false
        coEvery { mockRepository.toggleBookmark("site1") } returns true

        viewModel.loadSite("site1")
        advanceUntilIdle()

        // When
        viewModel.toggleBookmark()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isBookmarked.value)
        coVerify { mockRepository.toggleBookmark("site1") }
    }

    @Test
    fun `toggleBookmark should toggle from true to false`() = runTest {
        // Given
        coEvery { mockRepository.getSiteById("site1") } returns testSite
        coEvery { mockRepository.hasCheckedIn("site1") } returns false
        coEvery { mockRepository.isBookmarked("site1") } returns true
        coEvery { mockRepository.isFactUnlocked(any()) } returns false
        coEvery { mockRepository.toggleBookmark("site1") } returns false

        viewModel.loadSite("site1")
        advanceUntilIdle()

        // When
        viewModel.toggleBookmark()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.isBookmarked.value)
        coVerify { mockRepository.toggleBookmark("site1") }
    }

    @Test
    fun `isFactUnlocked should return correct state`() = runTest {
        // Given
        coEvery { mockRepository.getSiteById("site1") } returns testSite
        coEvery { mockRepository.hasCheckedIn("site1") } returns false
        coEvery { mockRepository.isBookmarked("site1") } returns false
        coEvery { mockRepository.isFactUnlocked("fact1") } returns true
        coEvery { mockRepository.isFactUnlocked("fact2") } returns false

        viewModel.loadSite("site1")
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isFactUnlocked("fact1"))
        assertFalse(viewModel.isFactUnlocked("fact2"))
    }
}
