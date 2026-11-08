package com.tecsup.books.ui.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun GraficaDona(
    totalGuardados: Int,
    totalFavoritos: Int,
    modifier: Modifier = Modifier
) {
    val colorFavoritos = MaterialTheme.colorScheme.primary
    val colorNoFavoritos = MaterialTheme.colorScheme.secondaryContainer
    val colorVacio = MaterialTheme.colorScheme.surfaceVariant
    val colorTexto = MaterialTheme.colorScheme.onSurface
    val colorSuave = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Proporción de Favoritos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val centroX = size.width / 2f
            val centroY = size.height / 2f
            val diametro = minOf(size.width, size.height) * 0.75f
            val grosorAro = 24.dp.toPx()
            val radioArea = (diametro - grosorAro) / 2f

            val topLeft = Offset(centroX - radioArea, centroY - radioArea)
            val tamanioDona = Size(radioArea * 2, radioArea * 2)

            val paintCentro = android.graphics.Paint().apply {
                color = colorTexto.toArgb()
                textSize = 16.dp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }

            val paintSubtexto = android.graphics.Paint().apply {
                color = colorSuave.toArgb()
                textSize = 12.dp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }

            if (totalGuardados == 0) {
                drawArc(
                    color = colorVacio,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = tamanioDona,
                    style = Stroke(width = grosorAro)
                )

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText("Sin libros", centroX, centroY + 4.dp.toPx(), paintCentro)
                }
            } else {
                val favoritosValidos = totalFavoritos.coerceAtMost(totalGuardados)
                val noFavoritos = maxOf(0, totalGuardados - favoritosValidos)
                val totalBase = totalGuardados.toFloat()

                val anguloFavoritos = (favoritosValidos / totalBase) * 360f
                val anguloNoFavoritos = 360f - anguloFavoritos

                drawArc(
                    color = colorFavoritos,
                    startAngle = -90f,
                    sweepAngle = anguloFavoritos,
                    useCenter = false,
                    topLeft = topLeft,
                    size = tamanioDona,
                    style = Stroke(width = grosorAro, cap = StrokeCap.Butt)
                )

                drawArc(
                    color = colorNoFavoritos,
                    startAngle = -90f + anguloFavoritos,
                    sweepAngle = anguloNoFavoritos,
                    useCenter = false,
                    topLeft = topLeft,
                    size = tamanioDona,
                    style = Stroke(width = grosorAro, cap = StrokeCap.Butt)
                )

                val porcentaje = ((favoritosValidos.toFloat() / totalGuardados) * 100).roundToInt()

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText("$porcentaje%", centroX, centroY - 2.dp.toPx(), paintCentro)
                    canvas.nativeCanvas.drawText("favoritos", centroX, centroY + 16.dp.toPx(), paintSubtexto)
                }
            }
        }
    }
}