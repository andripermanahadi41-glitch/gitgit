package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PresetRepository
import com.example.data.SetlistRepository
import com.example.model.SetlistSong
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
fun SetlistScreen(
    setlistRepo: SetlistRepository,
    presetRepo: PresetRepository,
    modifier: Modifier = Modifier
) {
    val setlists by setlistRepo.setlists.collectAsState()
    val activeSetlistId by setlistRepo.activeSetlistId.collectAsState()
    val activeSongId by setlistRepo.activeSongId.collectAsState()
    val activeSectionIndex by setlistRepo.activeSectionIndex.collectAsState()

    val currentSet = setlists.firstOrNull { it.id == activeSetlistId } ?: setlists.firstOrNull()
    val currentSong = currentSet?.songs?.firstOrNull { it.id == activeSongId } ?: currentSet?.songs?.firstOrNull()

    var isStageMode by remember { mutableStateOf(false) }
    var showAddSongDialog by remember { mutableStateOf(false) }

    var newSongTitle by remember { mutableStateOf("") }
    var newSongArtist by remember { mutableStateOf("") }
    var newSongKey by remember { mutableStateOf("E") }
    var newSongBpm by remember { mutableIntStateOf(120) }

    if (isStageMode && currentSong != null) {
        // --- Full-Screen High-Contrast Live Gig Stage View ---
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Stage Top Bar: Setlist Name & Exit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = currentSet?.name?.uppercase() ?: "LIVE STAGE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberGold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${currentSong.title.uppercase()} (${currentSong.musicalKey} • ${currentSong.bpm} BPM)",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Button(
                    onClick = { isStageMode = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ChassisCard),
                    border = BorderStroke(1.dp, ChassisBorder)
                ) {
                    Icon(Icons.Default.FullscreenExit, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Exit Stage", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // Giant Section / Patch Switching Footswitch Tiles
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                currentSong.sections.forEachIndexed { index, section ->
                    val isSectionActive = activeSectionIndex == index

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clickable { setlistRepo.selectSection(index) }
                            .testTag("stage_section_$index"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSectionActive) NeonCyan.copy(alpha = 0.25f) else ChassisCard
                        ),
                        border = BorderStroke(
                            width = if (isSectionActive) 3.dp else 1.dp,
                            color = if (isSectionActive) NeonCyan else ChassisBorder
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = section.name.uppercase(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSectionActive) NeonCyanBright else TextPrimary
                                )
                                Text(
                                    text = "Patch: ${section.presetName}",
                                    fontSize = 15.sp,
                                    color = TextSecondary
                                )
                            }

                            // Big Slot Stamp
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSectionActive) NeonCyan else ChassisPanel)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = section.slotCode,
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSectionActive) Color.Black else TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Stage Bottom Bar: Step Prev / Next Song Footswitches
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { setlistRepo.previousSong() },
                    colors = ButtonDefaults.buttonColors(containerColor = ChassisPanel),
                    border = BorderStroke(1.dp, ChassisBorder),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PREV SONG", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Button(
                    onClick = { setlistRepo.nextSong() },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("NEXT SONG", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.Black)
                }
            }
        }
        return
    }

    // --- Standard Setlist Manager View ---
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChassisDark)
    ) {
        // Header & Setlist Selector
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
                    Column {
                        Text(
                            text = "SETLIST MANAGER",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = currentSet?.venueOrDate ?: "Live Performances",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { isStageMode = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MatrixGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("launch_stage_view_btn")
                        ) {
                            Icon(Icons.Default.Fullscreen, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Stage View", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Setlists selection chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(setlists) { set ->
                        FilterChip(
                            selected = set.id == activeSetlistId,
                            onClick = { setlistRepo.selectSetlist(set.id) },
                            label = { Text(set.name, fontSize = 11.sp) },
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

        // Songs & Sections in active setlist
        if (currentSet != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SONGS IN SET (${currentSet.songs.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )

                        TextButton(onClick = { showAddSongDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Song", color = NeonCyan, fontSize = 12.sp)
                        }
                    }
                }

                items(currentSet.songs) { song ->
                    val isSongActive = song.id == activeSongId

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { setlistRepo.selectSong(song.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSongActive) ChassisCard else ChassisPanel
                        ),
                        border = BorderStroke(
                            width = if (isSongActive) 1.5.dp else 1.dp,
                            color = if (isSongActive) NeonCyan else ChassisBorder
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Song Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = if (isSongActive) NeonCyanBright else TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Column {
                                        Text(
                                            text = song.title,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        if (song.originalArtist.isNotBlank()) {
                                            Text(
                                                text = song.originalArtist,
                                                fontSize = 11.sp,
                                                color = AmberGold
                                            )
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ChassisBlack)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "KEY: ${song.musicalKey}",
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = TextSecondary
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ChassisBlack)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${song.bpm} BPM",
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = MatrixGreen
                                        )
                                    }
                                }
                            }

                            if (song.notes.isNotBlank()) {
                                Text(
                                    text = "💡 ${song.notes}",
                                    fontSize = 11.sp,
                                    color = TextTertiary
                                )
                            }

                            // Song Sections
                            HorizontalDivider(color = ChassisBorder.copy(alpha = 0.5f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                song.sections.forEachIndexed { sIdx, sec ->
                                    val isSecActive = isSongActive && activeSectionIndex == sIdx

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isSecActive) NeonCyan.copy(alpha = 0.25f) else ChassisBlack)
                                            .border(
                                                1.dp,
                                                if (isSecActive) NeonCyan else ChassisBorder,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable {
                                                setlistRepo.selectSong(song.id)
                                                setlistRepo.selectSection(sIdx)
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = sec.name,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSecActive) NeonCyanBright else TextSecondary
                                            )
                                            Text(
                                                text = "${sec.slotCode} • ${sec.presetName}",
                                                fontSize = 9.sp,
                                                fontFamily = FontFamily.Monospace,
                                                color = TextTertiary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Song Dialog
        if (showAddSongDialog) {
            val activePreset = presetRepo.getActivePreset()

            AlertDialog(
                onDismissRequest = { showAddSongDialog = false },
                title = { Text("Add Song to Setlist", color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newSongTitle,
                            onValueChange = { newSongTitle = it },
                            label = { Text("Song Title") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = ChassisBorder
                            )
                        )

                        OutlinedTextField(
                            value = newSongArtist,
                            onValueChange = { newSongArtist = it },
                            label = { Text("Artist (optional)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = ChassisBorder
                            )
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newSongKey,
                                onValueChange = { newSongKey = it },
                                label = { Text("Key") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = ChassisBorder
                                )
                            )

                            OutlinedTextField(
                                value = newSongBpm.toString(),
                                onValueChange = { newSongBpm = it.toIntOrNull() ?: 120 },
                                label = { Text("BPM") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = ChassisBorder
                                )
                            )
                        }

                        Text(
                            text = "Initial tone will be assigned to: ${activePreset.slotCode} - ${activePreset.name}",
                            fontSize = 11.sp,
                            color = NeonCyan
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val active = presetRepo.getActivePreset()
                            setlistRepo.addSongToActiveSetlist(
                                title = newSongTitle,
                                artist = newSongArtist,
                                key = newSongKey,
                                bpm = newSongBpm,
                                preset = active
                            )
                            showAddSongDialog = false
                            newSongTitle = ""
                            newSongArtist = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                    ) {
                        Text("Add Song", color = Color.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddSongDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                },
                containerColor = ChassisCard
            )
        }
    }
}
