package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PresetRepository
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ChassisBlack
import com.example.ui.theme.ChassisBorder
import com.example.ui.theme.ChassisCard
import com.example.ui.theme.ChassisDark
import com.example.ui.theme.ChassisPanel
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanBright
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.theme.ValveOrange

@Composable
fun LiveReadyScreen(
    presetRepo: PresetRepository,
    modifier: Modifier = Modifier
) {
    val presets by presetRepo.presets.collectAsState()
    val activePresetId by presetRepo.activePresetId.collectAsState()
    val activePreset = presets.firstOrNull { it.id == activePresetId } ?: presets.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChassisDark)
            .verticalScroll(rememberScrollState())
    ) {
        // --- 1. Hero Stage Banner Image ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_stage_hero_1792762616173),
                contentDescription = "Concert Stage Rig",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                ChassisDark.copy(alpha = 0.85f),
                                ChassisDark
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MatrixGreen)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "STAGE ADAPTATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Text(
                        text = "LIVE READY OPTIMIZER",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = "Acoustically tune home presets for 105 dB stage FRFR & concert sound systems",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        if (activePreset != null) {
            // --- 2. Active Target Preset Card ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = ChassisCard),
                border = BorderStroke(1.dp, ChassisBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TARGET PRESET", fontSize = 10.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${activePreset.slotCode} : ${activePreset.name}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (activePreset.liveReadyApplied) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MatrixGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("OPTIMIZED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MatrixGreen)
                                }
                            }
                        }
                        Text(
                            text = "Amp: ${activePreset.getModule("AMP")?.modelName ?: "None"} • Cab: ${activePreset.getModule("CAB")?.modelName ?: "None"}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = { presetRepo.applyLiveReadyOptimization(activePreset.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activePreset.liveReadyApplied) MatrixGreen else NeonCyan
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("apply_live_ready_btn")
                    ) {
                        Icon(
                            imageVector = if (activePreset.liveReadyApplied) Icons.Default.CheckCircle else Icons.Default.LiveTv,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (activePreset.liveReadyApplied) "Re-Optimize" else "Optimize Now",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- 3. The 5 Live Stage Rules Breakdown ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "ACOUSTIC STAGE COMPENSATION RULES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                LiveRuleItem(
                    icon = Icons.Default.Waves,
                    title = "1. Eliminate High-End Fizz (Cab Hi Cut 9.0 kHz)",
                    description = "Presets dialed through headphones or small studio monitors often have harsh 10kHz+ digital harmonic fizz that pierces ears painfully through massive stage line arrays.",
                    applied = activePreset.liveReadyApplied,
                    stat = "CAB High Cut: 9000 Hz"
                )

                LiveRuleItem(
                    icon = Icons.Default.VolumeDown,
                    title = "2. Sub-Bass Tightening (Cab Low Cut 80 Hz)",
                    description = "Stage floor monitors and PA subwoofers rumble below 80 Hz, causing muddy boom that clashes with the bass guitar and kick drum.",
                    applied = activePreset.liveReadyApplied,
                    stat = "CAB Low Cut: 80 Hz"
                )

                LiveRuleItem(
                    icon = Icons.Default.Hearing,
                    title = "3. Wet FX Attenuation (-30% Reverb & Delay)",
                    description = "Real concert halls and clubs already have immense natural reverberation. 50% wet bedroom reverb becomes a washed-out wall of sound on stage.",
                    applied = activePreset.liveReadyApplied,
                    stat = "RVB/DLY Mix: -30% & Trail ON"
                )

                LiveRuleItem(
                    icon = Icons.Default.Equalizer,
                    title = "4. Midrange Punch (+1.5 dB at 1.6 kHz)",
                    description = "Fletcher-Munson loudness contours cause human ears to hear more lows and highs at 105 dB. A targeted 1.6 kHz boost ensures guitar solos cut right through.",
                    applied = activePreset.liveReadyApplied,
                    stat = "EQ 1.6kHz: +1.5 dB Boost"
                )

                LiveRuleItem(
                    icon = Icons.Default.Mic,
                    title = "5. Stage Feedback Taming (-15% Amp Gain)",
                    description = "High volume air excitation causes acoustic feedback into guitar pickups. Reducing gain by 15-18% maintains sustain while preventing uncontrolled howling.",
                    applied = activePreset.liveReadyApplied,
                    stat = "AMP/DST Gain: -15% Trim"
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun LiveRuleItem(
    icon: ImageVector,
    title: String,
    description: String,
    applied: Boolean,
    stat: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ChassisCard),
        border = BorderStroke(1.dp, if (applied) MatrixGreen.copy(alpha = 0.5f) else ChassisBorder),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (applied) MatrixGreen.copy(alpha = 0.2f) else ChassisPanel),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (applied) MatrixGreen else TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (applied) {
                        Text(
                            text = "APPLIED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MatrixGreen,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(ChassisBlack)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stat,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = if (applied) NeonCyanBright else TextTertiary
                    )
                }
            }
        }
    }
}
