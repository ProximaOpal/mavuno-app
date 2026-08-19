package com.mavuno.church.bridge

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import com.mavuno.church.guard.AgeTier
import com.mavuno.church.guard.EllaOverlayManager
import com.mavuno.church.guard.MultimodalFilterEngine
import org.json.JSONObject

class MavunoWebBridge(
    private val context: Context,
    private val webView: WebView? = null
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun toggleGuardService(enabled: Boolean) {
        mainHandler.post {
            EllaOverlayManager.toggleGuard(enabled)
            Toast.makeText(
                context,
                "Kids Guard ${if (enabled) "Enabled 🛡️" else "Disabled ⏸️"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @JavascriptInterface
    fun setAgeProfile(tierName: String) {
        mainHandler.post {
            val tier = try {
                AgeTier.valueOf(tierName.uppercase())
            } catch (_: Exception) {
                AgeTier.CHILD
            }
            EllaOverlayManager.setAgeTier(tier)
            Toast.makeText(context, "Safety Age Profile set to: ${tier.title}", Toast.LENGTH_SHORT).show()
        }
    }

    @JavascriptInterface
    fun triggerEllaAssistant(prompt: String?) {
        mainHandler.post {
            EllaOverlayManager.triggerEllaAssistant(prompt)
        }
    }

    @JavascriptInterface
    fun getSafetyStatus(): String {
        val json = JSONObject().apply {
            put("guardActive", EllaOverlayManager.isGuardActive.value)
            put("ageTier", EllaOverlayManager.currentAgeTier.value.name)
            put("ageTitle", EllaOverlayManager.currentAgeTier.value.title)
            put("shieldActive", EllaOverlayManager.isShieldActive.value)
            put("scanning", EllaOverlayManager.isScanning.value)
            put("incidentCount", EllaOverlayManager.incidents.value.size)
        }
        return json.toString()
    }

    @JavascriptInterface
    fun simulateSafetyScan(text: String): String {
        val result = MultimodalFilterEngine.evaluateContent(text, EllaOverlayManager.currentAgeTier.value)
        mainHandler.post {
            EllaOverlayManager.triggerScanSimulation(text, "Hybrid WebContainer")
        }
        val json = JSONObject().apply {
            put("isSafe", result.isSafe)
            put("threatScore", result.threatScore.toDouble())
            put("category", result.triggeredCategory ?: "")
            put("explanation", result.ellaExplanation)
            put("requiresShield", result.requiresShield)
        }
        return json.toString()
    }

    @JavascriptInterface
    fun unlockWithPin(pin: String): Boolean {
        var unlocked = false
        mainHandler.post {
            unlocked = EllaOverlayManager.unlockShieldWithPin(pin)
            if (unlocked) {
                Toast.makeText(context, "Parent PIN verified · Shield Unlocked", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Incorrect PIN", Toast.LENGTH_SHORT).show()
            }
        }
        return unlocked
    }

    @JavascriptInterface
    fun getCampusInfo(): String {
        val json = JSONObject().apply {
            put("name", "Mavuno Church Nairobi Campus")
            put("location", "Hill City Campus, Bellevue, South C, Nairobi")
            put("services", "Sunday Services: 9:00 AM & 11:30 AM")
            put("paybill", "508000")
            put("contactPhone", "+254 700 000 000")
            put("contactEmail", "prayer@mavunochurch.org")
            put("series", "Fearless Faith: Moving From Fear To Freedom")
        }
        return json.toString()
    }

    @JavascriptInterface
    fun sendNativeEvent(eventName: String, payloadJson: String) {
        mainHandler.post {
            webView?.evaluateJavascript(
                "if (window.onMavunoNativeEvent) { window.onMavunoNativeEvent('$eventName', $payloadJson); }",
                null
            )
        }
    }
}
