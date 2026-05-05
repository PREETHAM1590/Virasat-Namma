package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.model.SiteType
import com.example.virasat.data.repository.HeritageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HeritageRepository(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedType = MutableStateFlow<SiteType?>(null)
    val selectedType = _selectedType.asStateFlow()

    private val _selectedDistrict = MutableStateFlow<String?>(null)
    val selectedDistrict = _selectedDistrict.asStateFlow()

    val allSites = repository.getAllSites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val filteredSites = combine(
        allSites,
        searchQuery,
        selectedType,
        selectedDistrict
    ) { sites, query, type, district ->
        sites.filter { site ->
            val matchesSearch = query.isBlank() ||
                site.name.contains(query, ignoreCase = true) ||
                site.nameLocal.contains(query, ignoreCase = true) ||
                site.location.contains(query, ignoreCase = true)
            val matchesType = type == null || site.type == type
            val matchesDistrict = district == null || site.district == district
            matchesSearch && matchesType && matchesDistrict
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val checkInCount = repository.getCheckInCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val uniqueSiteCount = repository.getUniqueSiteCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setTypeFilter(type: SiteType?) {
        _selectedType.value = type
    }

    fun setDistrictFilter(district: String?) {
        _selectedDistrict.value = district
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedType.value = null
        _selectedDistrict.value = null
    }
}
