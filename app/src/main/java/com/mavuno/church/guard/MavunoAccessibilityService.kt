package com.mavuno.church.guard

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class MavunoAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                    AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
            notificationTimeout = 250
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || !EllaOverlayManager.isGuardActive.value) return

        val source = event.source ?: return
        val extractedText = extractAllText(source)
        source.recycle()

        if (extractedText.isNotBlank()) {
            val tier = EllaOverlayManager.currentAgeTier.value
            val scanResult = MultimodalFilterEngine.evaluateContent(extractedText, tier)
            if (scanResult.requiresShield && !EllaOverlayManager.isShieldActive.value) {
                EllaOverlayManager.applyShield(
                    result = scanResult,
                    snippet = extractedText,
                    sourceApp = event.packageName?.toString() ?: "Active Application"
                )
            }
        }
    }

    private fun extractAllText(node: AccessibilityNodeInfo?): String {
        if (node == null) return ""
        val builder = StringBuilder()
        node.text?.let { builder.append(it).append(" ") }
        node.contentDescription?.let { builder.append(it).append(" ") }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                builder.append(extractAllText(child))
                child.recycle()
            }
        }
        return builder.toString().trim()
    }

    override fun onInterrupt() {
        // Handle service interruption gracefully
    }
}
