package com.example.audio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

data class LufsMeasurement(
    val momentaryLufs: Float,
    val shortTermLufs: Float,
    val integratedLufs: Float,
    val peakDb: Float,
    val targetLufs: Float = -16.0f
) {
    val deltaDb: Float get() = integratedLufs - targetLufs
    val isWithinTolerance: Boolean get() = kotlin.math.abs(deltaDb) <= 0.6f
}

class LufsAnalyzer(private val scope: CoroutineScope) {

    private val _isMeasuring = MutableStateFlow(false)
    val isMeasuring: StateFlow<Boolean> = _isMeasuring.asStateFlow()

    private val _measurement = MutableStateFlow(
        LufsMeasurement(
            momentaryLufs = -18.2f,
            shortTermLufs = -17.5f,
            integratedLufs = -16.8f,
            peakDb = -2.1f,
            targetLufs = -16.0f
        )
    )
    val measurement: StateFlow<LufsMeasurement> = _measurement.asStateFlow()

    private var measureJob: Job? = null

    fun startMeasurement(targetLufs: Float = -16.0f, baseToneGain: Float = 60f) {
        _isMeasuring.value = true
        measureJob?.cancel()
        measureJob = scope.launch {
            var integratedSum = 0f
            var count = 0

            while (isActive) {
                delay(120)
                // Realistic guitar playing dynamics simulation
                val strumFluctuation = (Random.nextFloat() - 0.5f) * 4.0f
                val baseline = -18.0f + (baseToneGain - 50f) * 0.15f
                val currentMom = (baseline + strumFluctuation).coerceIn(-45f, -6f)
                val currentShort = (baseline + strumFluctuation * 0.4f).coerceIn(-40f, -8f)

                integratedSum += currentShort
                count++
                val currentInt = integratedSum / count

                _measurement.value = LufsMeasurement(
                    momentaryLufs = currentMom,
                    shortTermLufs = currentShort,
                    integratedLufs = currentInt,
                    peakDb = (currentMom + 8.5f).coerceAtMost(0f),
                    targetLufs = targetLufs
                )
            }
        }
    }

    fun stopMeasurement() {
        _isMeasuring.value = false
        measureJob?.cancel()
        measureJob = null
    }

    fun reset() {
        stopMeasurement()
        _measurement.value = LufsMeasurement(
            momentaryLufs = -70f,
            shortTermLufs = -70f,
            integratedLufs = -70f,
            peakDb = -70f,
            targetLufs = -16.0f
        )
    }
}
