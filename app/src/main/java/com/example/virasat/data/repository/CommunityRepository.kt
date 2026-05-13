package com.example.virasat.data.repository

import android.graphics.Bitmap
import com.example.virasat.data.model.CommunityPost
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID

class CommunityRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun observePosts(): Flow<List<CommunityPost>> = callbackFlow {
        val listener = db.collection("communityPosts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                val posts = snap?.documents?.mapNotNull { doc ->
                    CommunityPost(
                        id = doc.id,
                        userId = doc.getString("userId") ?: "",
                        userName = doc.getString("userName") ?: "",
                        siteId = doc.getString("siteId") ?: "",
                        siteName = doc.getString("siteName") ?: "",
                        imageUrl = doc.getString("imageUrl"),
                        text = doc.getString("text") ?: "",
                        timestamp = doc.getTimestamp("timestamp")
                    )
                }
                trySend(posts ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun submitPost(post: CommunityPost, photoBitmap: Bitmap? = null) {
        val imageUrl = photoBitmap?.let { uploadPhoto(it, post.userId) }
        val doc = mapOf(
            "userId" to post.userId,
            "userName" to post.userName,
            "siteId" to post.siteId,
            "siteName" to post.siteName,
            "imageUrl" to imageUrl,
            "text" to post.text,
            "timestamp" to FieldValue.serverTimestamp()
        )
        db.collection("communityPosts").add(doc).await()
    }

    private suspend fun uploadPhoto(bitmap: Bitmap, userId: String): String {
        val ref = storage.reference.child("community/$userId/${UUID.randomUUID()}.jpg")
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
        ref.putBytes(stream.toByteArray()).await()
        return ref.downloadUrl.await().toString()
    }
}
