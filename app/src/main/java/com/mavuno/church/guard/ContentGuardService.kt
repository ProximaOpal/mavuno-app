package com.mavuno.church.guard

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mavuno.church.MainActivity
import com.mavuno.church.R

class ContentGuardService : Service() {

    companion object {
        const val CHANNEL_ID = "mavuno_kids_guard_channel"
        const val NOTIFICATION_ID = 9021
        const val ACTION_START_GUARD = "com.mavuno.church.ACTION_START_GUARD"
        const val ACTION_STOP_GUARD = "com.mavuno.church.ACTION_STOP_GUARD"

        fun startService(context: Context) {
            val intent = Intent(context, ContentGuardService::class.java).apply {
                action = ACTION_START_GUARD
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, ContentGuardService::class.java).apply {
                action = ACTION_STOP_GUARD
            }
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_GUARD -> {
                EllaOverlayManager.toggleGuard(false)
                stopForeground(true)
                stopSelf()
                return START_NOT_STICKY
            }
            else -> {
                EllaOverlayManager.toggleGuard(true)
                val notification = buildGuardNotification()
                startForeground(NOTIFICATION_ID, notification)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Mavuno Smart Kids Guard",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitors on-screen content and protects kids with age-adaptive filters"
                setShowBadge(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildGuardNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val currentTier = EllaOverlayManager.currentAgeTier.value

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Mavuno Smart Kids Guard · Active")
            .setContentText("Shielding active: ${currentTier.title} (${currentTier.ageRange})")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
