package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.LufsAnalyzer
import com.example.data.PresetRepository
import com.example.model.Preset
import com.example.ui.components.VuMeter
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ChassisBlack
import com.example.ui.theme.ChassisBorder
import com.example.ui.theme.ChassisCard
import com.example.ui.theme.ChassisDark
import com.example.ui.theme.ChassisPanel
import com.example.ui.theme.LedRed
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanBright
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.theme.ValveOrange

@Composable
fun HarmonizerScreen(
    presetRepo: PresetRepository,
    modifier: Modifier = Modifier
) {
    val presets by presetRepo.presets.collectAsState()
    val activePresetId by presetRepo.activePresetId.collectAsState()
    val activePreset = presets.firstOrNull { it.id == activePresetId } ?: presets.firstOrNull()

    val scope = rememberCoroutineScope()
    val analyzer = remember { LufsAnalyzer(scope) }
    val isMeasuring by analyzer.isMeasuring.collectAsState()
    val measurement by analyzer.measurement.collectAsState()

    var selectedTargetLufs by remember { mutableFloatStateOf(-16.0f) }
    val targets = listOf(
        "Rhythm / Normal" to -16.0f,
        "Lead / Solo" to -11.0f,
        "Clean / Ambient" to -18.0f
    )

    DisposableEffect(Unit) {
        onDispose {
            analyzer.stopMeasurement()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChassisDark)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "LUFS VOLUME HARMONIZER",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "EBU R128 loudness leveling & cascade volume matching",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NeonCyan.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "ITU-R BS.1770",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyanBright
                )
            }
        }

        // --- Active Preset Inspection & Target Selector ---
        if (activePreset != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ChassisCard),
                border = BorderStroke(1.dp, ChassisBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("CURRENT PATCH UNDER TEST", fontSize = 10.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${activePreset.slotCode} : ${activePreset.name}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ChassisBlack)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "PATCH VOL: ${activePreset.patchVolume}",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = ValveOrange
                            )
                        }
                    }

                    // Target chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        targets.forEach { (label, lufs) ->
                            FilterChip(
                                selected = selectedTargetLufs == lufs,
                                onClick = {
                                    selectedTargetLufs = lufs
                                    if (isMeasuring) {
                                        analyzer.startMeasurement(
                                            targetLufs = lufs,
                                            baseToneGain = (activePreset.patchVolume).toFloat()
                                        )
                                    }
                                },
                                label = { Text("$label ($lufs)", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonCyan.copy(alpha = 0.25f),
                                    selectedLabelColor = NeonCyanBright,
                                    containerColor = ChassisPanel,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }
            }
        }

        // --- Real-time Metering Dashboard ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ChassisPanel),
            border = BorderStroke(1.dp, ChassisBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REAL-TIME LOUDNESS METERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )

                    // Delta status pill
                    val delta = measurement.deltaDb
                    val deltaColor by animateColorAsState(
                        targetValue = when {
                            measurement.isWithinTolerance -> MatrixGreen
                            delta > 0f -> LedRed // Too loud
                            else -> AmberGold // Too quiet
                        },
                        label = "delta"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(deltaColor)
                        )
                        Text(
                            text = "${if (delta >= 0f) "+" else ""}${String.format("%.1f", delta)} dB GAP",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = deltaColor
                        )
                    }
                }

                // VU Meters
                VuMeter(
                    label = "Momentary (400ms)",
                    valueLufs = measurement.momentaryLufs,
                    targetLufs = selectedTargetLufs
                )

                VuMeter(
                    label = "Short-Term (3s)",
                    valueLufs = measurement.shortTermLufs,
                    targetLufs = selectedTargetLufs
                )

                VuMeter(
                    label = "Integrated (Overall)",
                    valueLufs = measurement.integratedLufs,
                    targetLufs = selectedTargetLufs
                )

                // Calibration Control Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            if (isMeasuring) {
                                analyzer.stopMeasurement()
                            } else {
                                analyzer.startMeasurement(
                                    targetLufs = selectedTargetLufs,
                                    baseToneGain = (activePreset?.patchVolume ?: 70).toFloat()
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMeasuring) LedRed else NeonCyan
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("toggle_calibration_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isMeasuring) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isMeasuring) "Stop Measurement" else "Start Live Leveling",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = {
                            if (activePreset != null) {
                                presetRepo.harmonizeVolume(
                                    presetId = activePreset.id,
                                    targetLufs = selectedTargetLufs,
                                    simulatedMeasuredLufs = measurement.integratedLufs
                                )
                                analyzer.reset()
                            }
                        },
                        enabled = isMeasuring || measurement.integratedLufs > -50f,
                        colors = ButtonDefaults.buttonColors(containerColor = MatrixGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("apply_harmonize_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Apply Level (Auto-Tweak)",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        // --- Batch Leveling for Setlist Presets ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ChassisCard),
            border = BorderStroke(1.dp, ChassisBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "BATCH SETLIST LEVELING",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Standardize all patches to target LUFS across your set",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = {
                            // Harmonize all presets
                            presets.forEach { p ->
                                val target = if (p.genre == "Metal" || p.name.contains("Lead", ignoreCase = true)) -11f else -16f
                                presetRepo.harmonizeVolume(p.id, target, -18.5f + (p.patchVolume - 50) * 0.15f)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("harmonize_all_btn")
                    ) {
                        Icon(Icons.Default.AutoMode, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Harmonize All", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                HorizontalDivider(color = ChassisBorder)

                // Presets preview rows
                presets.take(6).forEach { preset ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(ChassisPanel)
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = preset.slotCode,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                            Text(
                                text = preset.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Vol: ${preset.patchVolume}",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = ValveOrange
                            )
                            Text(
                                text = if (preset.measuredLufs != null) "${preset.measuredLufs} LUFS" else "~ -16 LUFS",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MatrixGreen
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
