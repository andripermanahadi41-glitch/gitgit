package com.example.model

import androidx.compose.ui.graphics.Color

enum class ModuleType(
    val code: String,
    val displayName: String,
    val category: String,
    val accentColor: Color,
    val description: String
) {
    PRE("PRE", "Pre-FX", "Dynamics", Color(0xFFE57373), "Compressors, Boosters & Pitch Shifters"),
    WAH("WAH", "Wah / Filter", "Filter", Color(0xFFFFB74D), "Wah pedals, Envelope Filters & Auto-Wah"),
    DST("DST", "Distortion", "Overdrive", Color(0xFFFF7043), "Overdrive, Distortion, Fuzz & Boost Stomps"),
    SNAP("SNAP", "SnapTone", "Neural", Color(0xFFBA68C8), "Neural Amp Models (NAM) & Capture Snapshots"),
    AMP("AMP", "Amplifier", "Preamps", Color(0xFF4FC3F7), "Legendary British & American Tube Amps"),
    NR("NR", "Noise Reducer", "Utility", Color(0xFF90A4AE), "Fast response Gate & Noise Suppressor"),
    CAB("CAB", "Cabinet IR", "Cab/IR", Color(0xFF81C784), "Impulse Responses & Speaker Cabinet Simulations"),
    EQ("EQ", "Equalizer", "Tone Shaping", Color(0xFFAED581), "Parametric & Graphic 5-Band Studio EQs"),
    MOD("MOD", "Modulation", "Modulation", Color(0xFF4DD0E1), "Chorus, Flanger, Phaser, Tremolo & Rotary"),
    DLY("DLY", "Delay", "Time-Based", Color(0xFF7986CB), "Digital, Tape Echo, Analog & Ambient Delays"),
    RVB("RVB", "Reverb", "Atmosphere", Color(0xFF9575CD), "Spring, Plate, Hall, Shimmer & Room Acoustics"),
    VOL("VOL", "Volume / FX", "Master", Color(0xFFFFD54F), "Expression pedal swell & Master Output Gain");

    companion object {
        val DEFAULT_CHAIN = listOf(
            PRE.code, WAH.code, DST.code, SNAP.code, AMP.code, NR.code,
            CAB.code, EQ.code, MOD.code, DLY.code, RVB.code, VOL.code
        )

        fun fromCode(code: String): ModuleType {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: PRE
        }
    }
}
