package com.example.model

object FactoryCatalog {

    fun createInitialPresets(): List<Preset> {
        val list = mutableListOf<Preset>()

        // 01-A : Petrucci Lead (Reference factory patch)
        list.add(
            Preset(
                id = 1, bankNumber = 1, bankLetter = 'A',
                name = "Petrucci Lead", author = "Valeton",
                description = "Dream Theater Mark IIC+ liquid lead with delay & reverb",
                genre = "Metal", patchVolume = 70, isFactory = true, isFavorite = true,
                artistName = "John Petrucci", songTitle = "Liquid Lead", variantName = "Studio",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP4", true, mapOf("Sustain" to 30f, "Attack" to 30f, "Level" to 70f, "Blend" to 35f)),
                    "WAH" to ModuleState("WAH", "V-Wah", false, mapOf("Position" to 50f, "Range" to 75f)),
                    "DST" to ModuleState("DST", "Green OD", true, mapOf("Gain" to 12f, "Tone" to 55f, "Volume" to 78f)),
                    "SNAP" to ModuleState("SNAP", "SnapTone: Mark IIC+ Lead", true, mapOf("Gain" to 68f, "Volume" to 72f, "Bass" to 35f, "Middle" to 45f, "Treble" to 65f)),
                    "AMP" to ModuleState("AMP", "Mess2C+ 2", true, mapOf("Gain" to 67.6f, "Presence" to 70f, "Volume" to 75f, "Bass" to 35f, "Middle" to 45f, "Treble" to 65f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 25f, "Decay" to 30f)),
                    "CAB" to ModuleState("CAB", "Mess 4x12", true, mapOf("Volume" to 78f, "Low Cut" to 80f, "High Cut" to 10000f)),
                    "EQ" to ModuleState("EQ", "Mess Graphic EQ", true, mapOf("80Hz" to 14f, "240Hz" to -4f, "750Hz" to -18f, "2.2kHz" to 0f, "6.6kHz" to 15f)),
                    "MOD" to ModuleState("MOD", "G-Chorus", false, mapOf("Rate" to 30f, "Depth" to 40f, "Mix" to 25f)),
                    "DLY" to ModuleState("DLY", "Digital Delay S", true, mapOf("Time" to 320f, "Feedback" to 22f, "Mix" to 24f, "Trail" to 1f)),
                    "RVB" to ModuleState("RVB", "Hall", true, mapOf("Decay" to 45f, "Pre Delay" to 20f, "Mix" to 18f, "Tone" to 50f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 01-B : Metallica - Master of Puppets Rhythm
        list.add(
            Preset(
                id = 2, bankNumber = 1, bankLetter = 'B',
                name = "MOP Rythm Fid", author = "Claude/Valeton",
                description = "Metallica album tight scooped thrash rhythm",
                genre = "Metal", patchVolume = 72, isFactory = true, isFavorite = true,
                artistName = "Metallica", songTitle = "Master of Puppets", variantName = "A - Fidèle",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", false),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Green OD", true, mapOf("Gain" to 15f, "Tone" to 65f, "Volume" to 82f)),
                    "SNAP" to ModuleState("SNAP", "SnapTone: Mark IIC+ Lead", true, mapOf("Gain" to 75f, "Volume" to 70f, "Bass" to 40f, "Middle" to 35f, "Treble" to 75f)),
                    "AMP" to ModuleState("AMP", "Mess2C+ 2", true, mapOf("Gain" to 78f, "Presence" to 75f, "Volume" to 72f, "Bass" to 38f, "Middle" to 35f, "Treble" to 70f)),
                    "NR" to ModuleState("NR", "Gate 2", true, mapOf("Threshold" to 48f, "Decay" to 18f)),
                    "CAB" to ModuleState("CAB", "Mess 4x12", true, mapOf("Volume" to 76f, "Low Cut" to 85f, "High Cut" to 9500f)),
                    "EQ" to ModuleState("EQ", "Mess Graphic EQ", true, mapOf("80Hz" to 12f, "240Hz" to -2f, "750Hz" to -15f, "2.2kHz" to 2f, "6.6kHz" to 14f)),
                    "MOD" to ModuleState("MOD", "G-Chorus", false),
                    "DLY" to ModuleState("DLY", "Digital Delay S", false),
                    "RVB" to ModuleState("RVB", "Room", true, mapOf("Decay" to 25f, "Mix" to 8f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 01-C : Pink Floyd - Comfortably Numb Solo
        list.add(
            Preset(
                id = 3, bankNumber = 1, bankLetter = 'C',
                name = "Numb Epic Solo", author = "Valeton",
                description = "David Gilmour soaring Hiwatt & Big Muff solo tone",
                genre = "Rock", patchVolume = 74, isFactory = true, isFavorite = true,
                artistName = "Pink Floyd", songTitle = "Comfortably Numb", variantName = "A - Fidèle",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", true, mapOf("Sustain" to 65f, "Attack" to 40f, "Level" to 75f)),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Big Fuzz", true, mapOf("Sustain" to 72f, "Tone" to 45f, "Volume" to 65f)),
                    "SNAP" to ModuleState("SNAP", "SnapTone: 68 Plexi Crunch", false),
                    "AMP" to ModuleState("AMP", "50s Plexi", true, mapOf("Gain" to 45f, "Presence" to 62f, "Volume" to 78f, "Bass" to 50f, "Middle" to 65f, "Treble" to 58f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 28f)),
                    "CAB" to ModuleState("CAB", "UK 4x12 Green", true, mapOf("Volume" to 80f, "Low Cut" to 80f, "High Cut" to 9000f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", true, mapOf("1.6kHz" to 3f, "4.0kHz" to 2f)),
                    "MOD" to ModuleState("MOD", "Uni-V", false, mapOf("Speed" to 35f, "Intensity" to 60f)),
                    "DLY" to ModuleState("DLY", "Digital Delay S", true, mapOf("Time" to 440f, "Feedback" to 38f, "Mix" to 32f, "Trail" to 1f)),
                    "RVB" to ModuleState("RVB", "Hall", true, mapOf("Decay" to 55f, "Mix" to 22f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 01-D : Dire Straits - Sultans of Swing
        list.add(
            Preset(
                id = 4, bankNumber = 1, bankLetter = 'D',
                name = "Sultans Clean", author = "Valeton",
                description = "Mark Knopfler clean Strat fingerpicked punch",
                genre = "Blues", patchVolume = 68, isFactory = true, isFavorite = true,
                artistName = "Dire Straits", songTitle = "Sultans of Swing", variantName = "A - Fidèle",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", true, mapOf("Sustain" to 55f, "Attack" to 35f, "Level" to 75f)),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Green Boost", false),
                    "SNAP" to ModuleState("SNAP", "SnapTone: 65 Deluxe Clean", false),
                    "AMP" to ModuleState("AMP", "US DLX", true, mapOf("Gain" to 35f, "Presence" to 55f, "Volume" to 82f, "Bass" to 48f, "Middle" to 58f, "Treble" to 68f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 20f)),
                    "CAB" to ModuleState("CAB", "US DLX 1x12", true, mapOf("Volume" to 82f, "Low Cut" to 90f, "High Cut" to 11000f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", true, mapOf("1.6kHz" to 2f, "4.0kHz" to 3f)),
                    "MOD" to ModuleState("MOD", "G-Chorus", false),
                    "DLY" to ModuleState("DLY", "Tape Echo", false),
                    "RVB" to ModuleState("RVB", "Tube Spring", true, mapOf("Decay" to 35f, "Mix" to 22f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 02-A : Van Halen - Brown Sound
        list.add(
            Preset(
                id = 5, bankNumber = 2, bankLetter = 'A',
                name = "Panama 84 Lead", author = "Valeton",
                description = "Eddie Van Halen cranked Plexi Variac brown sound",
                genre = "Rock", patchVolume = 75, isFactory = true, isFavorite = true,
                artistName = "Van Halen", songTitle = "Panama", variantName = "A - Fidèle",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", false),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Green OD", true, mapOf("Gain" to 18f, "Tone" to 60f, "Volume" to 75f)),
                    "SNAP" to ModuleState("SNAP", "SnapTone: 68 Plexi Crunch", true, mapOf("Gain" to 72f, "Volume" to 75f, "Bass" to 55f, "Middle" to 65f, "Treble" to 65f)),
                    "AMP" to ModuleState("AMP", "50s Plexi", true, mapOf("Gain" to 75f, "Presence" to 68f, "Volume" to 74f, "Bass" to 52f, "Middle" to 70f, "Treble" to 64f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 32f)),
                    "CAB" to ModuleState("CAB", "UK 4x12 Green", true, mapOf("Volume" to 78f, "Low Cut" to 85f, "High Cut" to 9500f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", false),
                    "MOD" to ModuleState("MOD", "Modern Phaser", true, mapOf("Speed" to 25f, "Depth" to 75f, "Feedback" to 40f)),
                    "DLY" to ModuleState("DLY", "Tape Echo", true, mapOf("Time" to 280f, "Feedback" to 25f, "Mix" to 20f)),
                    "RVB" to ModuleState("RVB", "Plate", true, mapOf("Decay" to 38f, "Mix" to 16f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 02-B : Guns N' Roses - Sweet Child O' Mine
        list.add(
            Preset(
                id = 6, bankNumber = 2, bankLetter = 'B',
                name = "Sweet Slash Lead", author = "Valeton",
                description = "Slash neck pickup warm singing Marshall tone",
                genre = "Rock", patchVolume = 72, isFactory = true, isFavorite = false,
                artistName = "Guns N' Roses", songTitle = "Sweet Child O' Mine", variantName = "A - Fidèle",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "Green Boost", true, mapOf("Gain" to 20f, "Volume" to 75f)),
                    "WAH" to ModuleState("WAH", "V-Wah", true, mapOf("Position" to 45f, "Range" to 70f)),
                    "DST" to ModuleState("DST", "Super OD", false),
                    "SNAP" to ModuleState("SNAP", "SnapTone: 68 Plexi Crunch", false),
                    "AMP" to ModuleState("AMP", "UK 800", true, mapOf("Gain" to 72f, "Presence" to 55f, "Volume" to 75f, "Bass" to 60f, "Middle" to 68f, "Treble" to 58f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 30f)),
                    "CAB" to ModuleState("CAB", "UK 4x12 Green", true, mapOf("Volume" to 80f, "Low Cut" to 80f, "High Cut" to 9000f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", true, mapOf("1.6kHz" to 3f)),
                    "MOD" to ModuleState("MOD", "G-Chorus", false),
                    "DLY" to ModuleState("DLY", "Digital Delay S", true, mapOf("Time" to 350f, "Feedback" to 30f, "Mix" to 22f)),
                    "RVB" to ModuleState("RVB", "Hall", true, mapOf("Decay" to 42f, "Mix" to 18f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 02-C : Stevie Ray Vaughan - Texas Flood
        list.add(
            Preset(
                id = 7, bankNumber = 2, bankLetter = 'C',
                name = "Texas Blues Man", author = "Valeton",
                description = "SRV blistering cranked Fender & TS808 dynamic bite",
                genre = "Blues", patchVolume = 70, isFactory = true, isFavorite = true,
                artistName = "Stevie Ray Vaughan", songTitle = "Pride and Joy", variantName = "A - Fidèle",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", true, mapOf("Sustain" to 45f, "Attack" to 40f, "Level" to 70f)),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Green OD", true, mapOf("Gain" to 28f, "Tone" to 60f, "Volume" to 80f)),
                    "SNAP" to ModuleState("SNAP", "SnapTone: 65 Deluxe Clean", true, mapOf("Gain" to 55f, "Volume" to 75f, "Bass" to 55f, "Middle" to 65f, "Treble" to 65f)),
                    "AMP" to ModuleState("AMP", "US DLX", true, mapOf("Gain" to 58f, "Presence" to 60f, "Volume" to 78f, "Bass" to 54f, "Middle" to 65f, "Treble" to 62f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 22f)),
                    "CAB" to ModuleState("CAB", "US DLX 1x12", true, mapOf("Volume" to 82f, "Low Cut" to 80f, "High Cut" to 10500f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", false),
                    "MOD" to ModuleState("MOD", "Uni-V", false),
                    "DLY" to ModuleState("DLY", "Tape Echo", false),
                    "RVB" to ModuleState("RVB", "Tube Spring", true, mapOf("Decay" to 40f, "Mix" to 26f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 02-D : Prince - Purple Rain
        list.add(
            Preset(
                id = 8, bankNumber = 2, bankLetter = 'D',
                name = "Purple Rain DC2", author = "Valeton",
                description = "Boss Dimension C chorus into Mesa lead with hall reverb",
                genre = "Pop", patchVolume = 72, isFactory = true, isFavorite = false,
                artistName = "Prince", songTitle = "Purple Rain", variantName = "A - Fidèle",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", true, mapOf("Sustain" to 50f, "Level" to 72f)),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Blues OD", true, mapOf("Gain" to 40f, "Tone" to 55f, "Volume" to 70f)),
                    "SNAP" to ModuleState("SNAP", "SnapTone: Mark IIC+ Lead", false),
                    "AMP" to ModuleState("AMP", "J-120 CL", true, mapOf("Gain" to 25f, "Volume" to 80f, "Bass" to 50f, "Middle" to 55f, "Treble" to 68f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 24f)),
                    "CAB" to ModuleState("CAB", "US DLX 1x12", true, mapOf("Volume" to 80f, "Low Cut" to 80f, "High Cut" to 11000f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", false),
                    "MOD" to ModuleState("MOD", "C-Chorus", true, mapOf("Mode" to 3f, "Mix" to 65f)),
                    "DLY" to ModuleState("DLY", "Digital Delay S", true, mapOf("Time" to 420f, "Feedback" to 32f, "Mix" to 26f)),
                    "RVB" to ModuleState("RVB", "Hall", true, mapOf("Decay" to 65f, "Mix" to 30f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 03-A : Modern Djent High-Gain 5150
        list.add(
            Preset(
                id = 9, bankNumber = 3, bankLetter = 'A',
                name = "Modern Djent 51", author = "Valeton",
                description = "Razor-sharp 8-string palm mute gating with 5150 crunch",
                genre = "Metal", patchVolume = 70, isFactory = true, isFavorite = true,
                artistName = "Periphery / Meshuggah", songTitle = "Djent Anthem", variantName = "High Gain",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP4", false),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Green OD", true, mapOf("Gain" to 10f, "Tone" to 75f, "Volume" to 85f)),
                    "SNAP" to ModuleState("SNAP", "SnapTone: 5150 Block Red", true, mapOf("Gain" to 76f, "Volume" to 68f, "Bass" to 45f, "Middle" to 60f, "Treble" to 65f)),
                    "AMP" to ModuleState("AMP", "EV 51", true, mapOf("Gain" to 72f, "Presence" to 75f, "Volume" to 68f, "Bass" to 45f, "Middle" to 62f, "Treble" to 65f)),
                    "NR" to ModuleState("NR", "Gate 2", true, mapOf("Threshold" to 55f, "Decay" to 12f)),
                    "CAB" to ModuleState("CAB", "EV 4x12", true, mapOf("Volume" to 76f, "Low Cut" to 95f, "High Cut" to 8500f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", true, mapOf("250Hz" to -3f, "1.6kHz" to 3f, "4.0kHz" to 2f)),
                    "MOD" to ModuleState("MOD", "G-Chorus", false),
                    "DLY" to ModuleState("DLY", "Digital Delay S", false),
                    "RVB" to ModuleState("RVB", "Room", true, mapOf("Decay" to 15f, "Mix" to 6f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 03-B : Ambient Shimmer Pad
        list.add(
            Preset(
                id = 10, bankNumber = 3, bankLetter = 'B',
                name = "Ethereal Cloud", author = "Valeton",
                description = "Post-rock atmospheric endless shimmer reverb pad",
                genre = "Ambient", patchVolume = 68, isFactory = true, isFavorite = true,
                artistName = "Post-Rock / Ambient", songTitle = "Stargaze", variantName = "Atmosphere",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", true, mapOf("Sustain" to 75f, "Attack" to 20f, "Level" to 75f)),
                    "WAH" to ModuleState("WAH", "Auto Swell", true, mapOf("Sens" to 70f, "Attack" to 40f, "Decay" to 70f, "Mix" to 90f)),
                    "DST" to ModuleState("DST", "Green Boost", false),
                    "SNAP" to ModuleState("SNAP", "SnapTone: 65 Deluxe Clean", false),
                    "AMP" to ModuleState("AMP", "J-120 CL", true, mapOf("Gain" to 15f, "Volume" to 85f, "Bass" to 50f, "Middle" to 50f, "Treble" to 60f)),
                    "NR" to ModuleState("NR", "Gate 1", false),
                    "CAB" to ModuleState("CAB", "US DLX 1x12", true, mapOf("Volume" to 80f, "Low Cut" to 70f, "High Cut" to 14000f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", false),
                    "MOD" to ModuleState("MOD", "G-Chorus", true, mapOf("Rate" to 25f, "Depth" to 65f, "Mix" to 50f)),
                    "DLY" to ModuleState("DLY", "Ping Pong", true, mapOf("Time" to 550f, "Feedback" to 65f, "Mix" to 45f, "Width" to 95f)),
                    "RVB" to ModuleState("RVB", "Shimmer", true, mapOf("Decay" to 85f, "Shimmer Mix" to 55f, "Mix" to 50f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 03-C : Acoustic Guitar Simulator
        list.add(
            Preset(
                id = 11, bankNumber = 3, bankLetter = 'C',
                name = "Acoustic Natural", author = "Valeton",
                description = "Transform electric guitar pickups into lush acoustic flat-top resonance",
                genre = "Acoustic", patchVolume = 69, isFactory = true, isFavorite = false,
                artistName = "Acoustic", songTitle = "Unplugged", variantName = "Dreadnought",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", true, mapOf("Sustain" to 40f, "Level" to 75f)),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Green Boost", false),
                    "SNAP" to ModuleState("SNAP", "SnapTone: 65 Deluxe Clean", false),
                    "AMP" to ModuleState("AMP", "J-120 CL", true, mapOf("Gain" to 10f, "Volume" to 88f, "Bass" to 45f, "Middle" to 40f, "Treble" to 70f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 18f)),
                    "CAB" to ModuleState("CAB", "User Custom IR", true, mapOf("Slot" to 1f, "Volume" to 85f, "Low Cut" to 80f, "High Cut" to 16000f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", true, mapOf("100Hz" to 1f, "250Hz" to -3f, "630Hz" to -2f, "1.6kHz" to 2f, "4.0kHz" to 4f)),
                    "MOD" to ModuleState("MOD", "G-Chorus", false),
                    "DLY" to ModuleState("DLY", "Digital Delay S", false),
                    "RVB" to ModuleState("RVB", "Hall", true, mapOf("Decay" to 35f, "Mix" to 16f)),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        // 03-D : Bass Modern Darkglass Crunch
        list.add(
            Preset(
                id = 12, bankNumber = 3, bankLetter = 'D',
                name = "Dark Bass Punch", author = "Valeton",
                description = "Modern clanky bass distortion with clean low-end blend",
                genre = "Bass", patchVolume = 72, isFactory = true, isFavorite = false,
                artistName = "Modern Bass", songTitle = "Progressive Bass", variantName = "B3K",
                modules = mapOf(
                    "PRE" to ModuleState("PRE", "COMP", true, mapOf("Sustain" to 60f, "Attack" to 25f, "Level" to 78f)),
                    "WAH" to ModuleState("WAH", "V-Wah", false),
                    "DST" to ModuleState("DST", "Darktale", true, mapOf("Drive" to 68f, "Blend" to 65f, "Attack" to 70f, "Grunt" to 85f)),
                    "SNAP" to ModuleState("SNAP", "SnapTone: SLO 100 Hot", false),
                    "AMP" to ModuleState("AMP", "UK 800", true, mapOf("Gain" to 40f, "Volume" to 80f, "Bass" to 70f, "Middle" to 55f, "Treble" to 60f)),
                    "NR" to ModuleState("NR", "Gate 1", true, mapOf("Threshold" to 30f)),
                    "CAB" to ModuleState("CAB", "EV 4x12", true, mapOf("Volume" to 82f, "Low Cut" to 40f, "High Cut" to 7500f)),
                    "EQ" to ModuleState("EQ", "Guitar EQ 1", true, mapOf("100Hz" to 4f, "630Hz" to -3f, "1.6kHz" to 3f)),
                    "MOD" to ModuleState("MOD", "G-Chorus", false),
                    "DLY" to ModuleState("DLY", "Digital Delay S", false),
                    "RVB" to ModuleState("RVB", "Room", false),
                    "VOL" to ModuleState("VOL", "Volume", true, mapOf("Volume" to 100f))
                )
            )
        )

        return list
    }

    fun createInitialSetlists(presets: List<Preset>): List<Setlist> {
        val p1 = presets.getOrNull(0) ?: return emptyList()
        val p2 = presets.getOrNull(1) ?: p1
        val p3 = presets.getOrNull(2) ?: p1
        val p4 = presets.getOrNull(3) ?: p1
        val p5 = presets.getOrNull(4) ?: p1
        val p6 = presets.getOrNull(5) ?: p1

        return listOf(
            Setlist(
                id = "set_1",
                name = "Stadium Tour 2026",
                venueOrDate = "Madison Square Garden - Headliner",
                songs = listOf(
                    SetlistSong(
                        id = "song_1",
                        title = "Master of Puppets",
                        originalArtist = "Metallica",
                        musicalKey = "E Minor",
                        bpm = 212,
                        sections = listOf(
                            SongSection("Main Riff", p2.id, p2.slotCode, p2.name),
                            SongSection("Clean Interlude", p4.id, p4.slotCode, p4.name),
                            SongSection("Kirk Solo", p1.id, p1.slotCode, p1.name)
                        ),
                        notes = "Fast downpicking, kick on Gate 2 tight"
                    ),
                    SetlistSong(
                        id = "song_2",
                        title = "Comfortably Numb",
                        originalArtist = "Pink Floyd",
                        musicalKey = "B Minor",
                        bpm = 127,
                        sections = listOf(
                            SongSection("Acoustic Verse", p4.id, p4.slotCode, p4.name),
                            SongSection("Final Solo", p3.id, p3.slotCode, p3.name)
                        ),
                        notes = "Expression pedal up for delay feedback swelling"
                    ),
                    SetlistSong(
                        id = "song_3",
                        title = "Sweet Child O' Mine",
                        originalArtist = "Guns N' Roses",
                        musicalKey = "D# Minor",
                        bpm = 125,
                        sections = listOf(
                            SongSection("Intro Riff", p6.id, p6.slotCode, p6.name),
                            SongSection("Verse Crunch", p5.id, p5.slotCode, p5.name),
                            SongSection("Wah Wah Outro", p6.id, p6.slotCode, p6.name)
                        ),
                        notes = "Neck pickup on intro, bridge on solo"
                    )
                )
            ),
            Setlist(
                id = "set_2",
                name = "Pub & Club Rockers",
                venueOrDate = "Weekend Club Gig",
                songs = listOf(
                    SetlistSong(
                        id = "pub_1",
                        title = "Sultans of Swing",
                        originalArtist = "Dire Straits",
                        musicalKey = "D Minor",
                        bpm = 148,
                        sections = listOf(
                            SongSection("Fingerstyle Clean", p4.id, p4.slotCode, p4.name)
                        ),
                        notes = "Position 2 (bridge + middle) on Strat"
                    ),
                    SetlistSong(
                        id = "pub_2",
                        title = "Panama",
                        originalArtist = "Van Halen",
                        musicalKey = "E",
                        bpm = 142,
                        sections = listOf(
                            SongSection("Drive Rhythm", p5.id, p5.slotCode, p5.name),
                            SongSection("Brown Lead", p5.id, p5.slotCode, p5.name)
                        ),
                        notes = "Phaser 90 engaged during verse breakdown"
                    )
                )
            )
        )
    }
}
