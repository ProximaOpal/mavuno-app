package com.mavuno.church.guard

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class SafetyIncident(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val category: String,
    val severity: ThreatSeverity,
    val snippet: String,
    val appOrSource: String,
    val actionTaken: String,
    val ageTier: AgeTier,
    val overriddenByParent: Boolean = false
) {
    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("h:mm a · MMM d", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}
