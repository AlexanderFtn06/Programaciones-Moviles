package com.faustino.ayudacampus.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.model.EstadoTicket

/** Abierto → En proceso → Resuelto → Cerrado */
@Composable
fun StepperTicket(estado: EstadoTicket, modifier: Modifier = Modifier) {
    val pasos = EstadoTicket.entries
    val actual = estado.ordinal
    val cerrado = estado == EstadoTicket.CERRADO
    val activo = MaterialTheme.colorScheme.primary
    val inactivo = MaterialTheme.colorScheme.outlineVariant

    Row(modifier.fillMaxWidth()) {
        pasos.forEachIndexed { i, paso ->
            val completado = i < actual || (cerrado && i == actual)
            val esActual = i == actual && !cerrado

            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    // Línea izquierda
                    Box(
                        Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(
                                when {
                                    i == 0 -> Color.Transparent
                                    i <= actual -> activo
                                    else -> inactivo
                                }
                            )
                    )
                    // Círculo
                    Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                        when {
                            completado -> Box(
                                Modifier
                                    .size(24.dp)
                                    .background(activo, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            esActual -> {
                                Box(
                                    Modifier
                                        .size(24.dp)
                                        .background(activo.copy(alpha = 0.25f), CircleShape)
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
                            .background(
                                when {
                                    i == pasos.lastIndex -> Color.Transparent
                                    i < actual -> activo
                                    else -> inactivo
                                }
                            )
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