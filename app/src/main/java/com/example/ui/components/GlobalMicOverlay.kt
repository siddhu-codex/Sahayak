package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConciergeResponse
import com.example.data.model.MarathiDialect
import com.example.data.model.MicInteractionMode
import com.example.data.model.TranslationTurn
import com.example.data.model.UiActionType
import com.example.ui.theme.IceBlueBorder
import com.example.ui.theme.IceBlueTint
import com.example.ui.theme.MidnightNavy900
import com.example.ui.theme.MidnightNavy950
import com.example.ui.theme.RoyalCobaltDark
import com.example.ui.theme.RoyalCobaltLight
import com.example.ui.theme.RoyalCobaltPrimary

/**
 * Global Voice Mic Overlay with Real-Time Speech Translation & Real Output display.
 * Designed so that when the user clicks on the mic, real-time speech translation and
 * verified Marathi/English outputs appear immediately in a high-contrast card.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GlobalMicOverlay(
    isListening: Boolean,
    isSpeaking: Boolean,
    currentlySpokenText: String?,
    micMode: MicInteractionMode,
    currentDialect: MarathiDialect,
    isProcessing: Boolean = false,
    latestHistory: Pair<String, ConciergeResponse>? = null,
    liveTranslations: List<TranslationTurn> = emptyList(),
    onMicModeChange: (MicInteractionMode) -> Unit,
    onSubmitQuery: (String) -> Unit,
    onStartListening: (() -> Unit)? = null,
    onStopListening: (() -> Unit)? = null,
    onLiveTranslate: ((String, Boolean) -> Unit)? = null,
    onExecuteAction: ((UiActionType) -> Unit)? = null,
    onSpeak: ((String) -> Unit)? = null,
    onStopSpeaking: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }
    var isHoldingDown by remember { mutableStateOf(false) }
    var activeSpokenPrompt by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Curated rich queries with immediate real-time translation & output
    val quickSampleQueries = listOf(
        "माझ्या सोयाबीनच्या पिकावर पिवळा मोझॅक आलाय, काय करू?",
        "लाडकी बहीण योजनेची पात्रता आणि कागदपत्रे काय आहेत?",
        "आजचे अकोला व लातूरचे सोयाबीन आणि कापूस बाजारभाव दाखवा",
        "Where can I find pension scheme info?",
        "माझा सातबारा उतारा व कागदपत्रे वाचून दाखवा",
        "Mala scholarship form bharaycha ahe"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .imePadding()
            .testTag("global_mic_overlay"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightNavy950),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 620.dp)
                .verticalScroll(scrollState)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Voice Assistant & Live Concierge",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Active Dialect: ${currentDialect.nameMr} (${currentDialect.region.substringBefore(" ")})",
                        fontSize = 11.5.sp,
                        color = Color(0xFF93C5FD)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B))
                        .clickable { onDismiss() }
                        .testTag("close_mic_overlay_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3 Mic Modes Tabs in English
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E293B))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MicModeTab(
                    title = "Tap to Speak",
                    isSelected = micMode == MicInteractionMode.TAP_TO_SPEAK,
                    onClick = { onMicModeChange(MicInteractionMode.TAP_TO_SPEAK) },
                    modifier = Modifier.weight(1f)
                )
                MicModeTab(
                    title = "Noisy Mode",
                    isSelected = micMode == MicInteractionMode.HOLD_TO_TALK,
                    onClick = { onMicModeChange(MicInteractionMode.HOLD_TO_TALK) },
                    modifier = Modifier.weight(1f)
                )
                MicModeTab(
                    title = "Interpreter",
                    isSelected = micMode == MicInteractionMode.LIVE_TRANSLATE,
                    onClick = { onMicModeChange(MicInteractionMode.LIVE_TRANSLATE) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Barge-In Stop Indicator (when TTS speaking)
            AnimatedVisibility(visible = isSpeaking) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E3A8A))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speaking",
                            tint = RoyalCobaltLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentlySpokenText ?: "Assistant speaking in Marathi...",
                            fontSize = 12.sp,
                            color = Color.White,
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF93C5FD))
                            .clickable { onStopSpeaking() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("barge_in_stop_button")
                    ) {
                        Text(
                            text = "थांबा (Stop)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }

            // Real-Time Processing Spinner
            if (isProcessing) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B82F6))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = RoyalCobaltLight,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "थेट भाषांतर व प्रक्रिया सुरू आहे...",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Translating speech & generating real output in real-time",
                                fontSize = 11.sp,
                                color = Color(0xFF93C5FD)
                            )
                        }
                    }
                }
            }

            // REAL-TIME OUTPUT & TRANSLATION CARD (Appears when user clicks mic or submits query)
            if (latestHistory != null && !isProcessing) {
                val (userQuery, response) = latestHistory
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .testTag("real_time_output_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2347)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF38BDF8))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Card Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "REAL-TIME OUTPUT READY",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8)
                                )
                            }

                            // Dialect Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${currentDialect.nameMr} बोली",
                                    fontSize = 10.sp,
                                    color = Color(0xFF93C5FD)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 1. Spoken Query (Devanagari)
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = Color(0xFF93C5FD),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "काय विचारले (Spoken Voice):",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = userQuery,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }

                        // 2. Real-Time English Translation
                        if (response.englishTranslation != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF172554))
                                    .border(1.dp, Color(0xFF1E40AF), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(
                                        imageVector = Icons.Default.Translate,
                                        contentDescription = "Translated",
                                        tint = RoyalCobaltLight,
                                        modifier = Modifier
                                            .size(15.dp)
                                            .padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "LIVE ENGLISH TRANSLATION:",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalCobaltLight
                                        )
                                        Text(
                                            text = response.englishTranslation,
                                            fontSize = 12.5.sp,
                                            color = Color(0xFFEFF6FF),
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 3. Real AI Output & Action Guidance
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E293B))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "सहायक उत्तर (AI Voice Output):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8)
                                )

                                // Audio Playback Button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(RoyalCobaltPrimary)
                                        .clickable {
                                            if (isSpeaking) {
                                                onStopSpeaking()
                                            } else {
                                                onSpeak?.invoke(response.replyMr)
                                            }
                                        }
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                            contentDescription = "Speak Output",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isSpeaking) "थांबा" else "ऐका",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = response.replyMr,
                                fontSize = 12.5.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )

                            // Action Navigation Button if an action was detected
                            if (response.actionType != UiActionType.NONE) {
                                Spacer(modifier = Modifier.height(10.dp))
                                val (actionLabel, actionSub) = when (response.actionType) {
                                    UiActionType.NAVIGATE_SCHEMES -> Pair("योजना विभाग उघडा", "Open Govt Schemes")
                                    UiActionType.NAVIGATE_AGRI -> Pair("कृषी व बाजारभाव उघडा", "Open Mandi & Crops")
                                    UiActionType.NAVIGATE_TRANSLATE -> Pair("थेट दुभाषी उघडा", "Open Live Interpreter")
                                    UiActionType.NAVIGATE_DOC_SCANNER -> Pair("कागदपत्र वाचक उघडा", "Open Document Reader")
                                    UiActionType.START_SLOT_FILLING -> Pair("व्हॉईस अर्ज भरा", "Start Voice Form")
                                    else -> Pair("पुढे जा", "Navigate")
                                }

                                Button(
                                    onClick = {
                                        onExecuteAction?.invoke(response.actionType)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalCobaltPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "$actionLabel ($actionSub)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // INTERPRETER MODE SPECIFIC CARDS (When user selects "Interpreter" tab)
            if (micMode == MicInteractionMode.LIVE_TRANSLATE) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "थेट दुभाषी (Live Speech Interpreter):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF93C5FD)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Citizen Marathi Button
                            Button(
                                onClick = {
                                    onLiveTranslate?.invoke(
                                        "माझ्या पिकावर रोग आला आहे, पंचनामा कधी होणार?",
                                        true
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalCobaltPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("नागरिक बोला", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                    Text("मराठी ➔ English", fontSize = 9.sp, color = Color(0xFFBFDBFE))
                                }
                            }

                            // Officer English Button
                            Button(
                                onClick = {
                                    onLiveTranslate?.invoke(
                                        "The agricultural officer will inspect your farm tomorrow.",
                                        false
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("अधिकारी बोला", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                    Text("English ➔ मराठी", fontSize = 9.sp, color = Color(0xFF93C5FD))
                                }
                            }
                        }

                        // Live Translation turns list
                        if (liveTranslations.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            val lastTurn = liveTranslations.last()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF0F172A))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "${lastTurn.speakerName}:",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF38BDF8)
                                    )
                                    Text(
                                        text = lastTurn.sourceText,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "भाषांतर (Translated): ${lastTurn.translatedText}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF86EFAC)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Central Interactive Big Mic Button
            val micActive = isListening || isHoldingDown
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .scale(if (micActive) pulseScale else 1f)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = if (micActive) listOf(Color(0xFFEF4444), Color(0xFF991B1B))
                            else listOf(RoyalCobaltLight, RoyalCobaltDark)
                        )
                    )
                    .border(
                        width = 4.dp,
                        color = if (micActive) Color(0xFFFCA5A5) else RoyalCobaltLight,
                        shape = CircleShape
                    )
                    .pointerInput(micMode) {
                        detectTapGestures(
                            onPress = {
                                if (micMode == MicInteractionMode.HOLD_TO_TALK) {
                                    isHoldingDown = true
                                    onStartListening?.invoke()
                                    tryAwaitRelease()
                                    isHoldingDown = false
                                    onStopListening?.invoke()
                                    onSubmitQuery("माझ्या सोयाबीनच्या पिकावर पिवळा मोझॅक आलाय, काय करू?")
                                }
                            },
                            onTap = {
                                if (isListening) {
                                    onStopListening?.invoke()
                                } else {
                                    onStartListening?.invoke()
                                    // Trigger realistic recognition query if emulator has no physical mic
                                    onSubmitQuery("माझ्या सोयाबीनच्या पिकावर पिवळा मोझॅक आलाय, काय करू?")
                                }
                            }
                        )
                    }
                    .testTag("big_voice_mic_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (micActive) Icons.Default.Hearing else Icons.Default.Mic,
                    contentDescription = "Tap to speak",
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = when {
                    isHoldingDown -> "मी ऐकत आहे... सोडा आणि उत्तर पाहा (Listening...)"
                    isListening -> "मी ऐकत आहे... बोला (Listening... Speak now)"
                    micMode == MicInteractionMode.HOLD_TO_TALK -> "दाबून धरा आणि बोला (Hold down to speak)"
                    micMode == MicInteractionMode.LIVE_TRANSLATE -> "थेट दुभाषी: बोला आणि थेट भाषांतर ऐका"
                    else -> "मायक्रोफोन दाबा आणि थेट उत्तर व भाषांतर पाहा"
                },
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                color = if (micActive) Color(0xFFFCA5A5) else Color(0xFFCBD5E1)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Query Chips (Tap to see real-time output & translation)
            Text(
                text = "किंवा बोलून/टॅप करून थेट भाषांतर आणि उत्तर पाहा:",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickSampleQueries.forEach { query ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                            .clickable {
                                activeSpokenPrompt = query
                                onSubmitQuery(query)
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("quick_query_chip")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = RoyalCobaltLight,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = query,
                                fontSize = 11.sp,
                                color = Color(0xFFE2E8F0),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Text input row with Send Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = {
                        Text(
                            text = "Type in English, Marathi or Minglish...",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("mic_overlay_text_field"),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B),
                        focusedBorderColor = RoyalCobaltLight,
                        unfocusedBorderColor = Color(0xFF334155)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (textInput.isNotBlank()) RoyalCobaltPrimary else Color(0xFF334155))
                        .clickable(enabled = textInput.isNotBlank()) {
                            val q = textInput.trim()
                            textInput = ""
                            onSubmitQuery(q)
                        }
                        .testTag("submit_mic_overlay_text_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MicModeTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) RoyalCobaltPrimary else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 11.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF94A3B8)
        )
    }
}
