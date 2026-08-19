package com.mavuno.church.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MavunoMark(
    modifier: Modifier = Modifier,
    size: Dp = 104.dp,
    color: Color = Color.White,
    animate: Boolean = true,
    delayMs: Int = 0
) {
    val progress = remember { Animatable(if (animate) 0f else 1f) }
    val scale = remember { Animatable(if (animate) 0.85f else 1f) }

    LaunchedEffect(animate) {
        if (animate) {
            kotlinx.coroutines.delay(delayMs.toLong())
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
    }

    Canvas(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                alpha = progress.value.coerceIn(0f, 1f)
            }
    ) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f

        // Outer broken arc 1
        val outerRadius = w * 0.40f
        drawArc(
            color = color,
            startAngle = -100f,
            sweepAngle = 270f * progress.value,
            useCenter = false,
            topLeft = Offset(cx - outerRadius, cy - outerRadius),
            size = Size(outerRadius * 2, outerRadius * 2),
            style = Stroke(width = w * 0.035f, cap = StrokeCap.Round)
        )

        // Inner broken arc 2
        val innerRadius = w * 0.33f
        drawArc(
            color = color,
            startAngle = 80f,
            sweepAngle = 240f * progress.value,
            useCenter = false,
            topLeft = Offset(cx - innerRadius, cy - innerRadius),
            size = Size(innerRadius * 2, innerRadius * 2),
            style = Stroke(width = w * 0.026f, cap = StrokeCap.Round)
        )

        // Geometric Brush 'M'
        val mPath = Path().apply {
            val left = cx - w * 0.18f
            val right = cx + w * 0.18f
            val topY = cy - h * 0.16f
            val bottomY = cy + h * 0.16f
            val midY = cy + h * 0.05f

            moveTo(left, bottomY)
            lineTo(left, topY)
            lineTo(cx, midY)
            lineTo(right, topY)
            lineTo(right, bottomY)
        }

        drawPath(
            path = mPath,
            color = color,
            style = Stroke(
                width = w * 0.065f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
