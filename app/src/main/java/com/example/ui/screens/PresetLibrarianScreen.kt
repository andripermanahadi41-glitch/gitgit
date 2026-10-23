package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PresetRepository
import com.example.model.Preset
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
fun PresetLibrarianScreen(
    presetRepo: PresetRepository,
    onPresetSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets by presetRepo.presets.collectAsState()
    val activePresetId by presetRepo.activePresetId.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    var onlyFavorites by remember { mutableStateOf(false) }

    var showCreateDialog by remember { mutableStateOf(false) }
    var newPresetName by remember { mutableStateOf("") }
    var newPresetGenre by remember { mutableStateOf("Rock") }

    val genres = listOf("All", "Metal", "Rock", "Blues", "Pop", "Ambient", "Acoustic", "Bass")

    val filteredPresets = presets.filter { preset ->
        val matchesQuery = searchQuery.isBlank() ||
                preset.name.contains(searchQuery, ignoreCase = true) ||
                preset.description.contains(searchQuery, ignoreCase = true) ||
                (preset.artistName?.contains(searchQuery, ignoreCase = true) ?: false) ||
                (preset.songTitle?.contains(searchQuery, ignoreCase = true) ?: false) ||
                preset.slotCode.contains(searchQuery, ignoreCase = true)

        val matchesGenre = selectedGenre == "All" || preset.genre.equals(selectedGenre, ignoreCase = true)
        val matchesFavorite = !onlyFavorites || preset.isFavorite

        matchesQuery && matchesGenre && matchesFavorite
    }

    Box(modifier = modifier.fillMaxSize().background(ChassisDark)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Search Bar & Filters
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = ChassisBlack,
                border = BorderStroke(1.dp, ChassisBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRESET LIBRARIAN",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${filteredPresets.size} / 200 Slots",
                            fontSize = 12.sp,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_preset_input"),
                        placeholder = { Text("Search by song, artist, bank (e.g. 01-A), or tone...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ChassisPanel,
                            unfocusedContainerColor = ChassisPanel,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = ChassisBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Genre Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            FilterChip(
                                selected = onlyFavorites,
                                onClick = { onlyFavorites = !onlyFavorites },
                                label = { Text("Favorites", fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (onlyFavorites) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = null,
                                        tint = if (onlyFavorites) AmberGold else TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AmberGold.copy(alpha = 0.2f),
                                    selectedLabelColor = AmberGold,
                                    containerColor = ChassisPanel,
                                    labelColor = TextSecondary
                                )
                            )
                        }

                        items(genres) { genre ->
                            FilterChip(
                                selected = selectedGenre == genre,
                                onClick = { selectedGenre = genre },
                                label = { Text(genre, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = NeonCyanBright,
                                    containerColor = ChassisPanel,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }
            }

            // Preset List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredPresets, key = { it.id }) { preset ->
                    val isActive = preset.id == activePresetId

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                presetRepo.selectPreset(preset.id)
                                onPresetSelected(preset.id)
                            }
                            .testTag("preset_item_${preset.slotCode.lowercase()}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isActive) ChassisCard else ChassisPanel
                        ),
                        border = BorderStroke(
                            width = if (isActive) 1.5.dp else 1.dp,
                            color = if (isActive) NeonCyan else ChassisBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Slot badge & Details
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Bank Slot Badge
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isActive) NeonCyan.copy(alpha = 0.25f) else ChassisBlack
                                        )
                                        .border(
                                            1.dp,
                                            if (isActive) NeonCyan else ChassisBorder,
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = preset.slotCode,
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isActive) NeonCyanBright else TextSecondary
                                        )
                                        val pc = (preset.bankNumber - 1) * 4 + (preset.bankLetter - 'A')
                                        Text(
                                            text = "PC $pc",
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = TextTertiary
                                        )
                                    }
                                }

                                // Preset Info
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = preset.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isActive) NeonCyanBright else TextPrimary
                                        )
                                        if (preset.variantName != null) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(ValveOrange.copy(alpha = 0.2f))
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = preset.variantName,
                                                    fontSize = 9.sp,
                                                    color = ValveOrange
                                                )
                                            }
                                        }
                                    }

                                    if (preset.artistName != null) {
                                        Text(
                                            text = "${preset.artistName} - ${preset.songTitle ?: ""}",
                                            fontSize = 11.sp,
                                            color = AmberGold
                                        )
                                    }

                                    Text(
                                        text = "${preset.genre} • ${preset.description}",
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            // Right: Volume Pill & Quick Actions
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Volume badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ChassisBlack)
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "VOL ${preset.patchVolume}",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextSecondary
                                    )
                                }

                                // Favorite Star
                                IconButton(
                                    onClick = { presetRepo.toggleFavorite(preset.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (preset.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "Favorite",
                                        tint = if (preset.isFavorite) AmberGold else TextTertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Duplicate
                                IconButton(
                                    onClick = {
                                        presetRepo.createCustomPreset(
                                            base = preset,
                                            newName = "${preset.name} (Copy)",
                                            genre = preset.genre
                                        )
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Duplicate",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Delete (if not factory)
                                if (!preset.isFactory) {
                                    IconButton(
                                        onClick = { presetRepo.deletePreset(preset.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = LedRed.copy(alpha = 0.8f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(72.dp)) }
            }
        }

        // Floating Action Button to create a new user preset
        FloatingActionButton(
            onClick = {
                val active = presetRepo.getActivePreset()
                newPresetName = "${active.name} User"
                newPresetGenre = active.genre
                showCreateDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("create_preset_fab"),
            containerColor = NeonCyan,
            contentColor = Color.Black
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create User Preset")
        }

        // Create Dialog
        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("Save as New User Preset", color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "This will copy the current signal chain and parameters into a user memory bank.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )

                        OutlinedTextField(
                            value = newPresetName,
                            onValueChange = { if (it.length <= 16) newPresetName = it },
                            label = { Text("Preset Name (16 chars max)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = ChassisBorder
                            )
                        )

                        OutlinedTextField(
                            value = newPresetGenre,
                            onValueChange = { newPresetGenre = it },
                            label = { Text("Genre / Category") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = ChassisBorder
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val active = presetRepo.getActivePreset()
                            presetRepo.createCustomPreset(active, newPresetName, newPresetGenre)
                            showCreateDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                    ) {
                        Text("Save Preset", color = Color.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                },
                containerColor = ChassisCard
            )
        }
    }
}
