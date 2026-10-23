package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ChassisBlack
import com.example.ui.theme.ChassisBorder
import com.example.ui.theme.ChassisCard
import com.example.ui.theme.LedRed
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun VuMeter(
    label: String,
    valueLufs: Float, // e.g. -16.0f
    targetLufs: Float? = null,
    minLufs: Float = -40f,
    maxLufs: Float = -4f,
    segmentCount: Int = 24,
    modifier: Modifier = Modifier
) {
    val range = maxLufs - minLufs
    val normalizedFraction = ((valueLufs - minLufs) / range).coerceIn(0f, 1f)
    val litSegments = (normalizedFraction * segmentCount).toInt()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ChassisCard)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (targetLufs != null) {
                    Text(
                        text = "TARGET: ${String.format("%.1f", targetLufs)} LUFS",
                        fontSize = 10.sp,
                        color = AmberGold
                    )
                }
                Text(
                    text = "${String.format("%.1f", valueLufs)} LUFS",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        // Segments Bar
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(ChassisBlack)
        ) {
            val totalWidth = size.width
            val segmentWidth = (totalWidth - (segmentCount - 1) * 2.dp.toPx()) / segmentCount
            val segmentHeight = size.height

            for (i in 0 until segmentCount) {
                val x = i * (segmentWidth + 2.dp.toPx())
                val fraction = i.toFloat() / segmentCount
                val isLit = i < litSegments

                val segmentColor = when {
                    fraction >= 0.88f -> LedRed
                    fraction >= 0.65f -> AmberGold
                    else -> MatrixGreen
                }

                drawRoundRect(
                    color = if (isLit) segmentColor else segmentColor.copy(alpha = 0.15f),
                    topLeft = Offset(x, 0f),
                    size = Size(segmentWidth, segmentHeight),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
            }

            // Target marker line if specified
            if (targetLufs != null) {
                val targetFraction = ((targetLufs - minLufs) / range).coerceIn(0f, 1f)
                val targetX = targetFraction * totalWidth
                drawLine(
                    color = Color.White,
                    start = Offset(targetX, 0f),
                    end = Offset(targetX, size.height),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
    }
}
