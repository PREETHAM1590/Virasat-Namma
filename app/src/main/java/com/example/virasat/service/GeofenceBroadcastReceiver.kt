package com.example.virasat.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.virasat.util.NotificationHelper
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent) ?: return
        if (event.hasError()) return
        event.triggeringGeofences?.forEach { geofence ->
            NotificationHelper.showProximityNotification(
                context, geofence.requestId, geofence.requestId
            )
        }
    }
}
