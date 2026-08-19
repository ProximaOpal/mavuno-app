package com.mavuno.church.guard

enum class AgeTier(
    val title: String,
    val ageRange: String,
    val description: String,
    val strictnessLevel: Int // 1 (Mild) to 3 (Strict)
) {
    CHILD(
        title = "Child (Early Years)",
        ageRange = "Ages 3–9",
        description = "Strict protection: Blocks all violence, profanity, adult themes, scary imagery, unmoderated chat, and financial links.",
        strictnessLevel = 3
    ),
    PRE_TEEN(
        title = "Pre-Teen (Intermediate)",
        ageRange = "Ages 10–12",
        description = "Balanced protection: Allows mild PG material with warnings. Blocks graphic violence, adult content, and unauthorized transactions.",
        strictnessLevel = 2
    ),
    TEEN(
        title = "Teen (Guided Independence)",
        ageRange = "Ages 13–17",
        description = "Smart advisory: Shields hate speech, scams, and explicit NSFW content while providing spiritual & educational guidance prompts.",
        strictnessLevel = 1
    )
}

data class FilterRule(
    val category: String,
    val severity: ThreatSeverity,
    val keywords: List<String>,
    val blockedForTiers: Set<AgeTier>,
    val explanation: String
)

enum class ThreatSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

data class ScanResult(
    val isSafe: Boolean,
    val threatScore: Float, // 0.0 (Safe) to 1.0 (Critical Threat)
    val triggeredCategory: String? = null,
    val severity: ThreatSeverity = ThreatSeverity.LOW,
    val flaggedTerms: List<String> = emptyList(),
    val ellaExplanation: String = "Content looks clean and kid-friendly! ✨",
    val recommendation: String = "No action required.",
    val requiresShield: Boolean = false
)
