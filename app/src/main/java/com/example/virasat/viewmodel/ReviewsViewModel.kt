package com.example.virasat.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasat.data.service.FirebaseAuthService
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID

class ReviewsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun submitReview(siteId: String, text: String, photoBitmap: Bitmap? = null) {
        require(text.length in 1..500) { "Review must be 1-500 characters" }
        viewModelScope.launch {
            val imageUrl = photoBitmap?.let { uploadReviewPhoto(it, siteId) }
            val uid = FirebaseAuthService.uid ?: return@launch
            db.collection("heritageSites").document(siteId)
                .collection("reviews")
                .add(mapOf(
                    "userId" to uid,
                    "text" to text,
                    "imageUrl" to imageUrl,
                    "timestamp" to FieldValue.serverTimestamp()
                )).await()
        }
    }

    private suspend fun uploadReviewPhoto(bitmap: Bitmap, siteId: String): String {
        val ref = storage.reference.child("reviews/$siteId/${UUID.randomUUID()}.jpg")
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
        ref.putBytes(stream.toByteArray()).await()
        return ref.downloadUrl.await().toString()
    }
}
