package com.faustino.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.ui.theme.CianContenedor
import com.faustino.ayudacampus.ui.theme.CianContenedorOscuro
import com.faustino.ayudacampus.ui.theme.VioletaContenedor
import com.faustino.ayudacampus.ui.theme.VioletaContenedorOscuro
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Propios: a la derecha, color primario.
 * Del otro perfil: a la izquierda, con el color claro del rol del autor
 * (violeta si escribe Soporte TI, cian si escribe un estudiante).
 */
@Composable
fun BurbujaComentario(
    texto: String,
    autor: String,
    fecha: Long,
    propio: Boolean,
    rolAutor: Rol,
    modifier: Modifier = Modifier
) {
    val oscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val fondo = when {
        propio -> MaterialTheme.colorScheme.primary
        rolAutor == Rol.ADMIN -> if (oscuro) VioletaContenedorOscuro else VioletaContenedor
        else -> if (oscuro) CianContenedorOscuro else CianContenedor
    }
    val colorTexto = when {
        propio -> MaterialTheme.colorScheme.onPrimary
        oscuro -> Color.White
        else -> Color(0xFF1F2937)
    }
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
            Text(
                "$autor · ${horaComentario(fecha)}",
                style = MaterialTheme.typography.labelSmall,
                color = colorTexto.copy(alpha = 0.75f)
            )
            Spacer(Modifier.height(2.dp))
            Text(texto, style = MaterialTheme.typography.bodyMedium, color = colorTexto)
        }
    }
}

/** "10:32" si es de hoy, "ayer", o "14/10" */
fun horaComentario(millis: Long): String {
    val fecha = Calendar.getInstance().apply { timeInMillis = millis }
    val hoy = Calendar.getInstance()
    val ayer = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    fun mismoDia(a: Calendar, b: Calendar) =
        a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)

    return when {
        mismoDia(fecha, hoy) -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(millis))
        mismoDia(fecha, ayer) -> "ayer"
        else -> SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date(millis))
    }
}