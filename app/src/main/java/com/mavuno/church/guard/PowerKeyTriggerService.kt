package com.mavuno.church.guard

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.SystemClock

class PowerKeyTriggerService : Service() {

    private var screenOffTime: Long = 0
    private var powerPressCount = 0
    private var lastPowerPressTime: Long = 0

    private val powerKeyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            val now = SystemClock.elapsedRealtime()

            if (action == Intent.ACTION_SCREEN_OFF) {
                screenOffTime = now
            } else if (action == Intent.ACTION_SCREEN_ON) {
                // If screen turned on rapidly or rapid presses detected
                if (now - lastPowerPressTime < 1500) {
                    powerPressCount++
                    if (powerPressCount >= 2) {
                        // Double/Triple Power Key gesture detected -> Trigger Ella!
                        EllaOverlayManager.triggerEllaAssistant("🛡️ Checking on-screen safety…")
                        powerPressCount = 0
                    }
                } else {
                    powerPressCount = 1
                }
                lastPowerPressTime = now
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(powerKeyReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(powerKeyReceiver)
        } catch (_: Exception) {}
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
