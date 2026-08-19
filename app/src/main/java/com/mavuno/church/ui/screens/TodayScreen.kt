package com.mavuno.church.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.ui.components.CircleIconButton
import com.mavuno.church.ui.components.InteractiveCard
import com.mavuno.church.ui.components.MandalaBackdrop
import com.mavuno.church.ui.components.ModalType
import com.mavuno.church.ui.components.OverlayModalSheet
import com.mavuno.church.ui.components.RadialClockTickWidget
import com.mavuno.church.ui.components.RingProgress
import com.mavuno.church.ui.components.ScreenHeader
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.MavunoTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun TodayScreen() {
    var currentTime by remember { mutableStateOf(Date()) }
    var modalType by remember { mutableStateOf<ModalType?>(null) }
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

    // Days to Sunday
    val daysToSunday = remember(currentTime) {
        val cal = Calendar.getInstance().apply { time = currentTime }
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // Sunday = 1, Saturday = 7
        val diff = (Calendar.SUNDAY - dayOfWeek + 7) % 7
        if (diff == 0) 7 else diff
    }

    // Live equalizer animations
    val infiniteTransition = rememberInfiniteTransition(label = "equalizer")
    val bar1Height by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar1"
    )
    val bar2Height by infiniteTransition.animateFloat(
        initialValue = 22f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar2"
    )
    val bar3Height by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 26f,
        animationSpec = infiniteRepeatable(
            animation = tween(480, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar3"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MavunoTheme.colors.background)
    ) {
        MandalaBackdrop(
            color = BrandOrange,
            opacity = if (isDark) 0.08f else 0.05f,
            size = 420.dp,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 54.dp, bottom = 100.dp)
        ) {
            ScreenHeader(
                category = "Live Service & Countdown",
                title = "Today at Mavuno",
                subtitle = "Countdown to Sunday service, giving targets, and real-time community fellowship."
            )

            // Combined Today Widget Card
            InteractiveCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                contentPadding = 20.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Countdown tick widget
                    RadialClockTickWidget(
                        timeStr = timeStr,
                        secNum = daysToSunday.toString(),
                        size = 86.dp,
                        count = 40
                    )

                    // Giving progress ring
                    RingProgress(
                        progress = 0.82f,
                        size = 96.dp,
                        strokeWidth = 7.dp,
                        trackColor = MavunoTheme.colors.line,
                        fillColor = BrandOrange,
                        label = "82%",
                        sublabel = "August Goal",
                        labelColor = MavunoTheme.colors.textPrimary
                    )

                    // Live pulse equalizer
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "LIVE",
                            color = BrandOrange,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(5.dp)
                                    .height(bar1Height.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(BrandOrange)
                            )
                            Box(
                                modifier = Modifier
                                    .width(5.dp)
                                    .height(bar2Height.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(BrandOrange)
                            )
                            Box(
                                modifier = Modifier
                                    .width(5.dp)
                                    .height(bar3Height.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(BrandOrange)
                            )
                        }
                    }
                }
            }

            // Quick actions trio
            Text(
                text = "QUICK ACTIONS & CONTACT",
                color = MavunoTheme.colors.textMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                CircleIconButton(
                    icon = Icons.Filled.Call,
                    contentDescription = "Contact Desk",
                    onClick = { modalType = ModalType.CONTACT },
                    size = 48.dp,
                    iconSize = 20.dp,
                    fillColor = MavunoTheme.colors.surfaceElevated,
                    iconColor = MavunoTheme.colors.textPrimary,
                    borderColor = MavunoTheme.colors.cardBorder,
                    testTag = "today_call_button"
                )

                CircleIconButton(
                    icon = Icons.Filled.ChatBubble,
                    contentDescription = "Prayer Chat",
                    onClick = { modalType = ModalType.CHAT },
                    size = 48.dp,
                    iconSize = 20.dp,
                    fillColor = MavunoTheme.colors.surfaceElevated,
                    iconColor = MavunoTheme.colors.textPrimary,
                    borderColor = MavunoTheme.colors.cardBorder,
                    testTag = "today_chat_button"
                )

                CircleIconButton(
                    icon = Icons.Filled.Favorite,
                    contentDescription = "Give Tithe",
                    onClick = { modalType = ModalType.GIVE },
                    size = 48.dp,
                    iconSize = 20.dp,
                    fillColor = BrandOrange,
                    iconColor = Color.White,
                    borderColor = Color.Transparent,
                    testTag = "today_give_button"
                )
            }

            // Info Card
            InteractiveCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 18.dp,
                backgroundColor = MavunoTheme.colors.primarySoft
            ) {
                Column {
                    Text(
                        text = "Sunday Gathering Countdown",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandOrange
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Only $daysToSunday day${if (daysToSunday > 1) "s" else ""} until our next live Sunday service at Hill City Campus. Tap the phone or chat icon to request prayer or ask questions.",
                        fontSize = 13.sp,
                        color = MavunoTheme.colors.textSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        OverlayModalSheet(
            type = modalType,
            onDismiss = { modalType = null }
        )
    }
}
