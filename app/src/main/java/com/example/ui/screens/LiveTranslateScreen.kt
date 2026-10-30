package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Translate
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarathiDialect
import com.example.data.model.TranslationTurn
import com.example.ui.components.AudioSpeakerButton
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.CardBorderSubtle
import com.example.ui.theme.IceBlueBorder
import com.example.ui.theme.IceBlueTint
import com.example.ui.theme.MidnightNavy900
import com.example.ui.theme.MidnightNavy950
import com.example.ui.theme.RoyalCobaltContainer
import com.example.ui.theme.RoyalCobaltDark
import com.example.ui.theme.RoyalCobaltLight
import com.example.ui.theme.RoyalCobaltPrimary
import com.example.ui.theme.RoyalOnCobaltContainer
import com.example.ui.theme.TextSlateMuted
import com.example.ui.theme.TextSlatePrimary
import com.example.ui.theme.TextSlateSecondary

@Composable
fun LiveTranslateScreen(
    currentDialect: MarathiDialect,
    liveTranslations: List<TranslationTurn>,
    isLargeFont: Boolean,
    isSpeaking: Boolean,
    currentlySpokenText: String?,
    onAddTranslation: (String, Boolean, String) -> Unit,
    onClearTranslations: (() -> Unit)? = null,
    onSpeak: (String) -> Unit,
    onStopSpeaking: () -> Unit,
    onNavigateToScanner: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var targetLanguage by remember { mutableStateOf("English") }
    var customInputText by remember { mutableStateOf("") }
    var isCitizenSpeaking by remember { mutableStateOf(true) } // true = Citizen (Marathi), false = Official (Target)
    var copiedNotice by remember { mutableStateOf<String?>(null) }
    val clipboardManager = LocalClipboardManager.current

    val supportedTargets = listOf("English", "Hindi", "Gujarati", "Kannada")

    val quickMarathiPhrases = listOf(
        "माझ्याकडे सातबारा उतारा आहे, यावर पीक कर्ज मिळेल का?",
        "लाडकी बहीण योजनेचा पहिला हप्ता कधी जमा होणार?",
        "माझे रेशन कार्ड केशरी आहे, मला धान्य किती मिळेल?",
        "अकोला बाजारात आज सोयाबीनला काय भाव चालू आहे?",
        "माझे आधार कार्ड बँक खात्याशी लिंक कसे करावे?",
        "उत्पन्नाचा दाखला काढण्यासाठी कोणती कागदपत्रे लागतात?"
    )

    val quickOfficialPhrases = listOf(
        "Please provide your Aadhaar card linked with your bank account.",
        "Your land record extract has been verified successfully.",
        "You are eligible for the 5 Lakh Rupee cashless medical health card.",
        "Please submit an income certificate issued by the Tehsildar.",
        "The minimum support price for soybean this season is 4,892 rupees per quintal."
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBackground)
            .testTag("live_translate_screen_lazy_column"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp)
    ) {
        // Header & Tool Switcher
        item {
            Text(
                text = "Live Speech Interpreter",
                fontSize = if (isLargeFont) 22.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextSlatePrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Face-to-Face Translation: Regional Marathi Dialects <-> English & Hindi",
                fontSize = 12.sp,
                color = TextSlateMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Switcher Tabs between Voice Interpreter and Document OCR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE2E8F0))
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(11.dp))
                        .background(RoyalCobaltPrimary)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Voice Interpreter",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(11.dp))
                        .clickable { onNavigateToScanner?.invoke() }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Document OCR Reader",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSlateSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Language & Direction Card (Midnight Navy)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("language_direction_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightNavy950)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Speaker 1 (Citizen - Marathi)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isCitizenSpeaking) Color(0xFF1E293B) else Color.Transparent)
                                .clickable { isCitizenSpeaking = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Speaker 1 (Citizen)", fontSize = 11.sp, color = Color(0xFF93C5FD))
                            Text(
                                text = "Marathi (${currentDialect.nameMr})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCitizenSpeaking) Color(0xFF60A5FA) else Color.White
                            )
                        }

                        // Toggle Direction Button
                        IconButton(
                            onClick = { isCitizenSpeaking = !isCitizenSpeaking },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(RoyalCobaltPrimary)
                                .testTag("swap_direction_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Swap Speaker Direction",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Speaker 2 (Official - Target Lang)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (!isCitizenSpeaking) Color(0xFF1E293B) else Color.Transparent)
                                .clickable { isCitizenSpeaking = false }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Speaker 2 (Official)", fontSize = 11.sp, color = Color(0xFF93C5FD))
                            Text(
                                text = targetLanguage,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isCitizenSpeaking) Color(0xFF60A5FA) else Color(0xFF93C5FD)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Target Language Selector Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        supportedTargets.forEach { lang ->
                            val isSel = targetLanguage == lang
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) RoyalCobaltPrimary else Color(0xFF1E293B))
                                    .clickable { targetLanguage = lang }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lang,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Text/Voice Translation Input Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("translation_input_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderSubtle)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isCitizenSpeaking) "Citizen Speaking (मराठीत बोला किंवा टाईप करा):" else "Official Speaking (Speak or type in $targetLanguage):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCitizenSpeaking) RoyalCobaltDark else MidnightNavy900
                        )

                        Text(
                            text = if (isCitizenSpeaking) "Translate -> $targetLanguage" else "Translate -> मराठी",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RoyalCobaltPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customInputText,
                        onValueChange = { customInputText = it },
                        placeholder = {
                            Text(
                                if (isCitizenSpeaking) "उदा: माझ्याकडे सातबारा आहे, पीक कर्ज कसे मिळेल?" else "e.g. Please provide your Aadhaar linked bank account.",
                                fontSize = 12.5.sp,
                                color = TextSlateMuted
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("translation_text_field"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF8FAFC),
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedBorderColor = RoyalCobaltPrimary,
                            unfocusedBorderColor = CardBorderSubtle
                        ),
                        trailingIcon = {
                            if (customInputText.isNotEmpty()) {
                                IconButton(onClick = { customInputText = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = TextSlateMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        minLines = 2,
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Action: Translate & Speak button
                        Button(
                            onClick = {
                                val textToTranslate = if (customInputText.isNotBlank()) {
                                    customInputText.trim()
                                } else {
                                    if (isCitizenSpeaking) quickMarathiPhrases.random() else quickOfficialPhrases.random()
                                }
                                customInputText = ""
                                onAddTranslation(textToTranslate, isCitizenSpeaking, targetLanguage)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("submit_translation_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalCobaltPrimary),
                            contentPadding = PaddingValues(vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Translate & Speak Aloud",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Scenario Chips (Citizen & Official)
            Column {
                Text(
                    text = if (isCitizenSpeaking) "Quick Citizen Scenarios (नागरिक विचारू शकतात):" else "Quick Official Scenarios (अधिकारी विचारू शकतात):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSlateSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val phrases = if (isCitizenSpeaking) quickMarathiPhrases else quickOfficialPhrases
                    items(phrases) { phrase ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .border(1.dp, CardBorderSubtle, RoundedCornerShape(14.dp))
                                .clickable {
                                    onAddTranslation(phrase, isCitizenSpeaking, targetLanguage)
                                }
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                                .testTag("scenario_chip")
                        ) {
                            Text(
                                text = phrase,
                                fontSize = 11.5.sp,
                                color = TextSlatePrimary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transcript Header & Clear Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Conversation Transcript (${liveTranslations.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSlatePrimary
                )

                if (liveTranslations.isNotEmpty() && onClearTranslations != null) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onClearTranslations() }
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Clear Transcript",
                            tint = TextSlateMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Clear",
                            fontSize = 11.5.sp,
                            color = TextSlateMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Live Captions / Conversation Turns
        items(liveTranslations) { turn ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .testTag("translation_turn_${turn.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (turn.isUserMarathi) Color.White else IceBlueTint
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (turn.isUserMarathi) CardBorderSubtle else IceBlueBorder
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Speaker Badge & Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (turn.isUserMarathi) RoyalCobaltPrimary else MidnightNavy950),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (turn.isUserMarathi) Icons.Default.Mic else Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = turn.speakerName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (turn.isUserMarathi) RoyalCobaltDark else MidnightNavy950
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Copy button
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(turn.translatedText))
                                    copiedNotice = "Copied to clipboard!"
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Translation",
                                    tint = TextSlateMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Audio Speaker button
                            AudioSpeakerButton(
                                textToSpeak = turn.translatedText,
                                isSpeakingThis = isSpeaking && currentlySpokenText == turn.translatedText,
                                onSpeak = onSpeak,
                                onStop = onStopSpeaking,
                                label = "Play Audio"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Original Speech Text
                    Text(
                        text = "Original: \"${turn.sourceText}\"",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSlateSecondary
                    )

                    // Dialect Normalization (Varhadi -> Pramaan)
                    if (turn.normalizedMarathi != null && turn.normalizedMarathi != turn.sourceText) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Standard Marathi: ${turn.normalizedMarathi}",
                                fontSize = 11.sp,
                                color = Color(0xFF78350F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Translated Target Text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(RoyalCobaltContainer)
                            .border(1.dp, IceBlueBorder, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Translated (${turn.targetLang}): ${turn.translatedText}",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalOnCobaltContainer
                        )
                    }
                }
            }
        }
    }
}
