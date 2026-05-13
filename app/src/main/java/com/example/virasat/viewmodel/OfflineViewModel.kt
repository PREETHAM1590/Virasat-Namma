package com.example.virasat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.VirasatApplication
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.data.model.HeritageSite
import com.example.virasat.data.service.FirebaseAuthService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OfflineViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RepositoryProvider.getRepository(application)
    private val networkMonitor = (application as VirasatApplication).networkMonitor

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline

    private val _bookmarkedSites = MutableStateFlow<List<HeritageSite>>(emptyList())
    val bookmarkedSites: StateFlow<List<HeritageSite>> = _bookmarkedSites.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observeBookmarks().collect { siteIds ->
                _bookmarkedSites.value = siteIds.mapNotNull { repo.getSiteById(it) }
            }
        }
        viewModelScope.launch {
            networkMonitor.isOnline.filter { it }.collect { syncBookmarksFromFirestore() }
        }
    }

    private suspend fun syncBookmarksFromFirestore() {
        val uid = FirebaseAuthService.uid ?: return
        try {
            val doc = FirebaseFirestore.getInstance()
                .collection("users").document(uid).get().await()
            val remoteIds = doc.get("bookmarks") as? List<*> ?: return
            remoteIds.filterIsInstance<String>().forEach { siteId ->
                if (!repo.isBookmarked(siteId)) repo.toggleBookmark(siteId)
            }
        } catch (_: Exception) { }
    }
}
