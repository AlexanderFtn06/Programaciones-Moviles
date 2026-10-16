package com.faustino.ayudacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Dato que reciben GraficoDona y GraficoBarras. */
data class DatoGrafico(val etiqueta: String, val valor: Float, val color: Color)

@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    tamano: Dp = 130.dp,
    grosor: Dp = 22.dp,
    textoCentro: String = "tickets"
) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val vacio = MaterialTheme.colorScheme.surfaceVariant

    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        // Dona dibujada con Canvas
        Box(Modifier.size(tamano), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val g = grosor.toPx()
                val origen = Offset(g / 2, g / 2)
                val arco = Size(size.width - g, size.height - g)
                if (total <= 0f) {
                    drawArc(vacio, 0f, 360f, false, origen, arco, style = Stroke(g))
                } else {
                    var inicio = -90f
                    datos.forEach { d ->
                        val barrido = 360f * d.valor / total
                        drawArc(d.color, inicio, barrido, false, origen, arco, style = Stroke(g))
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${total.toInt()}", style = MaterialTheme.typography.headlineSmall)
                Text(
                    textoCentro,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.width(20.dp))

        // Leyenda: color · etiqueta ............ número
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            datos.forEach { d ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(10.dp)
                            .background(d.color, RoundedCornerShape(3.dp))
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        d.etiqueta,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text("${d.valor.toInt()}", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}