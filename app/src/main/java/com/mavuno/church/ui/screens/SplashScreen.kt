package com.mavuno.church.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.ui.components.MandalaBackdrop
import com.mavuno.church.ui.components.MavunoMark
import com.mavuno.church.ui.components.RingProgress
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.BrandOrangeDeep
import com.mavuno.church.ui.theme.MavunoTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SplashScreen(
    onNavigateToMain: () -> Unit
) {
    val progress = remember { Animatable(0.08f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2200, easing = LinearEasing)
        )
        kotlinx.coroutines.delay(400)
        onNavigateToMain()
    }

    val todayFormatted = remember {
        SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date()).uppercase()
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(BrandOrange, BrandOrangeDeep)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        // Ambient background mandala
        MandalaBackdrop(
            color = Color.White,
            opacity = 0.08f,
            size = 540.dp,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Center Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(top = 70.dp, bottom = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MavunoMark(
                size = 110.dp,
                color = Color.White,
                animate = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "MAVUNO",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp
            )
            Text(
                text = "CHURCH",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "A people. A purpose. A place to belong.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(34.dp))

            // Progress Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RingProgress(
                    progress = progress.value,
                    size = 64.dp,
                    strokeWidth = 4.dp,
                    trackColor = Color(0x33FFFFFF),
                    fillColor = Color.White,
                    label = "${(progress.value * 100).toInt()}%",
                    labelColor = Color.White
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = todayFormatted,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Preparing your space…",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Bottom curved footer
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp))
                .clip(RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp))
                .background(MavunoTheme.colors.surface)
                .padding(top = 20.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    // Small circular preview buttons
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MavunoTheme.colors.surfaceAlt)
                            .clickable(onClick = onNavigateToMain),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ChatBubbleOutline,
                            contentDescription = "Chat",
                            tint = MavunoTheme.colors.textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MavunoTheme.colors.surfaceAlt)
                            .clickable(onClick = onNavigateToMain),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Call",
                            tint = MavunoTheme.colors.textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Main Enter Play Button
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .shadow(elevation = 6.dp, shape = CircleShape, spotColor = BrandOrangeDeep)
                            .clip(CircleShape)
                            .background(BrandOrange)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = Color.White),
                                onClick = onNavigateToMain
                            )
                            .testTag("splash_enter_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Enter",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Tap play to enter",
                    color = MavunoTheme.colors.textMuted,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.clickable(onClick = onNavigateToMain)
                )
            }
        }
    }
}
