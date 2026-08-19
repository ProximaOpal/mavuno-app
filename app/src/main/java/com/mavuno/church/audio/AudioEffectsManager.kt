package com.mavuno.church.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object AudioEffectsManager {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val _isSoundEnabled = MutableStateFlow(true)
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    private const val SAMPLE_RATE = 44100

    fun toggleSound(enabled: Boolean) {
        _isSoundEnabled.value = enabled
    }

    /**
     * Ultra-Premium Velvet Micro-Tap
     * Warm, damped transient with organic low-mid body (subtle 380Hz to 160Hz drop + 1.2kHz initial click)
     */
    fun playTap() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val durationMs = 32
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val normTime = i.toDouble() / numSamples
                // Smooth bell envelope (no click artifacts)
                val env = (1.0 - normTime) * exp(-normTime * 3.5)
                val freq = 420.0 * (1.0 - normTime * 0.5)
                val fundamental = sin(2.0 * PI * freq * t)
                val overtone = sin(2.0 * PI * 1180.0 * t) * 0.25 * exp(-normTime * 8.0)
                val sample = (fundamental + overtone) * env
                buffer[i] = (sample * Short.MAX_VALUE * 0.35).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    /**
     * Crystal Glass Bubble Tone for suggestion pills & interactive chips
     * Pure dual harmonic (940Hz + 1880Hz) with silky bell decay
     */
    fun playPillClick() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val durationMs = 55
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val normTime = i.toDouble() / numSamples
                val env = exp(-normTime * 4.2)
                val sweepFreq = 880.0 + (sin(normTime * PI * 0.5) * 320.0)
                val wave1 = sin(2.0 * PI * sweepFreq * t)
                val wave2 = sin(2.0 * PI * (sweepFreq * 2.0) * t) * 0.3
                val sample = (wave1 + wave2) * env
                buffer[i] = (sample * Short.MAX_VALUE * 0.32).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    /**
     * Luxurious Silk Navigation Pop for bottom tabs
     * Deep, warm acoustic thud (260Hz -> 140Hz)
     */
    fun playTabSwitch() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val durationMs = 45
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val normTime = i.toDouble() / numSamples
                val env = (1.0 - normTime) * (1.0 - normTime)
                val freq = 280.0 - (normTime * 140.0)
                val wave = sin(2.0 * PI * freq * t) + (sin(2.0 * PI * (freq * 0.5) * t) * 0.4)
                buffer[i] = (wave * env * Short.MAX_VALUE * 0.4).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    /**
     * Ethereal Celestial 4-Note Chime for Ella AI Activation
     * Ascending harmonic triad: E5 (659.3Hz) -> G#5 (830.6Hz) -> B5 (987.8Hz) -> E6 (1318.5Hz)
     */
    fun playEllaChime() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val noteDurationMs = 65
            val notes = doubleArrayOf(659.25, 830.61, 987.77, 1318.51)
            val totalSamples = (SAMPLE_RATE * (noteDurationMs * notes.size + 180)) / 1000
            val buffer = ShortArray(totalSamples)

            val noteSamples = (SAMPLE_RATE * noteDurationMs) / 1000
            var offset = 0

            for (freq in notes) {
                for (i in 0 until (noteSamples + 2000)) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val norm = i.toDouble() / (noteSamples + 2000)
                    val env = exp(-norm * 4.5)
                    val fundamental = sin(2.0 * PI * freq * t)
                    val shimmer = sin(2.0 * PI * (freq * 2.0) * t) * 0.25
                    val sample = (fundamental + shimmer) * env * 0.35
                    val targetIdx = offset + i
                    if (targetIdx < totalSamples) {
                        val currentVal = buffer[targetIdx].toInt()
                        val newVal = (currentVal + (sample * Short.MAX_VALUE).toInt()).coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                        buffer[targetIdx] = newVal.toShort()
                    }
                }
                offset += noteSamples
            }
            playPcmBuffer(buffer)
        }
    }

    /**
     * Warm Protective Safety Pulse for Content Shield Overlay
     * Soft minor-third dual tone (A4: 440Hz + C5: 523.3Hz)
     */
    fun playShieldAlert() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val durationMs = 190
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val norm = i.toDouble() / numSamples
                // Smooth attack and soft exponential release
                val attack = if (norm < 0.1) norm / 0.1 else 1.0
                val decay = exp(-norm * 3.0)
                val env = attack * decay
                val wave1 = sin(2.0 * PI * 440.0 * t)
                val wave2 = sin(2.0 * PI * 523.25 * t) * 0.7
                val wave3 = sin(2.0 * PI * 220.0 * t) * 0.4
                val sample = (wave1 + wave2 + wave3) * env * 0.28
                buffer[i] = (sample * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    /**
     * Resonant Golden Affirmation Chord for Parental Unlock & Success
     * Polyphonic C-Major chord (523Hz + 659Hz + 784Hz + 1046Hz) with rich decay
     */
    fun playSuccessChime() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val durationMs = 280
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            val chord = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val norm = i.toDouble() / numSamples
                val attack = if (norm < 0.05) norm / 0.05 else 1.0
                val decay = exp(-norm * 3.6)
                val env = attack * decay

                var sum = 0.0
                for (f in chord) {
                    sum += sin(2.0 * PI * f * t)
                }
                val sample = (sum / chord.size) * env * 0.38
                buffer[i] = (sample * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    /**
     * High-precision Action Dispatch sound for Ella Autonomous Automation & Coordinates
     */
    fun playActionDispatch() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val durationMs = 22
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val norm = i.toDouble() / numSamples
                val env = exp(-norm * 6.0)
                val freq = 1600.0 - (norm * 700.0)
                val wave = sin(2.0 * PI * freq * t)
                buffer[i] = (wave * env * Short.MAX_VALUE * 0.3).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    private fun playPcmBuffer(buffer: ShortArray) {
        try {
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            track.setNotificationMarkerPosition(buffer.size)
            track.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(t: AudioTrack?) {
                    t?.release()
                }

                override fun onPeriodicNotification(t: AudioTrack?) {}
            })
        } catch (_: Exception) {
            // AudioTrack safety fallback
        }
    }
}
