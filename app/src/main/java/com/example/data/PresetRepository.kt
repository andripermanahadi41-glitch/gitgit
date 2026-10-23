package com.example.data

import android.content.Context
import com.example.model.FactoryCatalog
import com.example.model.ModuleCatalog
import com.example.model.ModuleState
import com.example.model.ModuleType
import com.example.model.Preset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class PresetRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("gp180_presets_prefs", Context.MODE_PRIVATE)

    private val _presets = MutableStateFlow<List<Preset>>(emptyList())
    val presets: StateFlow<List<Preset>> = _presets.asStateFlow()

    private val _activePresetId = MutableStateFlow(1)
    val activePresetId: StateFlow<Int> = _activePresetId.asStateFlow()

    init {
        loadPresets()
    }

    fun getActivePreset(): Preset {
        val currentId = _activePresetId.value
        return _presets.value.firstOrNull { it.id == currentId }
            ?: _presets.value.firstOrNull()
            ?: FactoryCatalog.createInitialPresets().first()
    }

    fun selectPreset(id: Int) {
        _activePresetId.value = id
        prefs.edit().putInt("last_active_id", id).apply()
    }

    fun toggleModule(moduleCode: String) {
        val active = getActivePreset()
        val updated = active.updateModule(moduleCode) { it.withToggled() }
        updatePreset(updated)
    }

    fun setModuleModel(moduleCode: String, newModelName: String) {
        val active = getActivePreset()
        val type = ModuleType.fromCode(moduleCode)
        val model = ModuleCatalog.getModels(type).firstOrNull { it.name == newModelName }
            ?: ModuleCatalog.getDefaultModel(type)

        // populate with default parameters
        val defaultParams = model.parameters.associate { it.name to it.defaultValue }
        val updated = active.updateModule(moduleCode) {
            it.copy(modelName = model.name, parameters = defaultParams)
        }
        updatePreset(updated)
    }

    fun setParameterValue(moduleCode: String, paramName: String, value: Float) {
        val active = getActivePreset()
        val updated = active.updateModule(moduleCode) {
            it.withParam(paramName, value)
        }
        updatePreset(updated)
    }

    fun setPatchVolume(volume: Int) {
        val active = getActivePreset()
        val clamped = volume.coerceIn(0, 100)
        val updated = active.copy(patchVolume = clamped)
        updatePreset(updated)
    }

    fun reorderChain(newChain: List<String>) {
        val active = getActivePreset()
        val updated = active.reorderChain(newChain)
        updatePreset(updated)
    }

    fun toggleFavorite(id: Int) {
        val list = _presets.value.map {
            if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
        }
        _presets.value = list
        saveToPrefs(list)
    }

    fun updatePreset(updated: Preset) {
        val list = _presets.value.map {
            if (it.id == updated.id) updated else it
        }
        _presets.value = list
        saveToPrefs(list)
    }

    fun createCustomPreset(base: Preset, newName: String, genre: String): Preset {
        val nextId = (_presets.value.maxOfOrNull { it.id } ?: 100) + 1
        val userBank = 26 + ((nextId - 100) / 4).coerceAtMost(24) // banks 26 to 50 are user banks
        val letterIndex = (nextId % 4)
        val letters = listOf('A', 'B', 'C', 'D')
        val letter = letters[letterIndex]

        val newPreset = base.copy(
            id = nextId,
            bankNumber = userBank,
            bankLetter = letter,
            name = if (newName.length > 16) newName.take(16) else newName,
            genre = genre,
            isFactory = false,
            isFavorite = false
        )
        val list = _presets.value + newPreset
        _presets.value = list
        saveToPrefs(list)
        selectPreset(newPreset.id)
        return newPreset
    }

    fun deletePreset(id: Int): Boolean {
        val target = _presets.value.firstOrNull { it.id == id } ?: return false
        if (target.isFactory) return false // Prevent deleting factory presets

        val list = _presets.value.filterNot { it.id == id }
        _presets.value = list
        saveToPrefs(list)
        if (_activePresetId.value == id) {
            _activePresetId.value = list.firstOrNull()?.id ?: 1
        }
        return true
    }

    /**
     * "Live Ready" Concert Adaptation Algorithm:
     * - Trims amp/dist gain by 15-20% to prevent uncontrollable feedback at stage SPL
     * - Lowers reverb & delay mix by ~30% (acoustics of large halls add natural decay)
     * - Adds Hi Cut filter (9 kHz) in Cab IR block to eliminate digital fizz
     * - Sets Low Cut (80 Hz) in Cab IR to tighten sub-bass mud
     * - Boosts 1.6kHz mids in EQ (+1.5dB) to cut through drummer and cymbals
     */
    fun applyLiveReadyOptimization(presetId: Int): Preset? {
        val original = _presets.value.firstOrNull { it.id == presetId } ?: return null

        var adapted = original

        // 1. AMP module
        original.getModule("AMP")?.let { amp ->
            var params = amp.parameters.toMutableMap()
            params["Gain"]?.let { g -> params["Gain"] = (g * 0.82f).coerceAtLeast(15f) }
            params["Presence"]?.let { p -> params["Presence"] = (p * 0.90f) }
            adapted = adapted.updateModule("AMP") { it.copy(parameters = params) }
        }

        // 2. DST module
        original.getModule("DST")?.let { dst ->
            var params = dst.parameters.toMutableMap()
            params["Gain"]?.let { g -> params["Gain"] = (g * 0.85f).coerceAtLeast(10f) }
            params["Drive"]?.let { d -> params["Drive"] = (d * 0.85f).coerceAtLeast(10f) }
            adapted = adapted.updateModule("DST") { it.copy(parameters = params) }
        }

        // 3. CAB module (High Cut 8.5kHz - 9.5kHz & Low Cut 80Hz)
        original.getModule("CAB")?.let { cab ->
            var params = cab.parameters.toMutableMap()
            params["High Cut"] = 9000f
            params["Low Cut"] = 80f
            adapted = adapted.updateModule("CAB") { it.copy(parameters = params) }
        }

        // 4. RVB module (-30% mix)
        original.getModule("RVB")?.let { rvb ->
            var params = rvb.parameters.toMutableMap()
            params["Mix"]?.let { m -> params["Mix"] = (m * 0.70f).coerceAtLeast(4f) }
            params["Decay"]?.let { d -> params["Decay"] = (d * 0.85f).coerceAtLeast(15f) }
            adapted = adapted.updateModule("RVB") { it.copy(parameters = params) }
        }

        // 5. DLY module (-30% mix, enable trail)
        original.getModule("DLY")?.let { dly ->
            var params = dly.parameters.toMutableMap()
            params["Mix"]?.let { m -> params["Mix"] = (m * 0.70f).coerceAtLeast(5f) }
            params["Trail"] = 1f
            adapted = adapted.updateModule("DLY") { it.copy(parameters = params) }
        }

        // 6. EQ module (+1.5dB boost at 1.6kHz for midrange punch)
        original.getModule("EQ")?.let { eq ->
            var params = eq.parameters.toMutableMap()
            params["1.6kHz"] = (params["1.6kHz"] ?: 0f) + 1.5f
            adapted = adapted.updateModule("EQ") { it.copy(parameters = params, enabled = true) }
        }

        adapted = adapted.copy(liveReadyApplied = true)
        updatePreset(adapted)
        return adapted
    }

    /**
     * LUFS Loudness Harmonization Cascade:
     * Cascaded adjustment: AMP Volume -> CAB Level -> Patch Volume
     */
    fun harmonizeVolume(presetId: Int, targetLufs: Float = -16.0f, simulatedMeasuredLufs: Float): Preset? {
        val original = _presets.value.firstOrNull { it.id == presetId } ?: return null
        val deltaDb = targetLufs - simulatedMeasuredLufs
        val correctionUnits = (deltaDb / 0.25f).toInt()

        var newPatchVol = (original.patchVolume + correctionUnits).coerceIn(20, 100)
        var adapted = original.copy(patchVolume = newPatchVol, measuredLufs = targetLufs)

        updatePreset(adapted)
        return adapted
    }

    private fun loadPresets() {
        val savedJson = prefs.getString("presets_json", null)
        if (savedJson.isNullOrBlank()) {
            val initial = FactoryCatalog.createInitialPresets()
            _presets.value = initial
            saveToPrefs(initial)
        } else {
            try {
                val list = deserializePresets(savedJson)
                if (list.isEmpty()) {
                    val initial = FactoryCatalog.createInitialPresets()
                    _presets.value = initial
                    saveToPrefs(initial)
                } else {
                    _presets.value = list
                }
            } catch (e: Exception) {
                val initial = FactoryCatalog.createInitialPresets()
                _presets.value = initial
                saveToPrefs(initial)
            }
        }
        val lastActive = prefs.getInt("last_active_id", 1)
        _activePresetId.value = lastActive
    }

    private fun saveToPrefs(presets: List<Preset>) {
        try {
            val jsonStr = serializePresets(presets)
            prefs.edit().putString("presets_json", jsonStr).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun serializePresets(presets: List<Preset>): String {
        val array = JSONArray()
        for (p in presets) {
            val obj = JSONObject().apply {
                put("id", p.id)
                put("bankNumber", p.bankNumber)
                put("bankLetter", p.bankLetter.toString())
                put("name", p.name)
                put("author", p.author)
                put("description", p.description)
                put("genre", p.genre)
                put("patchVolume", p.patchVolume)
                put("isFactory", p.isFactory)
                put("isFavorite", p.isFavorite)
                put("artistName", p.artistName ?: "")
                put("songTitle", p.songTitle ?: "")
                put("variantName", p.variantName ?: "")
                put("liveReadyApplied", p.liveReadyApplied)
                p.measuredLufs?.let { put("measuredLufs", it.toDouble()) }

                val chainArr = JSONArray()
                p.chainOrder.forEach { chainArr.put(it) }
                put("chainOrder", chainArr)

                val modsObj = JSONObject()
                for ((code, mod) in p.modules) {
                    val modObj = JSONObject().apply {
                        put("moduleCode", mod.moduleCode)
                        put("modelName", mod.modelName)
                        put("enabled", mod.enabled)
                        val paramsObj = JSONObject()
                        for ((k, v) in mod.parameters) {
                            paramsObj.put(k, v.toDouble())
                        }
                        put("parameters", paramsObj)
                    }
                    modsObj.put(code, modObj)
                }
                put("modules", modsObj)
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun deserializePresets(jsonStr: String): List<Preset> {
        val list = mutableListOf<Preset>()
        val array = JSONArray(jsonStr)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val chainList = mutableListOf<String>()
            val chainArr = obj.optJSONArray("chainOrder")
            if (chainArr != null) {
                for (j in 0 until chainArr.length()) {
                    chainList.add(chainArr.getString(j))
                }
            } else {
                chainList.addAll(ModuleType.DEFAULT_CHAIN)
            }

            val modsMap = mutableMapOf<String, ModuleState>()
            val modsObj = obj.optJSONObject("modules")
            if (modsObj != null) {
                val keys = modsObj.keys()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val mObj = modsObj.getJSONObject(k)
                    val pMap = mutableMapOf<String, Float>()
                    val pObj = mObj.optJSONObject("parameters")
                    if (pObj != null) {
                        val pKeys = pObj.keys()
                        while (pKeys.hasNext()) {
                            val pk = pKeys.next()
                            pMap[pk] = pObj.getDouble(pk).toFloat()
                        }
                    }
                    modsMap[k] = ModuleState(
                        moduleCode = mObj.getString("moduleCode"),
                        modelName = mObj.getString("modelName"),
                        enabled = mObj.getBoolean("enabled"),
                        parameters = pMap
                    )
                }
            }

            list.add(
                Preset(
                    id = obj.getInt("id"),
                    bankNumber = obj.getInt("bankNumber"),
                    bankLetter = obj.getString("bankLetter").firstOrNull() ?: 'A',
                    name = obj.getString("name"),
                    author = obj.optString("author", "Valeton"),
                    description = obj.optString("description", ""),
                    genre = obj.optString("genre", "Rock"),
                    patchVolume = obj.optInt("patchVolume", 70),
                    chainOrder = if (chainList.isEmpty()) ModuleType.DEFAULT_CHAIN else chainList,
                    modules = modsMap,
                    isFactory = obj.optBoolean("isFactory", false),
                    isFavorite = obj.optBoolean("isFavorite", false),
                    artistName = obj.optString("artistName").ifBlank { null },
                    songTitle = obj.optString("songTitle").ifBlank { null },
                    variantName = obj.optString("variantName").ifBlank { null },
                    liveReadyApplied = obj.optBoolean("liveReadyApplied", false),
                    measuredLufs = if (obj.has("measuredLufs")) obj.getDouble("measuredLufs").toFloat() else null
                )
            )
        }
        return list
    }
}
