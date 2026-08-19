package com.mavuno.church

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mavuno.church.guard.ContentGuardService
import com.mavuno.church.guard.PowerKeyTriggerService
import com.mavuno.church.ui.navigation.AppNavigation
import com.mavuno.church.ui.theme.DeepWhiteBackground
import com.mavuno.church.ui.theme.MavunoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            ContentGuardService.startService(this)
            val powerIntent = Intent(this, PowerKeyTriggerService::class.java)
            startService(powerIntent)
        } catch (_: Exception) {
            // Graceful fallback in environments with restricted background service start
        }

        setContent {
            MavunoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DeepWhiteBackground
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
