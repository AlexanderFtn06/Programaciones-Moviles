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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GraficaBarras(
    totalGuardados: Int,
    totalFavoritos: Int,
    totalUsuarios: Int?,
    modifier: Modifier = Modifier
) {
    val colorGuardados = MaterialTheme.colorScheme.primary
    val colorFavoritos = MaterialTheme.colorScheme.tertiary
    val colorUsuarios = MaterialTheme.colorScheme.secondary
    val colorEtiquetas = MaterialTheme.colorScheme.onSurface
    val colorError = MaterialTheme.colorScheme.error

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Métricas Generales",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val paddingEtiquetas = 32.dp.toPx()
            val paddingSuperior = 28.dp.toPx()
            val alturaDisponible = canvasHeight - paddingEtiquetas - paddingSuperior
            val lineaBaseY = canvasHeight - paddingEtiquetas

            val maximoValor = maxOf(
                totalGuardados,
                totalFavoritos,
                totalUsuarios ?: 0,
                1
            )

            val numColumnas = 3
            val anchoColumna = canvasWidth / numColumnas
            val anchoBarra = (anchoColumna * 0.45f).coerceAtMost(48.dp.toPx())

            val paint = android.graphics.Paint().apply {
                color = colorEtiquetas.toArgb()
                textSize = 12.dp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }

            val paintError = android.graphics.Paint().apply {
                color = colorError.toArgb()
                textSize = 11.dp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }

            val datos = listOf(
                Triple("Guardados", totalGuardados, colorGuardados),
                Triple("Favoritos", totalFavoritos, colorFavoritos),
                Triple("Usuarios", totalUsuarios, colorUsuarios)
            )

            datos.forEachIndexed { i, (etiqueta, valor, colorBarra) ->
                val centroX = anchoColumna * i + (anchoColumna / 2f)

                drawIntoCanvas { canvas ->
                    paint.color = colorEtiquetas.toArgb()
                    canvas.nativeCanvas.drawText(etiqueta, centroX, canvasHeight - 8.dp.toPx(), paint)
                }

                if (valor != null) {
                    val alturaBarra = (valor.toFloat() / maximoValor) * alturaDisponible
                    val topY = lineaBaseY - alturaBarra

                    drawRoundRect(
                        color = colorBarra,
                        topLeft = Offset(centroX - (anchoBarra / 2f), topY),
                        size = Size(anchoBarra, alturaBarra),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    drawIntoCanvas { canvas ->
                        paint.color = colorEtiquetas.toArgb()
                        canvas.nativeCanvas.drawText(valor.toString(), centroX, topY - 8.dp.toPx(), paint)
                    }
                } else {
                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText("Sin datos", centroX, lineaBaseY - 24.dp.toPx(), paintError)
                    }
                }
            }
        }
    }
}