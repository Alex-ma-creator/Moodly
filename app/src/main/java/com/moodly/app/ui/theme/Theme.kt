package com.moodly.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta cálida y acogedora para bienestar emocional
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)

val md_primary = Color(0xFF7C6FCD)
val md_on_primary = Color(0xFFFFFFFF)
val md_primary_container = Color(0xFFE8E0FF)
val md_secondary = Color(0xFF625B71)
val md_tertiary = Color(0xFF7D5260)
val md_background = Color(0xFFFFFBFE)
val md_surface = Color(0xFFFFFBFE)
val md_surface_variant = Color(0xFFE7E0EC)

private val LightColorScheme = lightColorScheme(
    primary = md_primary,
    onPrimary = md_on_primary,
    primaryContainer = md_primary_container,
    secondary = md_secondary,
    tertiary = md_tertiary,
    background = md_background,
    surface = md_surface,
    surfaceVariant = md_surface_variant,
)

@Composable
fun MoodlyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
