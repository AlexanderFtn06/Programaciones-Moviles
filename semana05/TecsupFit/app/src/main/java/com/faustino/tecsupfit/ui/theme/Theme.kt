package com.faustino.tecsupfit.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color


private val EsquemaClaro = lightColorScheme(
    primary = VerdeTecsup,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeTecsup,
    secondaryContainer = VerdeClaro,
    onSecondaryContainer = VerdeTecsup,
    background = Color.White,
    surface = Color.White,
    surfaceVariant = GrisTarjeta,
    onSurfaceVariant = GrisTexto
)

@Composable
fun TecsupFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}