package com.mavuno.church.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.audio.AudioEffectsManager
import com.mavuno.church.guard.AgeTier
import com.mavuno.church.guard.EllaOverlayManager
import com.mavuno.church.guard.ScanResult
import com.mavuno.church.ui.theme.BrandGold
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.BrandOrangeDeep
import com.mavuno.church.ui.theme.MavunoTheme

@Composable
fun GlassmorphicShieldOverlay(
    isShieldActive: Boolean,
    scanResult: ScanResult?,
    ageTier: AgeTier,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPinPad by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    val isDark = MavunoTheme.colors.isDark

    LaunchedEffect(isShieldActive) {
        if (isShieldActive) {
            AudioEffectsManager.playShieldAlert()
        }
    }

    val overlayBackdrop = if (isDark) Color(0xF2000000) else Color(0xEBFFFFFF)
    val cardBg = if (isDark) Color(0xFF111114) else Color(0xFFFFFFFF)
    val noticeBg = if (isDark) Color(0xFF1C1C20) else Color(0xFFF1F5F9)
    val noticeBorder = if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val textMuted = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    AnimatedVisibility(
        visible = isShieldActive,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayBackdrop)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = cardBg,
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    Brush.verticalGradient(listOf(BrandOrange.copy(alpha = 0.5f), MavunoTheme.colors.cardBorder))
                ),
                shadowElevation = 24.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("glassmorphic_shield_card")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Shield Icon with glowing ring
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(16.dp, CircleShape, spotColor = BrandOrange)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(BrandOrange, BrandOrangeDeep)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = "Shield Active",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Content Protected by Ella",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Text(
                        text = "Active Protection: ${ageTier.title}",
                        fontSize = 12.sp,
                        color = BrandGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Notice Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(noticeBg)
                            .border(1.dp, noticeBorder, RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Category: ${scanResult?.triggeredCategory ?: "Restricted Content"}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandOrange
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = scanResult?.ellaExplanation ?: "This page contains material filtered for your age profile.",
                                fontSize = 12.sp,
                                color = textMuted,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (!showPinPad) {
                        // Actions
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("shield_dismiss_button")
                        ) {
                            Text("Return to Safe Screen", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { showPinPad = true },
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) Color(0x55FFFFFF) else Color(0x330F172A)),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("shield_parent_unlock_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = textColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Parent Unlock (PIN)", color = textColor, fontSize = 13.sp)
                        }
                    } else {
                        // PIN keypad view
                        Text(
                            text = "Enter Parent Passcode",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = if (pinError) "Incorrect PIN! Try default (1234)" else "Default passcode is 1234",
                            fontSize = 11.sp,
                            color = if (pinError) Color(0xFFEF4444) else textMuted,
                            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                        )

                        // 4 PIN dots
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            for (i in 0 until 4) {
                                val filled = i < enteredPin.length
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(if (filled) BrandOrange else if (isDark) Color(0x44FFFFFF) else Color(0x220F172A))
                                        .border(1.dp, if (isDark) Color(0x66FFFFFF) else Color(0x330F172A), CircleShape)
                                )
                            }
                        }

                        // Keypad grid
                        val keys = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("Cancel", "0", "Del")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            keys.forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    row.forEach { key ->
                                        Box(
                                            modifier = Modifier
                                                .size(width = 72.dp, height = 44.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isDark) Color(0x22FFFFFF) else Color(0xFFF1F5F9))
                                                .border(1.dp, if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A), RoundedCornerShape(12.dp))
                                                .clickable {
                                                    when (key) {
                                                        "Cancel" -> {
                                                             showPinPad = false
                                                            enteredPin = ""
                                                            pinError = false
                                                        }
                                                        "Del" -> {
                                                            if (enteredPin.isNotEmpty()) {
                                                                enteredPin = enteredPin.dropLast(1)
                                                                pinError = false
                                                            }
                                                        }
                                                        else -> {
                                                            if (enteredPin.length < 4) {
                                                                val next = enteredPin + key
                                                                enteredPin = next
                                                                if (next.length == 4) {
                                                                    val success = EllaOverlayManager.unlockShieldWithPin(next)
                                                                    if (success) {
                                                                        AudioEffectsManager.playSuccessChime()
                                                                        showPinPad = false
                                                                        enteredPin = ""
                                                                        pinError = false
                                                                    } else {
                                                                        pinError = true
                                                                        enteredPin = ""
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (key == "Del") {
                                                Icon(
                                                    imageVector = Icons.Filled.Backspace,
                                                    contentDescription = "Delete",
                                                    tint = textColor,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            } else {
                                                Text(
                                                    text = key,
                                                    fontSize = if (key == "Cancel") 12.sp else 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = textColor
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
