package com.mavuno.church.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.ui.theme.BrandOrange
import com.mavuno.church.ui.theme.MavunoTheme

@Composable
fun AccentStat(
    value: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    caption: String? = null,
    accentColor: Color = BrandOrange,
    isBig: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.5.dp)
                .height(if (caption != null) 58.dp else 42.dp)
                .background(accentColor, shape = RoundedCornerShape(2.dp))
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            if (!eyebrow.isNullOrEmpty()) {
                Text(
                    text = eyebrow.uppercase(),
                    color = MavunoTheme.colors.textMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            Text(
                text = value,
                color = MavunoTheme.colors.textPrimary,
                fontSize = if (isBig) 20.sp else 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = if (isBig) 24.sp else 20.sp
            )
            if (!caption.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = caption,
                    color = MavunoTheme.colors.textSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
