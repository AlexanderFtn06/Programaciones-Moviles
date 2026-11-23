package com.faustino.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Color(0xFF1B6FA8),
    onPrimary = Color.White,
    background = Color(0xFFFBFBFF),
    surface = Color(0xFFFBFBFF),
    surfaceVariant = Color(0xFFEAF0F7),
    onSurfaceVariant = Color(0xFF5B6673)
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}