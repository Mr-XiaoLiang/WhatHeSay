package com.lollipop.whs.service

import android.app.NotificationManager
import android.content.Intent
import android.net.VpnService
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lollipop.whs.R

class ProxyService : VpnService() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID_ONGOING = "proxy_service"
        const val NOTIFICATION_ID_ONGOING = 233
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        return START_STICKY_COMPATIBILITY
    }

    private fun createNotificationChannel(): NotificationManagerCompat {
        val manager = NotificationManagerCompat.from(this)
        val channel = NotificationChannelCompat.Builder(
            NOTIFICATION_CHANNEL_ID_ONGOING,
            NotificationManager.IMPORTANCE_DEFAULT
        ).setName("On going service")
            .setDescription("On going service")
            .build()
        manager.createNotificationChannel(channel)
        return manager
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_ONGOING)
            .setContentTitle("Proxy Service")
            .setContentText("Proxy Service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        val managerCompat = createNotificationChannel()
        if (managerCompat.areNotificationsEnabled()) {
            managerCompat.notify(NOTIFICATION_ID_ONGOING, notification)
        }
    }

}