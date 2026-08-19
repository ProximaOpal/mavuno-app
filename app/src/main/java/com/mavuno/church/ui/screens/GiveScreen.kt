package com.mavuno.church.ui.screens

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.mavuno.church.data.MavunoRepository
import com.mavuno.church.ui.components.CircleIconButton
import com.mavuno.church.ui.components.InteractiveCard
import com.mavuno.church.ui.components.ModalType
import com.mavuno.church.ui.components.OverlayModalSheet
import com.mavuno.church.ui.components.RingProgress
import com.mavuno.church.ui.components.ScreenHeader
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.BrandOrangeDeep
import com.mavuno.church.ui.theme.MavunoTheme

@Composable
fun GiveScreen() {
    var modalVisible by remember { mutableStateOf(false) }

    val heroGradient = Brush.verticalGradient(
        colors = listOf(BrandOrange, BrandOrangeDeep)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MavunoTheme.colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) {
            // Hero Header with Orange Gradient
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                        .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                        .background(heroGradient)
                        .padding(top = 54.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        RingProgress(
                            progress = 0.82f,
                            size = 120.dp,
                            strokeWidth = 8.dp,
                            trackColor = Color(0x47FFFFFF),
                            fillColor = Color.White,
                            label = "82%",
                            sublabel = "OF AUGUST GOAL",
                            labelColor = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "KES 1,230,000 of KES 1,500,000 raised this month",
                            color = Color.White.copy(alpha = 0.95f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Screen Header
            item {
                ScreenHeader(
                    category = "Generosity & Impact",
                    title = "Give & Partner",
                    subtitle = "Every seed sown here builds lives, strengthens families, and expands community.",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
                )
            }

            // Giving Option Cards
            items(MavunoRepository.givingOptions, key = { it.id }) { option ->
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)) {
                    InteractiveCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("give_option_${option.id}"),
                        contentPadding = 16.dp,
                        onClick = { modalVisible = true }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircleIconButton(
                                icon = Icons.Filled.Favorite,
                                contentDescription = option.title,
                                onClick = { modalVisible = true },
                                size = 42.dp,
                                iconSize = 18.dp,
                                fillColor = BrandOrange,
                                iconColor = Color.White,
                                borderColor = Color.Transparent
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = option.eyebrow.uppercase(),
                                    color = BrandOrange,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = option.title,
                                    color = MavunoTheme.colors.textPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = option.meta,
                                    color = MavunoTheme.colors.textSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        OverlayModalSheet(
            type = if (modalVisible) ModalType.GIVE else null,
            onDismiss = { modalVisible = false }
        )
    }
}
