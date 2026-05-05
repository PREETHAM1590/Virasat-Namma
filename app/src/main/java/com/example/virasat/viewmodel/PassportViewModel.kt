package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.CheckIn
import com.example.virasat.data.repository.HeritageRepository
import kotlinx.coroutines.flow.*

class PassportViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HeritageRepository(application)

    val checkIns: StateFlow<List<CheckIn>> = repository.getAllCheckIns()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val checkInCount: StateFlow<Int> = repository.getCheckInCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val uniqueSiteCount: StateFlow<Int> = repository.getUniqueSiteCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val unlockedFactCount: StateFlow<Int> = repository.getUnlockedFactCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)
}
