package com.mavuno.church.guard

import java.util.Locale

object MultimodalFilterEngine {

    private val rules: List<FilterRule> = listOf(
        FilterRule(
            category = "Adult & Inappropriate Themes",
            severity = ThreatSeverity.CRITICAL,
            keywords = listOf(
                "nsfw", "porn", "xxx", "erotic", "nude", "nudity", "adult only",
                "18+", "explicit content", "sexually explicit", "stripclub", "escort"
            ),
            blockedForTiers = setOf(AgeTier.CHILD, AgeTier.PRE_TEEN, AgeTier.TEEN),
            explanation = "This page contains adult or sexually suggestive material that is not suitable for your age profile."
        ),
        FilterRule(
            category = "Graphic Violence & Gore",
            severity = ThreatSeverity.HIGH,
            keywords = listOf(
                "blood gore", "decapitation", "murder footage", "brutal kill",
                "torture video", "gun violence live", "mass shooting video", "suicide method", "self-harm instructions"
            ),
            blockedForTiers = setOf(AgeTier.CHILD, AgeTier.PRE_TEEN, AgeTier.TEEN),
            explanation = "Ella detected graphic violence or disturbing content. A protective safety shield has been engaged."
        ),
        FilterRule(
            category = "Mild Violence & Scary Imagery",
            severity = ThreatSeverity.MEDIUM,
            keywords = listOf(
                "horror movie", "jumpscare", "creepy monster", "bloody fight", "terrifying ghost",
                "zombie massacre", "killing game", "death battle"
            ),
            blockedForTiers = setOf(AgeTier.CHILD),
            explanation = "Ella detected scary or intense themes that might be frightening for younger children."
        ),
        FilterRule(
            category = "Profanity & Vulgar Speech",
            severity = ThreatSeverity.MEDIUM,
            keywords = listOf(
                "f*ck", "fuck", "sh*t", "shit", "bitch", "bastard", "crap", "damn", "asshole", "wtf"
            ),
            blockedForTiers = setOf(AgeTier.CHILD, AgeTier.PRE_TEEN),
            explanation = "Ella detected vulgar language or swearing that doesn't align with family standards."
        ),
        FilterRule(
            category = "Cyberbullying & Toxic Interactions",
            severity = ThreatSeverity.HIGH,
            keywords = listOf(
                "hate you", "kill yourself", "ugly loser", "nobody likes you", "stupid idiot",
                "go die", "harassment", "freak"
            ),
            blockedForTiers = setOf(AgeTier.CHILD, AgeTier.PRE_TEEN, AgeTier.TEEN),
            explanation = "Ella noticed harmful or bullying words in this conversation. Remember you are valued and safe!"
        ),
        FilterRule(
            category = "Scams, Gambling & Phishing",
            severity = ThreatSeverity.HIGH,
            keywords = listOf(
                "free v-bucks hack", "free robux generator", "claim free gift card now",
                "enter credit card", "crypto giveaway", "casino bet", "gambling win real money",
                "send parents card info", "urgent wire transfer"
            ),
            blockedForTiers = setOf(AgeTier.CHILD, AgeTier.PRE_TEEN, AgeTier.TEEN),
            explanation = "Ella shielded a potential scam asking for private credentials or money."
        ),
        FilterRule(
            category = "Unsupervised In-App Purchases",
            severity = ThreatSeverity.MEDIUM,
            keywords = listOf(
                "buy now for $", "tap to purchase $99", "checkout your order now", "paybill number"
            ),
            blockedForTiers = setOf(AgeTier.CHILD),
            explanation = "Ella paused checkout to prevent accidental one-tap purchases."
        )
    )

    fun evaluateContent(text: String, ageTier: AgeTier): ScanResult {
        if (text.isBlank()) {
            return ScanResult(
                isSafe = true,
                threatScore = 0.0f,
                ellaExplanation = "Everything on this screen is verified safe! 🌟",
                recommendation = "Safe to continue exploring."
            )
        }

        val normalized = text.lowercase(Locale.ROOT)
        val matchedRules = mutableListOf<Pair<FilterRule, List<String>>>()

        for (rule in rules) {
            val matchedKeywords = rule.keywords.filter { keyword ->
                normalized.contains(keyword.lowercase(Locale.ROOT))
            }
            if (matchedKeywords.isNotEmpty()) {
                matchedRules.add(Pair(rule, matchedKeywords))
            }
        }

        if (matchedRules.isEmpty()) {
            return ScanResult(
                isSafe = true,
                threatScore = 0.05f,
                ellaExplanation = "Ella analyzed the on-screen text and visuals — all clear! ✨",
                recommendation = "Safe kid-friendly content."
            )
        }

        // Check if any rule triggers for the current AgeTier
        val blockingRules = matchedRules.filter { (rule, _) ->
            rule.blockedForTiers.contains(ageTier)
        }

        if (blockingRules.isNotEmpty()) {
            val highestSeverityRule = blockingRules.maxByOrNull { it.first.severity.ordinal }!!
            val allFlagged = blockingRules.flatMap { it.second }.distinct()
            val score = when (highestSeverityRule.first.severity) {
                ThreatSeverity.CRITICAL -> 0.95f
                ThreatSeverity.HIGH -> 0.80f
                ThreatSeverity.MEDIUM -> 0.55f
                ThreatSeverity.LOW -> 0.30f
            }

            return ScanResult(
                isSafe = false,
                threatScore = score,
                triggeredCategory = highestSeverityRule.first.category,
                severity = highestSeverityRule.first.severity,
                flaggedTerms = allFlagged,
                ellaExplanation = "🛡️ Ella Shield: ${highestSeverityRule.first.explanation}",
                recommendation = "Glassmorphic overlay activated. Ask a parent or guardian to review.",
                requiresShield = true
            )
        }

        // Matched rules exist but are allowed for this older age tier (e.g. teen allowed mild words with advisory)
        val advisory = matchedRules.first()
        return ScanResult(
            isSafe = true,
            threatScore = 0.25f,
            triggeredCategory = advisory.first.category,
            severity = ThreatSeverity.LOW,
            flaggedTerms = advisory.second,
            ellaExplanation = "ℹ️ Ella Note: Contains ${advisory.first.category.lowercase(Locale.ROOT)}, permissible under ${ageTier.title} mode.",
            recommendation = "Permissible with parental awareness.",
            requiresShield = false
        )
    }

    fun getSuggestedEllaPrompts(ageTier: AgeTier): List<String> {
        return when (ageTier) {
            AgeTier.CHILD -> listOf(
                "✨ Tell me a fun Bible story",
                "🛡️ Is this cartoon safe for me?",
                "🎨 Help me draw something cool",
                "🦁 What does this animal eat?",
                "🙏 Teach me a short bedtime prayer"
            )
            AgeTier.PRE_TEEN -> listOf(
                "🛡️ Is this YouTube video PG?",
                "📖 Explain Sunday's sermon scripture",
                "💡 Help me understand my science homework",
                "🎮 Are there safe games like this?",
                "🙏 Prayer for school exams this week"
            )
            AgeTier.TEEN -> listOf(
                "✨ Summarize this long article safely",
                "🛡️ Fact-check this social media post",
                "⛪ Mavuno Youth service schedule & topics",
                "🎯 Advice on handling peer pressure",
                "💡 Safe study tools for high school"
            )
        }
    }
}
