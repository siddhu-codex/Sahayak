package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IceBlueBorder
import com.example.ui.theme.RoyalCobaltContainer
import com.example.ui.theme.RoyalCobaltDark
import com.example.ui.theme.RoyalCobaltPrimary
import com.example.ui.theme.RoyalOnCobaltContainer

@Composable
fun AudioSpeakerButton(
    textToSpeak: String,
    isSpeakingThis: Boolean,
    onSpeak: (String) -> Unit,
    onStop: () -> Unit,
    label: String? = "Listen",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isSpeakingThis) 1.2f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    if (label != null) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(20.dp))
                .background(if (isSpeakingThis) RoyalCobaltPrimary else RoyalCobaltContainer)
                .clickable {
                    if (isSpeakingThis) onStop() else onSpeak(textToSpeak)
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("audio_speaker_button_row"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSpeakingThis) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Listen to audio",
                tint = if (isSpeakingThis) Color.White else RoyalCobaltDark,
                modifier = Modifier
                    .size(16.dp)
                    .scale(if (isSpeakingThis) pulseScale else 1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isSpeakingThis) "Stop" else label,
                color = if (isSpeakingThis) Color.White else RoyalOnCobaltContainer,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    } else {
        Box(
            modifier = modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isSpeakingThis) RoyalCobaltPrimary else RoyalCobaltContainer)
                .clickable {
                    if (isSpeakingThis) onStop() else onSpeak(textToSpeak)
                }
                .testTag("audio_speaker_circle_btn"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSpeakingThis) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Listen to audio",
                tint = if (isSpeakingThis) Color.White else RoyalCobaltDark,
                modifier = Modifier
                    .size(18.dp)
                    .scale(if (isSpeakingThis) pulseScale else 1f)
            )
        }
    }
}
