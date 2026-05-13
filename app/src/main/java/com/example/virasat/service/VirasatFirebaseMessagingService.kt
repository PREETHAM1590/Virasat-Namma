package com.example.virasat.service

import com.example.virasat.data.service.FirebaseAuthService
import com.example.virasat.util.NotificationHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class VirasatFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        val uid = FirebaseAuthService.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users").document(uid)
            .update("fcmToken", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: return
        val body = message.notification?.body ?: message.data["body"] ?: ""
        val deepLink = message.data["deepLink"]
        NotificationHelper.showRemoteNotification(this, title, body, deepLink)
    }
}
