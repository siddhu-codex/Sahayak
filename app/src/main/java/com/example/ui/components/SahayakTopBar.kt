package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarathiDialect
import com.example.data.model.UserPersona
import com.example.ui.theme.MidnightNavy950
import com.example.ui.theme.RoyalCobaltLight
import com.example.ui.theme.RoyalCobaltPrimary

/**
 * Modern, clean TopBar where all words fit naturally with generous touch targets.
 * App branding, Persona indicator, Dialect selector, and Audio Screen Reader are balanced
 * without clipping on any screen width.
 */
@Composable
fun SahayakTopBar(
    currentPersona: UserPersona,
    currentDialect: MarathiDialect,
    isLargeFont: Boolean,
    onPersonaChange: (UserPersona) -> Unit,
    onDialectChange: (MarathiDialect) -> Unit,
    onToggleFontSize: () -> Unit,
    onReadScreen: () -> Unit,
    onOpenGuideBot: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isDialectMenuOpen by remember { mutableStateOf(false) }
    var isPersonaMenuOpen by remember { mutableStateOf(false) }

    val personaShort = when (currentPersona) {
        UserPersona.KISAN -> "Farmer"
        UserPersona.AAJI -> "Senior"
        UserPersona.YUVA -> "Youth"
    }

    val dialectShort = when (currentDialect) {
        MarathiDialect.PRAMAAN -> "प्रमाण"
        MarathiDialect.VARHADI -> "वऱ्हाडी"
        MarathiDialect.AHIRANI -> "अहिराणी"
        MarathiDialect.MALVANI -> "मालवणी"
        MarathiDialect.MARATHWADI -> "मराठवाडी"
        MarathiDialect.ZADI_BOLI -> "झाडीबोली"
        MarathiDialect.MINGLISH -> "Minglish"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MidnightNavy950)
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("sahayak_top_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: App Logo + Title + Persona Chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(RoyalCobaltPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = "Sahayak AI",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sahayak",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(RoyalCobaltLight)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "AI",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Persona Switcher Pill
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { isPersonaMenuOpen = true }
                                .padding(vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = personaShort,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF93C5FD)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Switch Persona",
                                tint = Color(0xFF93C5FD),
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = isPersonaMenuOpen,
                            onDismissRequest = { isPersonaMenuOpen = false }
                        ) {
                            UserPersona.values().forEach { persona ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(persona.labelEn, fontWeight = FontWeight.SemiBold)
                                            Text(persona.labelMr, fontSize = 11.sp, color = Color.Gray)
                                        }
                                    },
                                    onClick = {
                                        onPersonaChange(persona)
                                        isPersonaMenuOpen = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Right: Dialect Chip + Guide Bot + Font Size Toggle + Screen Reader Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Dialect selector chip
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                            .clickable { isDialectMenuOpen = true }
                            .padding(horizontal = 9.dp, vertical = 6.dp)
                            .testTag("dialect_picker_button"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dialectShort,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF93C5FD),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Dialect",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = isDialectMenuOpen,
                        onDismissRequest = { isDialectMenuOpen = false }
                    ) {
                        MarathiDialect.values().forEach { d ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(d.nameMr, fontWeight = FontWeight.SemiBold)
                                        Text("${d.region} • ${d.code}", fontSize = 11.sp, color = Color.Gray)
                                    }
                                },
                                onClick = {
                                    onDialectChange(d)
                                    isDialectMenuOpen = false
                                }
                            )
                        }
                    }
                }

                // Guide Bot Button ("How to use this app")
                if (onOpenGuideBot != null) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .border(1.5.dp, Color(0xFF38BDF8), CircleShape)
                            .clickable { onOpenGuideBot() }
                            .testTag("top_bar_guide_bot_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "App Guide Chatbot",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Font size toggle for seniors (A / A+)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isLargeFont) RoyalCobaltPrimary else Color(0xFF1E293B))
                        .clickable { onToggleFontSize() }
                        .testTag("font_size_toggle_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "Toggle Font Size",
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }

                // Screen Reader "Read screen aloud" Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(RoyalCobaltPrimary)
                        .clickable { onReadScreen() }
                        .testTag("screen_reader_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Read screen aloud",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
