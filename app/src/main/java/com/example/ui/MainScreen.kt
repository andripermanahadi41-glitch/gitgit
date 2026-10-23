package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.ConnectionStatus
import com.example.audio.UsbMidiManager
import com.example.data.PresetRepository
import com.example.data.SetlistRepository
import com.example.ui.screens.DeviceScreen
import com.example.ui.screens.HarmonizerScreen
import com.example.ui.screens.LiveReadyScreen
import com.example.ui.screens.PresetLibrarianScreen
import com.example.ui.screens.SetlistScreen
import com.example.ui.screens.SignalChainScreen
import com.example.ui.screens.ToolsScreen
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

enum class MainTab(val title: String, val icon: ImageVector) {
    CHAIN("Chain", Icons.Default.GraphicEq),
    LIBRARY("Library", Icons.Default.Album),
    LIVE_READY("Live Stage", Icons.Default.Bolt),
    SETLIST("Setlist", Icons.Default.LibraryMusic),
    HARMONIZER("LUFS Level", Icons.Default.Equalizer),
    TOOLS("Tools", Icons.Default.MusicNote),
    DEVICE("USB Sync", Icons.Default.Usb)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    presetRepo: PresetRepository,
    setlistRepo: SetlistRepository,
    usbManager: UsbMidiManager,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = MainTab.entries

    val activePresetId by presetRepo.activePresetId.collectAsState()
    val presets by presetRepo.presets.collectAsState()
    val activePreset = presets.firstOrNull { it.id == activePresetId } ?: presets.firstOrNull()

    val usbStatus by usbManager.status.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ChassisDark,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Logo Badge
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "GP",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }

                        Column {
                            Text(
                                text = "GP-180 STUDIO",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary,
                                letterSpacing = 1.sp
                            )
                            if (activePreset != null) {
                                Text(
                                    text = "${activePreset.slotCode} • ${activePreset.name}",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = NeonCyanBright
                                )
                            }
                        }
                    }
                },
                actions = {
                    // Hardware Status Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(ChassisPanel)
                            .border(1.dp, ChassisBorder, RoundedCornerShape(16.dp))
                            .clickable { selectedTab = MainTab.DEVICE.ordinal }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    when (usbStatus) {
                                        ConnectionStatus.CONNECTED_USB -> MatrixGreen
                                        ConnectionStatus.SEARCHING -> ValveOrange
                                        ConnectionStatus.SIMULATED_STUDIO -> NeonCyan
                                    }
                                )
                        )
                        Text(
                            text = when (usbStatus) {
                                ConnectionStatus.CONNECTED_USB -> "GP-180 USB"
                                ConnectionStatus.SEARCHING -> "SCANNING..."
                                ConnectionStatus.SIMULATED_STUDIO -> "STUDIO SIM"
                            },
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    // Quick Tuner tool shortcut
                    IconButton(
                        onClick = { selectedTab = MainTab.TOOLS.ordinal }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Tuner",
                            tint = NeonCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ChassisBlack,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = ChassisBlack,
                contentColor = TextPrimary,
                tonalElevation = 8.dp
            ) {
                // We show 5 primary destinations in bottom bar: Chain, Library, Live Stage, Setlist, Tools
                val bottomBarTabs = listOf(
                    MainTab.CHAIN,
                    MainTab.LIBRARY,
                    MainTab.LIVE_READY,
                    MainTab.SETLIST,
                    MainTab.HARMONIZER
                )

                bottomBarTabs.forEach { tab ->
                    val isSelected = selectedTab == tab.ordinal
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab.ordinal },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonCyanBright,
                            indicatorColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { tabIndex ->
                when (tabs[tabIndex]) {
                    MainTab.CHAIN -> SignalChainScreen(
                        presetRepo = presetRepo,
                        onNavigateToLibrarian = { selectedTab = MainTab.LIBRARY.ordinal }
                    )
                    MainTab.LIBRARY -> PresetLibrarianScreen(
                        presetRepo = presetRepo,
                        onPresetSelected = {
                            // After selecting, return to pedalboard chain for editing
                            selectedTab = MainTab.CHAIN.ordinal
                        }
                    )
                    MainTab.LIVE_READY -> LiveReadyScreen(presetRepo = presetRepo)
                    MainTab.SETLIST -> SetlistScreen(
                        setlistRepo = setlistRepo,
                        presetRepo = presetRepo
                    )
                    MainTab.HARMONIZER -> HarmonizerScreen(presetRepo = presetRepo)
                    MainTab.TOOLS -> ToolsScreen()
                    MainTab.DEVICE -> DeviceScreen(
                        usbManager = usbManager,
                        presetRepo = presetRepo
                    )
                }
            }
        }
    }
}
