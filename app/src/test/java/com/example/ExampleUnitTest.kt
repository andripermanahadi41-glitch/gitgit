package com.example

import com.example.model.FactoryCatalog
import com.example.model.ModuleCatalog
import com.example.model.ModuleType
import com.example.model.Preset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testModuleChainHas12Slots() {
        assertEquals(12, ModuleType.DEFAULT_CHAIN.size)
        assertTrue(ModuleType.DEFAULT_CHAIN.contains("PRE"))
        assertTrue(ModuleType.DEFAULT_CHAIN.contains("SNAP"))
        assertTrue(ModuleType.DEFAULT_CHAIN.contains("AMP"))
        assertTrue(ModuleType.DEFAULT_CHAIN.contains("CAB"))
        assertTrue(ModuleType.DEFAULT_CHAIN.contains("RVB"))
    }

    @Test
    fun testSlotCodeAndPcCalculation() {
        val p1 = Preset(id = 1, bankNumber = 1, bankLetter = 'A', name = "Lead")
        assertEquals("01-A", p1.slotCode)
        val pc1 = (p1.bankNumber - 1) * 4 + (p1.bankLetter - 'A')
        assertEquals(0, pc1)

        val p5 = Preset(id = 5, bankNumber = 2, bankLetter = 'B', name = "Rhythm")
        assertEquals("02-B", p5.slotCode)
        val pc5 = (p5.bankNumber - 1) * 4 + (p5.bankLetter - 'A')
        assertEquals(5, pc5)
    }

    @Test
    fun testFactoryPresetsInitialized() {
        val presets = FactoryCatalog.createInitialPresets()
        assertTrue(presets.isNotEmpty())
        assertTrue(presets.size >= 10)

        val petrucci = presets.firstOrNull { it.name.contains("Petrucci", ignoreCase = true) }
        assertNotNull(petrucci)
        assertEquals("01-A", petrucci?.slotCode)
    }

    @Test
    fun testModuleCatalogHasParameters() {
        val ampModels = ModuleCatalog.getModels(ModuleType.AMP)
        assertTrue(ampModels.isNotEmpty())
        val jcm800 = ampModels.firstOrNull { it.name == "UK 800" }
        assertNotNull(jcm800)
        assertTrue(jcm800!!.parameters.any { it.name == "Gain" })
        assertTrue(jcm800.parameters.any { it.name == "Presence" })
    }
}
