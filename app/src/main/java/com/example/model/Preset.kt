package com.example.model

data class ParameterValue(
    val name: String,
    val value: Float
)

data class ModuleState(
    val moduleCode: String,
    val modelName: String,
    val enabled: Boolean,
    val parameters: Map<String, Float> = emptyMap()
) {
    fun getParam(name: String, defaultVal: Float = 50f): Float =
        parameters[name] ?: defaultVal

    fun withParam(name: String, value: Float): ModuleState =
        copy(parameters = parameters + (name to value))

    fun withToggled(): ModuleState = copy(enabled = !enabled)
}

data class Preset(
    val id: Int,
    val bankNumber: Int, // 1 to 50
    val bankLetter: Char, // 'A', 'B', 'C', 'D'
    val name: String,
    val author: String = "GP180 Studio",
    val description: String = "",
    val genre: String = "Rock",
    val patchVolume: Int = 70, // 0 to 100
    val chainOrder: List<String> = ModuleType.DEFAULT_CHAIN,
    val modules: Map<String, ModuleState> = emptyMap(),
    val isFactory: Boolean = false,
    val isFavorite: Boolean = false,
    val artistName: String? = null,
    val songTitle: String? = null,
    val variantName: String? = null,
    val liveReadyApplied: Boolean = false,
    val measuredLufs: Float? = null
) {
    val slotCode: String get() = "${"%02d".format(bankNumber)}-$bankLetter"

    fun getModule(code: String): ModuleState? = modules[code]

    fun updateModule(code: String, updater: (ModuleState) -> ModuleState): Preset {
        val current = modules[code] ?: ModuleState(
            moduleCode = code,
            modelName = ModuleCatalog.getDefaultModel(ModuleType.fromCode(code)).name,
            enabled = true
        )
        return copy(modules = modules + (code to updater(current)))
    }

    fun reorderChain(newChain: List<String>): Preset = copy(chainOrder = newChain)

    fun duplicate(newId: Int, newBank: Int, newLetter: Char, newName: String = "$name (Copy)"): Preset =
        copy(
            id = newId,
            bankNumber = newBank,
            bankLetter = newLetter,
            name = if (newName.length > 16) newName.take(16) else newName,
            isFactory = false
        )
}
