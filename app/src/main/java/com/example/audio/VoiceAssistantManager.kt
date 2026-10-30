package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

class VoiceAssistantManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var speechRecognizer: SpeechRecognizer? = null

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _voiceSpeed = MutableStateFlow(1.0f)
    val voiceSpeed: StateFlow<Float> = _voiceSpeed.asStateFlow()

    private val _currentlySpokenText = MutableStateFlow<String?>(null)
    val currentlySpokenText: StateFlow<String?> = _currentlySpokenText.asStateFlow()

    private val _transcriptFlow = MutableStateFlow<String?>(null)
    val transcriptFlow: StateFlow<String?> = _transcriptFlow.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            // Try Marathi locale first, fallback to Hindi or default
            val marathiLocale = Locale.forLanguageTag("mr-IN")
            val result = tts?.setLanguage(marathiLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Try Indian English or device default
                tts?.setLanguage(Locale.forLanguageTag("en-IN"))
            }
            tts?.setSpeechRate(_voiceSpeed.value)

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlySpokenText.value = null
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlySpokenText.value = null
                }
            })
        }
    }

    fun setSpeechRate(rate: Float) {
        _voiceSpeed.value = rate
        tts?.setSpeechRate(rate)
    }

    fun speak(text: String, onStart: (() -> Unit)? = null, onDone: (() -> Unit)? = null) {
        // Barge-in: stop any active speech first
        stopSpeaking()

        _currentlySpokenText.value = text
        _isSpeaking.value = true
        onStart?.invoke()

        if (isTtsInitialized && tts != null) {
            val params = Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "sahayak_speech_${System.currentTimeMillis()}")
            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "sahayak_speech")
            if (result == TextToSpeech.ERROR) {
                // If TTS failed on this engine, fallback to tone chime and auto-clear
                playChime(660, 200)
                CoroutineScope(Dispatchers.Main).launch {
                    kotlinx.coroutines.delay(2500)
                    _isSpeaking.value = false
                    _currentlySpokenText.value = null
                    onDone?.invoke()
                }
            }
        } else {
            // Emulated TTS tone chime
            playChime(587, 180)
            CoroutineScope(Dispatchers.Main).launch {
                kotlinx.coroutines.delay(2000)
                _isSpeaking.value = false
                _currentlySpokenText.value = null
                onDone?.invoke()
            }
        }
    }

    fun stopSpeaking() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            // ignore
        }
        _isSpeaking.value = false
        _currentlySpokenText.value = null
    }

    fun startListening(onResult: (String) -> Unit) {
        // Barge-in: if currently speaking, interrupt it
        stopSpeaking()
        playChime(880, 120) // high pitch prompt chime
        _isListening.value = true

        try {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                if (speechRecognizer == null) {
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                }

                val intent = android.content.Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "mr-IN")
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "mr-IN")
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }

                speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {}
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        _isListening.value = false
                    }
                    override fun onError(error: Int) {
                        _isListening.value = false
                    }
                    override fun onResults(results: Bundle?) {
                        _isListening.value = false
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull()
                        if (!text.isNullOrBlank()) {
                            _transcriptFlow.value = text
                            onResult(text)
                        }
                    }
                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull()
                        if (!text.isNullOrBlank()) {
                            _transcriptFlow.value = text
                        }
                    }
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
                speechRecognizer?.startListening(intent)
            }
        } catch (e: Exception) {
            _isListening.value = false
        }
    }

    fun stopListening() {
        _isListening.value = false
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            // ignore
        }
    }

    // Audio tone feedback synthesizer (works anywhere, zero external files)
    fun playChime(frequencyHz: Int = 523, durationMs: Int = 150) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val sampleRate = 8000
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val angle = 2.0 * Math.PI * i / (sampleRate / frequencyHz)
                    val envelope = if (i > numSamples - 300) (numSamples - i) / 300.0 else 1.0
                    buffer[i] = (sin(angle) * 32767 * 0.4 * envelope).toInt().toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                kotlinx.coroutines.delay(durationMs.toLong() + 50)
                audioTrack.release()
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun destroy() {
        tts?.stop()
        tts?.shutdown()
        speechRecognizer?.destroy()
    }
}
