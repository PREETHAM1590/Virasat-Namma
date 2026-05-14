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

    private val _qrError = MutableStateFlow<String?>(null)
    val qrError: StateFlow<String?> = _qrError

    private val _isCheckingIn = MutableStateFlow(false)
    val isCheckingIn: StateFlow<Boolean> = _isCheckingIn

    private val _hasCheckedIn = MutableStateFlow(false)
    val hasCheckedIn: StateFlow<Boolean> = _hasCheckedIn

    /** The hidden fact unlocked during this QR scan session (null until check-in) */
    private val _unlockedFact = MutableStateFlow<Fact?>(null)
    val unlockedFact: StateFlow<Fact?> = _unlockedFact

    fun processQrCode(qrData: String) {
        viewModelScope.launch {
            _qrError.value = null
            val token = extractSiteId(qrData)
            if (token.isBlank()) {
                _scannedSite.value = null
                _qrError.value = "Unrecognized QR code"
                return@launch
            }

            // 1. Try direct doc-ID lookup (if QR encodes plain site id like "hampi")
            // 2. Try qrCodeId field lookup (handles "QR-HAMPI-001" format)
            // 3. Fallback: scan all sites for either match
            val site = repository.getSiteById(token)
                ?: repository.getSiteByQrCode(token)
                ?: repository.getAllSitesList().find {
                    it.qrCodeId.equals(token, ignoreCase = true) ||
                    it.id.equals(token, ignoreCase = true)
                }

            if (site == null) {
                _scannedSite.value = null
                _qrError.value = "No heritage site found for this QR code"
            } else {
                _scannedSite.value = site
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
        // Hard limit then strip non-printable / control chars
        val input = raw.trim().take(500).replace(Regex("[\\x00-\\x1F\\x7F]"), "")
        if (input.isBlank()) return ""

        // URL format — extract last path segment and sanitize
        if (input.startsWith("http://", ignoreCase = true) ||
            input.startsWith("https://", ignoreCase = true)
        ) {
            val path = input.substringAfter("://").substringAfter("/")
            val segment = path.trimEnd('/').substringAfterLast("/")
            // Whitelist: alphanumeric, dash, underscore only for path segments
            return segment.replace(Regex("[^a-zA-Z0-9\\-_]"), "").take(50)
        }

        // JSON format — extract siteId field
        if (input.startsWith("{")) {
            val match = Regex("\"siteId\"\\s*:\\s*\"([a-zA-Z0-9\\-_]{1,50})\"").find(input)
            if (match != null) return match.groupValues[1]
            return ""
        }

        // Plain ID or QR code ID (QR-HAMPI-001): whitelist alphanumeric, dash, underscore
        return input.replace(Regex("[^a-zA-Z0-9\\-_]"), "").take(50)
    }

    fun checkIn() {
        viewModelScope.launch {
            val site = _scannedSite.value ?: return@launch
            _isCheckingIn.value = true
            try {
                repository.checkIn(site)
                _hasCheckedIn.value = true
                // Unlock the next unread hidden fact for this site
                val fact = findNextFact(site)
                if (fact != null) {
                    repository.unlockFact(site.id, fact)
                    _unlockedFact.value = fact
                }
            } catch (e: Exception) {
                _qrError.value = "Check-in failed: ${e.message ?: "Unknown error"}"
            } finally {
                // Always clear spinner even on failure
                _isCheckingIn.value = false
            }
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

    fun clearQrError() {
        _qrError.value = null
    }

    fun reset() {
        _scannedSite.value = null
        _isCheckingIn.value = false
        _hasCheckedIn.value = false
        _unlockedFact.value = null
        _qrError.value = null
    }
}
