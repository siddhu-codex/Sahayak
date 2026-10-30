package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.AppGuideChatDialog
import com.example.ui.components.GlobalMicOverlay
import com.example.ui.components.SahayakBottomNav
import com.example.ui.components.SahayakTopBar
import com.example.ui.components.VoiceSlotFillingDialog
import com.example.ui.screens.AgriMandiScreen
import com.example.ui.screens.DocumentScannerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveTranslateScreen
import com.example.ui.screens.SchemesScreen
import com.example.ui.theme.SahayakTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SahayakTheme {
                SahayakApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SahayakApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val persona by viewModel.persona.collectAsState()
    val dialect by viewModel.dialect.collectAsState()
    val micMode by viewModel.micMode.collectAsState()
    val isLargeFont by viewModel.isLargeFont.collectAsState()
    val isConciergeOpen by viewModel.isConciergeOpen.collectAsState()
    val isSpeaking by viewModel.voiceManager.isSpeaking.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val currentlySpokenText by viewModel.voiceManager.currentlySpokenText.collectAsState()
    val conciergeHistory by viewModel.conciergeHistory.collectAsState()
    val liveTranslations by viewModel.liveTranslations.collectAsState()
    val selectedDoc by viewModel.selectedDocument.collectAsState()
    val slotState by viewModel.slotFillingState.collectAsState()
    val lastActionNotice by viewModel.lastActionNotice.collectAsState()
    val isGuideBotOpen by viewModel.isGuideBotOpen.collectAsState()
    val guideBotMessages by viewModel.guideBotMessages.collectAsState()
    val isProcessingQuery by viewModel.isProcessingQuery.collectAsState()

    // Handle back button on sub-screens
    if (currentScreen != Screen.Home) {
        BackHandler {
            viewModel.navigateTo(Screen.Home)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("sahayak_main_scaffold"),
        topBar = {
            SahayakTopBar(
                currentPersona = persona,
                currentDialect = dialect,
                isLargeFont = isLargeFont,
                onPersonaChange = { viewModel.setPersona(it) },
                onDialectChange = { viewModel.setDialect(it) },
                onToggleFontSize = { viewModel.toggleLargeFont() },
                onReadScreen = { viewModel.readCurrentScreenAloud() },
                onOpenGuideBot = { viewModel.openGuideBot(true) }
            )
        },
        bottomBar = {
            SahayakBottomNav(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) },
                onMicClick = { viewModel.openConcierge(true) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Home -> HomeScreen(
                    persona = persona,
                    dialect = dialect,
                    isLargeFont = isLargeFont,
                    isSpeaking = isSpeaking,
                    currentlySpokenText = currentlySpokenText,
                    conciergeHistory = conciergeHistory,
                    lastActionNotice = lastActionNotice,
                    onNavigate = { viewModel.navigateTo(it) },
                    onSpeak = { viewModel.voiceManager.speak(it) },
                    onStopSpeaking = { viewModel.voiceManager.stopSpeaking() },
                    onPlayWelcomeAudio = { viewModel.playWelcomeAudio() },
                    onTriggerMic = { viewModel.openConcierge(true) },
                    onSetPersona = { viewModel.setPersona(it) },
                    onStartVoiceSlotFilling = { viewModel.startSlotFilling("मुख्यमंत्री लाडकी बहीण योजना") },
                    onOpenGuideBot = { viewModel.openGuideBot(true) }
                )

                Screen.Schemes -> SchemesScreen(
                    isLargeFont = isLargeFont,
                    isSpeaking = isSpeaking,
                    currentlySpokenText = currentlySpokenText,
                    onSpeak = { viewModel.voiceManager.speak(it) },
                    onStopSpeaking = { viewModel.voiceManager.stopSpeaking() },
                    onStartSlotFilling = { viewModel.startSlotFilling(it) }
                )

                Screen.AgriMandi -> AgriMandiScreen(
                    isLargeFont = isLargeFont,
                    isSpeaking = isSpeaking,
                    currentlySpokenText = currentlySpokenText,
                    onSpeak = { viewModel.voiceManager.speak(it) },
                    onStopSpeaking = { viewModel.voiceManager.stopSpeaking() }
                )

                Screen.LiveTranslate -> LiveTranslateScreen(
                    currentDialect = dialect,
                    liveTranslations = liveTranslations,
                    isLargeFont = isLargeFont,
                    isSpeaking = isSpeaking,
                    currentlySpokenText = currentlySpokenText,
                    onAddTranslation = { text, isMarathi, target ->
                        viewModel.addLiveTranslation(text, isMarathi, target)
                    },
                    onClearTranslations = { viewModel.clearLiveTranslations() },
                    onSpeak = { viewModel.voiceManager.speak(it) },
                    onStopSpeaking = { viewModel.voiceManager.stopSpeaking() },
                    onNavigateToScanner = { viewModel.navigateTo(Screen.DocumentScanner) }
                )

                Screen.DocumentScanner -> DocumentScannerScreen(
                    selectedDocument = selectedDoc,
                    isLargeFont = isLargeFont,
                    isSpeaking = isSpeaking,
                    currentlySpokenText = currentlySpokenText,
                    onSelectDocument = { viewModel.selectDocumentSample(it) },
                    onSpeak = { viewModel.voiceManager.speak(it) },
                    onStopSpeaking = { viewModel.voiceManager.stopSpeaking() },
                    onNavigateToInterpreter = { viewModel.navigateTo(Screen.LiveTranslate) }
                )
            }

            // Global Mic Overlay Sheet / Dialog
            if (isConciergeOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    GlobalMicOverlay(
                        isListening = isListening,
                        isSpeaking = isSpeaking,
                        currentlySpokenText = currentlySpokenText,
                        micMode = micMode,
                        currentDialect = dialect,
                        isProcessing = isProcessingQuery,
                        latestHistory = conciergeHistory.lastOrNull(),
                        liveTranslations = liveTranslations,
                        onMicModeChange = { viewModel.setMicMode(it) },
                        onSubmitQuery = { query ->
                            viewModel.processVoiceQuery(query)
                        },
                        onStartListening = { viewModel.startOverlayVoiceListening() },
                        onStopListening = { viewModel.voiceManager.stopListening() },
                        onLiveTranslate = { text, isMarathi ->
                            viewModel.addLiveTranslation(text, isMarathi, if (isMarathi) "English" else "मराठी")
                        },
                        onExecuteAction = { actionType ->
                            viewModel.executeGuideAction(actionType)
                            viewModel.openConcierge(false)
                        },
                        onSpeak = { viewModel.voiceManager.speak(it) },
                        onStopSpeaking = { viewModel.voiceManager.stopSpeaking() },
                        onDismiss = { viewModel.openConcierge(false) }
                    )
                }
            }

            // Voice Slot-Filling Interview Dialog
            if (slotState.isActive) {
                VoiceSlotFillingDialog(
                    state = slotState,
                    onAnswer = { answer ->
                        viewModel.answerSlotFillingStep(answer)
                    },
                    onDismiss = { viewModel.resetSlotFilling() }
                )
            }

            // App Guide & Help Chatbot Dialog ("How to use this app")
            if (isGuideBotOpen) {
                AppGuideChatDialog(
                    messages = guideBotMessages,
                    isSpeaking = isSpeaking,
                    currentlySpokenText = currentlySpokenText,
                    isListening = isListening,
                    onSendMessage = { query ->
                        viewModel.sendGuideBotQuestion(query)
                    },
                    onExecuteAction = { action ->
                        viewModel.executeGuideAction(action)
                    },
                    onSpeak = { viewModel.voiceManager.speak(it) },
                    onStopSpeaking = { viewModel.voiceManager.stopSpeaking() },
                    onStartVoiceInput = { viewModel.startGuideVoiceInput() },
                    onStopVoiceInput = { viewModel.stopGuideVoiceInput() },
                    onDismiss = { viewModel.openGuideBot(false) }
                )
            }
        }
    }
}
