package com.mavuno.church.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.audio.AudioEffectsManager
import com.mavuno.church.data.MavunoRepository
import com.mavuno.church.data.MessageSender
import com.mavuno.church.data.Sermon
import com.mavuno.church.ui.theme.BrandGold
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.MavunoTheme

enum class ModalType {
    CONTACT,
    CHAT,
    VIDEO,
    GIVE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlayModalSheet(
    type: ModalType?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    selectedSermon: Sermon? = null
) {
    if (type == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val isDark = MavunoTheme.colors.isDark

    LaunchedEffect(type) {
        if (type != null) {
            AudioEffectsManager.playTap()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MavunoTheme.colors.surface,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Modal Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val headerIcon: ImageVector = when (type) {
                        ModalType.CONTACT -> Icons.Filled.Call
                        ModalType.CHAT -> Icons.Filled.QuestionAnswer
                        ModalType.VIDEO -> Icons.Filled.PlayArrow
                        ModalType.GIVE -> Icons.Filled.Favorite
                    }
                    val headerTitle: String = when (type) {
                        ModalType.CONTACT -> "Contact Mavuno Campus"
                        ModalType.CHAT -> "Live Support & Community Chat"
                        ModalType.VIDEO -> "Latest Sermon — Watch Live"
                        ModalType.GIVE -> "Mavuno Giving & Tithes"
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BrandOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = headerIcon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = headerTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MavunoTheme.colors.textPrimary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .background(MavunoTheme.colors.surfaceAlt, CircleShape)
                        .testTag("modal_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MavunoTheme.colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MavunoTheme.colors.line)
            )

            when (type) {
                ModalType.CONTACT -> ContactPanel(context = context)
                ModalType.CHAT -> ChatPanel()
                ModalType.VIDEO -> VideoPanel(sermon = selectedSermon ?: MavunoRepository.sermons.first())
                ModalType.GIVE -> GivingPanel(context = context)
            }
        }
    }
}

@Composable
private fun ContactPanel(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        Text(
            text = "Get in touch with Mavuno Church Nairobi Campus",
            color = MavunoTheme.colors.textSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        // Desk
        ContactItemRow(
            icon = Icons.Filled.Call,
            iconTint = BrandOrange,
            title = "Campus Main Desk",
            detail = "+254 700 000 000",
            meta = "Mon–Fri · 8:00 AM – 5:00 PM",
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+254700000000"))
                context.startActivity(intent)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Prayer line
        ContactItemRow(
            icon = Icons.Filled.Handshake,
            iconTint = BrandGold,
            title = "24/7 Prayer Line",
            detail = "prayer@mavunochurch.org",
            meta = "Confidential Pastoral Care",
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:prayer@mavunochurch.org")
                    putExtra(Intent.EXTRA_SUBJECT, "Mavuno Church Prayer Request")
                }
                context.startActivity(Intent.createChooser(intent, "Send Prayer Request"))
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Location
        ContactItemRow(
            icon = Icons.Filled.LocationOn,
            iconTint = BrandOrange,
            title = "Nairobi Campus Location",
            detail = "Hill City Campus, Bellevue, South C",
            meta = "Sunday Services: 9:00 AM & 11:30 AM",
            onClick = {
                val gmmIntentUri = Uri.parse("geo:0,0?q=Mavuno+Church+Hill+City+Campus+Nairobi")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                context.startActivity(mapIntent)
            }
        )
    }
}

@Composable
private fun ContactItemRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    detail: String,
    meta: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MavunoTheme.colors.surfaceAlt)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MavunoTheme.colors.textPrimary)
            Text(text = detail, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BrandOrange)
            Text(text = meta, fontSize = 11.sp, color = MavunoTheme.colors.textMuted)
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = MavunoTheme.colors.textMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ChatPanel() {
    val messages by MavunoRepository.messages.collectAsState()
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(top = 12.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isUser = msg.sender == MessageSender.USER
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isUser) 16.dp else 4.dp,
                                    bottomEnd = if (isUser) 4.dp else 16.dp
                                )
                            )
                            .background(if (isUser) BrandOrange else MavunoTheme.colors.surfaceAlt)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = msg.text,
                            color = if (isUser) Color.White else MavunoTheme.colors.textPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                    Text(
                        text = msg.time,
                        color = MavunoTheme.colors.textMuted,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Input row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Ask a question or request prayer…", fontSize = 13.sp, color = MavunoTheme.colors.textMuted) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(24.dp),
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

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        AudioEffectsManager.playTap()
                        MavunoRepository.sendMessage(input)
                        input = ""
                    }
                },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(BrandOrange)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun VideoPanel(sermon: Sermon) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        // Simulated Video Player container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MavunoTheme.colors.surfaceElevated)
                .clickable {
                    isPlaying = !isPlaying
                    Toast.makeText(
                        context,
                        if (isPlaying) "Playing '${sermon.title}' stream..." else "Paused stream",
                        Toast.LENGTH_SHORT
                    ).show()
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.PlayArrow else Icons.Filled.PlayCircleFilled,
                    contentDescription = "Play",
                    tint = BrandOrange,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = sermon.title,
                    color = MavunoTheme.colors.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "${sermon.speaker} · ${if (isPlaying) "Streaming Live" else "Tap to Play"}",
                    color = MavunoTheme.colors.textMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Sermon Highlights & Scripture",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MavunoTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = sermon.description,
            fontSize = 13.sp,
            color = MavunoTheme.colors.textSecondary,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun GivingPanel(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        Text(
            text = "Support the work of Mavuno Church via M-Pesa or Online",
            color = MavunoTheme.colors.textSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        // M-Pesa Paybill
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MavunoTheme.colors.surfaceAlt)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = BrandOrange,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "M-Pesa Paybill", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MavunoTheme.colors.textPrimary)
                Text(text = "Business No: 508000", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandOrange)
                Text(text = "Account: TITHE / OFFERING / MISSIONS", fontSize = 11.sp, color = MavunoTheme.colors.textMuted)
            }
            IconButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Paybill", "508000")
                    clipboard.setPrimaryClip(clip)
                    AudioEffectsManager.playSuccessChime()
                    Toast.makeText(context, "Paybill 508000 copied!", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Paybill",
                    tint = BrandOrange,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Card Giving
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MavunoTheme.colors.surfaceAlt)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Handshake,
                contentDescription = null,
                tint = BrandGold,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Card / Online Giving", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MavunoTheme.colors.textPrimary)
                Text(text = "Secure Visa & Mastercard", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BrandGold)
                Text(text = "Instant digital receipt and confirmation", fontSize = 11.sp, color = MavunoTheme.colors.textMuted)
            }
        }
    }
}
