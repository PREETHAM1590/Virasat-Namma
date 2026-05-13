package com.example.virasat.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.model.CommunityPost
import com.example.virasat.data.repository.CommunityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CommunityViewModel(application: Application) : AndroidViewModel(application) {
    private val communityRepo = CommunityRepository()

    val posts: StateFlow<List<CommunityPost>> =
        communityRepo.observePosts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun submitPost(post: CommunityPost, photoBitmap: Bitmap? = null) {
        viewModelScope.launch {
            communityRepo.submitPost(post, photoBitmap)
        }
    }
}
