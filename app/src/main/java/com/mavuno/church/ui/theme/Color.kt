package com.mavuno.church.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Brand Accents
val BrandOrange = Color(0xFFF5821F)
val BrandOrangeDeep = Color(0xFFD9660B)
val BrandOrangeSoftLight = Color(0xFFFFEDD5)
val BrandOrangeSoftDark = Color(0xFF3B1E08)

val BrandGold = Color(0xFFD97706)
val BrandGoldSoftLight = Color(0xFFFEF3C7)
val BrandGoldSoftDark = Color(0xFF362005)

// Light Mode Tokens (Pure & Crisp White)
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceAlt = Color(0xFFF8FAFC)
val LightSurfaceElevated = Color(0xFFF1F5F9)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF475569)
val LightTextMuted = Color(0xFF94A3B8)
val LightLineColor = Color(0x140F172A)
val LightLineSubtle = Color(0x0A0F172A)

// Dark Mode Tokens (Deep Pitch & OLED Black)
val DarkBackground = Color(0xFF000000)
val DarkSurface = Color(0xFF0D0D0E)
val DarkSurfaceAlt = Color(0xFF161618)
val DarkSurfaceElevated = Color(0xFF1E1E22)
val DarkTextPrimary = Color(0xFFF8FAFC)
val DarkTextSecondary = Color(0xFFCBD5E1)
val DarkTextMuted = Color(0xFF64748B)
val DarkLineColor = Color(0x26FFFFFF)
val DarkLineSubtle = Color(0x14FFFFFF)

// Legacy Compatibility Aliases (re-routed dynamically via MavunoTheme.colors)
val DeepWhiteBackground = LightBackground
val SurfaceWhite = LightSurface
val SurfaceAlt = LightSurfaceAlt
val SurfaceDark = Color(0xFF0F172A)
val TextPrimary = LightTextPrimary
val TextSecondary = LightTextSecondary
val TextMuted = LightTextMuted
val TextOnDark = Color(0xFFFFFFFF)
val TextOnOrange = Color(0xFFFFFFFF)
val LineColor = LightLineColor
val LineSubtle = LightLineSubtle
val BorderGlass = Color(0xCCFFFFFF)
val BrandOrangeSoft = BrandOrangeSoftLight
val BrandGoldSoft = BrandGoldSoftLight

data class MavunoColorPalette(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val surfaceElevated: Color,
    val primary: Color = BrandOrange,
    val primaryDeep: Color = BrandOrangeDeep,
    val primarySoft: Color,
    val secondary: Color = BrandGold,
    val secondarySoft: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textOnPrimary: Color = Color.White,
    val line: Color,
    val lineSubtle: Color,
    val cardBorder: Color
)

val LocalMavunoColors = staticCompositionLocalOf {
    MavunoColorPalette(
        isDark = false,
        background = LightBackground,
        surface = LightSurface,
        surfaceAlt = LightSurfaceAlt,
        surfaceElevated = LightSurfaceElevated,
        primarySoft = BrandOrangeSoftLight,
        secondarySoft = BrandGoldSoftLight,
        textPrimary = LightTextPrimary,
        textSecondary = LightTextSecondary,
        textMuted = LightTextMuted,
        line = LightLineColor,
        lineSubtle = LightLineSubtle,
        cardBorder = LightLineColor
    )
}
