package com.faustino.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faustino.ayudacampus.ui.components.AyudaButton

/**
 * Pantalla de inicio.
 * mostrarBotones = false mientras se revisa la sesión guardada
 * (si hay sesión, NavAyuda navega directo sin mostrar los botones).
 */
@Composable
fun SplashScreen(
    mostrarBotones: Boolean,
    onComenzar: () -> Unit,
    onYaTengoCuenta: () -> Unit
) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            // Icono de la app
            Box(
                Modifier
                    .size(96.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Sms,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(Modifier.height(24.dp))
            Text(
                "AyudaCampus",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 30.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Reporta y sigue tus incidencias",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(48.dp))

            // Tipos de incidencia: equipo, red e impresión
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(Icons.Outlined.DesktopWindows, Icons.Outlined.Wifi, Icons.Outlined.Print)
                    .forEach { icono ->
                        Box(
                            Modifier
                                .size(76.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                icono,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
            }

            Spacer(Modifier.weight(1f))

            if (mostrarBotones) {
                AyudaButton("Comenzar", onClick = onComenzar)
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onYaTengoCuenta) {
                    Text(
                        "Ya tengo una cuenta",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                // Reserva el espacio de los botones para que nada "salte"
                Spacer(Modifier.height(108.dp))
            }
        }
    }
}