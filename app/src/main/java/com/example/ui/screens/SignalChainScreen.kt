package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PresetRepository
import com.example.model.ModuleCatalog
import com.example.model.ModuleType
import com.example.model.Preset
import com.example.ui.components.KnobControl
import com.example.ui.components.StompCard
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SignalChainScreen(
    presetRepo: PresetRepository,
    onNavigateToLibrarian: () -> Unit,
    modifier: Modifier = Modifier
) {
    val presets by presetRepo.presets.collectAsState()
    val activePresetId by presetRepo.activePresetId.collectAsState()
    val activePreset = presets.firstOrNull { it.id == activePresetId } ?: presets.firstOrNull()

    var selectedModuleCode by remember { mutableStateOf<String?>("AMP") }

    if (activePreset == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Preset Loaded", color = TextSecondary)
        }
        return
    }

    val selectedModule = selectedModuleCode?.let { activePreset.getModule(it) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChassisDark)
            .verticalScroll(rememberScrollState())
    ) {
        // --- 1. Top Hardware Chassis Banner ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ChassisBlack,
            shadowElevation = 4.dp,
            border = BorderStroke(1.dp, ChassisBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Slot badge & Preset Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, NeonCyan, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = activePreset.slotCode,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyanBright
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = activePreset.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                if (activePreset.isFavorite) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Favorite",
                                        tint = AmberGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                if (activePreset.liveReadyApplied) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MatrixGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "LIVE READY",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MatrixGreen
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${activePreset.genre} • ${activePreset.description}",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Patch Volume Knob & Preset library trigger
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        KnobControl(
                            name = "Patch Vol",
                            value = activePreset.patchVolume.toFloat(),
                            min = 0f,
                            max = 100f,
                            unit = "%",
                            size = 54.dp,
                            accentColor = ValveOrange,
                            onValueChange = { presetRepo.setPatchVolume(it.toInt()) }
                        )

                        Button(
                            onClick = onNavigateToLibrarian,
                            colors = ButtonDefaults.buttonColors(containerColor = ChassisPanel),
                            border = BorderStroke(1.dp, ChassisBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Album,
                                contentDescription = "Presets",
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Presets", fontSize = 12.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 2. 12-Slot Signal Rack / Pedalboard Chain ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "SIGNAL CHAIN (12 SLOTS)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = "Tap to edit • Arrows to reorder",
                fontSize = 11.sp,
                color = TextTertiary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal scrollable stompboard
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Audio IN Jack
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ChassisPanel)
                    .border(1.dp, ChassisBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "INPUT\nGUITAR",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    lineHeight = 12.sp
                )
            }

            // Stomp cards
            activePreset.chainOrder.forEachIndexed { index, code ->
                val module = activePreset.getModule(code) ?: return@forEachIndexed
                StompCard(
                    moduleState = module,
                    isSelected = selectedModuleCode == code,
                    canMoveLeft = index > 0,
                    canMoveRight = index < activePreset.chainOrder.size - 1,
                    onSelect = { selectedModuleCode = code },
                    onToggle = { presetRepo.toggleModule(code) },
                    onMoveLeft = {
                        if (index > 0) {
                            val newOrder = activePreset.chainOrder.toMutableList()
                            val item = newOrder.removeAt(index)
                            newOrder.add(index - 1, item)
                            presetRepo.reorderChain(newOrder)
                        }
                    },
                    onMoveRight = {
                        if (index < activePreset.chainOrder.size - 1) {
                            val newOrder = activePreset.chainOrder.toMutableList()
                            val item = newOrder.removeAt(index)
                            newOrder.add(index + 1, item)
                            presetRepo.reorderChain(newOrder)
                        }
                    }
                )
            }

            // Audio OUT Jack
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ChassisPanel)
                    .border(1.dp, ChassisBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "OUTPUT\nSTEREO",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    lineHeight = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // --- 3. Module Inspector & Deep Parameter Editing ---
        if (selectedModule != null) {
            val modType = ModuleType.fromCode(selectedModule.moduleCode)
            val availableModels = ModuleCatalog.getModels(modType)
            val currentModelDef = availableModels.firstOrNull { it.name == selectedModule.modelName }
                ?: availableModels.firstOrNull()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("module_inspector_${selectedModule.moduleCode.lowercase()}"),
                colors = CardDefaults.cardColors(containerColor = ChassisCard),
                border = BorderStroke(1.dp, modType.accentColor.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header with Module Title, Bypass Toggle & Model Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(modType.accentColor)
                            )
                            Text(
                                text = "${modType.code} : ${modType.displayName}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        // Stomp Bypass button
                        Button(
                            onClick = { presetRepo.toggleModule(selectedModule.moduleCode) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedModule.enabled) MatrixGreen.copy(alpha = 0.2f) else ChassisPanel
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selectedModule.enabled) MatrixGreen else ChassisBorder
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PowerSettingsNew,
                                contentDescription = null,
                                tint = if (selectedModule.enabled) MatrixGreen else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (selectedModule.enabled) "ACTIVE" else "BYPASS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedModule.enabled) MatrixGreen else TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Model Selector Dropdown
                    var dropdownExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedModule.modelName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Model Algorithm") },
                            supportingText = {
                                Text(
                                    currentModelDef?.realGearReference ?: modType.description,
                                    color = TextSecondary
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = ChassisBorder,
                                focusedLabelColor = NeonCyan,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = ChassisPanel,
                                unfocusedContainerColor = ChassisPanel
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
                            availableModels.forEach { model ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = model.name,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = model.realGearReference,
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    },
                                    onClick = {
                                        presetRepo.setModuleModel(selectedModule.moduleCode, model.name)
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = ChassisBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Interactive Knobs Grid
                    val paramDefs = currentModelDef?.parameters ?: emptyList()
                    if (paramDefs.isNotEmpty()) {
                        Text(
                            text = "PARAMETERS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            maxItemsInEachRow = 4
                        ) {
                            paramDefs.forEach { param ->
                                val curVal = selectedModule.getParam(param.name, param.defaultValue)
                                KnobControl(
                                    name = param.name,
                                    value = curVal,
                                    min = param.min,
                                    max = param.max,
                                    unit = param.unit,
                                    size = 64.dp,
                                    accentColor = modType.accentColor,
                                    onValueChange = { newVal ->
                                        presetRepo.setParameterValue(selectedModule.moduleCode, param.name, newVal)
                                    }
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "This algorithm has no user-adjustable parameters.",
                            fontSize = 12.sp,
                            color = TextTertiary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
