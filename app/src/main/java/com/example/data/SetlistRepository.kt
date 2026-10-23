package com.example.data

import android.content.Context
import com.example.model.FactoryCatalog
import com.example.model.Preset
import com.example.model.Setlist
import com.example.model.SetlistSong
import com.example.model.SongSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class SetlistRepository(private val context: Context, private val presetRepo: PresetRepository) {

    private val prefs = context.getSharedPreferences("gp180_setlists_prefs", Context.MODE_PRIVATE)

    private val _setlists = MutableStateFlow<List<Setlist>>(emptyList())
    val setlists: StateFlow<List<Setlist>> = _setlists.asStateFlow()

    private val _activeSetlistId = MutableStateFlow<String?>("set_1")
    val activeSetlistId: StateFlow<String?> = _activeSetlistId.asStateFlow()

    private val _activeSongId = MutableStateFlow<String?>("song_1")
    val activeSongId: StateFlow<String?> = _activeSongId.asStateFlow()

    private val _activeSectionIndex = MutableStateFlow(0)
    val activeSectionIndex: StateFlow<Int> = _activeSectionIndex.asStateFlow()

    init {
        loadSetlists()
    }

    fun getActiveSetlist(): Setlist? =
        _setlists.value.firstOrNull { it.id == _activeSetlistId.value }
            ?: _setlists.value.firstOrNull()

    fun getActiveSong(): SetlistSong? {
        val currentSet = getActiveSetlist() ?: return null
        return currentSet.songs.firstOrNull { it.id == _activeSongId.value }
            ?: currentSet.songs.firstOrNull()
    }

    fun selectSetlist(id: String) {
        _activeSetlistId.value = id
        val set = _setlists.value.firstOrNull { it.id == id }
        _activeSongId.value = set?.songs?.firstOrNull()?.id
        _activeSectionIndex.value = 0
    }

    fun selectSong(songId: String) {
        _activeSongId.value = songId
        _activeSectionIndex.value = 0
        // Switch preset to song's first section preset
        val song = getActiveSong()
        val firstSection = song?.sections?.firstOrNull()
        if (firstSection != null) {
            presetRepo.selectPreset(firstSection.presetId)
        }
    }

    fun selectSection(index: Int) {
        _activeSectionIndex.value = index
        val song = getActiveSong()
        val section = song?.sections?.getOrNull(index)
        if (section != null) {
            presetRepo.selectPreset(section.presetId)
        }
    }

    fun nextSong() {
        val currentSet = getActiveSetlist() ?: return
        val currentIdx = currentSet.songs.indexOfFirst { it.id == _activeSongId.value }
        if (currentIdx in 0 until currentSet.songs.lastIndex) {
            selectSong(currentSet.songs[currentIdx + 1].id)
        }
    }

    fun previousSong() {
        val currentSet = getActiveSetlist() ?: return
        val currentIdx = currentSet.songs.indexOfFirst { it.id == _activeSongId.value }
        if (currentIdx > 0) {
            selectSong(currentSet.songs[currentIdx - 1].id)
        }
    }

    fun createSetlist(name: String, venue: String): Setlist {
        val newSet = Setlist(
            id = "set_" + System.currentTimeMillis(),
            name = name.ifBlank { "New Live Set" },
            venueOrDate = venue,
            songs = emptyList()
        )
        val updated = _setlists.value + newSet
        _setlists.value = updated
        saveToPrefs(updated)
        selectSetlist(newSet.id)
        return newSet
    }

    fun addSongToActiveSetlist(title: String, artist: String, key: String, bpm: Int, preset: Preset) {
        val currentSet = getActiveSetlist() ?: return
        val newSong = SetlistSong(
            id = "song_" + System.currentTimeMillis(),
            title = title.ifBlank { "Untitled Song" },
            originalArtist = artist,
            musicalKey = key,
            bpm = bpm,
            sections = listOf(
                SongSection("Main Tone", preset.id, preset.slotCode, preset.name)
            )
        )
        val updatedSongs = currentSet.songs + newSong
        val updatedSet = currentSet.copy(songs = updatedSongs)
        val updatedList = _setlists.value.map { if (it.id == updatedSet.id) updatedSet else it }
        _setlists.value = updatedList
        saveToPrefs(updatedList)
        selectSong(newSong.id)
    }

    fun addSectionToActiveSong(name: String, preset: Preset) {
        val currentSet = getActiveSetlist() ?: return
        val currentSong = getActiveSong() ?: return
        val newSection = SongSection(name, preset.id, preset.slotCode, preset.name)
        val updatedSong = currentSong.copy(sections = currentSong.sections + newSection)
        val updatedSongs = currentSet.songs.map { if (it.id == updatedSong.id) updatedSong else it }
        val updatedSet = currentSet.copy(songs = updatedSongs)
        val updatedList = _setlists.value.map { if (it.id == updatedSet.id) updatedSet else it }
        _setlists.value = updatedList
        saveToPrefs(updatedList)
    }

    fun deleteActiveSetlist() {
        val currentSet = getActiveSetlist() ?: return
        if (_setlists.value.size <= 1) return // Keep at least one
        val updated = _setlists.value.filterNot { it.id == currentSet.id }
        _setlists.value = updated
        saveToPrefs(updated)
        selectSetlist(updated.first().id)
    }

    private fun loadSetlists() {
        val savedJson = prefs.getString("setlists_json", null)
        if (savedJson.isNullOrBlank()) {
            val initial = FactoryCatalog.createInitialSetlists(presetRepo.presets.value)
            _setlists.value = initial
            saveToPrefs(initial)
        } else {
            try {
                val list = deserializeSetlists(savedJson)
                if (list.isEmpty()) {
                    val initial = FactoryCatalog.createInitialSetlists(presetRepo.presets.value)
                    _setlists.value = initial
                    saveToPrefs(initial)
                } else {
                    _setlists.value = list
                }
            } catch (e: Exception) {
                val initial = FactoryCatalog.createInitialSetlists(presetRepo.presets.value)
                _setlists.value = initial
                saveToPrefs(initial)
            }
        }
    }

    private fun saveToPrefs(setlists: List<Setlist>) {
        try {
            val arr = JSONArray()
            for (s in setlists) {
                val sObj = JSONObject().apply {
                    put("id", s.id)
                    put("name", s.name)
                    put("venueOrDate", s.venueOrDate)
                    val songsArr = JSONArray()
                    for (song in s.songs) {
                        val songObj = JSONObject().apply {
                            put("id", song.id)
                            put("title", song.title)
                            put("originalArtist", song.originalArtist)
                            put("musicalKey", song.musicalKey)
                            put("bpm", song.bpm)
                            put("notes", song.notes)
                            val secArr = JSONArray()
                            for (sec in song.sections) {
                                val secObj = JSONObject().apply {
                                    put("name", sec.name)
                                    put("presetId", sec.presetId)
                                    put("slotCode", sec.slotCode)
                                    put("presetName", sec.presetName)
                                }
                                secArr.put(secObj)
                            }
                            put("sections", secArr)
                        }
                        songsArr.put(songObj)
                    }
                    put("songs", songsArr)
                }
                arr.put(sObj)
            }
            prefs.edit().putString("setlists_json", arr.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deserializeSetlists(json: String): List<Setlist> {
        val list = mutableListOf<Setlist>()
        val arr = JSONArray(json)
        for (i in 0 until arr.length()) {
            val sObj = arr.getJSONObject(i)
            val songsList = mutableListOf<SetlistSong>()
            val songsArr = sObj.optJSONArray("songs")
            if (songsArr != null) {
                for (j in 0 until songsArr.length()) {
                    val songObj = songsArr.getJSONObject(j)
                    val secList = mutableListOf<SongSection>()
                    val secArr = songObj.optJSONArray("sections")
                    if (secArr != null) {
                        for (k in 0 until secArr.length()) {
                            val secObj = secArr.getJSONObject(k)
                            secList.add(
                                SongSection(
                                    name = secObj.getString("name"),
                                    presetId = secObj.getInt("presetId"),
                                    slotCode = secObj.getString("slotCode"),
                                    presetName = secObj.getString("presetName")
                                )
                            )
                        }
                    }
                    songsList.add(
                        SetlistSong(
                            id = songObj.getString("id"),
                            title = songObj.getString("title"),
                            originalArtist = songObj.optString("originalArtist", ""),
                            musicalKey = songObj.optString("musicalKey", "E"),
                            bpm = songObj.optInt("bpm", 120),
                            notes = songObj.optString("notes", ""),
                            sections = secList
                        )
                    )
                }
            }
            list.add(
                Setlist(
                    id = sObj.getString("id"),
                    name = sObj.getString("name"),
                    venueOrDate = sObj.optString("venueOrDate", ""),
                    songs = songsList
                )
            )
        }
        return list
    }
}
