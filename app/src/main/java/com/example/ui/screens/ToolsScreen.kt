package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    modifier: Modifier = Modifier
) {
    var selectedToolTab by remember { mutableIntStateOf(0) }
    val toolTabs = listOf("Tuner", "Drum Machine", "180s Looper")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChassisDark)
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedToolTab,
            containerColor = ChassisBlack,
            contentColor = NeonCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedToolTab]),
                    color = NeonCyan
                )
            }
        ) {
            toolTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedToolTab == index,
                    onClick = { selectedToolTab = index },
                    text = {
                        Text(
                            text = title.uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedToolTab) {
                0 -> TunerTool()
                1 -> DrumMachineTool()
                2 -> LooperTool()
            }
        }
    }
}

@Composable
fun TunerTool() {
    var targetHz by remember { mutableIntStateOf(440) }
    var currentCents by remember { mutableFloatStateOf(0f) }
    var detectedNote by remember { mutableStateOf("E") }
    var octave by remember { mutableIntStateOf(2) }
    var isMuted by remember { mutableStateOf(false) }

    // Guitar strings for quick check
    val standardStrings = listOf("E2", "A2", "D3", "G3", "B3", "E4")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tuner Display Unit
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tuner_display_card"),
            colors = CardDefaults.cardColors(containerColor = ChassisCard),
            border = BorderStroke(1.dp, ChassisBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Pitch Reference Selector & Mute Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("REF PITCH:", fontSize = 10.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
                        listOf(432, 440, 442).forEach { hz ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (targetHz == hz) NeonCyan else ChassisPanel)
                                    .clickable { targetHz = hz }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$hz Hz",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = if (targetHz == hz) Color.Black else TextSecondary
                                )
                            }
                        }
                    }

                    // Mute on stage
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isMuted) LedRed.copy(alpha = 0.2f) else ChassisPanel)
                            .border(1.dp, if (isMuted) LedRed else ChassisBorder, RoundedCornerShape(6.dp))
                            .clickable { isMuted = !isMuted }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = if (isMuted) LedRed else MatrixGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isMuted) "MUTED" else "THRU",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isMuted) LedRed else MatrixGreen
                        )
                    }
                }

                // Needle Gauge Arc
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val centerX = size.width / 2f
                        val needleBaseY = size.height - 10.dp.toPx()
                        val radius = size.height * 0.9f

                        // Draw tick marks (-50 to +50 cents)
                        for (c in -50..50 step 10) {
                            val angleRad = Math.toRadians((270.0 + (c * 1.2)).toDouble())
                            val isCenter = c == 0
                            val tickLen = if (isCenter) 18.dp.toPx() else 10.dp.toPx()

                            val startX = (centerX + (radius - tickLen) * Math.cos(angleRad)).toFloat()
                            val startY = (needleBaseY + (radius - tickLen) * Math.sin(angleRad)).toFloat()
                            val endX = (centerX + radius * Math.cos(angleRad)).toFloat()
                            val endY = (needleBaseY + radius * Math.sin(angleRad)).toFloat()

                            drawLine(
                                color = if (isCenter) MatrixGreen else ChassisBorder,
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = if (isCenter) 3.dp.toPx() else 1.5.dp.toPx()
                            )
                        }

                        // Needle line
                        val needleAngle = Math.toRadians((270.0 + (currentCents * 1.2)).toDouble())
                        val needleEndX = (centerX + radius * Math.cos(needleAngle)).toFloat()
                        val needleEndY = (needleBaseY + radius * Math.sin(needleAngle)).toFloat()

                        val inTune = kotlin.math.abs(currentCents) <= 3f
                        drawLine(
                            color = if (inTune) MatrixGreen else NeonCyan,
                            start = Offset(centerX, needleBaseY),
                            end = Offset(needleEndX, needleEndY),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // Pivot circle
                        drawCircle(
                            color = Color.White,
                            radius = 5.dp.toPx(),
                            center = Offset(centerX, needleBaseY)
                        )
                    }
                }

                // Big Note & Status
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val inTune = kotlin.math.abs(currentCents) <= 3f
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = detectedNote,
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = if (inTune) MatrixGreen else TextPrimary
                        )
                        Text(
                            text = octave.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    Text(
                        text = if (inTune) "IN TUNE (±0 CENTS)" else "${if (currentCents > 0) "+" else ""}${currentCents.toInt()} CENTS",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (inTune) MatrixGreen else AmberGold
                    )
                }

                // Quick string tuner triggers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    standardStrings.forEach { stringNote ->
                        val isSelectedString = "${detectedNote}${octave}" == stringNote

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isSelectedString) NeonCyan else ChassisPanel)
                                .border(1.dp, if (isSelectedString) NeonCyanBright else ChassisBorder, CircleShape)
                                .clickable {
                                    detectedNote = stringNote.take(1)
                                    octave = stringNote.drop(1).toIntOrNull() ?: 2
                                    currentCents = 0f
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringNote,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelectedString) Color.Black else TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrumMachineTool() {
    var isPlaying by remember { mutableStateOf(false) }
    var currentBpm by remember { mutableIntStateOf(120) }
    var currentBeat by remember { mutableIntStateOf(1) }
    var selectedPattern by remember { mutableStateOf("01 - Rock 4/4 Basic Beat") }
    var drumVolume by remember { mutableFloatStateOf(80f) }

    val patterns = listOf(
        "01 - Rock 4/4 Basic Beat",
        "02 - Heavy Rock Driving 8th",
        "03 - Metal Double-Bass Blast",
        "04 - Thrash Fast 16th",
        "05 - Texas Blues Shuffle 12/8",
        "06 - Slow Blues 6/8",
        "07 - Funk 16th Ghost Snare",
        "08 - Neo Soul Laidback Pocket",
        "09 - Pop Dance 4-on-Floor",
        "10 - Jazz Swing Ride Cymbal",
        "11 - Reggae One-Drop",
        "12 - Studio Click Metronome"
    )

    // Beat pulsation loop
    LaunchedEffect(isPlaying, currentBpm) {
        if (isPlaying) {
            val beatDuration = (60000 / currentBpm).toLong()
            while (true) {
                delay(beatDuration)
                currentBeat = (currentBeat % 4) + 1
            }
        } else {
            currentBeat = 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("drum_machine_card"),
            colors = CardDefaults.cardColors(containerColor = ChassisCard),
            border = BorderStroke(1.dp, ChassisBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Header & Beat Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("100 DRUM PATTERNS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextTertiary)
                        Text("RHYTHM ENGINE", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }

                    // 4-Beat LED lights
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..4).forEach { beat ->
                            val isBeatLit = isPlaying && currentBeat == beat
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isBeatLit) {
                                            if (beat == 1) LedRed else MatrixGreen
                                        } else ChassisPanel
                                    )
                                    .border(1.dp, ChassisBorder, CircleShape)
                            )
                        }
                    }
                }

                // Pattern Selector
                var dropdownExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedPattern,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Drum Style / Pattern") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ChassisPanel,
                            unfocusedContainerColor = ChassisPanel,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = ChassisBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.background(ChassisPanel)
                    ) {
                        patterns.forEach { pattern ->
                            DropdownMenuItem(
                                text = { Text(pattern, color = TextPrimary) },
                                onClick = {
                                    selectedPattern = pattern
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // BPM Slider & Tap Tempo
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TEMPO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Text("$currentBpm BPM", fontSize = 16.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = NeonCyan)
                    }

                    Slider(
                        value = currentBpm.toFloat(),
                        onValueChange = { currentBpm = it.toInt() },
                        valueRange = 40f..250f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = ChassisPanel
                        )
                    )
                }

                // Volume Slider
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("DRUM VOLUME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Text("${drumVolume.toInt()}%", fontSize = 13.sp, fontFamily = FontFamily.Monospace, color = TextPrimary)
                    }

                    Slider(
                        value = drumVolume,
                        onValueChange = { drumVolume = it },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ValveOrange,
                            activeTrackColor = ValveOrange,
                            inactiveTrackColor = ChassisPanel
                        )
                    )
                }

                // Controls: Play / Stop & Tap Tempo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { isPlaying = !isPlaying },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPlaying) LedRed else MatrixGreen
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isPlaying) "STOP DRUMS" else "PLAY DRUMS",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    var lastTapTime by remember { mutableStateOf(0L) }
                    Button(
                        onClick = {
                            val now = System.currentTimeMillis()
                            if (lastTapTime > 0) {
                                val diff = now - lastTapTime
                                if (diff in 200..2000) {
                                    currentBpm = (60000 / diff).toInt().coerceIn(40, 250)
                                }
                            }
                            lastTapTime = now
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ChassisPanel),
                        border = BorderStroke(1.dp, ChassisBorder),
                        modifier = Modifier
                            .weight(0.7f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("TAP TEMPO", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun LooperTool() {
    var looperState by remember { mutableStateOf("STOP") } // REC, DUB, PLAY, STOP
    var recordedSeconds by remember { mutableFloatStateOf(0f) }
    var halfSpeed by remember { mutableStateOf(false) }
    var reverse by remember { mutableStateOf(false) }
    var looperLevel by remember { mutableFloatStateOf(80f) }

    val maxCapacitySeconds = 180f

    // Simulated loop timer
    LaunchedEffect(looperState) {
        if (looperState == "REC") {
            while (recordedSeconds < maxCapacitySeconds) {
                delay(100)
                recordedSeconds += 0.1f
            }
            looperState = "PLAY"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("looper_card"),
            colors = CardDefaults.cardColors(containerColor = ChassisCard),
            border = BorderStroke(1.dp, ChassisBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("180 SECONDS STEREO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextTertiary)
                        Text("PHRASE LOOPER", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (looperState) {
                                    "REC" -> LedRed.copy(alpha = 0.2f)
                                    "DUB" -> ValveOrange.copy(alpha = 0.2f)
                                    "PLAY" -> MatrixGreen.copy(alpha = 0.2f)
                                    else -> ChassisPanel
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = looperState,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = when (looperState) {
                                "REC" -> LedRed
                                "DUB" -> ValveOrange
                                "PLAY" -> MatrixGreen
                                else -> TextTertiary
                            }
                        )
                    }
                }

                // Big Circular Loop Tracker
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 8.dp.toPx()
                        val fraction = (recordedSeconds / maxCapacitySeconds).coerceIn(0f, 1f)

                        // Track
                        drawCircle(
                            color = ChassisPanel,
                            style = Stroke(width = strokeWidth)
                        )

                        // Active loop progress arc
                        drawArc(
                            color = when (looperState) {
                                "REC" -> LedRed
                                "DUB" -> ValveOrange
                                "PLAY" -> MatrixGreen
                                else -> NeonCyan
                            },
                            startAngle = -90f,
                            sweepAngle = fraction * 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1fs", recordedSeconds),
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "/ 180s",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Looper Stomp Buttons: REC/DUB & PLAY/STOP
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            looperState = when (looperState) {
                                "STOP" -> "REC"
                                "REC" -> "PLAY"
                                "PLAY" -> "DUB"
                                "DUB" -> "PLAY"
                                else -> "REC"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (looperState) {
                                "REC" -> LedRed
                                "DUB" -> ValveOrange
                                else -> ChassisPanel
                            }
                        ),
                        border = BorderStroke(1.dp, ChassisBorder),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FiberManualRecord,
                            contentDescription = null,
                            tint = if (looperState == "REC" || looperState == "DUB") Color.White else LedRed
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (looperState == "REC") "REC -> PLAY" else "REC / DUB",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            looperState = if (looperState == "PLAY") "STOP" else "PLAY"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (looperState == "PLAY") MatrixGreen else ChassisPanel
                        ),
                        border = BorderStroke(1.dp, ChassisBorder),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (looperState == "PLAY") Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (looperState == "PLAY") Color.Black else MatrixGreen
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (looperState == "PLAY") "STOP" else "PLAY",
                            fontWeight = FontWeight.Bold,
                            color = if (looperState == "PLAY") Color.Black else Color.White
                        )
                    }
                }

                // Secondary Controls: Clear, 1/2 Speed, Reverse
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            looperState = "STOP"
                            recordedSeconds = 0f
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ChassisPanel),
                        border = BorderStroke(1.dp, ChassisBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CLEAR", color = LedRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { halfSpeed = !halfSpeed },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (halfSpeed) NeonCyan else ChassisPanel
                            ),
                            border = BorderStroke(1.dp, ChassisBorder),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("1/2 SPEED", color = if (halfSpeed) Color.Black else TextPrimary, fontSize = 11.sp)
                        }

                        Button(
                            onClick = { reverse = !reverse },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (reverse) NeonCyan else ChassisPanel
                            ),
                            border = BorderStroke(1.dp, ChassisBorder),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("REVERSE", color = if (reverse) Color.Black else TextPrimary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
