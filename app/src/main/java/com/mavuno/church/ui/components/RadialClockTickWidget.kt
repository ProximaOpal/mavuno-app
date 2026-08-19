package com.mavuno.church.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mavuno.church.ui.theme.MavunoTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadialClockTickWidget(
    timeStr: String,
    secNum: String,
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    count: Int = 40,
    tickColor: Color = MavunoTheme.colors.line
) {
    val textColor = MavunoTheme.colors.textPrimary

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h / 2f
            val r = w / 2f

            for (i in 0 until count) {
                val angleRad = Math.toRadians(i * (360.0 / count))
                val rOuter = r - 2f
                val rInner = r - 12f

                val x1 = cx + cos(angleRad).toFloat() * rOuter
                val y1 = cy + sin(angleRad).toFloat() * rOuter
                val x2 = cx + cos(angleRad).toFloat() * rInner
                val y2 = cy + sin(angleRad).toFloat() * rInner

                drawLine(
                    color = tickColor,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 2f
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .border(width = 1.dp, color = textColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = secNum,
                    color = textColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = timeStr,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
