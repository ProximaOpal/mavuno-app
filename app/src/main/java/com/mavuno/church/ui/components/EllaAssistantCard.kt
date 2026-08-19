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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.audio.AudioEffectsManager
import com.mavuno.church.guard.AgeTier
import com.mavuno.church.guard.EllaOverlayManager
import com.mavuno.church.ui.theme.BrandGold
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.MavunoTheme

data class EllaSuggestionPill(
    val iconEmoji: String,
    val text: String
)

val defaultEllaPhotoPills = listOf(
    EllaSuggestionPill("🪄", "Boost my Grade 8 child's English writing."),
    EllaSuggestionPill("📰", "Nameless Celebrates 25 Years of Kenyan Music 🎬"),
    EllaSuggestionPill("🖼️", "Convert invoice images")
)

@Composable
fun EllaAssistantCard(
    isVisible: Boolean,
    ageTier: AgeTier,
    response: String?,
    isThinking: Boolean,
    onAsk: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isInputMode by remember { mutableStateOf(false) }
    var queryText by remember { mutableStateOf("") }
    var isMenuOpen by remember { mutableStateOf(false) }
    var isTierDropdownOpen by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            AudioEffectsManager.playEllaChime()
        }
    }

    // Pulsing animation for listening wave
    val infiniteTransition = rememberInfiniteTransition(label = "listening_pulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // 1. Floating Suggestion Pill Stack (Cloned exactly as in photo)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                defaultEllaPhotoPills.forEach { pill ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shadowElevation = 6.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x22000000)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = BrandOrange),
                                onClick = {
                                    AudioEffectsManager.playPillClick()
                                    onAsk("${pill.iconEmoji} ${pill.text}")
                                }
                            )
                            .testTag("ella_photo_pill_${pill.text.take(8)}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pill.iconEmoji,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = pill.text,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF18181B),
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }
            }

            // 2. Main Ella Bottom Drawer (Exact Clone from photo)
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White,
                shadowElevation = 24.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x1F000000)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ella_bottom_drawer")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .padding(top = 12.dp, bottom = 16.dp)
                        .navigationBarsPadding()
                ) {
                    // Header Bar with Menu, "Ella ▾" badge, and AI Star Orbit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 3-Bar Hamburger Menu Icon
                        Box {
                            IconButton(
                                onClick = { isMenuOpen = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("ella_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Ella Menu",
                                    tint = Color(0xFF27272A),
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = isMenuOpen,
                                onDismissRequest = { isMenuOpen = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Simulate Home Screen Overlay", fontSize = 13.sp) },
                                    onClick = {
                                        isMenuOpen = false
                                        EllaOverlayManager.toggleHomeScreenSimulation(true)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Profile: ${ageTier.title}", fontSize = 13.sp) },
                                    onClick = {
                                        isMenuOpen = false
                                        isTierDropdownOpen = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Close Assistant", fontSize = 13.sp) },
                                    onClick = {
                                        isMenuOpen = false
                                        onDismiss()
                                    }
                                )
                            }
                        }

                        // Center "Ella ▾" Dropdown Capsule
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .clickable { isTierDropdownOpen = true }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                                    .testTag("ella_profile_badge"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ella",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = isTierDropdownOpen,
                                onDismissRequest = { isTierDropdownOpen = false }
                            ) {
                                AgeTier.values().forEach { tier ->
                                    DropdownMenuItem(
                                        text = { Text("${tier.title} (${tier.ageRange})", fontSize = 13.sp) },
                                        onClick = {
                                            isTierDropdownOpen = false
                                            EllaOverlayManager.setAgeTier(tier)
                                        }
                                    )
                                }
                            }
                        }

                        // Right Sparkle / AI Star Orbit Icon
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { isInputMode = !isInputMode }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = "AI Sparkle",
                                tint = Color(0xFF3B82F6),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Response or Thinking Card (if active)
                    if (isThinking) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = BrandOrange,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Ella is analyzing on-screen context… ✨",
                                    fontSize = 12.sp,
                                    color = Color(0xFF475569)
                                )
                            }
                        }
                    } else if (!response.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFF7ED))
                                .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ella Response · ${ageTier.title}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandOrange
                                    )
                                    IconButton(
                                        onClick = { EllaOverlayManager.dismissElla() },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Close",
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = response,
                                    fontSize = 12.5.sp,
                                    color = Color(0xFF1F2937),
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }

                    // 3. Listening Bar Capsule (Exact Clone: "I'm listening" with Keypad + Plus icon)
                    if (!isInputMode) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFFF4F4F5))
                                .border(1.dp, Color(0xFFE4E4E7), RoundedCornerShape(26.dp))
                                .clickable { isInputMode = true }
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: "I'm listening" text in soft purple / violet
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFC026D3).copy(alpha = dotAlpha))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "I'm listening",
                                    color = Color(0xFFC026D3),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily.SansSerif,
                                    letterSpacing = 0.3.sp
                                )
                            }

                            // Right: Keypad grid icon & Plus icon
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Dialpad,
                                    contentDescription = "Keyboard Input",
                                    tint = Color(0xFF52525B),
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { isInputMode = true }
                                        .testTag("ella_keypad_icon")
                                )

                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Content / File",
                                    tint = Color(0xFF52525B),
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickable {
                                            onAsk("Convert invoice images")
                                        }
                                        .testTag("ella_plus_icon")
                                )
                            }
                        }
                    } else {
                        // Active Keyboard Query Input Box
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = queryText,
                                onValueChange = { queryText = it },
                                placeholder = { Text("Ask Ella anything…", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("ella_text_input_field"),
                                shape = RoundedCornerShape(22.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF4F4F5),
                                    unfocusedContainerColor = Color(0xFFF4F4F5),
                                    focusedTextColor = Color(0xFF0F172A),
                                    unfocusedTextColor = Color(0xFF0F172A),
                                    focusedBorderColor = BrandOrange,
                                    unfocusedBorderColor = Color(0xFFE4E4E7)
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {
                                    if (queryText.isNotBlank()) {
                                        onAsk(queryText)
                                        queryText = ""
                                        isInputMode = false
                                    } else {
                                        isInputMode = false
                                    }
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(BrandOrange)
                                    .testTag("ella_send_query_button")
                            ) {
                                Icon(
                                    imageVector = if (queryText.isNotBlank()) Icons.Filled.Send else Icons.Filled.Mic,
                                    contentDescription = "Send",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
