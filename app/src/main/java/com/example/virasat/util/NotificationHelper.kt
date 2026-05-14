package com.example.virasat.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.virasat.R

object NotificationHelper {
    private const val CHANNEL_PROXIMITY = "virasat_proximity"
    private const val CHANNEL_WEEKLY = "virasat_weekly"
    private const val CHANNEL_BADGES = "virasat_badges"

    fun createChannels(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_PROXIMITY, "Nearby Heritage Sites", NotificationManager.IMPORTANCE_HIGH)
        )
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_WEEKLY, "Weekly Heritage Facts", NotificationManager.IMPORTANCE_DEFAULT)
        )
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_BADGES, "Badge Unlocks", NotificationManager.IMPORTANCE_DEFAULT)
        )
    }

    fun showProximityNotification(context: Context, siteName: String, siteId: String) {
        if (!NotificationPreferences.isEnabled(context, "proximity")) return
        if (!hasPermission(context)) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("virasat://site/$siteId"))
        val pi = PendingIntent.getActivity(context, siteId.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, CHANNEL_PROXIMITY)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("You're near $siteName!")
            .setContentText("Tap to explore this heritage site")
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(siteId.hashCode(), notification)
    }

    fun showBadgeNotification(context: Context, badgeName: String) {
        if (!NotificationPreferences.isEnabled(context, "badges")) return
        if (!hasPermission(context)) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("virasat://badges"))
        val pi = PendingIntent.getActivity(context, badgeName.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, CHANNEL_BADGES)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Badge Unlocked: $badgeName")
            .setContentText("Congratulations! You earned a new badge.")
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(badgeName.hashCode(), notification)
    }

    fun showRemoteNotification(context: Context, title: String, body: String, deepLink: String?) {
        if (!NotificationPreferences.isEnabled(context, "weekly")) return
        if (!hasPermission(context)) return
        val pi = deepLink?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            PendingIntent.getActivity(context, it.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_WEEKLY)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .apply { pi?.let { setContentIntent(it) } }
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(title.hashCode(), notification)
    }

    private fun hasPermission(context: Context): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true
    }
}
