package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChassisBlack
import com.example.ui.theme.ChassisBorder
import com.example.ui.theme.ChassisCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanBright
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun KnobControl(
    name: String,
    value: Float,
    min: Float,
    max: Float,
    unit: String = "",
    size: Dp = 68.dp,
    accentColor: Color = NeonCyan,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val range = max - min
    val normalizedFraction = if (range > 0f) ((value - min) / range).coerceIn(0f, 1f) else 0.5f

    // Knob sweeps from 135 deg (bottom-left) to 405 deg (270 deg total arc)
    val startAngle = 135f
    val sweepAngle = 270f
    val currentSweep = normalizedFraction * sweepAngle

    var dragAccumulator by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier.testTag("knob_${name.lowercase().replace(" ", "_")}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = name.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            letterSpacing = 0.5.sp
        )

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .pointerInput(min, max, value) {
                    detectDragGestures(
                        onDragStart = { dragAccumulator = 0f },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            // Dragging upward (negative Y) increases value; dragging right (positive X) increases
                            val deltaPx = -dragAmount.y + dragAmount.x
                            val sensitivity = range / 200f
                            val newValue = (value + deltaPx * sensitivity).coerceIn(min, max)
                            onValueChange(newValue)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size)) {
                val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
                val radius = (size.toPx() - 12.dp.toPx()) / 2f
                val strokeW = 4.dp.toPx()

                // Background track arc
                drawArc(
                    color = ChassisBorder,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )

                // Active value arc with glow
                if (currentSweep > 0f) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(accentColor.copy(alpha = 0.6f), accentColor, NeonCyanBright)
                        ),
                        startAngle = startAngle,
                        sweepAngle = currentSweep,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeW + 1.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Inner knob metal body
                val knobRadius = radius - 6.dp.toPx()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(ChassisCard, ChassisBlack),
                        center = center,
                        radius = knobRadius
                    ),
                    radius = knobRadius,
                    center = center
                )
                // Knob border bevel
                drawCircle(
                    color = ChassisBorder,
                    radius = knobRadius,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )

                // Indicator line/dot
                val angleRad = Math.toRadians((startAngle + currentSweep).toDouble())
                val dotRadius = knobRadius * 0.72f
                val dotCenter = Offset(
                    (center.x + dotRadius * cos(angleRad)).toFloat(),
                    (center.y + dotRadius * sin(angleRad)).toFloat()
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = dotCenter
                )
            }
        }

        // Value text
        val formattedVal = if (range > 20f) {
            value.toInt().toString()
        } else {
            String.format("%.1f", value)
        }

        Text(
            text = if (unit.isNotEmpty()) "$formattedVal $unit" else formattedVal,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}
