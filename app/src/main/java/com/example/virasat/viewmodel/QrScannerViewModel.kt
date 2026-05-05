package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.repository.HeritageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QrScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HeritageRepository(application)

    private val _scannedSite = MutableStateFlow<HeritageSite?>(null)
    val scannedSite: StateFlow<HeritageSite?> = _scannedSite

    private val _isCheckingIn = MutableStateFlow(false)
    val isCheckingIn: StateFlow<Boolean> = _isCheckingIn

    private val _hasCheckedIn = MutableStateFlow(false)
    val hasCheckedIn: StateFlow<Boolean> = _hasCheckedIn

    fun processQrCode(qrData: String) {
        val site = repository.getSiteById(qrData)
            ?: repository.getAllSites().value.find { it.qrCodeId == qrData }
        _scannedSite.value = site
        if (site != null) {
            viewModelScope.launch {
                _hasCheckedIn.value = repository.hasCheckedIn(site.id)
            }
        }
    }

    fun checkIn() {
        viewModelScope.launch {
            _scannedSite.value?.let { site ->
                _isCheckingIn.value = true
                repository.checkIn(site)
                _hasCheckedIn.value = true
                _isCheckingIn.value = false
            }
        }
    }

    fun reset() {
        _scannedSite.value = null
        _hasCheckedIn.value = false
    }
}
