package com.faustino.ayudacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Barras verticales con el valor encima de cada barra. */
@Composable
fun GraficoBarras(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    alto: Dp = 130.dp,
    anchoBarra: Dp = 36.dp
) {
    val maximo = (datos.maxOfOrNull { it.valor } ?: 0f).coerceAtLeast(1f)
    val medidor = rememberTextMeasurer()
    val estiloValor = MaterialTheme.typography.labelSmall.copy(
        color = MaterialTheme.colorScheme.onSurface
    )

    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
        datos.forEach { d ->
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .height(alto)
                ) {
                    val texto = medidor.measure("${d.valor.toInt()}", estiloValor)
                    val espacioTexto = texto.size.height + 4.dp.toPx()
                    val altoBarra = (size.height - espacioTexto) * (d.valor / maximo)
                    val ancho = anchoBarra.toPx().coerceAtMost(size.width)
                    val x = (size.width - ancho) / 2
                    val y = size.height - altoBarra

                    if (altoBarra > 0f) {
                        drawRoundRect(
                            color = d.color,
                            topLeft = Offset(x, y),
                            size = Size(ancho, altoBarra),
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )
                    }
                    // Número encima de la barra
                    drawText(
                        texto,
                        topLeft = Offset((size.width - texto.size.width) / 2, y - espacioTexto)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    d.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}