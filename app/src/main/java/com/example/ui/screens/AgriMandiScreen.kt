package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgriMarketItem
import com.example.data.repository.SahayakRepository
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
fun AgriMandiScreen(
    isLargeFont: Boolean,
    isSpeaking: Boolean,
    currentlySpokenText: String?,
    onSpeak: (String) -> Unit,
    onStopSpeaking: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("Mandi Rates") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBackground)
            .testTag("agri_screen_lazy_column"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp)
    ) {
        // Header
        item {
            Text(
                text = "MahaAgri Market & Crop Doctor",
                fontSize = if (isLargeFont) 22.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextSlatePrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Official APMC Commodity Prices & MahaVISTAAR Pest Advisories",
                fontSize = 12.sp,
                color = TextSlateMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Weather & Mandi Summary Banner (Midnight Navy)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("weather_agri_banner"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightNavy950)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Weather",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Weather Advisory",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Dry conditions across Vidarbha & Marathwada. Favorable for soybean harvesting & threshing.",
                            fontSize = 12.sp,
                            color = Color(0xFF93C5FD),
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    AudioSpeakerButton(
                        textToSpeak = "हवामान इशारा: विदर्भ व मराठवाड्यात पुढील ३ दिवस कोरडे हवामान राहील. सोयाबीन काढणी व मळणीची कामे उरकून घेण्याचा सल्ला आहे.",
                        isSpeakingThis = isSpeaking && currentlySpokenText?.contains("हवामान इशारा") == true,
                        onSpeak = onSpeak,
                        onStop = onStopSpeaking,
                        label = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Switcher in English (Mandi Rates vs Crop Doctor)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE2E8F0))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedTab == "Mandi Rates") RoyalCobaltPrimary else Color.Transparent)
                        .clickable { selectedTab = "Mandi Rates" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mandi Rates",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == "Mandi Rates") Color.White else TextSlateSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedTab == "Crop Doctor") RoyalCobaltPrimary else Color.Transparent)
                        .clickable { selectedTab = "Crop Doctor" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Crop Doctor & Pests",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == "Crop Doctor") Color.White else TextSlateSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Market Items
        items(SahayakRepository.agriMarkets) { item ->
            AgriCard(
                item = item,
                showPestRemedy = selectedTab == "Crop Doctor",
                isLargeFont = isLargeFont,
                isSpeaking = isSpeaking && currentlySpokenText == item.audioSummaryMr,
                onSpeak = onSpeak,
                onStop = onStopSpeaking
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun AgriCard(
    item: AgriMarketItem,
    showPestRemedy: Boolean,
    isLargeFont: Boolean,
    isSpeaking: Boolean,
    onSpeak: (String) -> Unit,
    onStop: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("agri_card_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderSubtle)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Crop Name & Speaker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.cropNameEn,
                            fontSize = if (isLargeFont) 17.sp else 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightNavy950
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${item.cropNameMr.substringBefore(" ")})",
                            fontSize = 12.sp,
                            color = RoyalCobaltDark
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Market Hub",
                            tint = TextSlateMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${item.mandiName} • ${item.district}",
                            fontSize = 11.sp,
                            color = TextSlateMuted
                        )
                    }
                }

                AudioSpeakerButton(
                    textToSpeak = item.audioSummaryMr,
                    isSpeakingThis = isSpeaking,
                    onSpeak = onSpeak,
                    onStop = onStop,
                    label = "Audio"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rates Container (Ice Blue)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(IceBlueTint)
                    .border(1.dp, IceBlueBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    Text(text = "Modal Price", fontSize = 11.sp, color = TextSlateMuted)
                    Text(
                        text = "₹${item.modalPrice} / Qtl",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalCobaltPrimary
                    )
                }

                Column(modifier = Modifier.weight(1.2f)) {
                    Text(text = "Min - Max", fontSize = 11.sp, color = TextSlateMuted)
                    Text(
                        text = "₹${item.minPrice} - ₹${item.maxPrice}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSlatePrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(RoyalCobaltContainer)
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = item.priceTrend,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalCobaltDark
                    )
                }
            }

            // Pest Warning & Remedy
            if (item.pestAlert != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (showPestRemedy) Color(0xFFFEF3C7) else Color(0xFFFFFBEB)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = "Pest Alert",
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Pest Alert: ${item.pestAlert}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }

                        if (item.pestRemedyMr != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Remedy: ${item.pestRemedyMr}",
                                fontSize = 12.sp,
                                color = Color(0xFF78350F),
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
