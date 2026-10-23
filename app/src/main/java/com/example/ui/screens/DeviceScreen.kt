package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.audio.ConnectionStatus
import com.example.audio.UsbMidiManager
import com.example.data.PresetRepository
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
import kotlinx.coroutines.launch

@Composable
fun DeviceScreen(
    usbManager: UsbMidiManager,
    presetRepo: PresetRepository,
    modifier: Modifier = Modifier
) {
    val status by usbManager.status.collectAsState()
    val lastLog by usbManager.lastLog.collectAsState()
    val isInjecting by usbManager.isInjecting.collectAsState()
    val injectionProgress by usbManager.injectionProgress.collectAsState()

    val activePreset = presetRepo.getActivePreset()
    val scope = rememberCoroutineScope()

    var injectBank by remember { mutableIntStateOf(1) }
    var injectLetter by remember { mutableStateOf("A") }
    val letters = listOf("A", "B", "C", "D")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChassisDark)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "HARDWARE & USB OTG SYNC",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Valeton GP-180 SysEx & Program Change Communications",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        when (status) {
                            ConnectionStatus.CONNECTED_USB -> MatrixGreen.copy(alpha = 0.2f)
                            ConnectionStatus.SEARCHING -> AmberGold.copy(alpha = 0.2f)
                            ConnectionStatus.SIMULATED_STUDIO -> NeonCyan.copy(alpha = 0.2f)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = when (status) {
                        ConnectionStatus.CONNECTED_USB -> "ONLINE (USB OTG)"
                        ConnectionStatus.SEARCHING -> "CONNECTING..."
                        ConnectionStatus.SIMULATED_STUDIO -> "STUDIO SIMULATOR"
                    },
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = when (status) {
                        ConnectionStatus.CONNECTED_USB -> MatrixGreen
                        ConnectionStatus.SEARCHING -> AmberGold
                        ConnectionStatus.SIMULATED_STUDIO -> NeonCyanBright
                    }
                )
            }
        }

        // Connection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ChassisCard),
            border = BorderStroke(1.dp, ChassisBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ChassisPanel),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Usb,
                                contentDescription = null,
                                tint = if (status == ConnectionStatus.CONNECTED_USB) MatrixGreen else NeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "USB OTG Device Interface",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "VID: 0x84EF • PID: 0x002A • SysEx: '!%~GP-2'",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextSecondary
                            )
                        }
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                if (status == ConnectionStatus.CONNECTED_USB) {
                                    usbManager.disconnect()
                                } else {
                                    usbManager.connect(true)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (status == ConnectionStatus.CONNECTED_USB) LedRed else NeonCyan
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (status == ConnectionStatus.CONNECTED_USB) "Disconnect" else "Connect USB",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                HorizontalDivider(color = ChassisBorder)

                // Status message
                Text(
                    text = "Status: $lastLog",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan
                )
            }
        }

        // Slot Injector Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ChassisCard),
            border = BorderStroke(1.dp, ChassisBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "HARDWARE SLOT INJECTOR",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Write active preset directly to Valeton GP-180 EEPROM",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ChassisBlack)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "TARGET: ${"%02d".format(injectBank)}-$injectLetter",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = ValveOrange
                        )
                    }
                }

                Text(
                    text = "Active preset to send: '${activePreset.name}' (${activePreset.genre})",
                    fontSize = 12.sp,
                    color = TextPrimary
                )

                // Bank & Letter selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bank slider
                    Column(modifier = Modifier.weight(1f)) {
                        Text("BANK: ${"%02d".format(injectBank)} (1-32)", fontSize = 10.sp, color = TextSecondary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { if (injectBank > 1) injectBank-- },
                                colors = ButtonDefaults.buttonColors(containerColor = ChassisPanel),
                                modifier = Modifier.size(36.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                            ) {
                                Text("-", fontSize = 16.sp, color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "%02d".format(injectBank),
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { if (injectBank < 32) injectBank++ },
                                colors = ButtonDefaults.buttonColors(containerColor = ChassisPanel),
                                modifier = Modifier.size(36.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                            ) {
                                Text("+", fontSize = 16.sp, color = TextPrimary)
                            }
                        }
                    }

                    // Footswitch Letter
                    Column(modifier = Modifier.weight(1f)) {
                        Text("SLOT FOOTSWITCH", fontSize = 10.sp, color = TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            letters.forEach { l ->
                                FilterChip(
                                    selected = injectLetter == l,
                                    onClick = { injectLetter = l },
                                    label = { Text(l, fontWeight = FontWeight.Bold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NeonCyan,
                                        selectedLabelColor = Color.Black,
                                        containerColor = ChassisPanel,
                                        labelColor = TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                // Progress indicator during injection
                if (isInjecting) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Streaming SysEx chunks: ${(injectionProgress * 100).toInt()}%",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = NeonCyan
                        )
                        LinearProgressIndicator(
                            progress = { injectionProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NeonCyan,
                            trackColor = ChassisBlack
                        )
                    }
                }

                // Action buttons: Inject & Program Change
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val slot = "${"%02d".format(injectBank)}-$injectLetter"
                            scope.launch {
                                usbManager.injectPreset(slot, activePreset.name) { step, total -> }
                            }
                        },
                        enabled = !isInjecting,
                        colors = ButtonDefaults.buttonColors(containerColor = MatrixGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("inject_to_hardware_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Inject to Hardware", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                usbManager.sendProgramChange(injectBank, injectLetter.first())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ChassisPanel),
                        border = BorderStroke(1.dp, ChassisBorder),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = TextPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send PC Switch", color = TextPrimary)
                    }
                }
            }
        }

        // Live SysEx Log Console
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ChassisBlack),
            border = BorderStroke(1.dp, ChassisBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "USB SYSEX PROTOCOL MONITOR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "F0 21 25 7E 47 50 2D 32 12 20 09 00 00 [366 B NIBBLE] F7\n" +
                            ">> Handshake verified with ACK SysEx 40b\n" +
                            ">> Baud: 31250 / USB 2.0 Bulk Endpoint 0x03\n" +
                            ">> Device: Valeton GP-180 Audio & SysEx Processor",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextTertiary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
