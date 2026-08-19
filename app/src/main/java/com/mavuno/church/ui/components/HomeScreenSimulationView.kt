package com.mavuno.church.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.audio.AudioEffectsManager
import com.mavuno.church.guard.AgeTier
import com.mavuno.church.guard.EllaAutonomousAgent
import com.mavuno.church.guard.EllaOverlayManager
import com.mavuno.church.ui.theme.BrandOrange

data class SimAppIcon(
    val name: String,
    val initial: String,
    val bgColor: Color,
    val textColor: Color = Color.White,
    val isUnsafeForChild: Boolean = false
)

val simulatedAppIcons = listOf(
    SimAppIcon("SecurityPlugin", "🔒", Color(0xFF0D9488)),
    SimAppIcon("Settings", "⚙️", Color(0xFF64748B)),
    SimAppIcon("SIM Toolkit", "💳", Color(0xFFEAB308)),
    SimAppIcon("Slack", "💬", Color(0xFF4A154B)),
    SimAppIcon("Snapchat", "👻", Color(0xFFFFFC00), textColor = Color.Black, isUnsafeForChild = true),
    SimAppIcon("Spotify", "🎵", Color(0xFF1DB954)),
    SimAppIcon("Themes", "🎨", Color(0xFFEC4899)),
    SimAppIcon("Threads", "🧵", Color(0xFF18181B)),
    SimAppIcon("TikTok Lite", "🎵", Color(0xFF000000), isUnsafeForChild = true),
    SimAppIcon("Truecaller", "📞", Color(0xFF0284C7)),
    SimAppIcon("Uber", "🚗", Color(0xFF000000)),
    SimAppIcon("Video Wallpaper", "🌌", Color(0xFF8B5CF6)),
    SimAppIcon("YouTube", "▶️", Color(0xFFEF4444)),
    SimAppIcon("Mahjong Vita", "🀄", Color(0xFF10B981)),
    SimAppIcon("Weather", "⛅", Color(0xFF38BDF8)),
    SimAppIcon("WhatsApp", "💬", Color(0xFF22C55E))
)

@Composable
fun HomeScreenSimulationView(
    isVisible: Boolean,
    ageTier: AgeTier,
    isEllaVisible: Boolean,
    ellaResponse: String?,
    isEllaThinking: Boolean,
    onCloseSimulation: () -> Unit
) {
    val isAgentRunning by EllaAutonomousAgent.isAgentRunning.collectAsState()
    val lastEmittedCommand by EllaAutonomousAgent.lastEmittedCommand.collectAsState()
    val activeCoordinate by EllaAutonomousAgent.activeTargetCoordinate.collectAsState()
    var showJsonTelemetry by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    // Deep comic / superhero wallpaper gradient clone matching photo
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF881337), // Dark crimson red top
                            Color(0xFF4C0519),
                            Color(0xFF1C1917)  // Deep slate bottom
                        )
                    )
                )
                .testTag("home_screen_simulation_view")
        ) {
            // Background grid & phone UI
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                // Top phone status bar simulation & Exit / Inspector Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "14:34",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Filled.Wifi,
                            contentDescription = "WiFi",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // JSON Telemetry toggle
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (showJsonTelemetry) BrandOrange else Color(0x99000000),
                            modifier = Modifier.clickable {
                                AudioEffectsManager.playTap()
                                showJsonTelemetry = !showJsonTelemetry
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Terminal,
                                    contentDescription = "JSON Telemetry",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "JSON Log",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Exit Simulator Button
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0x99000000),
                            modifier = Modifier.clickable {
                                AudioEffectsManager.playTap()
                                onCloseSimulation()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Exit",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }

                // Banner explaining overlay mode & Agent Status
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isAgentRunning) Color(0x88EA580C) else Color(0x66000000))
                        .border(1.dp, if (isAgentRunning) BrandOrange else Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = "Active Guard",
                                tint = if (isAgentRunning) Color.White else BrandOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAgentRunning) "🤖 Ella Agent Autonomous Loop Active" else "Ella Overlay Active over Phone & Apps",
                                fontSize = 11.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (lastEmittedCommand != null) {
                            Text(
                                text = "Action: ${lastEmittedCommand?.action}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD54F)
                            )
                        }
                    }
                }

                // JSON Output Live Terminal Overlay (Collapsible)
                if (showJsonTelemetry && lastEmittedCommand != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xE60A0A0A))
                            .border(1.dp, Color(0x44FFFFFF), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "GEMINI-3.7-FLASH | ENVIRONMENT_MOBILE",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                                Text(
                                    text = lastEmittedCommand?.status ?: "",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFF59E0B)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lastEmittedCommand?.toJsonString() ?: "",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFE2E8F0),
                                lineHeight = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Simulated App Icons Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(simulatedAppIcons) { app ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    AudioEffectsManager.playTap()
                                    if (app.isUnsafeForChild && ageTier == AgeTier.CHILD) {
                                        EllaAutonomousAgent.executeAutonomousTask(
                                            "Auditing unverified stream in ${app.name}",
                                            ageTier
                                        )
                                    } else {
                                        EllaOverlayManager.triggerEllaAssistant("Opened ${app.name}. Ella is monitoring content safety.")
                                    }
                                }
                                .padding(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(app.bgColor)
                                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = app.initial,
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = app.name,
                                fontSize = 10.sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Visual Coordinate Target Crosshair when Autonomous Agent Dispatches Action
            if (activeCoordinate != null) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 0.8f,
                    targetValue = 1.4f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(400, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulseScale"
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .scale(pulseScale)
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0x55F97316))
                        .border(2.dp, Color(0xFFFFD54F), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.GpsFixed,
                        contentDescription = "Target Coordinate",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Ella Floating Overlay on Top of Phone Home & Apps (Exact Photo Clone)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            ) {
                EllaAssistantCard(
                    isVisible = isEllaVisible,
                    ageTier = ageTier,
                    response = ellaResponse,
                    isThinking = isEllaThinking,
                    onAsk = { query ->
                        EllaOverlayManager.askElla(query)
                    },
                    onDismiss = {
                        EllaOverlayManager.dismissElla()
                    }
                )
            }
        }
    }
}
