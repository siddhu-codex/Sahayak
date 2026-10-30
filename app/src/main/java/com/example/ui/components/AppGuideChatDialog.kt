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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.ai.AppGuideBotService
import com.example.data.ai.GuideChatMessage
import com.example.data.model.UiActionType
import com.example.ui.theme.CardBorderSubtle
import com.example.ui.theme.IceBlueBorder
import com.example.ui.theme.IceBlueTint
import com.example.ui.theme.MidnightNavy950
import com.example.ui.theme.RoyalCobaltDark
import com.example.ui.theme.RoyalCobaltPrimary
import com.example.ui.theme.TextSlateMuted
import com.example.ui.theme.TextSlatePrimary

/**
 * Interactive Voice-First App Guide Chatbot Dialog ("Sahayak Guide" / ॲप मार्गदर्शक).
 * Built with full voice microphone support so non-tech citizens, farmers, women, and seniors
 * can speak naturally or tap spoken voice pills without having to type.
 */
@Composable
fun AppGuideChatDialog(
    messages: List<GuideChatMessage>,
    isSpeaking: Boolean,
    currentlySpokenText: String?,
    isListening: Boolean = false,
    onSendMessage: (String) -> Unit,
    onExecuteAction: (UiActionType) -> Unit,
    onSpeak: (String) -> Unit,
    onStopSpeaking: () -> Unit,
    onStartVoiceInput: (() -> Unit)? = null,
    onStopVoiceInput: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var isVoicePanelOpen by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Pulse animation for the listening mic
    val infiniteTransition = rememberInfiniteTransition(label = "guide_bot_voice_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_pulse"
    )

    // Waveform heights
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_anim"
    )

    // Spoken query recommendations designed for non-tech citizens
    val spokenSuggestions = listOf(
        "हे ॲप कसे चालवायचे?",
        "लाडकी बहीण योजनेची माहिती द्या",
        "माझ्या पिकावर रोग आलाय, काय करू?",
        "बाजारभाव व हमीभाव कसा बघायचा?",
        "सातबारा उतारा कसा वाचायचा?",
        "थेट भाषांतर (Live Translation) कसे काम करते?",
        "अक्षरांचा आकार मोठा कसा करायचा?"
    )

    // Auto-scroll to latest message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.60f))
                .padding(horizontal = 12.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
                    .shadow(24.dp, RoundedCornerShape(24.dp))
                    .testTag("app_guide_chat_dialog"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header (Midnight Navy)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MidnightNavy950)
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(RoyalCobaltPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SmartToy,
                                        contentDescription = "Guide Bot",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Sahayak App Guide",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF10B981))
                                                .padding(horizontal = 6.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "VOICE READY",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                    Text(
                                        text = "ॲप कसे वापरावे याबद्दल बोलून विचारा",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF93C5FD)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Voice Mode Indicator / Toggle in Top Bar
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isVoicePanelOpen || isListening) Color(0xFFEF4444) else RoyalCobaltPrimary)
                                        .clickable {
                                            if (isVoicePanelOpen || isListening) {
                                                isVoicePanelOpen = false
                                                onStopVoiceInput?.invoke()
                                            } else {
                                                isVoicePanelOpen = true
                                                onStartVoiceInput?.invoke()
                                            }
                                        }
                                        .padding(horizontal = 8.dp, vertical = 5.dp)
                                        .testTag("top_bar_voice_toggle")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isVoicePanelOpen || isListening) Icons.Default.Stop else Icons.Default.Mic,
                                            contentDescription = "Toggle Mic",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isVoicePanelOpen || isListening) "Stop" else "बोला",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1E293B))
                                        .testTag("close_guide_bot_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close Guide",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Messages List
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 14.dp),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(messages, key = { it.id }) { msg ->
                            if (msg.isUser) {
                                // User Message Bubble (Right)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.84f)
                                            .clip(RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp))
                                            .background(RoyalCobaltPrimary)
                                            .padding(14.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.Top) {
                                            Icon(
                                                imageVector = Icons.Default.Mic,
                                                contentDescription = null,
                                                tint = Color.White.copy(alpha = 0.85f),
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .padding(top = 2.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = msg.text,
                                                color = Color.White,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Bot Message Bubble (Left)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(0.94f),
                                        shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = IceBlueTint),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, IceBlueBorder)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.SmartToy,
                                                        contentDescription = null,
                                                        tint = RoyalCobaltPrimary,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "Sahayak Guide (मार्गदर्शक)",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = RoyalCobaltDark
                                                    )
                                                }

                                                // Listen Audio button for non-tech citizens
                                                if (msg.spokenAudioText != null) {
                                                    val isCurrentlySpeaking = isSpeaking && currentlySpokenText == msg.spokenAudioText
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(if (isCurrentlySpeaking) RoyalCobaltPrimary else Color.White)
                                                            .border(1.dp, IceBlueBorder, RoundedCornerShape(12.dp))
                                                            .clickable {
                                                                if (isCurrentlySpeaking) {
                                                                    onStopSpeaking()
                                                                } else {
                                                                    onSpeak(msg.spokenAudioText)
                                                                }
                                                            }
                                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(
                                                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                                contentDescription = "Listen",
                                                                tint = if (isCurrentlySpeaking) Color.White else RoyalCobaltPrimary,
                                                                modifier = Modifier.size(14.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(
                                                                text = if (isCurrentlySpeaking) "थांबा (Stop)" else "ऐका (Listen)",
                                                                fontSize = 11.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = if (isCurrentlySpeaking) Color.White else RoyalCobaltPrimary
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = msg.text,
                                                color = TextSlatePrimary,
                                                fontSize = 13.sp,
                                                lineHeight = 19.sp
                                            )

                                            // Action Button (Navigate directly to feature)
                                            if (msg.suggestedActionTitle != null && msg.suggestedActionType != null) {
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Button(
                                                    onClick = {
                                                        onExecuteAction(msg.suggestedActionType)
                                                        onDismiss()
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalCobaltPrimary),
                                                    shape = RoundedCornerShape(12.dp),
                                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                                    modifier = Modifier.testTag("guide_action_button")
                                                ) {
                                                    Text(
                                                        text = msg.suggestedActionTitle,
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
                        }
                    }

                    // Dedicated Voice Assistant Mode for Non-Tech Users
                    AnimatedVisibility(
                        visible = isVoicePanelOpen || isListening,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("guide_bot_voice_panel"),
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF)),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalCobaltPrimary.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFEF4444))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "मी ऐकत आहे... (Listening...)",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalCobaltDark
                                        )
                                    }

                                    // Switch to keyboard button
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White)
                                            .border(1.dp, IceBlueBorder, RoundedCornerShape(8.dp))
                                            .clickable {
                                                isVoicePanelOpen = false
                                                onStopVoiceInput?.invoke()
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Keyboard,
                                                contentDescription = "Keyboard",
                                                tint = TextSlateMuted,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Keyboard",
                                                fontSize = 11.sp,
                                                color = TextSlateMuted
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Central Pulsing Voice Mic Button
                                Box(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .scale(pulseScale)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                colors = listOf(
                                                    RoyalCobaltPrimary,
                                                    RoyalCobaltDark
                                                )
                                            )
                                        )
                                        .clickable {
                                            // Toggle mic or re-trigger listening
                                            if (isListening) {
                                                onStopVoiceInput?.invoke()
                                            } else {
                                                onStartVoiceInput?.invoke()
                                            }
                                        }
                                        .testTag("guide_bot_pulsing_mic"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Speak to Bot",
                                        tint = Color.White,
                                        modifier = Modifier.size(38.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "मराठीत किंवा कोणत्याही भाषेत बोला",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = RoyalCobaltDark,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Spoken Voice Query Chips for Non-Tech users (1-tap spoken queries)
                                Text(
                                    text = "किंवा खालीलपैकी एक पर्याय निवडून थेट ऐका:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSlateMuted,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 2.dp)
                                ) {
                                    items(spokenSuggestions) { query ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Color.White)
                                                .border(1.dp, IceBlueBorder, RoundedCornerShape(16.dp))
                                                .clickable {
                                                    isVoicePanelOpen = false
                                                    onStopVoiceInput?.invoke()
                                                    onSendMessage(query)
                                                }
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                                .testTag("spoken_query_pill")
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Mic,
                                                    contentDescription = null,
                                                    tint = RoyalCobaltPrimary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = query,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = RoyalCobaltDark
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Quick Question Suggestions bar (when voice panel is collapsed)
                    if (!isVoicePanelOpen && !isListening) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, CardBorderSubtle)
                                .padding(vertical = 8.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Frequent Questions (वारंवार विचारले जाणारे प्रश्न):",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSlateMuted
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable {
                                            isVoicePanelOpen = true
                                            onStartVoiceInput?.invoke()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = null,
                                            tint = RoyalCobaltPrimary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "बोलून विचारा",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalCobaltPrimary
                                        )
                                    }
                                }
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    items(AppGuideBotService.quickQuestions) { question ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Color.White)
                                                .border(1.dp, IceBlueBorder, RoundedCornerShape(14.dp))
                                                .clickable {
                                                    onSendMessage(question)
                                                }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                                .testTag("quick_faq_chip")
                                        ) {
                                            Text(
                                                text = question,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = RoyalCobaltDark
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Bottom Input Row (with prominent Microphone button for non-tech users)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Prominent Microphone Button (>= 48dp touch target)
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isVoicePanelOpen || isListening) Color(0xFFEF4444) else RoyalCobaltPrimary
                                )
                                .clickable {
                                    if (isVoicePanelOpen || isListening) {
                                        isVoicePanelOpen = false
                                        onStopVoiceInput?.invoke()
                                    } else {
                                        isVoicePanelOpen = true
                                        onStartVoiceInput?.invoke()
                                    }
                                }
                                .testTag("guide_bot_mic_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isVoicePanelOpen || isListening) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = "Voice Input Mic",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text(
                                    "Type or tap Mic to speak...",
                                    fontSize = 13.sp,
                                    color = TextSlateMuted
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("guide_bot_input_field"),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = RoyalCobaltPrimary,
                                unfocusedBorderColor = CardBorderSubtle
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Send Button
                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    val q = inputText.trim()
                                    inputText = ""
                                    onSendMessage(q)
                                }
                            },
                            enabled = inputText.isNotBlank(),
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (inputText.isNotBlank()) RoyalCobaltPrimary else Color(0xFFCBD5E1))
                                .testTag("send_guide_question_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Question",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
