package com.mavuno.church.guard

data class EllaActionCommand(
    val thought: String,
    val safety_assessment: SafetyAssessment,
    val action: String, // "CLICK" | "SWIPE" | "TYPE" | "GLOBAL_ACTION" | "SHIELD_OVERLAY" | "FINISH"
    val coordinates: Coordinates? = null,
    val swipe_vector: SwipeVector? = null,
    val text_input: String = "",
    val key_code: String? = null, // "KEYCODE_BACK" | "KEYCODE_HOME"
    val status: String = "IN_PROGRESS" // "IN_PROGRESS" | "SUCCESS" | "FAILED"
) {
    fun toJsonString(): String {
        val coordsJson = if (coordinates != null) {
            """{"x": ${coordinates.x}, "y": ${coordinates.y}}"""
        } else "null"

        val swipeJson = if (swipe_vector != null) {
            """{"start_x": ${swipe_vector.start_x}, "start_y": ${swipe_vector.start_y}, "end_x": ${swipe_vector.end_x}, "end_y": ${swipe_vector.end_y}}"""
        } else "null"

        val keyCodeJson = if (key_code != null) """"$key_code"""" else "null"

        return """{
  "thought": "${thought.replace("\"", "\\\"")}",
  "safety_assessment": {
    "content_flagged": ${safety_assessment.content_flagged},
    "flagged_category": "${safety_assessment.flagged_category}",
    "recommended_guard_action": "${safety_assessment.recommended_guard_action}"
  },
  "action": "$action",
  "coordinates": $coordsJson,
  "swipe_vector": $swipeJson,
  "text_input": "${text_input.replace("\"", "\\\"")}",
  "key_code": $keyCodeJson,
  "status": "$status"
}"""
    }
}

data class SafetyAssessment(
    val content_flagged: Boolean,
    val flagged_category: String, // "NONE" | "EXPLICIT" | "VIOLENCE" | "INAPPROPRIATE_TEXT" | "SCAM_PHISHING"
    val recommended_guard_action: String // "ALLOW" | "BLOCK_SCREEN" | "SWIPE_PAST" | "CLOSE_APP"
)

data class Coordinates(
    val x: Int,
    val y: Int
)

data class SwipeVector(
    val start_x: Int,
    val start_y: Int,
    val end_x: Int,
    val end_y: Int
)
