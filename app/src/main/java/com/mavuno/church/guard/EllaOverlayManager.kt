package com.mavuno.church.guard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object EllaOverlayManager {

    private val scope = CoroutineScope(Dispatchers.Main)

    // Guard Enabled Status
    private val _isGuardActive = MutableStateFlow(true)
    val isGuardActive: StateFlow<Boolean> = _isGuardActive.asStateFlow()

    // Current Age Tier
    private val _currentAgeTier = MutableStateFlow(AgeTier.CHILD)
    val currentAgeTier: StateFlow<AgeTier> = _currentAgeTier.asStateFlow()

    // Scanning Animation state (pulsing border)
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    // Shield State (Glassmorphism overlay on inappropriate content)
    private val _isShieldActive = MutableStateFlow(false)
    val isShieldActive: StateFlow<Boolean> = _isShieldActive.asStateFlow()

    private val _activeShieldScanResult = MutableStateFlow<ScanResult?>(null)
    val activeShieldScanResult: StateFlow<ScanResult?> = _activeShieldScanResult.asStateFlow()

    // Floating Ella Assistant Card / Pill Stack State
    private val _isEllaVisible = MutableStateFlow(false)
    val isEllaVisible: StateFlow<Boolean> = _isEllaVisible.asStateFlow()

    private val _ellaQuery = MutableStateFlow("")
    val ellaQuery: StateFlow<String> = _ellaQuery.asStateFlow()

    private val _ellaResponse = MutableStateFlow<String?>(null)
    val ellaResponse: StateFlow<String?> = _ellaResponse.asStateFlow()

    private val _isEllaThinking = MutableStateFlow(false)
    val isEllaThinking: StateFlow<Boolean> = _isEllaThinking.asStateFlow()

    // Home Screen and External Apps Simulation Mode
    private val _isHomeScreenSimulation = MutableStateFlow(false)
    val isHomeScreenSimulation: StateFlow<Boolean> = _isHomeScreenSimulation.asStateFlow()

    // Safety Incidents History
    private val _incidents = MutableStateFlow<List<SafetyIncident>>(
        listOf(
            SafetyIncident(
                category = "Adult & Inappropriate Themes",
                severity = ThreatSeverity.CRITICAL,
                snippet = "Blocked explicit URL navigation: adult-stream-example.xyz",
                appOrSource = "Web Browser Container",
                actionTaken = "Glassmorphic Shield Applied",
                ageTier = AgeTier.CHILD
            ),
            SafetyIncident(
                category = "Scams, Gambling & Phishing",
                severity = ThreatSeverity.HIGH,
                snippet = "Blocked pop-up: 'Claim 10,000 Free Robux Now!'",
                appOrSource = "Online Game Portal",
                actionTaken = "Shield Engaged & Alert Sent",
                ageTier = AgeTier.CHILD
            )
        )
    )
    val incidents: StateFlow<List<SafetyIncident>> = _incidents.asStateFlow()

    // Parent PIN configuration
    var parentPin: String = "1234"
        private set

    // Bridge notification stream (for web bridge listeners)
    private val _bridgeEvents = MutableSharedFlow<Pair<String, String>>()
    val bridgeEvents: SharedFlow<Pair<String, String>> = _bridgeEvents.asSharedFlow()

    fun toggleGuard(enabled: Boolean) {
        _isGuardActive.value = enabled
        if (!enabled) {
            dismissShield()
            _isScanning.value = false
        }
        emitBridgeEvent("guard_status_change", """{"enabled": $enabled}""")
    }

    fun setAgeTier(tier: AgeTier) {
        _currentAgeTier.value = tier
        emitBridgeEvent("age_profile_change", """{"tier": "${tier.name}", "title": "${tier.title}"}""")
    }

    fun triggerEllaAssistant(customPrompt: String? = null) {
        _isEllaVisible.value = true
        if (!customPrompt.isNullOrBlank()) {
            askElla(customPrompt)
        } else {
            _ellaResponse.value = "Hi there! I'm Ella, your Mavuno Smart Safety & Learning buddy. How can I help you today? ✨"
        }
    }

    fun dismissElla() {
        _isEllaVisible.value = false
        _isEllaThinking.value = false
    }

    fun askElla(prompt: String) {
        _ellaQuery.value = prompt
        _isEllaThinking.value = true
        _isScanning.value = true

        scope.launch {
            delay(1200) // Realistic AI assistant processing pause
            _isEllaThinking.value = false
            _isScanning.value = false

            val tier = _currentAgeTier.value
            val scan = MultimodalFilterEngine.evaluateContent(prompt, tier)

            if (!scan.isSafe && scan.requiresShield) {
                _ellaResponse.value = "⚠️ Warning: That question contains terms (${scan.triggeredCategory}) that are restricted under ${tier.title}. Let's focus on fun, positive, and safe topics!"
            } else {
                _ellaResponse.value = generateEllaResponse(prompt, tier)
            }
        }
    }

    private fun generateEllaResponse(prompt: String, tier: AgeTier): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("bible") || lower.contains("scripture") || lower.contains("story") ->
                "📖 Bible Spark: Did you know David was just a teenager when he showed great courage against Goliath with faith in God? (1 Samuel 17). God gives you strength too!"

            lower.contains("prayer") || lower.contains("pray") ->
                "🙏 Little Heart Prayer: 'Dear God, thank you for loving me and keeping my mind and heart safe today. Guide my thoughts and bless my family. In Jesus' name, Amen!' ✨"

            lower.contains("pg") || lower.contains("safe") || lower.contains("video") ->
                "🛡️ Ella Content Check: I scanned this view. Visuals, audio tracks, and text ratings are rated PG-Clean with zero detected violence or adult themes. Enjoy safely! 👍"

            lower.contains("homework") || lower.contains("science") || lower.contains("study") ->
                "💡 Learning Guide: Break your task down into 3 simple steps: 1) Read the core question, 2) Highlight key facts, 3) Write your answer in your own words. You've got this!"

            lower.contains("sermon") || lower.contains("mavuno") ->
                "⛪ Mavuno Note: This month's series is 'Fearless Faith'. Pastor taught that turning our worries over to God through prayer replaces fear with deep peace."

            else ->
                "🌟 Ella says: You're doing amazing! Always remember you are fearfully and wonderfully made. Let me know if you want me to scan any screen or help with anything else!"
        }
    }

    fun triggerScanSimulation(sampleText: String, sourceApp: String = "Test Sandbox") {
        if (!_isGuardActive.value) return

        _isScanning.value = true
        scope.launch {
            delay(900) // scanning animation
            _isScanning.value = false

            val result = MultimodalFilterEngine.evaluateContent(sampleText, _currentAgeTier.value)
            if (result.requiresShield) {
                applyShield(result, sampleText, sourceApp)
            } else {
                _activeShieldScanResult.value = result
            }
        }
    }

    fun applyShield(result: ScanResult, snippet: String, sourceApp: String) {
        _isShieldActive.value = true
        _activeShieldScanResult.value = result

        val incident = SafetyIncident(
            category = result.triggeredCategory ?: "Restricted Material",
            severity = result.severity,
            snippet = snippet.take(160),
            appOrSource = sourceApp,
            actionTaken = "Shield Engaged",
            ageTier = _currentAgeTier.value
        )
        _incidents.value = listOf(incident) + _incidents.value

        emitBridgeEvent("shield_triggered", """{"category": "${incident.category}", "severity": "${incident.severity}"}""")
    }

    fun unlockShieldWithPin(pin: String): Boolean {
        if (pin == parentPin) {
            _isShieldActive.value = false
            // update incident log
            val currentList = _incidents.value.toMutableList()
            if (currentList.isNotEmpty()) {
                currentList[0] = currentList[0].copy(overriddenByParent = true, actionTaken = "Overridden with Parent PIN")
                _incidents.value = currentList
            }
            return true
        }
        return false
    }

    fun toggleHomeScreenSimulation(enabled: Boolean) {
        _isHomeScreenSimulation.value = enabled
        if (enabled) {
            _isEllaVisible.value = true
        }
    }

    fun dismissShield() {
        _isShieldActive.value = false
        _activeShieldScanResult.value = null
    }

    fun setCustomParentPin(newPin: String) {
        if (newPin.length >= 4) {
            parentPin = newPin
        }
    }

    private fun emitBridgeEvent(name: String, dataJson: String) {
        scope.launch {
            _bridgeEvents.emit(Pair(name, dataJson))
        }
    }
}
