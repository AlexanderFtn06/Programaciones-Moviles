package com.faustino.ayudacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/** Barras horizontales: una fila por dato. */
@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val maximo = (datos.maxOfOrNull { it.valor } ?: 0f).coerceAtLeast(1f)
    val pista = MaterialTheme.colorScheme.surfaceVariant

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        datos.forEach { d ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    d.etiqueta,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(96.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Canvas(
                    Modifier
                        .weight(1f)
                        .height(16.dp)
                ) {
                    val radio = CornerRadius(size.height / 2)
                    drawRoundRect(color = pista, cornerRadius = radio)
                    val ancho = size.width * (d.valor / maximo)
                    if (ancho > 0f) {
                        drawRoundRect(
                            color = d.color,
                            size = Size(ancho, size.height),
                            cornerRadius = radio
                        )
                    }
                }
                Text(
                    "${d.valor.toInt()}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}