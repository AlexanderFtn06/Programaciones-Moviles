package com.faustino.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Propios a la derecha (color primario); los del otro perfil a la izquierda. */
@Composable
fun BurbujaComentario(
    texto: String,
    autor: String,
    fecha: Long,
    propio: Boolean,
    modifier: Modifier = Modifier
) {
    val fondo = if (propio) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.surfaceVariant
    val colorTexto = if (propio) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onSurface
    val forma = if (propio) RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
    else RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)

    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = if (propio) Arrangement.End else Arrangement.Start
    ) {
        Column(
            Modifier
                .widthIn(max = 280.dp)
                .background(fondo, forma)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            if (!propio) {
                Text(
                    autor,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(texto, style = MaterialTheme.typography.bodyMedium, color = colorTexto)
            Text(
                fechaHora(fecha),
                style = MaterialTheme.typography.labelSmall,
                color = colorTexto.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

fun fechaHora(millis: Long): String =
    SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(millis))