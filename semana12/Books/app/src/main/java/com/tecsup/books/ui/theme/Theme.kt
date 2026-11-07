package com.tecsup.books.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    primaryContainer = TealClaro,
    onPrimaryContainer = TealOscuro,
    secondary = TealOscuro,
    onSecondary = Color.White,
    background = Superficie,
    onBackground = TextoOscuro,
    surface = Superficie,
    onSurface = TextoOscuro,
    surfaceVariant = Color(0xFFE6EEF2),
    onSurfaceVariant = TextoSuave,
    error = Rojo,
    onError = Color.White,
    errorContainer = RojoClaro,
    onErrorContainer = Rojo,
    tertiaryContainer = Color(0xFFFFF3CD),
    onTertiaryContainer = Color(0xFF6B4E00)
)

private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFF7FD0E6),
    onPrimary = Color(0xFF00363F),
    primaryContainer = Color(0xFF004F5E),
    onPrimaryContainer = TealClaro,
    background = Color(0xFF101A1F),
    onBackground = Color(0xFFE1ECF0),
    surface = Color(0xFF18262D),
    onSurface = Color(0xFFE1ECF0),
    onSurfaceVariant = Color(0xFFB8C7CD),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    tertiaryContainer = Color(0xFF5C4500),
    onTertiaryContainer = Color(0xFFFFE08A)
)

@Composable
fun BooksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}