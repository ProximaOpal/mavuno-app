package com.mavuno.church.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightPalette = MavunoColorPalette(
    isDark = false,
    background = LightBackground,
    surface = LightSurface,
    surfaceAlt = LightSurfaceAlt,
    surfaceElevated = LightSurfaceElevated,
    primary = BrandOrange,
    primaryDeep = BrandOrangeDeep,
    primarySoft = BrandOrangeSoftLight,
    secondary = BrandGold,
    secondarySoft = BrandGoldSoftLight,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textMuted = LightTextMuted,
    textOnPrimary = Color.White,
    line = LightLineColor,
    lineSubtle = LightLineSubtle,
    cardBorder = Color(0x1F0F172A)
)

private val DarkPalette = MavunoColorPalette(
    isDark = true,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceAlt = DarkSurfaceAlt,
    surfaceElevated = DarkSurfaceElevated,
    primary = BrandOrange,
    primaryDeep = BrandOrangeDeep,
    primarySoft = BrandOrangeSoftDark,
    secondary = BrandGold,
    secondarySoft = BrandGoldSoftDark,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    textMuted = DarkTextMuted,
    textOnPrimary = Color.White,
    line = DarkLineColor,
    lineSubtle = DarkLineSubtle,
    cardBorder = Color(0x33FFFFFF)
)

private val LightMaterialColorScheme = lightColorScheme(
    primary = BrandOrange,
    onPrimary = Color.White,
    primaryContainer = BrandOrangeSoftLight,
    onPrimaryContainer = BrandOrangeDeep,
    secondary = BrandGold,
    onSecondary = Color.White,
    secondaryContainer = BrandGoldSoftLight,
    onSecondaryContainer = BrandGold,
    tertiary = LightSurfaceElevated,
    onTertiary = LightTextPrimary,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceAlt,
    onSurfaceVariant = LightTextSecondary,
    outline = LightLineColor
)

private val DarkMaterialColorScheme = darkColorScheme(
    primary = BrandOrange,
    onPrimary = Color.White,
    primaryContainer = BrandOrangeSoftDark,
    onPrimaryContainer = Color(0xFFFFD8A8),
    secondary = BrandGold,
    onSecondary = Color.White,
    secondaryContainer = BrandGoldSoftDark,
    onSecondaryContainer = Color(0xFFFFE08A),
    tertiary = DarkSurfaceElevated,
    onTertiary = DarkTextPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceAlt,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkLineColor
)

object MavunoTheme {
    val colors: MavunoColorPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalMavunoColors.current
}

@Composable
fun MavunoTheme(
    forceDark: Boolean? = null,
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val themeMode by ThemeStateManager.themeMode.collectAsState()

    val isDark = forceDark ?: when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> false
    }

    val customPalette = if (isDark) DarkPalette else LightPalette
    val materialScheme = if (isDark) DarkMaterialColorScheme else LightMaterialColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = (if (isDark) DarkBackground else LightBackground).toArgb()
            window.navigationBarColor = (if (isDark) DarkSurface else LightSurface).toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !isDark
            controller.isAppearanceLightNavigationBars = !isDark
        }
    }

    CompositionLocalProvider(LocalMavunoColors provides customPalette) {
        MaterialTheme(
            colorScheme = materialScheme,
            typography = Typography,
            content = content
        )
    }
}
