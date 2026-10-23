package com.example.model

data class SongSection(
    val name: String, // e.g. "Intro", "Verse (Clean)", "Chorus (Heavy)", "Solo (Lead)"
    val presetId: Int,
    val slotCode: String,
    val presetName: String
)

data class SetlistSong(
    val id: String,
    val title: String,
    val originalArtist: String = "",
    val musicalKey: String = "E",
    val bpm: Int = 120,
    val sections: List<SongSection> = emptyList(),
    val notes: String = ""
)

data class Setlist(
    val id: String,
    val name: String,
    val venueOrDate: String = "",
    val songs: List<SetlistSong> = emptyList()
)
