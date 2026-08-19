package com.mavuno.church.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MandalaBackdrop(
    modifier: Modifier = Modifier,
    size: Dp = 480.dp,
    color: Color = Color(0xFFF5821F),
    opacity: Float = 0.06f,
    ticks: Int = 48
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = w / 2f

        val drawColor = color.copy(alpha = opacity)

        // Concentric circles
        val fractions = listOf(0.98f, 0.80f, 0.60f, 0.40f, 0.20f)
        fractions.forEachIndexed { i, f ->
            drawCircle(
                color = drawColor,
                radius = radius * f,
                center = Offset(cx, cy),
                style = Stroke(width = if (i == 0) 1.5f else 1f)
            )
        }

        // Radial ticks
        for (i in 0 until ticks) {
            val angleRad = Math.toRadians(i * (360.0 / ticks))
            val rOuter = radius * 0.98f
            val rInner = radius * if (i % 4 == 0) 0.88f else 0.93f

            val x1 = cx + cos(angleRad).toFloat() * rOuter
            val y1 = cy + sin(angleRad).toFloat() * rOuter
            val x2 = cx + cos(angleRad).toFloat() * rInner
            val y2 = cy + sin(angleRad).toFloat() * rInner

            drawLine(
                color = drawColor,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = if (i % 4 == 0) 1.5f else 1f
            )
        }
    }
}
