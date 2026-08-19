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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.data.ChurchEvent
import com.mavuno.church.data.MavunoRepository
import com.mavuno.church.ui.components.CircleIconButton
import com.mavuno.church.ui.components.InteractiveCard
import com.mavuno.church.ui.components.MandalaBackdrop
import com.mavuno.church.ui.components.ModalType
import com.mavuno.church.ui.components.OverlayModalSheet
import com.mavuno.church.ui.components.ScreenHeader
import com.mavuno.church.ui.theme.BrandGold
import com.mavuno.church.ui.theme.MavunoTheme

@Composable
fun EventsScreen() {
    var modalVisible by remember { mutableStateOf(false) }
    val isDark = MavunoTheme.colors.isDark

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MavunoTheme.colors.background)
    ) {
        MandalaBackdrop(
            color = BrandGold,
            opacity = if (isDark) 0.08f else 0.05f,
            size = 380.dp,
            modifier = Modifier.align(Alignment.TopStart)
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
                    category = "Gatherings & Fellowship",
                    title = "Upcoming Events",
                    subtitle = "Gatherings, youth nights, retreats, and prayer fellowships across Nairobi."
                )
            }

            items(MavunoRepository.events, key = { it.id }) { event ->
                InteractiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("event_item_${event.id}"),
                    contentPadding = 12.dp,
                    onClick = { modalVisible = true }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = event.imageRes),
                            contentDescription = event.title,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.eyebrow.uppercase(),
                                color = BrandGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = event.title,
                                color = MavunoTheme.colors.textPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = event.meta,
                                color = MavunoTheme.colors.textSecondary,
                                fontSize = 12.sp
                            )
                        }

                        CircleIconButton(
                            icon = Icons.Filled.CalendarMonth,
                            contentDescription = "Event Details",
                            onClick = { modalVisible = true },
                            size = 36.dp,
                            iconSize = 16.dp,
                            fillColor = MavunoTheme.colors.surfaceElevated,
                            iconColor = MavunoTheme.colors.textPrimary,
                            borderColor = MavunoTheme.colors.cardBorder
                        )
                    }
                }
            }
        }

        OverlayModalSheet(
            type = if (modalVisible) ModalType.CONTACT else null,
            onDismiss = { modalVisible = false }
        )
    }
}
