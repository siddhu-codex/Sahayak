package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.MidnightNavy900
import com.example.ui.theme.MidnightNavy950
import com.example.ui.theme.RoyalCobaltDark
import com.example.ui.theme.RoyalCobaltLight
import com.example.ui.theme.RoyalCobaltPrimary

/**
 * Modern floating bottom navigation bar with the Mic in the EXACT CENTER (2 items left, 2 items right).
 * Symmetrical layout ensures the Mic is mathematically aligned at 50% width with ample spacing
 * for all text labels to prevent any truncation or awkward line wrapping.
 */
@Composable
fun SahayakBottomNav(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mic_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("sahayak_bottom_nav"),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Main Nav Bar Surface
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = Color(0x330F172A))
                .clip(RoundedCornerShape(28.dp))
                .background(MidnightNavy950)
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(28.dp))
                .padding(horizontal = 6.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left 2 items (Dashboard, Schemes) - weight 2
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavItem(
                        icon = Icons.Default.Home,
                        label = "Home",
                        isSelected = currentScreen == Screen.Home,
                        onClick = { onNavigate(Screen.Home) },
                        modifier = Modifier.weight(1f)
                    )

                    NavItem(
                        icon = Icons.Default.Policy,
                        label = "Schemes",
                        isSelected = currentScreen == Screen.Schemes,
                        onClick = { onNavigate(Screen.Schemes) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Center placeholder space for the elevated central Mic (width 64dp)
                Spacer(modifier = Modifier.size(64.dp))

                // Right 2 items (Mandi, Translate & Docs) - weight 2
                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavItem(
                        icon = Icons.Default.Agriculture,
                        label = "Mandi",
                        isSelected = currentScreen == Screen.AgriMandi,
                        onClick = { onNavigate(Screen.AgriMandi) },
                        modifier = Modifier.weight(1f)
                    )

                    NavItem(
                        icon = Icons.Default.Translate,
                        label = "Translate",
                        isSelected = currentScreen == Screen.LiveTranslate || currentScreen == Screen.DocumentScanner,
                        onClick = { onNavigate(Screen.LiveTranslate) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Central Raised Mic Button (Exact Horizontal Center, Floating & Glowing)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .offset(y = (-14).dp)
                .testTag("nav_center_mic_container")
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .scale(pulseScale)
                    .shadow(12.dp, CircleShape, spotColor = RoyalCobaltPrimary)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                RoyalCobaltLight,
                                RoyalCobaltPrimary,
                                RoyalCobaltDark
                            )
                        )
                    )
                    .border(2.5.dp, Color(0xFF93C5FD), CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false, color = Color.White)
                    ) {
                        onMicClick()
                    }
                    .testTag("nav_center_mic_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Assistant Mic",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "SPEAK",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF93C5FD),
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = RoyalCobaltLight)
            ) { onClick() }
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Color(0xFF1E293B) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) RoyalCobaltLight else Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF94A3B8),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
