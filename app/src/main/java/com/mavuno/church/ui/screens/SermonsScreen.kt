package com.mavuno.church.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.data.MavunoRepository
import com.mavuno.church.data.Sermon
import com.mavuno.church.ui.components.InteractiveCard
import com.mavuno.church.ui.components.MandalaBackdrop
import com.mavuno.church.ui.components.ModalType
import com.mavuno.church.ui.components.OverlayModalSheet
import com.mavuno.church.ui.components.ScreenHeader
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.MavunoTheme

@Composable
fun SermonsScreen() {
    var selectedSermon by remember { mutableStateOf<Sermon?>(null) }
    var modalVisible by remember { mutableStateOf(false) }
    val isDark = MavunoTheme.colors.isDark

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MavunoTheme.colors.background)
    ) {
        MandalaBackdrop(
            color = BrandOrange,
            opacity = if (isDark) 0.08f else 0.05f,
            size = 400.dp,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 54.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ScreenHeader(
                    category = "Teaching & Word",
                    title = "Sermons & Media",
                    subtitle = "Catch up on messages, live streams, and teaching series from Mavuno pastors."
                )
            }

            items(MavunoRepository.sermons, key = { it.id }) { sermon ->
                InteractiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sermon_item_${sermon.id}"),
                    contentPadding = 12.dp,
                    onClick = {
                        selectedSermon = sermon
                        modalVisible = true
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbnail
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            Image(
                                painter = painterResource(id = sermon.imageRes),
                                contentDescription = sermon.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0x4D0F172A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // Info
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sermon.category.uppercase(),
                                color = BrandOrange,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = sermon.title,
                                color = MavunoTheme.colors.textPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${sermon.speaker} · ${sermon.duration}",
                                color = MavunoTheme.colors.textSecondary,
                                fontSize = 12.sp
                            )
                        }

                        IconButton(
                            onClick = {
                                selectedSermon = sermon
                                modalVisible = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PlayCircleOutline,
                                contentDescription = "Play Sermon",
                                tint = BrandOrange,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }

        OverlayModalSheet(
            type = if (modalVisible) ModalType.VIDEO else null,
            onDismiss = {
                modalVisible = false
                selectedSermon = null
            },
            selectedSermon = selectedSermon
        )
    }
}
