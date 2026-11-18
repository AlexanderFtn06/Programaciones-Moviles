package com.faustino.centrotareas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Azul = Color(0xFF1B6CA8)

private val EsquemaClaro = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3E4F7),
    onPrimaryContainer = Color(0xFF0B3A5E),
    background = Color(0xFFFBF9FF),
    surface = Color(0xFFFFFFFF)
)

@Composable
fun CentroTareasTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaClaro, content = content)
}