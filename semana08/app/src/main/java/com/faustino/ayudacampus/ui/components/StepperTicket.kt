package com.faustino.ayudacampus.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.ui.theme.ColorResuelto

/** Abierto → En proceso → Resuelto → Cerrado */
@Composable
fun StepperTicket(estado: EstadoTicket, modifier: Modifier = Modifier) {
    val pasos = EstadoTicket.entries
    val actual = estado.ordinal
    val cerrado = estado == EstadoTicket.CERRADO
    val activo = MaterialTheme.colorScheme.primary
    val inactivo = MaterialTheme.colorScheme.outlineVariant

    // Animación de pulso para el anillo del paso actual
    val infiniteTransition = rememberInfiniteTransition(label = "stepper_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Row(modifier.fillMaxWidth()) {
        pasos.forEachIndexed { i, paso ->
            val completado = i < actual || (cerrado && i == actual)
            val esActual = i == actual && !cerrado

            val colorLineaIzq = animateColorAsState(
                targetValue = when {
                    i == 0 -> Color.Transparent
                    i <= actual -> activo
                    else -> inactivo
                },
                animationSpec = tween(300),
                label = "line_left_$i"
            ).value

            val colorLineaDer = animateColorAsState(
                targetValue = when {
                    i == pasos.lastIndex -> Color.Transparent
                    i < actual -> activo
                    else -> inactivo
                },
                animationSpec = tween(300),
                label = "line_right_$i"
            ).value

            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    // Línea izquierda
                    Box(
                        Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(colorLineaIzq)
                    )
                    // Círculo
                    Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                        when {
                            completado -> Box(
                                Modifier
                                    .size(24.dp)
                                    .background(ColorResuelto, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            esActual -> {
                                Box(
                                    Modifier
                                        .size(24.dp)
                                        .background(activo.copy(alpha = pulseAlpha), CircleShape)
                                )
                                Box(
                                    Modifier
                                        .size(14.dp)
                                        .background(activo, CircleShape)
                                )
                            }

                            else -> Box(
                                Modifier
                                    .size(20.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                                    .border(2.dp, inactivo, CircleShape)
                            )
                        }
                    }
                    // Línea derecha
                    Box(
                        Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(colorLineaDer)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    paso.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = if (esActual) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (i <= actual) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
