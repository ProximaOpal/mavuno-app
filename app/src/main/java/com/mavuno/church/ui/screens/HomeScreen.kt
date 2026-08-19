package com.mavuno.church.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.R
import com.mavuno.church.data.MavunoRepository
import com.mavuno.church.data.Sermon
import com.mavuno.church.ui.components.AccentStat
import com.mavuno.church.ui.components.CircleIconButton
import com.mavuno.church.ui.components.IconRail
import com.mavuno.church.ui.components.InteractiveCard
import com.mavuno.church.ui.components.MandalaBackdrop
import com.mavuno.church.ui.components.MavunoMark
import com.mavuno.church.ui.components.ModalType
import com.mavuno.church.ui.components.OverlayModalSheet
import com.mavuno.church.ui.components.RadialClockTickWidget
import com.mavuno.church.ui.theme.BrandGold
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.BrandOrangeDeep
import com.mavuno.church.ui.theme.MavunoTheme
import com.mavuno.church.ui.theme.ThemeStateManager
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToTab: (String) -> Unit
) {
    var currentTime by remember { mutableStateOf(Date()) }
    var modalType by remember { mutableStateOf<ModalType?>(null) }
    var activeSermon by remember { mutableStateOf<Sermon?>(null) }
    val isDark = MavunoTheme.colors.isDark

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }

    val timeStr = remember(currentTime) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime)
    }
    val secNum = remember(currentTime) {
        SimpleDateFormat("ss", Locale.getDefault()).format(currentTime)
    }
    val dateStr = remember(currentTime) {
        SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(currentTime)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MavunoTheme.colors.background)
    ) {
        // Ambient background mandala
        MandalaBackdrop(
            color = BrandOrange,
            opacity = if (isDark) 0.08f else 0.05f,
            size = 460.dp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-80).dp)
        )

        Row(modifier = Modifier.fillMaxSize()) {
            // Main content area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 20.dp, end = 12.dp, top = 48.dp, bottom = 90.dp)
            ) {
                // Header Bar with Quick Theme Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MavunoMark(
                            size = 32.dp,
                            color = BrandOrange,
                            animate = false
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "MAVUNO CHURCH",
                            color = MavunoTheme.colors.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quick Mode Switcher (White Light / Black Dark)
                        IconButton(
                            onClick = { ThemeStateManager.toggleLightDark() },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MavunoTheme.colors.surfaceAlt)
                                .testTag("theme_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = "Toggle Mode",
                                tint = BrandOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(MavunoTheme.colors.primarySoft)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "NAIROBI CAMPUS",
                                color = BrandOrangeDeep,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }

                // Radial Clock & Circular Button Trio Widget
                InteractiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    contentPadding = 16.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Clock tick ring widget
                        RadialClockTickWidget(
                            timeStr = timeStr,
                            secNum = secNum,
                            size = 90.dp,
                            count = 40
                        )

                        // Circular Button Widgets Trio (Call, Chat, Give)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircleIconButton(
                                icon = Icons.Filled.Call,
                                contentDescription = "Call Campus Desk",
                                onClick = { modalType = ModalType.CONTACT },
                                size = 42.dp,
                                iconSize = 18.dp,
                                fillColor = MavunoTheme.colors.surfaceElevated,
                                iconColor = MavunoTheme.colors.textPrimary,
                                borderColor = MavunoTheme.colors.cardBorder,
                                testTag = "home_call_button"
                            )

                            CircleIconButton(
                                icon = Icons.Filled.ChatBubble,
                                contentDescription = "Community Chat",
                                onClick = { modalType = ModalType.CHAT },
                                size = 42.dp,
                                iconSize = 18.dp,
                                fillColor = MavunoTheme.colors.surfaceElevated,
                                iconColor = MavunoTheme.colors.textPrimary,
                                borderColor = MavunoTheme.colors.cardBorder,
                                testTag = "home_chat_button"
                            )

                            CircleIconButton(
                                icon = Icons.Filled.Favorite,
                                contentDescription = "Give Tithe / Offering",
                                onClick = { modalType = ModalType.GIVE },
                                size = 42.dp,
                                iconSize = 18.dp,
                                fillColor = BrandOrange,
                                iconColor = Color.White,
                                borderColor = Color.Transparent,
                                testTag = "home_give_button"
                            )
                        }
                    }
                }

                // Sermon Media Card with Photo & Video Trigger
                val latestSermon = MavunoRepository.sermons.first()
                InteractiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    contentPadding = 0.dp,
                    onClick = {
                        activeSermon = latestSermon
                        modalType = ModalType.VIDEO
                    }
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Image(
                                painter = painterResource(id = latestSermon.imageRes),
                                contentDescription = latestSermon.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Overlay gradient
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(if (isDark) Color(0x66000000) else Color(0x3D0F172A))
                            )
                            // Play Badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(12.dp)
                                    .size(40.dp)
                                    .shadow(elevation = 4.dp, shape = CircleShape)
                                    .clip(CircleShape)
                                    .background(BrandOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Play Sermon",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "LATEST SERMON · WATCH & LISTEN",
                                color = BrandOrange,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = latestSermon.title,
                                color = MavunoTheme.colors.textPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${latestSermon.speaker} · Sunday Main Service",
                                color = MavunoTheme.colors.textSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Stats & Word for Today
                AccentStat(
                    eyebrow = dateStr,
                    value = timeStr,
                    caption = "You're right on time for prayer and fellowship.",
                    accentColor = BrandOrange,
                    isBig = true
                )

                AccentStat(
                    eyebrow = "Word for Today",
                    value = "“Blessed are the\npeacemakers.”",
                    caption = "Matthew 5:9 — reflect on this before Sunday service.",
                    accentColor = BrandGold,
                    isBig = false
                )

                AccentStat(
                    eyebrow = "Next Service",
                    value = "Sun · 9:00 AM",
                    caption = "Second Service · Main Auditorium · Doors open 8:30",
                    accentColor = BrandOrange,
                    isBig = false
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Footer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    MavunoMark(size = 20.dp, color = MavunoTheme.colors.textMuted, animate = false)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mavuno Church · Nairobi Campus",
                        color = MavunoTheme.colors.textMuted,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Right Icon Rail
            IconRail(
                onSelect = { key ->
                    when (key) {
                        "live" -> onNavigateToTab("Today")
                        "guard" -> onNavigateToTab("Guard")
                        "web" -> onNavigateToTab("Web")
                        "sermons" -> onNavigateToTab("Sermons")
                        "events" -> onNavigateToTab("Events")
                        "give" -> modalType = ModalType.GIVE
                        "prayer" -> modalType = ModalType.CONTACT
                        "chat" -> modalType = ModalType.CHAT
                        "contact" -> modalType = ModalType.CONTACT
                    }
                },
                modifier = Modifier.padding(top = 40.dp, bottom = 80.dp)
            )
        }

        // Overlay Modal Sheet
        OverlayModalSheet(
            type = modalType,
            onDismiss = { modalType = null },
            selectedSermon = activeSermon
        )
    }
}
