package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.repository.HeritageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HeritageRepository(application)

    private val _site = MutableStateFlow<HeritageSite?>(null)
    val site = _site.asStateFlow()

    private val _hasCheckedIn = MutableStateFlow(false)
    val hasCheckedIn = _hasCheckedIn.asStateFlow()

    private val _unlockedFacts = MutableStateFlow<List<String>>(emptyList())
    val unlockedFacts = _unlockedFacts.asStateFlow()

    val checkIns = repository.getAllCheckIns()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allUnlockedFacts = repository.getAllUnlockedFacts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadSite(siteId: String) {
        _site.value = repository.getSiteById(siteId)
        viewModelScope.launch {
            _hasCheckedIn.value = repository.hasCheckedIn(siteId)
            site.value?.facts?.forEach { fact ->
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
}
