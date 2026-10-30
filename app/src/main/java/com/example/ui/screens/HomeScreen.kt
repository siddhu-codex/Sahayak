package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConciergeResponse
import com.example.data.model.MarathiDialect
import com.example.data.model.UserPersona
import com.example.ui.Screen
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
import com.example.ui.theme.TextSlateMuted
import com.example.ui.theme.TextSlatePrimary
import com.example.ui.theme.TextSlateSecondary

/**
 * Modern Dashboard view adhering to the reference design:
 * Clean white canvas, royal navy cards, electric cobalt blue accents, and spacious typography
 * designed so all text and words fit without truncation or cramping.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    persona: UserPersona,
    dialect: MarathiDialect,
    isLargeFont: Boolean,
    isSpeaking: Boolean,
    currentlySpokenText: String?,
    conciergeHistory: List<Pair<String, ConciergeResponse>>,
    lastActionNotice: String?,
    onNavigate: (Screen) -> Unit,
    onSpeak: (String) -> Unit,
    onStopSpeaking: () -> Unit,
    onPlayWelcomeAudio: () -> Unit,
    onTriggerMic: () -> Unit,
    onSetPersona: (UserPersona) -> Unit,
    onStartVoiceSlotFilling: () -> Unit,
    onOpenGuideBot: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    val categories = listOf("All", "Govt Schemes", "Agri & Mandi", "Live Interpreter", "Document OCR")

    val titleSize = if (isLargeFont) 21.sp else 18.sp
    val bodySize = if (isLargeFont) 14.sp else 12.5.sp

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBackground)
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // 1. Top Telemetry Metrics Row
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Dashboard",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSlatePrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Maharashtra AI & Voice Services",
                            fontSize = 12.sp,
                            color = TextSlateMuted
                        )
                    }

                    // Welcome Audio Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(RoyalCobaltContainer)
                            .border(1.dp, IceBlueBorder, RoundedCornerShape(20.dp))
                            .clickable { onPlayWelcomeAudio() }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                            .testTag("play_welcome_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Play Welcome Guide",
                                tint = RoyalCobaltDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Welcome Audio",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RoyalCobaltDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3 Telemetry Cards with words fitting perfectly
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricStatCard(
                        value = "5+",
                        label = "Active Schemes",
                        subtext = "Ladki Bahin & DBT",
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        value = "4 Hubs",
                        label = "Mandi Markets",
                        subtext = "Akola, Pune, Latur",
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        value = "<1.5s",
                        label = "Voice Speed",
                        subtext = "Live Voice AI",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // App Guide & Help Chatbot Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onOpenGuideBot?.invoke() }
                    .testTag("home_guide_bot_banner"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = IceBlueTint),
                border = androidx.compose.foundation.BorderStroke(1.dp, IceBlueBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RoyalCobaltPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "App Guide",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "How to use Sahayak? Chat with Guide",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoyalCobaltDark
                            )
                            Text(
                                text = "Learn voice search, schemes, mandi rates & translation",
                                fontSize = 11.sp,
                                color = TextSlateSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(RoyalCobaltPrimary)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Ask Bot",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // 2. Featured Deep Midnight Navy Card (matching reference design hero card)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hero_banner_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MidnightNavy950),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(MidnightNavy950, MidnightNavy900)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E3A8A))
                                    .padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "AI VOICE INTELLIGENCE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF93C5FD),
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Active AI",
                                tint = RoyalCobaltLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Dual-Brain Voice Concierge &\nLive Speech Interpreter",
                            fontSize = titleSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Ask questions naturally in ${dialect.nameMr} or English. Navigate schemes, check mandi rates, and translate live conversations.",
                            fontSize = bodySize,
                            color = Color(0xFF94A3B8),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Action Buttons: START VOICE / VOICE FORM
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(RoyalCobaltPrimary)
                                    .clickable { onTriggerMic() }
                                    .padding(vertical = 12.dp, horizontal = 14.dp)
                                    .testTag("hero_speak_now_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Start Voice",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "START VOICE",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.5.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                                    .clickable { onStartVoiceSlotFilling() }
                                    .padding(vertical = 12.dp, horizontal = 12.dp)
                                    .testTag("hero_voice_form_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Voice Form",
                                    color = Color(0xFF93C5FD),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Action Notice Banner (when deep link executes)
        if (lastActionNotice != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(RoyalCobaltContainer)
                        .border(1.dp, IceBlueBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "⚡ AI Action: $lastActionNotice",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RoyalCobaltDark
                    )
                }
            }
        }

        // 3. Persona Selector Pills (Farmer, Senior, Youth)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "User Persona Mode",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSlatePrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UserPersona.values().forEach { p ->
                        val isSelected = persona == p
                        val (enLabel, mrLabel) = when (p) {
                            UserPersona.KISAN -> Pair("Farmer", "शेतकरी")
                            UserPersona.AAJI -> Pair("Senior", "ज्येष्ठ")
                            UserPersona.YUVA -> Pair("Youth", "विद्यार्थी")
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) RoyalCobaltPrimary else Color.White)
                                .border(
                                    1.dp,
                                    if (isSelected) RoyalCobaltPrimary else CardBorderSubtle,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { onSetPersona(p) }
                                .padding(vertical = 10.dp, horizontal = 4.dp)
                                .testTag("persona_tab_${p.name}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = enLabel,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else TextSlatePrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = mrLabel,
                                    fontSize = 11.sp,
                                    color = if (isSelected) Color(0xFF93C5FD) else TextSlateMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Quick Access Category Filter Pills
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Key Services & Workflows",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSlatePrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(categories) { cat ->
                        val isSel = selectedCategoryFilter == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSel) RoyalCobaltPrimary else Color.White)
                                .border(
                                    1.dp,
                                    if (isSel) RoyalCobaltPrimary else CardBorderSubtle,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedCategoryFilter = cat }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("home_category_$cat")
                        ) {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color.White else TextSlatePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Service Cards with words fitting completely
                if (selectedCategoryFilter == "All" || selectedCategoryFilter == "Govt Schemes") {
                    ServiceCard(
                        icon = Icons.Default.Policy,
                        title = "Government Welfare Schemes",
                        subtitle = "Ladki Bahin (₹1,500/mo), MahaDBT, Health Cards",
                        badge = "5 Schemes",
                        audioText = "शासकीय योजना विभागात लाडकी बहीण, वृद्धापकाळ पेन्शन आणि मोफत आरोग्य उपचारांची संपूर्ण माहिती आणि अर्ज उपलब्ध आहेत.",
                        isSpeaking = isSpeaking && currentlySpokenText?.contains("शासकीय योजना विभागात") == true,
                        onNavigate = { onNavigate(Screen.Schemes) },
                        onSpeak = onSpeak,
                        onStop = onStopSpeaking
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (selectedCategoryFilter == "All" || selectedCategoryFilter == "Live Interpreter") {
                    ServiceCard(
                        icon = Icons.Default.Translate,
                        title = "Live Speech Interpreter",
                        subtitle = "Real-time Marathi Dialects <-> English & Hindi",
                        badge = "Bi-Directional",
                        audioText = "थेट दुभाषी विभागात आपण मराठीत बोलून इंग्रजी किंवा हिंदीत समोरच्या व्यक्तीशी संवाद साधू शकता.",
                        isSpeaking = isSpeaking && currentlySpokenText?.contains("थेट दुभाषी विभागात") == true,
                        onNavigate = { onNavigate(Screen.LiveTranslate) },
                        onSpeak = onSpeak,
                        onStop = onStopSpeaking
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (selectedCategoryFilter == "All" || selectedCategoryFilter == "Document OCR") {
                    ServiceCard(
                        icon = Icons.Default.Description,
                        title = "Document OCR Reader",
                        subtitle = "\"Read this paper\": 7/12 Satbara, Ration Card, Orders",
                        badge = "AR Viewfinder",
                        audioText = "कागदपत्र वाचक विभागात आपल्या हातातील कोणताही सरकारी कागद किंवा ७/१२ उतारा कॅमेऱ्यासमोर धरून ऑडिओमध्ये समजावून घेऊ शकता.",
                        isSpeaking = isSpeaking && currentlySpokenText?.contains("कागदपत्र वाचक विभागात") == true,
                        onNavigate = { onNavigate(Screen.DocumentScanner) },
                        onSpeak = onSpeak,
                        onStop = onStopSpeaking
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (selectedCategoryFilter == "All" || selectedCategoryFilter == "Agri & Mandi") {
                    ServiceCard(
                        icon = Icons.Default.Agriculture,
                        title = "MahaAgri Market & Pest Doctor",
                        subtitle = "Soybean (Akola ₹4,850), Cotton, Tur & Pest Remedies",
                        badge = "Live APMC",
                        audioText = "कृषी हमीभाव विभागात आजचे अकोला, लातूर, नाशिक मधील सोयाबीन, कापूस बाजारभाव आणि पिवळा मोझॅक कीड नियंत्रण उपाय उपलब्ध आहेत.",
                        isSpeaking = isSpeaking && currentlySpokenText?.contains("कृषी हमीभाव विभागात") == true,
                        onNavigate = { onNavigate(Screen.AgriMandi) },
                        onSpeak = onSpeak,
                        onStop = onStopSpeaking
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        // 5. Recent Voice Inquiries / Telemetry log
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Quick Voice Queries",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSlatePrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap any question to speak or simulate voice assistant response",
                    fontSize = 11.5.sp,
                    color = TextSlateMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                val samplePrompts = listOf(
                    "लाडकी बहीण योजनेची पात्रता काय आहे?",
                    "What are the soybean mandi rates in Akola?",
                    "Where can I get crop pest advice?",
                    "माझा ७/१२ उतारा वाचून दाखवा"
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    samplePrompts.forEach { query ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(1.dp, CardBorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { onTriggerMic() }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Query",
                                    tint = RoyalCobaltPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = query,
                                    fontSize = 11.5.sp,
                                    color = TextSlatePrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. Security & Inclusivity Notice
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IceBlueTint),
                border = androidx.compose.foundation.BorderStroke(1.dp, IceBlueBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Secure",
                        tint = RoyalCobaltPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "DPDP Act Compliant: Voice interactions are encrypted and PII (Aadhaar/Bank details) is securely masked.",
                        fontSize = 11.sp,
                        color = TextSlateSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricStatCard(
    value: String,
    label: String,
    subtext: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderSubtle)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalCobaltPrimary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSlatePrimary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                fontSize = 9.5.sp,
                color = TextSlateMuted,
                maxLines = 1
            )
        }
    }
}

/**
 * Service card where the badge is displayed in a dedicated tag row so the title
 * fits completely across the full width without awkward clipping.
 */
@Composable
private fun ServiceCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String,
    audioText: String,
    isSpeaking: Boolean,
    onNavigate: () -> Unit,
    onSpeak: (String) -> Unit,
    onStop: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onNavigate() }
            .testTag("service_card_${title.take(6)}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(IceBlueTint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = RoyalCobaltPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Badge on top for clean hierarchy
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(RoyalCobaltContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalCobaltDark
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = TextSlatePrimary
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextSlateMuted,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            AudioSpeakerButton(
                textToSpeak = audioText,
                isSpeakingThis = isSpeaking,
                onSpeak = onSpeak,
                onStop = onStop,
                label = null
            )
        }
    }
}
