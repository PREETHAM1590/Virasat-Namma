package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.di.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QrScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RepositoryProvider.getRepository(application)

    private val _scannedSite = MutableStateFlow<HeritageSite?>(null)
    val scannedSite: StateFlow<HeritageSite?> = _scannedSite

    private val _isCheckingIn = MutableStateFlow(false)
    val isCheckingIn: StateFlow<Boolean> = _isCheckingIn

    private val _hasCheckedIn = MutableStateFlow(false)
    val hasCheckedIn: StateFlow<Boolean> = _hasCheckedIn

    fun processQrCode(qrData: String) {
        val site = repository.getSiteById(qrData)
            ?: repository.getAllSitesList().find { it.qrCodeId == qrData }
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
