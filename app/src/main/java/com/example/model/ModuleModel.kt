package com.example.model

data class ParameterDefinition(
    val name: String,
    val min: Float,
    val max: Float,
    val defaultValue: Float,
    val unit: String = "",
    val isInteger: Boolean = false,
    val options: List<String> = emptyList()
)

data class ModuleModel(
    val id: String,
    val name: String,
    val moduleType: ModuleType,
    val realGearReference: String,
    val parameters: List<ParameterDefinition>
)

object ModuleCatalog {
    val ALL_MODELS: Map<ModuleType, List<ModuleModel>> = mapOf(
        ModuleType.PRE to listOf(
            ModuleModel(
                "pre_comp", "COMP", ModuleType.PRE, "Based on Ross Compressor",
                listOf(
                    ParameterDefinition("Sustain", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Attack", 0f, 100f, 40f, "%"),
                    ParameterDefinition("Level", 0f, 100f, 65f, "%"),
                    ParameterDefinition("Clipping", 0f, 100f, 30f, "%")
                )
            ),
            ModuleModel(
                "pre_comp4", "COMP4", ModuleType.PRE, "4-Knob Studio Compressor",
                listOf(
                    ParameterDefinition("Sustain", 0f, 100f, 45f, "%"),
                    ParameterDefinition("Attack", 0f, 100f, 35f, "%"),
                    ParameterDefinition("Level", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Blend", 0f, 100f, 60f, "%")
                )
            ),
            ModuleModel(
                "pre_boost", "Green Boost", ModuleType.PRE, "Transparent Clean Pre-Amp Boost",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 25f, "%"),
                    ParameterDefinition("Tone", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 75f, "%")
                )
            ),
            ModuleModel(
                "pre_pitch", "Harmonizer", ModuleType.PRE, "Intelligent Pitch & Octave Divider",
                listOf(
                    ParameterDefinition("Pitch", -24f, 24f, 12f, "st", isInteger = true),
                    ParameterDefinition("Mix", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Tracking", 0f, 100f, 80f, "%")
                )
            )
        ),
        ModuleType.WAH to listOf(
            ModuleModel(
                "wah_v", "V-Wah", ModuleType.WAH, "Based on Dunlop CryBaby Classic",
                listOf(
                    ParameterDefinition("Position", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Range", 0f, 100f, 75f, "%"),
                    ParameterDefinition("Q Factor", 0f, 100f, 60f, "%")
                )
            ),
            ModuleModel(
                "wah_clyde", "Clyde Wah", ModuleType.WAH, "Based on Vox Clyde McCoy",
                listOf(
                    ParameterDefinition("Position", 0f, 100f, 40f, "%"),
                    ParameterDefinition("Range", 0f, 100f, 80f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 100f, "%")
                )
            ),
            ModuleModel(
                "wah_auto", "Auto Swell", ModuleType.WAH, "Dynamic Envelope Follower",
                listOf(
                    ParameterDefinition("Sens", 0f, 100f, 65f, "%"),
                    ParameterDefinition("Attack", 0f, 100f, 30f, "%"),
                    ParameterDefinition("Decay", 0f, 100f, 55f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 80f, "%")
                )
            )
        ),
        ModuleType.DST to listOf(
            ModuleModel(
                "dst_green", "Green OD", ModuleType.DST, "Based on Ibanez TS808 Tube Screamer",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 25f, "%"),
                    ParameterDefinition("Tone", 0f, 100f, 55f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 75f, "%")
                )
            ),
            ModuleModel(
                "dst_tube", "Tube 9", ModuleType.DST, "Based on Ibanez TS9 Classic Overdrive",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 35f, "%"),
                    ParameterDefinition("Tone", 0f, 100f, 60f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 70f, "%")
                )
            ),
            ModuleModel(
                "dst_blues", "Blues OD", ModuleType.DST, "Based on Boss BD-2 Blues Driver",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 45f, "%"),
                    ParameterDefinition("Tone", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 68f, "%")
                )
            ),
            ModuleModel(
                "dst_sod", "Super OD", ModuleType.DST, "Based on Boss SD-1 Super Overdrive",
                listOf(
                    ParameterDefinition("Drive", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Tone", 0f, 100f, 52f, "%"),
                    ParameterDefinition("Level", 0f, 100f, 72f, "%")
                )
            ),
            ModuleModel(
                "dst_riot", "Red Riot", ModuleType.DST, "Based on Suhr Riot High Gain Distortion",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 75f, "%"),
                    ParameterDefinition("Tone", 0f, 100f, 55f, "%"),
                    ParameterDefinition("Voice", 1f, 3f, 1f, "", isInteger = true),
                    ParameterDefinition("Volume", 0f, 100f, 65f, "%")
                )
            ),
            ModuleModel(
                "dst_muff", "Big Fuzz", ModuleType.DST, "Based on Electro-Harmonix Big Muff Pi",
                listOf(
                    ParameterDefinition("Sustain", 0f, 100f, 80f, "%"),
                    ParameterDefinition("Tone", 0f, 100f, 48f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 60f, "%")
                )
            ),
            ModuleModel(
                "dst_dark", "Darktale", ModuleType.DST, "Based on Darkglass B3K Modern Bass Overdrive",
                listOf(
                    ParameterDefinition("Drive", 0f, 100f, 65f, "%"),
                    ParameterDefinition("Blend", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Attack", 0f, 100f, 60f, "%"),
                    ParameterDefinition("Grunt", 0f, 100f, 80f, "%")
                )
            )
        ),
        ModuleType.SNAP to listOf(
            ModuleModel(
                "snap_plexi", "SnapTone: 68 Plexi Crunch", ModuleType.SNAP, "NAM Capture: 1968 Marshall Super Lead 100",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 65f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 75f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 55f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 60f, "%")
                )
            ),
            ModuleModel(
                "snap_mark", "SnapTone: Mark IIC+ Lead", ModuleType.SNAP, "NAM Capture: Mesa Boogie Mark IIC+ Rhythm/Lead",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 72f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 40f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 68f, "%")
                )
            ),
            ModuleModel(
                "snap_5150", "SnapTone: 5150 Block Red", ModuleType.SNAP, "NAM Capture: Peavey 5150 Block Letter High Gain",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 78f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 65f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 60f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 45f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 65f, "%")
                )
            ),
            ModuleModel(
                "snap_dlx", "SnapTone: 65 Deluxe Clean", ModuleType.SNAP, "NAM Capture: Fender 1965 Blackface Deluxe Reverb",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 32f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 80f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 55f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 60f, "%")
                )
            ),
            ModuleModel(
                "snap_slo", "SnapTone: SLO 100 Hot", ModuleType.SNAP, "NAM Capture: Soldano Super Lead Overdrive 100",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 82f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 68f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 58f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 62f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 64f, "%")
                )
            )
        ),
        ModuleType.AMP to listOf(
            ModuleModel(
                "amp_uk800", "UK 800", ModuleType.AMP, "Based on Marshall JCM800 2203",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 68f, "%"),
                    ParameterDefinition("Presence", 0f, 100f, 65f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 72f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 55f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 65f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 60f, "%")
                )
            ),
            ModuleModel(
                "amp_ev51", "EV 51", ModuleType.AMP, "Based on Peavey 5150 Lead Channel",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 75f, "%"),
                    ParameterDefinition("Presence", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 68f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 58f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 45f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 62f, "%")
                )
            ),
            ModuleModel(
                "amp_mess2c", "Mess2C+ 2", ModuleType.AMP, "Based on Mesa/Boogie Mark II C+ Lead",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 67.6f, "%"),
                    ParameterDefinition("Presence", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 75f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 35f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 45f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 65f, "%")
                )
            ),
            ModuleModel(
                "amp_plexi", "50s Plexi", ModuleType.AMP, "Based on Marshall 1959 Super Lead Plexi",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 58f, "%"),
                    ParameterDefinition("Presence", 0f, 100f, 60f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 78f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 60f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 55f, "%")
                )
            ),
            ModuleModel(
                "amp_jc120", "J-120 CL", ModuleType.AMP, "Based on Roland JC-120 Jazz Chorus Solid State",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 20f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 85f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 65f, "%")
                )
            ),
            ModuleModel(
                "amp_usdlx", "US DLX", ModuleType.AMP, "Based on Fender Deluxe Reverb Blackface",
                listOf(
                    ParameterDefinition("Gain", 0f, 100f, 35f, "%"),
                    ParameterDefinition("Volume", 0f, 100f, 82f, "%"),
                    ParameterDefinition("Bass", 0f, 100f, 52f, "%"),
                    ParameterDefinition("Middle", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Treble", 0f, 100f, 64f, "%")
                )
            )
        ),
        ModuleType.NR to listOf(
            ModuleModel(
                "nr_gate1", "Gate 1", ModuleType.NR, "Studio Noise Gate Fast Attenuation",
                listOf(
                    ParameterDefinition("Threshold", 0f, 100f, 35f, "%"),
                    ParameterDefinition("Decay", 0f, 100f, 40f, "%")
                )
            ),
            ModuleModel(
                "nr_gate2", "Gate 2", ModuleType.NR, "High Gain Metal Palm-Mute Clamp Gate",
                listOf(
                    ParameterDefinition("Threshold", 0f, 100f, 48f, "%"),
                    ParameterDefinition("Decay", 0f, 100f, 20f, "%")
                )
            )
        ),
        ModuleType.CAB to listOf(
            ModuleModel(
                "cab_ev", "EV 4x12", ModuleType.CAB, "Based on Peavey 5150 4x12 Sheffield Speakers",
                listOf(
                    ParameterDefinition("Volume", 0f, 100f, 75f, "%"),
                    ParameterDefinition("Low Cut", 20f, 500f, 85f, "Hz"),
                    ParameterDefinition("High Cut", 2000f, 20000f, 9000f, "Hz")
                )
            ),
            ModuleModel(
                "cab_mess", "Mess 4x12", ModuleType.CAB, "Based on Mesa/Boogie Rectifier 4x12 Celestion V30",
                listOf(
                    ParameterDefinition("Volume", 0f, 100f, 78f, "%"),
                    ParameterDefinition("Low Cut", 20f, 500f, 80f, "Hz"),
                    ParameterDefinition("High Cut", 2000f, 20000f, 10000f, "Hz")
                )
            ),
            ModuleModel(
                "cab_green", "UK 4x12 Green", ModuleType.CAB, "Based on Marshall 1960AX Celestion G12M Greenbacks",
                listOf(
                    ParameterDefinition("Volume", 0f, 100f, 75f, "%"),
                    ParameterDefinition("Low Cut", 20f, 500f, 90f, "Hz"),
                    ParameterDefinition("High Cut", 2000f, 20000f, 8500f, "Hz")
                )
            ),
            ModuleModel(
                "cab_usdlx", "US DLX 1x12", ModuleType.CAB, "Based on Fender 1965 Deluxe Reverb Oxford 12",
                listOf(
                    ParameterDefinition("Volume", 0f, 100f, 80f, "%"),
                    ParameterDefinition("Low Cut", 20f, 500f, 80f, "Hz"),
                    ParameterDefinition("High Cut", 2000f, 20000f, 12000f, "Hz")
                )
            ),
            ModuleModel(
                "cab_ir_user", "User Custom IR", ModuleType.CAB, "User 500ms 24-bit 48kHz Acoustic/Cabinet IR",
                listOf(
                    ParameterDefinition("Slot", 1f, 20f, 1f, "", isInteger = true),
                    ParameterDefinition("Volume", 0f, 100f, 78f, "%"),
                    ParameterDefinition("Low Cut", 20f, 500f, 80f, "Hz"),
                    ParameterDefinition("High Cut", 2000f, 20000f, 9500f, "Hz")
                )
            )
        ),
        ModuleType.EQ to listOf(
            ModuleModel(
                "eq_guitar", "Guitar EQ 1", ModuleType.EQ, "5-Band Graphic Guitar Stomp EQ",
                listOf(
                    ParameterDefinition("100Hz", -15f, 15f, 0f, "dB"),
                    ParameterDefinition("250Hz", -15f, 15f, 2f, "dB"),
                    ParameterDefinition("630Hz", -15f, 15f, -2f, "dB"),
                    ParameterDefinition("1.6kHz", -15f, 15f, 3f, "dB"),
                    ParameterDefinition("4.0kHz", -15f, 15f, 1f, "dB"),
                    ParameterDefinition("Level", -12f, 12f, 0f, "dB")
                )
            ),
            ModuleModel(
                "eq_mess", "Mess Graphic EQ", ModuleType.EQ, "Mesa Mark Series V-Curve Slider EQ",
                listOf(
                    ParameterDefinition("80Hz", -15f, 15f, 8f, "dB"),
                    ParameterDefinition("240Hz", -15f, 15f, 0f, "dB"),
                    ParameterDefinition("750Hz", -15f, 15f, -6f, "dB"),
                    ParameterDefinition("2.2kHz", -15f, 15f, 2f, "dB"),
                    ParameterDefinition("6.6kHz", -15f, 15f, 6f, "dB")
                )
            )
        ),
        ModuleType.MOD to listOf(
            ModuleModel(
                "mod_gchorus", "G-Chorus", ModuleType.MOD, "Warm Analog Stereo Chorus",
                listOf(
                    ParameterDefinition("Rate", 0f, 100f, 35f, "%"),
                    ParameterDefinition("Depth", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Pre Delay", 0f, 50f, 10f, "ms"),
                    ParameterDefinition("Mix", 0f, 100f, 45f, "%")
                )
            ),
            ModuleModel(
                "mod_cchorus", "C-Chorus", ModuleType.MOD, "Based on Roland Dimension C DC-2 4-Mode",
                listOf(
                    ParameterDefinition("Mode", 1f, 4f, 2f, "", isInteger = true),
                    ParameterDefinition("Mix", 0f, 100f, 50f, "%")
                )
            ),
            ModuleModel(
                "mod_jet", "Jet Flanger", ModuleType.MOD, "Classic Airplane Jet Through-Zero Flanger",
                listOf(
                    ParameterDefinition("Speed", 0f, 100f, 30f, "%"),
                    ParameterDefinition("Depth", 0f, 100f, 65f, "%"),
                    ParameterDefinition("Feedback", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Manual", 0f, 100f, 40f, "%")
                )
            ),
            ModuleModel(
                "mod_phaser", "Modern Phaser", ModuleType.MOD, "Based on MXR Phase 90",
                listOf(
                    ParameterDefinition("Speed", 0f, 100f, 40f, "%"),
                    ParameterDefinition("Depth", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Feedback", 0f, 100f, 35f, "%")
                )
            ),
            ModuleModel(
                "mod_univ", "Uni-V", ModuleType.MOD, "Based on Shin-ei Uni-Vibe Optical Chorus",
                listOf(
                    ParameterDefinition("Speed", 0f, 100f, 45f, "%"),
                    ParameterDefinition("Intensity", 0f, 100f, 75f, "%"),
                    ParameterDefinition("Mode", 0f, 1f, 1f, "Chorus/Vibrato", isInteger = true)
                )
            )
        ),
        ModuleType.DLY to listOf(
            ModuleModel(
                "dly_digital", "Digital Delay S", ModuleType.DLY, "Pristine Stereo Digital Delay with Trails",
                listOf(
                    ParameterDefinition("Time", 10f, 1500f, 380f, "ms"),
                    ParameterDefinition("Feedback", 0f, 100f, 35f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 22f, "%"),
                    ParameterDefinition("Trail", 0f, 1f, 1f, "On/Off", isInteger = true)
                )
            ),
            ModuleModel(
                "dly_analog", "Pure Delay", ModuleType.DLY, "Warm BBD Analog Delay",
                listOf(
                    ParameterDefinition("Time", 20f, 1000f, 420f, "ms"),
                    ParameterDefinition("Feedback", 0f, 100f, 45f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 25f, "%"),
                    ParameterDefinition("Mod", 0f, 100f, 15f, "%")
                )
            ),
            ModuleModel(
                "dly_tape", "Tape Echo", ModuleType.DLY, "Vintage Roland RE-201 Space Echo Style",
                listOf(
                    ParameterDefinition("Time", 50f, 1200f, 320f, "ms"),
                    ParameterDefinition("Feedback", 0f, 100f, 40f, "%"),
                    ParameterDefinition("Flutter", 0f, 100f, 25f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 28f, "%")
                )
            ),
            ModuleModel(
                "dly_pingpong", "Ping Pong", ModuleType.DLY, "True Stereo Bouncing Ping-Pong Delay",
                listOf(
                    ParameterDefinition("Time", 50f, 1000f, 450f, "ms"),
                    ParameterDefinition("Feedback", 0f, 100f, 30f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 25f, "%"),
                    ParameterDefinition("Width", 0f, 100f, 85f, "%")
                )
            )
        ),
        ModuleType.RVB to listOf(
            ModuleModel(
                "rvb_room", "Room", ModuleType.RVB, "Natural Studio Live Room Reverb",
                listOf(
                    ParameterDefinition("Decay", 0f, 100f, 35f, "%"),
                    ParameterDefinition("Pre Delay", 0f, 100f, 15f, "ms"),
                    ParameterDefinition("Tone", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 12f, "%"),
                    ParameterDefinition("Trail", 0f, 1f, 1f, "On/Off", isInteger = true)
                )
            ),
            ModuleModel(
                "rvb_hall", "Hall", ModuleType.RVB, "Concert Hall Lush Acoustic Space",
                listOf(
                    ParameterDefinition("Decay", 0f, 100f, 55f, "%"),
                    ParameterDefinition("Pre Delay", 0f, 100f, 25f, "ms"),
                    ParameterDefinition("Tone", 0f, 100f, 45f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 18f, "%")
                )
            ),
            ModuleModel(
                "rvb_spring", "Tube Spring", ModuleType.RVB, "Vintage Twin Spring Tank Reverb",
                listOf(
                    ParameterDefinition("Decay", 0f, 100f, 40f, "%"),
                    ParameterDefinition("Dwell", 0f, 100f, 50f, "%"),
                    ParameterDefinition("Tone", 0f, 100f, 55f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 25f, "%")
                )
            ),
            ModuleModel(
                "rvb_shimmer", "Shimmer", ModuleType.RVB, "Ethereal Pitch-Shifted Octave Shimmer",
                listOf(
                    ParameterDefinition("Decay", 0f, 100f, 70f, "%"),
                    ParameterDefinition("Shimmer Mix", 0f, 100f, 40f, "%"),
                    ParameterDefinition("Mix", 0f, 100f, 22f, "%")
                )
            )
        ),
        ModuleType.VOL to listOf(
            ModuleModel(
                "vol_master", "Volume", ModuleType.VOL, "Master Expression & Level Curve",
                listOf(
                    ParameterDefinition("Volume", 0f, 100f, 100f, "%"),
                    ParameterDefinition("Curve", 0f, 2f, 1f, "Linear/Log", isInteger = true),
                    ParameterDefinition("Min Volume", 0f, 100f, 0f, "%")
                )
            )
        )
    )

    fun getModels(type: ModuleType): List<ModuleModel> = ALL_MODELS[type] ?: emptyList()

    fun getDefaultModel(type: ModuleType): ModuleModel =
        ALL_MODELS[type]?.firstOrNull() ?: ModuleModel(
            type.code.lowercase(),
            type.displayName,
            type,
            "Default",
            listOf(ParameterDefinition("Level", 0f, 100f, 70f, "%"))
        )
}
