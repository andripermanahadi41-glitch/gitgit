package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.audio.UsbMidiManager
import com.example.data.PresetRepository
import com.example.data.SetlistRepository
import com.example.ui.MainScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val presetRepository = PresetRepository(applicationContext)
        val setlistRepository = SetlistRepository(applicationContext, presetRepository)
        val usbMidiManager = UsbMidiManager()

        setContent {
            MyApplicationTheme {
                MainScreen(
                    presetRepo = presetRepository,
                    setlistRepo = setlistRepository,
                    usbManager = usbMidiManager
                )
            }
        }
    }
}
