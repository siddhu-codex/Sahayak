package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.VoiceAssistantManager
import com.example.data.ai.AppGuideBotService
import com.example.data.ai.GeminiAiService
import com.example.data.ai.GuideChatMessage
import com.example.data.model.AgriMarketItem
import com.example.data.model.ConciergeResponse
import com.example.data.model.DocumentSample
import com.example.data.model.MarathiDialect
import com.example.data.model.MicInteractionMode
import com.example.data.model.SchemeItem
import com.example.data.model.TranslationTurn
import com.example.data.model.UiActionType
import com.example.data.model.UserPersona
import com.example.data.repository.SahayakRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class Screen(val id: String, val titleMr: String, val titleEn: String) {
    object Home : Screen("home", "Dashboard", "Dashboard")
    object Schemes : Screen("schemes", "Govt Schemes", "Govt Schemes")
    object AgriMandi : Screen("agri", "Agri & Mandi", "Agri & Mandi")
    object LiveTranslate : Screen("translate", "Live Interpreter", "Live Interpreter")
    object DocumentScanner : Screen("scanner", "Document OCR", "Document OCR")
}

data class SlotFillingState(
    val isActive: Boolean = false,
    val currentStepIndex: Int = 0,
    val schemeTitle: String = "मुख्यमंत्री माझी लाडकी बहीण योजना",
    val questions: List<String> = listOf(
        "तुमचा जिल्हा कोणता आहे?",
        "तुमच्या कुटुंबाचे वार्षिक उत्पन्न २.५ लाख रुपयांपेक्षा कमी आहे का?",
        "तुमच्याकडे आधार लिंक केलेले बँक खाते आहे का?"
    ),
    val answers: MutableMap<Int, String> = mutableMapOf(),
    val isComplete: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val voiceManager = VoiceAssistantManager(application)

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _persona = MutableStateFlow(UserPersona.KISAN)
    val persona: StateFlow<UserPersona> = _persona.asStateFlow()

    private val _dialect = MutableStateFlow(MarathiDialect.VARHADI)
    val dialect: StateFlow<MarathiDialect> = _dialect.asStateFlow()

    private val _micMode = MutableStateFlow(MicInteractionMode.TAP_TO_SPEAK)
    val micMode: StateFlow<MicInteractionMode> = _micMode.asStateFlow()

    private val _isLargeFont = MutableStateFlow(false)
    val isLargeFont: StateFlow<Boolean> = _isLargeFont.asStateFlow()

    private val _isConciergeOpen = MutableStateFlow(false)
    val isConciergeOpen: StateFlow<Boolean> = _isConciergeOpen.asStateFlow()

    private val _conciergeHistory = MutableStateFlow<List<Pair<String, ConciergeResponse>>>(emptyList())
    val conciergeHistory: StateFlow<List<Pair<String, ConciergeResponse>>> = _conciergeHistory.asStateFlow()

    private val _isProcessingQuery = MutableStateFlow(false)
    val isProcessingQuery: StateFlow<Boolean> = _isProcessingQuery.asStateFlow()

    private val _liveTranslations = MutableStateFlow<List<TranslationTurn>>(
        listOf(
            TranslationTurn(
                isUserMarathi = true,
                speakerName = "तुकाराम पाटील (शेतकरी)",
                sourceText = "माझ्या सोयाबीनच्या पिकावर पिवळा मोझॅक आलाय, या ॲपमध्ये कुठे तक्रार करू?",
                sourceDialectOrLang = "वऱ्हाडी (Varhadi)",
                normalizedMarathi = "माझ्या सोयाबीनच्या पिकावर पिवळा मोझॅक रोग आला आहे, मी या ॲपमध्ये कुठे मदत मागू शकतो?",
                translatedText = "My soybean crop has Yellow Mosaic disease; where can I seek help in this app?",
                targetLang = "English"
            ),
            TranslationTurn(
                isUserMarathi = false,
                speakerName = "Officer / Agri Expert",
                sourceText = "Please upload a photo of the affected leaf in the Crop Doctor section.",
                sourceDialectOrLang = "English",
                normalizedMarathi = null,
                translatedText = "कृपया पीक सल्ला विभागात जाऊन बाधित पानाचा फोटो अपलोड करा.",
                targetLang = "मराठी"
            )
        )
    )
    val liveTranslations: StateFlow<List<TranslationTurn>> = _liveTranslations.asStateFlow()

    private val _selectedDocument = MutableStateFlow(SahayakRepository.documentSamples.first())
    val selectedDocument: StateFlow<DocumentSample> = _selectedDocument.asStateFlow()

    private val _slotFillingState = MutableStateFlow(SlotFillingState())
    val slotFillingState: StateFlow<SlotFillingState> = _slotFillingState.asStateFlow()

    private val _lastActionNotice = MutableStateFlow<String?>(null)
    val lastActionNotice: StateFlow<String?> = _lastActionNotice.asStateFlow()

    private val _isGuideBotOpen = MutableStateFlow(false)
    val isGuideBotOpen: StateFlow<Boolean> = _isGuideBotOpen.asStateFlow()

    private val _guideBotMessages = MutableStateFlow<List<GuideChatMessage>>(
        listOf(AppGuideBotService.initialWelcomeMessage)
    )
    val guideBotMessages: StateFlow<List<GuideChatMessage>> = _guideBotMessages.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        _lastActionNotice.value = null
    }

    fun setPersona(newPersona: UserPersona) {
        _persona.value = newPersona
        when (newPersona) {
            UserPersona.AAJI -> {
                _isLargeFont.value = true
                _dialect.value = MarathiDialect.PRAMAAN
                voiceManager.setSpeechRate(0.75f)
            }
            UserPersona.KISAN -> {
                _isLargeFont.value = false
                _dialect.value = MarathiDialect.VARHADI
                voiceManager.setSpeechRate(1.0f)
            }
            UserPersona.YUVA -> {
                _isLargeFont.value = false
                _dialect.value = MarathiDialect.MINGLISH
                voiceManager.setSpeechRate(1.25f)
            }
        }
    }

    fun setDialect(newDialect: MarathiDialect) {
        _dialect.value = newDialect
    }

    fun setMicMode(newMode: MicInteractionMode) {
        _micMode.value = newMode
    }

    fun toggleLargeFont() {
        _isLargeFont.value = !_isLargeFont.value
    }

    fun setVoiceSpeed(rate: Float) {
        voiceManager.setSpeechRate(rate)
    }

    fun openConcierge(isOpen: Boolean) {
        _isConciergeOpen.value = isOpen
    }

    fun selectDocumentSample(doc: DocumentSample) {
        _selectedDocument.value = doc
    }

    // Play welcome audio (PRD section 6: 6-second warm Marathi welcome guide)
    fun playWelcomeAudio() {
        val welcomeGreeting = when (_dialect.value) {
            MarathiDialect.VARHADI -> "राम राम बापू! सहायक ॲपमध्ये तुमचं स्वागत आहे. शासकीय योजना, हमीभाव आणि कागदपत्रे वाचण्यासाठी मधला मायक्रोफोन दाबा!"
            MarathiDialect.AHIRANI -> "राम राम भौ! सहायक ॲपमा तुमना स्वागत शे. शासकीय योजना अन हमीभाव वाचनासाठी मायक्रोफोन दाबा!"
            MarathiDialect.MALVANI -> "नमस्कार रे! सहायक ॲपमध्ये तुमका स्वागत आसा. शासकीय योजना आणि बाजारभाव माहितीसाठी मायक्रोफोन दाबा!"
            else -> "नमस्कार! सहायक ॲपमध्ये आपले मनःपूर्वक स्वागत आहे. कोणत्याही सेवेसाठी खालील हिरवे मायक्रोफोन बटण दाबून बोला."
        }
        voiceManager.speak(welcomeGreeting)
    }

    // Read current screen aloud (PRD Section 3.1 & 4: "या पानावर काय लिहिले आहे?")
    fun readCurrentScreenAloud() {
        viewModelScope.launch {
            val response = GeminiAiService.queryConcierge(
                userQuery = "या पानावर काय लिहिले आहे ते सांगा",
                dialect = _dialect.value,
                currentScreenId = _currentScreen.value.id
            )
            voiceManager.speak(response.replyMr)
        }
    }

    // Execute user query via Voice Concierge & Agentic Actions
    fun processVoiceQuery(query: String) {
        viewModelScope.launch {
            _isProcessingQuery.value = true
            _isConciergeOpen.value = true

            val response = GeminiAiService.queryConcierge(
                userQuery = query,
                dialect = _dialect.value,
                currentScreenId = _currentScreen.value.id
            )

            _conciergeHistory.value = _conciergeHistory.value + (query to response)
            _isProcessingQuery.value = false

            // Speak the reply aloud in natural Marathi
            voiceManager.speak(response.replyMr)

            // Deep-link agentic UI actions
            when (response.actionType) {
                UiActionType.NAVIGATE_SCHEMES -> {
                    _currentScreen.value = Screen.Schemes
                    _lastActionNotice.value = "योजना विभाग उघडला (Scheme Section Opened)"
                }
                UiActionType.NAVIGATE_AGRI -> {
                    _currentScreen.value = Screen.AgriMandi
                    _lastActionNotice.value = "कृषी बाजारभाव विभाग उघडला (Agri Section Opened)"
                }
                UiActionType.NAVIGATE_TRANSLATE -> {
                    _currentScreen.value = Screen.LiveTranslate
                    _lastActionNotice.value = "थेट दुभाषी उघडला (Live Interpreter Opened)"
                }
                UiActionType.NAVIGATE_DOC_SCANNER -> {
                    _currentScreen.value = Screen.DocumentScanner
                    _lastActionNotice.value = "कागदपत्र वाचक उघडला (Doc Scanner Opened)"
                }
                UiActionType.ADJUST_VOICE_SPEED -> {
                    _lastActionNotice.value = "आवाज वेग समायोजित केला"
                }
                UiActionType.START_SLOT_FILLING -> {
                    startSlotFilling("लाडकी बहीण योजना")
                }
                else -> Unit
            }
        }
    }

    // Live Speech Translation bi-directional
    fun addLiveTranslation(spokenText: String, isMarathi: Boolean, targetLang: String) {
        viewModelScope.launch {
            voiceManager.playChime(784, 120)
            val (normMr, translated) = GeminiAiService.translateSpeechLive(
                speechText = spokenText,
                sourceIsMarathi = isMarathi,
                dialect = _dialect.value,
                targetLang = targetLang
            )

            val turn = TranslationTurn(
                isUserMarathi = isMarathi,
                speakerName = if (isMarathi) "नागरिक (Citizen - ${_dialect.value.nameMr})" else "अधिकारी (Official - $targetLang)",
                sourceText = spokenText,
                sourceDialectOrLang = if (isMarathi) _dialect.value.nameMr else targetLang,
                normalizedMarathi = if (isMarathi) normMr else null,
                translatedText = translated,
                targetLang = if (isMarathi) targetLang else "मराठी"
            )

            _liveTranslations.value = _liveTranslations.value + turn

            // Play the translated audio
            voiceManager.speak(translated)
        }
    }

    fun clearLiveTranslations() {
        _liveTranslations.value = emptyList()
    }

    fun openGuideBot(isOpen: Boolean) {
        _isGuideBotOpen.value = isOpen
        if (isOpen && _guideBotMessages.value.isEmpty()) {
            _guideBotMessages.value = listOf(AppGuideBotService.initialWelcomeMessage)
        }
    }

    fun sendGuideBotQuestion(question: String) {
        val userMsg = GuideChatMessage(
            isUser = true,
            text = question
        )
        _guideBotMessages.value = _guideBotMessages.value + userMsg

        viewModelScope.launch {
            val botReply = AppGuideBotService.answerQuestion(question)
            _guideBotMessages.value = _guideBotMessages.value + botReply

            botReply.spokenAudioText?.let {
                voiceManager.speak(it)
            }
        }
    }

    fun startGuideVoiceInput() {
        voiceManager.startListening { query ->
            sendGuideBotQuestion(query)
        }
    }

    fun stopGuideVoiceInput() {
        voiceManager.stopListening()
    }

    fun startOverlayVoiceListening() {
        voiceManager.startListening { query ->
            processVoiceQuery(query)
        }
    }

    fun executeGuideAction(actionType: UiActionType) {
        when (actionType) {
            UiActionType.NAVIGATE_SCHEMES -> _currentScreen.value = Screen.Schemes
            UiActionType.NAVIGATE_AGRI -> _currentScreen.value = Screen.AgriMandi
            UiActionType.NAVIGATE_TRANSLATE -> _currentScreen.value = Screen.LiveTranslate
            UiActionType.NAVIGATE_DOC_SCANNER -> _currentScreen.value = Screen.DocumentScanner
            UiActionType.ADJUST_VOICE_SPEED -> toggleLargeFont()
            UiActionType.READ_SCREEN -> readCurrentScreenAloud()
            UiActionType.START_SLOT_FILLING -> startSlotFilling("लाडकी बहीण योजना")
            else -> _currentScreen.value = Screen.Home
        }
    }

    fun startSlotFilling(schemeName: String) {
        _slotFillingState.value = SlotFillingState(
            isActive = true,
            schemeTitle = schemeName,
            currentStepIndex = 0
        )
        val firstQ = _slotFillingState.value.questions[0]
        voiceManager.speak(firstQ)
    }

    fun answerSlotFillingStep(answer: String) {
        val curr = _slotFillingState.value
        val newAnswers = curr.answers.toMutableMap()
        newAnswers[curr.currentStepIndex] = answer
        val nextIndex = curr.currentStepIndex + 1

        if (nextIndex < curr.questions.size) {
            _slotFillingState.value = curr.copy(
                currentStepIndex = nextIndex,
                answers = newAnswers
            )
            val nextQ = curr.questions[nextIndex]
            voiceManager.speak(nextQ)
        } else {
            _slotFillingState.value = curr.copy(
                isComplete = true,
                answers = newAnswers
            )
            val congrats = "छान! आपले सर्व तपशील जमा झाले आहेत. आपण या योजनेसाठी १००% पात्र आहात. अधिकृत महा-ई-सेवा केंद्रात अर्ज पाठवला जात आहे."
            voiceManager.speak(congrats)
        }
    }

    fun resetSlotFilling() {
        _slotFillingState.value = SlotFillingState(isActive = false)
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.destroy()
    }
}
