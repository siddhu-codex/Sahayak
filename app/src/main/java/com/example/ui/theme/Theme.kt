package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RoyalCobaltLight,
    onPrimary = Color.White,
    primaryContainer = RoyalCobaltDark,
    onPrimaryContainer = RoyalCobaltContainer,
    secondary = AccentCyan,
    onSecondary = MidnightNavy950,
    secondaryContainer = MidnightNavy800,
    onSecondaryContainer = IceBlueBorder,
    tertiary = RoyalCobaltPrimary,
    background = MidnightNavy950,
    onBackground = Color(0xFFF1F5F9),
    surface = MidnightNavy900,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = MidnightNavy800,
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalCobaltPrimary,
    onPrimary = Color.White,
    primaryContainer = RoyalCobaltContainer,
    onPrimaryContainer = RoyalOnCobaltContainer,
    secondary = RoyalCobaltDark,
    onSecondary = Color.White,
    secondaryContainer = IceBlueBorder,
    onSecondaryContainer = RoyalOnCobaltContainer,
    tertiary = RoyalCobaltLight,
    background = CanvasBackground,
    onBackground = TextSlatePrimary,
    surface = CardBackgroundWhite,
    onSurface = TextSlatePrimary,
    surfaceVariant = IceBlueTint,
    onSurfaceVariant = TextSlateSecondary
)

@Composable
fun SahayakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
