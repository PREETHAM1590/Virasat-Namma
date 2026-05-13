package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.Itinerary
import com.example.virasat.data.repository.ItineraryRepository
import com.example.virasat.domain.ItineraryPlanner
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ItineraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RepositoryProvider.getRepository(application)
    private val itineraryRepo = ItineraryRepository()

    val savedItineraries: StateFlow<List<Itinerary>> =
        itineraryRepo.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _currentSites = MutableStateFlow<List<HeritageSite>>(emptyList())
    val orderedSites: StateFlow<List<HeritageSite>> = _currentSites.asStateFlow()

    fun addSite(site: HeritageSite) {
        _currentSites.value = ItineraryPlanner.nearestNeighbour(_currentSites.value + site)
    }

    fun removeSite(siteId: String) {
        _currentSites.value = ItineraryPlanner.nearestNeighbour(
            _currentSites.value.filter { it.id != siteId }
        )
    }

    fun saveItinerary(name: String) {
        require(name.length in 1..100) { "Name must be 1-100 characters" }
        viewModelScope.launch {
            val itinerary = Itinerary(
                name = name,
                siteIds = _currentSites.value.map { it.id }
            )
            itineraryRepo.save(itinerary)
        }
    }

    fun exportDeepLink(itineraryId: String): String = "virasat://itinerary/$itineraryId"

    fun loadItinerary(id: String) {
        viewModelScope.launch {
            val itinerary = itineraryRepo.getById(id) ?: return@launch
            val sites = itinerary.siteIds.mapNotNull { repo.getSiteById(it) }
            _currentSites.value = sites
        }
    }
}
