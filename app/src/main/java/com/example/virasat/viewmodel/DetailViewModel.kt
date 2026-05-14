package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.di.RepositoryProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RepositoryProvider.getRepository(application)

    private val _site = MutableStateFlow<HeritageSite?>(null)
    val site = _site.asStateFlow()

    private val _hasCheckedIn = MutableStateFlow(false)
    val hasCheckedIn = _hasCheckedIn.asStateFlow()

    private val _unlockedFacts = MutableStateFlow<List<String>>(emptyList())
    val unlockedFacts = _unlockedFacts.asStateFlow()

    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked = _isBookmarked.asStateFlow()

    val checkIns = repository.getAllCheckIns()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allUnlockedFacts = repository.getAllUnlockedFacts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadSite(siteId: String) {
        viewModelScope.launch {
            _site.value = repository.getSiteById(siteId)
            _hasCheckedIn.value = repository.hasCheckedIn(siteId)
            _isBookmarked.value = repository.isBookmarked(siteId)
            _site.value?.facts?.forEach { fact ->
                if (repository.isFactUnlocked(fact.id)) {
                    _unlockedFacts.value = _unlockedFacts.value + fact.id
                }
            }
        }
    }

    fun checkIn() {
        viewModelScope.launch {
            site.value?.let { s ->
                repository.checkIn(s)
                _hasCheckedIn.value = true
            }
        }
    }

    fun unlockFact(factId: String) {
        viewModelScope.launch {
            site.value?.let { s ->
                val fact = s.facts.find { it.id == factId }
                fact?.let {
                    repository.unlockFact(s.id, it)
                    _unlockedFacts.value = _unlockedFacts.value + factId
                }
            }
        }
    }

    fun isFactUnlocked(factId: String): Boolean {
        return _unlockedFacts.value.contains(factId)
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            site.value?.let { s ->
                val newState = repository.toggleBookmark(s.id)
                _isBookmarked.value = newState
            }
        }
    }
}
