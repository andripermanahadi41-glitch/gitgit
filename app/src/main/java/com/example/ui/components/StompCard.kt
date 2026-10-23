package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ModuleState
import com.example.model.ModuleType
import com.example.ui.theme.ChassisBlack
import com.example.ui.theme.ChassisBorder
import com.example.ui.theme.ChassisCard
import com.example.ui.theme.ChassisPanel
import com.example.ui.theme.LedRedOff
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun StompCard(
    moduleState: ModuleState,
    isSelected: Boolean,
    canMoveLeft: Boolean,
    canMoveRight: Boolean,
    onSelect: () -> Unit,
    onToggle: () -> Unit,
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moduleType = ModuleType.fromCode(moduleState.moduleCode)
    val isEnabled = moduleState.enabled

    val cardBg = if (isEnabled) ChassisCard else ChassisBlack
    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> NeonCyan
            isEnabled -> moduleType.accentColor.copy(alpha = 0.6f)
            else -> ChassisBorder.copy(alpha = 0.5f)
        },
        label = "border"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 2.dp,
        label = "elevation"
    )

    Surface(
        modifier = modifier
            .width(135.dp)
            .height(180.dp)
            .shadow(elevation, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onSelect)
            .testTag("stomp_card_${moduleState.moduleCode.lowercase()}"),
        shape = RoundedCornerShape(12.dp),
        color = cardBg,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = if (isEnabled) {
                            listOf(
                                moduleType.accentColor.copy(alpha = 0.12f),
                                ChassisCard
                            )
                        } else {
                            listOf(ChassisBlack, ChassisBlack)
                        }
                    )
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Code & LED
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Band
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isEnabled) moduleType.accentColor.copy(alpha = 0.25f)
                            else ChassisPanel
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = moduleType.code,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEnabled) moduleType.accentColor else TextTertiary
                    )
                }

                // LED Indicator
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            if (isEnabled) MatrixGreen else LedRedOff
                        )
                        .border(
                            1.dp,
                            if (isEnabled) Color.White.copy(alpha = 0.8f) else Color.Transparent,
                            CircleShape
                        )
                )
            }

            // Model Name & Gear Ref
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = moduleState.modelName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isEnabled) TextPrimary else TextTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = moduleType.displayName,
                    fontSize = 10.sp,
                    color = TextSecondary,
                    maxLines = 1
                )
            }

            // Parameter summary pills
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(ChassisPanel)
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val topParams = moduleState.parameters.entries.take(2)
                if (topParams.isNotEmpty()) {
                    topParams.forEach { (name, value) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = name.take(7),
                                fontSize = 9.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = value.toInt().toString(),
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (isEnabled) NeonCyan else TextTertiary
                            )
                        }
                    }
                } else {
                    Text(
                        text = if (isEnabled) "Active" else "Bypassed",
                        fontSize = 9.sp,
                        color = TextTertiary
                    )
                }
            }

            // Bottom controls: Move arrows & Stomp Footswitch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Move Left
                IconButton(
                    onClick = onMoveLeft,
                    enabled = canMoveLeft,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Move Left",
                        tint = if (canMoveLeft) TextSecondary else Color.Transparent,
                        modifier = Modifier.size(14.dp)
                    )
                }

                // Stomp Bypass Switch Button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    if (isEnabled) NeonCyan.copy(alpha = 0.25f) else ChassisPanel,
                                    ChassisBlack
                                )
                            )
                        )
                        .border(
                            1.5.dp,
                            if (isEnabled) NeonCyan else ChassisBorder,
                            CircleShape
                        )
                        .clickable(onClick = onToggle),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Bypass Stomp",
                        tint = if (isEnabled) NeonCyan else TextTertiary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Move Right
                IconButton(
                    onClick = onMoveRight,
                    enabled = canMoveRight,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Move Right",
                        tint = if (canMoveRight) TextSecondary else Color.Transparent,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
