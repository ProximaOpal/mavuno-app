package com.mavuno.church.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.audio.AudioEffectsManager
import com.mavuno.church.guard.AgeTier
import com.mavuno.church.guard.EllaAutonomousAgent
import com.mavuno.church.guard.EllaOverlayManager
import com.mavuno.church.guard.ThreatSeverity
import com.mavuno.church.ui.components.InteractiveCard
import com.mavuno.church.ui.components.MandalaBackdrop
import com.mavuno.church.ui.components.ScreenHeader
import com.mavuno.church.ui.theme.BrandGold
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.MavunoTheme

@Composable
fun GuardScreen(
    modifier: Modifier = Modifier
) {
    val isGuardActive by EllaOverlayManager.isGuardActive.collectAsState()
    val currentAgeTier by EllaOverlayManager.currentAgeTier.collectAsState()
    val isScanning by EllaOverlayManager.isScanning.collectAsState()
    val incidents by EllaOverlayManager.incidents.collectAsState()
    val isDark = MavunoTheme.colors.isDark

    // Autonomous Agent States
    val isAgentRunning by EllaAutonomousAgent.isAgentRunning.collectAsState()
    val currentAgentTask by EllaAutonomousAgent.currentTask.collectAsState()
    val lastEmittedCommand by EllaAutonomousAgent.lastEmittedCommand.collectAsState()
    val commandHistory by EllaAutonomousAgent.commandHistory.collectAsState()

    var testInputText by remember { mutableStateOf("Claim 5000 Free Robux and enter parents card") }
    var selectedPreset by remember { mutableStateOf<String?>(null) }
    var customAutonomousGoal by remember { mutableStateOf("") }

    val presetTests = listOf(
        "Free Robux Scam Link" to "Claim 10,000 Free Robux now! Enter parent credit card number.",
        "Clean Sunday Sermon" to "Pastor shared how David defeated Goliath with fearless faith and love.",
        "Scary Horror Clip" to "Terrifying ghost jumpscare and bloody zombie massacre fight in movie.",
        "Explicit 18+ Adult Site" to "Visit 18+ explicit erotic nsfw video stream now."
    )

    val autonomousPresets = listOf(
        "Convert scanned invoice" to "Convert scanned receipt to digital expense invoice PDF",
        "Grade 8 English writing" to "Compose Grade 8 English writing draft with courage theme",
        "Curate 25 Years Kenyan Mix" to "Scan Spotify playlist for 25 Years of Kenyan Music family safe",
        "Audit Unsafe Social Stream" to "Navigate TikTok Lite and shield unverified explicit links"
    )

    Box(modifier = modifier.fillMaxSize().background(MavunoTheme.colors.background)) {
        MandalaBackdrop(
            color = BrandOrange,
            opacity = if (isDark) 0.08f else 0.05f
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ScreenHeader(
                    title = "Smart Kids Guard",
                    subtitle = "Multimodal AI screen protection & Ella autonomous safety agent",
                    actionIcon = Icons.Filled.AutoAwesome,
                    onActionClick = {
                        AudioEffectsManager.playEllaChime()
                        EllaOverlayManager.triggerEllaAssistant()
                    }
                )
            }

            // Status Card
            item {
                InteractiveCard(
                    backgroundColor = if (isGuardActive) MavunoTheme.colors.surfaceElevated else MavunoTheme.colors.surface,
                    elevation = 6.dp,
                    contentPadding = 20.dp
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(if (isGuardActive) BrandOrange else if (isDark) Color(0x33FFFFFF) else Color(0x33000000)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isGuardActive) Icons.Filled.Shield else Icons.Filled.PowerSettingsNew,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column {
                                    Text(
                                        text = if (isGuardActive) "Kids Guard Active" else "Kids Guard Paused",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MavunoTheme.colors.textPrimary
                                    )
                                    Text(
                                        text = if (isGuardActive) "Real-time on-screen protection enabled" else "Tap switch to protect device",
                                        fontSize = 12.sp,
                                        color = if (isGuardActive) BrandGold else MavunoTheme.colors.textMuted
                                    )
                                }
                            }

                            Switch(
                                checked = isGuardActive,
                                onCheckedChange = {
                                    AudioEffectsManager.playTap()
                                    EllaOverlayManager.toggleGuard(it)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = BrandOrange,
                                    uncheckedThumbColor = MavunoTheme.colors.textMuted,
                                    uncheckedTrackColor = MavunoTheme.colors.surfaceAlt
                                ),
                                modifier = Modifier.testTag("guard_toggle_switch")
                            )
                        }

                        if (isGuardActive) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isDark) Color(0x22FFFFFF) else Color(0x10F97316))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = BrandOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Active Profile: ${currentAgeTier.title} (${currentAgeTier.ageRange})",
                                    fontSize = 12.sp,
                                    color = MavunoTheme.colors.textPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Ella Autonomous Phone Use & Content Safety Agent Console
            item {
                InteractiveCard(
                    backgroundColor = MavunoTheme.colors.surfaceElevated,
                    borderColor = BrandOrange,
                    elevation = 6.dp,
                    contentPadding = 18.dp
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(BrandOrange),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = "Autonomous Agent",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "Ella Autonomous Mobile Agent",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MavunoTheme.colors.textPrimary
                                    )
                                    Text(
                                        text = "Model: ${EllaAutonomousAgent.MODEL_NAME} • ${EllaAutonomousAgent.ENVIRONMENT}",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BrandGold
                                    )
                                }
                            }

                            if (isAgentRunning) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = BrandOrange,
                                    strokeWidth = 2.5.dp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Executes Phone Use UI automation, MediaProjection visual intake, and age-adaptive safety policy enforcement.",
                            fontSize = 11.sp,
                            color = MavunoTheme.colors.textSecondary,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Autonomous Execution Task Presets
                        Text(
                            text = "Trigger Autonomous Agent Action:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MavunoTheme.colors.textPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        autonomousPresets.forEach { (label, prompt) ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MavunoTheme.colors.surfaceAlt)
                                    .border(1.dp, MavunoTheme.colors.cardBorder, RoundedCornerShape(12.dp))
                                    .clickable {
                                        AudioEffectsManager.playPillClick()
                                        EllaAutonomousAgent.executeAutonomousTask(prompt, currentAgeTier)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MavunoTheme.colors.textPrimary
                                        )
                                        Text(
                                            text = prompt,
                                            fontSize = 10.sp,
                                            color = MavunoTheme.colors.textMuted,
                                            maxLines = 1
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            AudioEffectsManager.playPillClick()
                                            EllaAutonomousAgent.executeAutonomousTask(prompt, currentAgeTier)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = "Run",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Dispatch", fontSize = 10.sp, color = Color.White)
                                    }
                                }
                            }
                        }

                        // Emitted JSON Output Terminal View
                        if (lastEmittedCommand != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Live Emitted Command JSON Schema:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MavunoTheme.colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0F172A))
                                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "ACTION: ${lastEmittedCommand?.action}",
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF38BDF8)
                                        )
                                        Text(
                                            text = "STATUS: ${lastEmittedCommand?.status}",
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = if (lastEmittedCommand?.status == "SUCCESS") Color(0xFF10B981) else Color(0xFFF59E0B)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = lastEmittedCommand?.toJsonString() ?: "",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFFE2E8F0),
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Home Screen & Apps Overlay Simulation Card (Clone from photo)
            item {
                InteractiveCard(
                    backgroundColor = MavunoTheme.colors.surfaceElevated,
                    borderColor = BrandOrange,
                    elevation = 4.dp,
                    contentPadding = 16.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(BrandOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = "Home Overlay",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Home Screen & App Overlay Mode",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MavunoTheme.colors.textPrimary
                            )
                            Text(
                                text = "Simulate Ella floating over phone apps & home wallpaper as shown in photo.",
                                fontSize = 11.sp,
                                color = MavunoTheme.colors.textSecondary,
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                AudioEffectsManager.playTap()
                                EllaOverlayManager.toggleHomeScreenSimulation(true)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("launch_home_simulation_button")
                        ) {
                            Text("Launch 📲", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Age Profiles Selector Matrix
            item {
                Text(
                    text = "Age-Adaptive Safety Profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MavunoTheme.colors.textPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(AgeTier.values()) { tier ->
                val isSelected = tier == currentAgeTier
                InteractiveCard(
                    backgroundColor = if (isSelected) MavunoTheme.colors.surface else MavunoTheme.colors.surfaceAlt,
                    borderColor = if (isSelected) BrandOrange else MavunoTheme.colors.cardBorder,
                    elevation = if (isSelected) 4.dp else 1.dp,
                    onClick = {
                        AudioEffectsManager.playPillClick()
                        EllaOverlayManager.setAgeTier(tier)
                    },
                    contentPadding = 16.dp,
                    modifier = Modifier.testTag("age_tier_${tier.name}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MavunoTheme.colors.primarySoft else if (isDark) Color(0x22FFFFFF) else Color(0x1F0F172A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (tier) {
                                    AgeTier.CHILD -> Icons.Filled.ChildCare
                                    AgeTier.PRE_TEEN -> Icons.Filled.Psychology
                                    AgeTier.TEEN -> Icons.Filled.Shield
                                },
                                contentDescription = null,
                                tint = if (isSelected) BrandOrange else MavunoTheme.colors.textMuted,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = tier.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BrandOrange else MavunoTheme.colors.textPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) BrandOrange else MavunoTheme.colors.textMuted)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = tier.ageRange,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tier.description,
                                fontSize = 11.sp,
                                color = MavunoTheme.colors.textSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // Multimodal Test Sandbox
            item {
                Text(
                    text = "Multimodal Guard Sandbox",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MavunoTheme.colors.textPrimary,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            item {
                InteractiveCard(
                    backgroundColor = MavunoTheme.colors.surface,
                    contentPadding = 16.dp
                ) {
                    Column {
                        Text(
                            text = "Test Screen Content Simulation",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MavunoTheme.colors.textPrimary
                        )
                        Text(
                            text = "Choose a preset or type custom text to test real-time shield activation.",
                            fontSize = 11.sp,
                            color = MavunoTheme.colors.textSecondary,
                            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                        )

                        // Preset chips
                        presetTests.forEach { (label, content) ->
                            val isChosen = selectedPreset == label
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isChosen) MavunoTheme.colors.primarySoft else MavunoTheme.colors.surfaceAlt)
                                    .border(1.dp, if (isChosen) BrandOrange else MavunoTheme.colors.cardBorder, RoundedCornerShape(12.dp))
                                    .clickable {
                                        AudioEffectsManager.playPillClick()
                                        selectedPreset = label
                                        testInputText = content
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isChosen) BrandOrange else MavunoTheme.colors.textPrimary
                                    )
                                    Text(
                                        text = "Load",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isChosen) BrandOrange else MavunoTheme.colors.textMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = testInputText,
                            onValueChange = { testInputText = it },
                            placeholder = { Text("Enter text to scan...", fontSize = 12.sp, color = MavunoTheme.colors.textMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("sandbox_text_field"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MavunoTheme.colors.surfaceAlt,
                                unfocusedContainerColor = MavunoTheme.colors.surfaceAlt,
                                focusedTextColor = MavunoTheme.colors.textPrimary,
                                unfocusedTextColor = MavunoTheme.colors.textPrimary,
                                focusedBorderColor = BrandOrange,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                AudioEffectsManager.playTap()
                                EllaOverlayManager.triggerScanSimulation(testInputText, "Guard Sandbox")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("simulate_scan_button")
                        ) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Simulate Live On-Screen Scan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Incidents Log Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Safety Incidents & History",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MavunoTheme.colors.textPrimary
                    )
                    Text(
                        text = "${incidents.size} Logged",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandOrange
                    )
                }
            }

            items(incidents) { incident ->
                InteractiveCard(
                    backgroundColor = MavunoTheme.colors.surface,
                    contentPadding = 14.dp,
                    modifier = Modifier.testTag("incident_item_${incident.id}")
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    when (incident.severity) {
                                        ThreatSeverity.CRITICAL, ThreatSeverity.HIGH -> Color(0x22EF4444)
                                        ThreatSeverity.MEDIUM -> Color(0x22F59E0B)
                                        ThreatSeverity.LOW -> Color(0x2210B981)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (incident.severity) {
                                    ThreatSeverity.CRITICAL, ThreatSeverity.HIGH -> Icons.Filled.Warning
                                    ThreatSeverity.MEDIUM -> Icons.Filled.Shield
                                    ThreatSeverity.LOW -> Icons.Filled.CheckCircle
                                },
                                contentDescription = null,
                                tint = when (incident.severity) {
                                    ThreatSeverity.CRITICAL, ThreatSeverity.HIGH -> Color(0xFFEF4444)
                                    ThreatSeverity.MEDIUM -> Color(0xFFF59E0B)
                                    ThreatSeverity.LOW -> Color(0xFF10B981)
                                },
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = incident.category,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MavunoTheme.colors.textPrimary
                                )
                                Text(
                                    text = incident.formattedTime,
                                    fontSize = 10.sp,
                                    color = MavunoTheme.colors.textMuted
                                )
                            }

                            Text(
                                text = incident.snippet,
                                fontSize = 11.sp,
                                color = MavunoTheme.colors.textSecondary,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Source: ${incident.appOrSource}",
                                    fontSize = 10.sp,
                                    color = MavunoTheme.colors.textMuted
                                )
                                Text(
                                    text = incident.actionTaken,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (incident.overriddenByParent) BrandGold else BrandOrange
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
