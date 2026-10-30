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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DocumentSample
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
fun DocumentScannerScreen(
    selectedDocument: DocumentSample,
    isLargeFont: Boolean,
    isSpeaking: Boolean,
    currentlySpokenText: String?,
    onSelectDocument: (DocumentSample) -> Unit,
    onSpeak: (String) -> Unit,
    onStopSpeaking: () -> Unit,
    onNavigateToInterpreter: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBackground)
            .testTag("doc_scanner_screen_lazy_column"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp)
    ) {
        // Header & Tool Switcher
        item {
            Text(
                text = "Document OCR Reader & Summary",
                fontSize = if (isLargeFont) 22.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextSlatePrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "\"Read this paper out loud\": Official 7/12 land records, ration cards, & pension orders",
                fontSize = 12.sp,
                color = TextSlateMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                        .clickable { onNavigateToInterpreter?.invoke() }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Voice Interpreter",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSlateSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(11.dp))
                        .background(RoyalCobaltPrimary)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Document OCR Reader",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // AR / Camera Viewfinder Card (Midnight Navy, inspired by reference image)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scanner_viewfinder_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightNavy950),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(MidnightNavy950, MidnightNavy900)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Viewfinder Target Frame
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .border(2.dp, RoyalCobaltLight, RoundedCornerShape(16.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CenterFocusStrong,
                                contentDescription = "Camera Scanner",
                                tint = Color(0xFF93C5FD),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Align Document Here",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Optical AI Active",
                                fontSize = 9.sp,
                                color = Color(0xFF93C5FD)
                            )
                        }
                    }

                    // Floating Scan & Audio Trigger Button
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(RoyalCobaltPrimary)
                            .clickable {
                                onSpeak(selectedDocument.simpleAudioExplanationMr)
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("read_doc_aloud_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Read Aloud",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Read Aloud (Audio Summary)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sample Document Selector Carousel
            Text(
                text = "Select Document Sample:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextSlatePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SahayakRepository.documentSamples.forEach { doc ->
                    val isSelected = selectedDocument.id == doc.id
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectDocument(doc) }
                            .testTag("doc_sample_${doc.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) RoyalCobaltContainer else Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) RoyalCobaltPrimary else CardBorderSubtle
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 10.dp)
                        ) {
                            val shortTitle = when (doc.id) {
                                "doc_satbara" -> "7/12 Land Record"
                                "doc_ration_card" -> "Ration Card"
                                "doc_pension_order" -> "Pension Order"
                                else -> doc.titleEn
                            }
                            Text(
                                text = doc.sampleType.substringBefore(" "),
                                fontSize = 9.5.sp,
                                color = TextSlateMuted,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = shortTitle,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) RoyalCobaltDark else TextSlatePrimary,
                                maxLines = 2,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Document Intelligence & Verification:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextSlatePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Extracted Document Information Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("extracted_doc_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderSubtle)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedDocument.titleEn,
                                fontSize = if (isLargeFont) 16.sp else 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightNavy950
                            )
                            Text(
                                text = selectedDocument.titleMr,
                                fontSize = 12.sp,
                                color = RoyalCobaltDark
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = RoyalCobaltPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Verified Portal: ${selectedDocument.verifiedGovtPortal}",
                                    fontSize = 11.sp,
                                    color = RoyalCobaltDark,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        AudioSpeakerButton(
                            textToSpeak = selectedDocument.simpleAudioExplanationMr,
                            isSpeakingThis = isSpeaking && currentlySpokenText == selectedDocument.simpleAudioExplanationMr,
                            onSpeak = onSpeak,
                            onStop = onStopSpeaking,
                            label = "Audio"
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simple Marathi Audio Summary Box (Ice Blue)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(IceBlueTint)
                            .border(1.dp, IceBlueBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Audio Explanation: ${selectedDocument.simpleAudioExplanationMr}",
                            fontSize = if (isLargeFont) 13.sp else 12.sp,
                            color = RoyalOnCobaltContainer,
                            lineHeight = 17.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Key Extracted Fields:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSlateSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    selectedDocument.keyFields.forEach { (key, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = key,
                                fontSize = 12.sp,
                                color = TextSlateMuted,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = value,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSlatePrimary,
                                modifier = Modifier.weight(1.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}
