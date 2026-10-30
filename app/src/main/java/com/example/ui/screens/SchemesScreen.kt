package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchemeItem
import com.example.data.repository.SahayakRepository
import com.example.ui.components.AudioSpeakerButton
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.CardBorderSubtle
import com.example.ui.theme.IceBlueBorder
import com.example.ui.theme.IceBlueTint
import com.example.ui.theme.MidnightNavy950
import com.example.ui.theme.RoyalCobaltContainer
import com.example.ui.theme.RoyalCobaltDark
import com.example.ui.theme.RoyalCobaltLight
import com.example.ui.theme.RoyalCobaltPrimary
import com.example.ui.theme.RoyalOnCobaltContainer
import com.example.ui.theme.TextSlateMuted
import com.example.ui.theme.TextSlatePrimary
import com.example.ui.theme.TextSlateSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SchemesScreen(
    isLargeFont: Boolean,
    isSpeaking: Boolean,
    currentlySpokenText: String?,
    onSpeak: (String) -> Unit,
    onStopSpeaking: () -> Unit,
    onStartSlotFilling: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Women & Child", "Farmers", "Healthcare", "Seniors", "Students")

    val filteredSchemes = SahayakRepository.schemes.filter {
        val matchesQuery = it.titleMr.contains(searchQuery, ignoreCase = true) ||
                it.titleEn.contains(searchQuery, ignoreCase = true) ||
                it.benefitHighlightMr.contains(searchQuery, ignoreCase = true)

        val matchesCategory = when (selectedCategory) {
            "Women & Child" -> it.id == "ladki_bahin"
            "Farmers" -> it.id == "namo_shetkari"
            "Healthcare" -> it.id == "mjpjay_health"
            "Seniors" -> it.id == "shravanbal_pension"
            "Students" -> it.id == "mahadbt_scholarship"
            else -> true
        }
        matchesQuery && matchesCategory
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBackground)
            .testTag("schemes_screen_lazy_column"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp)
    ) {
        // Header
        item {
            Text(
                text = "Government Welfare Schemes",
                fontSize = if (isLargeFont) 22.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextSlatePrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Verified Maharashtra State Welfare Portals & DBT Benefits",
                fontSize = 12.sp,
                color = TextSlateMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search scheme or benefit...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextSlateMuted
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("schemes_search_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = RoyalCobaltPrimary,
                    unfocusedBorderColor = CardBorderSubtle
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Pills
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) RoyalCobaltPrimary else Color.White)
                            .border(
                                1.dp,
                                if (isSelected) RoyalCobaltPrimary else CardBorderSubtle,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("scheme_category_$cat")
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextSlatePrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Scheme Items
        items(filteredSchemes) { scheme ->
            SchemeCard(
                scheme = scheme,
                isLargeFont = isLargeFont,
                isSpeaking = isSpeaking && currentlySpokenText == scheme.audioSummaryMr,
                onSpeak = onSpeak,
                onStop = onStopSpeaking,
                onStartSlotFilling = { onStartSlotFilling(scheme.titleMr) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SchemeCard(
    scheme: SchemeItem,
    isLargeFont: Boolean,
    isSpeaking: Boolean,
    onSpeak: (String) -> Unit,
    onStop: () -> Unit,
    onStartSlotFilling: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("scheme_card_${scheme.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderSubtle)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Title & Audio Speak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = scheme.titleEn,
                        fontSize = if (isLargeFont) 16.sp else 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightNavy950
                    )
                    Text(
                        text = scheme.titleMr,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = RoyalCobaltDark
                    )
                    Text(
                        text = scheme.department,
                        fontSize = 11.sp,
                        color = TextSlateMuted
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                AudioSpeakerButton(
                    textToSpeak = scheme.audioSummaryMr,
                    isSpeakingThis = isSpeaking,
                    onSpeak = onSpeak,
                    onStop = onStop,
                    label = "Listen"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Benefit Box (Ice Blue)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(IceBlueTint)
                    .border(1.dp, IceBlueBorder, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = "Benefit: ${scheme.benefitHighlightMr}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalOnCobaltContainer
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Eligibility
            Text(
                text = "Eligibility Criteria:",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSlateSecondary
            )
            Text(
                text = scheme.eligibilitySummaryMr,
                fontSize = if (isLargeFont) 13.sp else 12.sp,
                color = TextSlatePrimary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Required Documents
            Text(
                text = "Required Documents:",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSlateSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                scheme.requiredDocuments.take(3).forEach { doc ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "✓ $doc",
                            fontSize = 10.5.sp,
                            color = TextSlatePrimary,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Row: Helpline & Voice Apply
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Helpline",
                        tint = TextSlateMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = scheme.officialHelpline,
                        fontSize = 11.sp,
                        color = TextSlateMuted
                    )
                }

                // Voice Apply Button (Royal Cobalt)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(RoyalCobaltPrimary)
                        .clickable { onStartSlotFilling() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("start_voice_form_${scheme.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Apply",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Voice Apply",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
