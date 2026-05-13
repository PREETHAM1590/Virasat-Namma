package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.Fact
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

    /** The hidden fact unlocked during this QR scan session (null until check-in) */
    private val _unlockedFact = MutableStateFlow<Fact?>(null)
    val unlockedFact: StateFlow<Fact?> = _unlockedFact

    fun processQrCode(qrData: String) {
        viewModelScope.launch {
            val siteId = extractSiteId(qrData)
            if (siteId.isBlank()) {
                _scannedSite.value = null
                return@launch
            }

            val site = repository.getSiteById(siteId)
                ?: repository.getAllSitesList().find {
                    it.qrCodeId == siteId || it.id == siteId
                }
            _scannedSite.value = site
            if (site != null) {
                _hasCheckedIn.value = repository.hasCheckedIn(site.id)
            }
        }
    }

    /** Extract site ID from various QR formats:
     *  - Plain site ID: "hampi"
     *  - QR code ID: "QR-HAMPI-001"
     *  - URL: "https://virasat.app/site/hampi" or similar
     *  - JSON: {"siteId": "hampi"}
     */
    private fun extractSiteId(raw: String): String {
        val input = raw.trim().replace(Regex("[<>\"';&]"), "").take(200)
        if (input.isBlank()) return ""

        // URL format — extract last path segment
        if (input.startsWith("http")) {
            val path = input.substringAfter("://").substringAfter("/")
            val segment = path.trimEnd('/').substringAfterLast("/")
            return segment.take(50)
        }

        // JSON format — extract siteId field
        if (input.startsWith("{")) {
            val match = Regex("\"siteId\"\\s*:\\s*\"([^\"]+)\"").find(input)
            if (match != null) return match.groupValues[1].take(50)
        }

        // Plain ID or QR code ID (QR-HAMPI-001)
        return input.take(50)
    }

    fun checkIn() {
        viewModelScope.launch {
            val site = _scannedSite.value ?: return@launch
            _isCheckingIn.value = true
            // Do the check-in
            repository.checkIn(site)
            _hasCheckedIn.value = true

            // Unlock the next unread hidden fact for this site
            val fact = findNextFact(site)
            if (fact != null) {
                repository.unlockFact(site.id, fact)
                _unlockedFact.value = fact
            }

            _isCheckingIn.value = false
        }
    }

    private suspend fun findNextFact(site: HeritageSite): Fact? {
        val facts = site.facts
        if (facts.isEmpty()) return null
        // Find first fact not yet unlocked
        for (fact in facts) {
            val alreadyUnlocked = repository.isFactUnlocked(fact.id)
            if (!alreadyUnlocked) return fact
        }
        // All unlocked — return last one as a reminder
        return facts.last()
    }

    fun reset() {
        _scannedSite.value = null
        _hasCheckedIn.value = false
        _unlockedFact.value = null
    }
}
