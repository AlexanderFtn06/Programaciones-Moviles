package com.faustino.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.ui.theme.colorEstado

/** Etiqueta redondeada con un punto de color. La usan EstadoChip y PrioridadChip. */
@Composable
fun EtiquetaColor(texto: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        color = color.copy(alpha = 0.14f),
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(6.dp).background(color, CircleShape))
            Spacer(Modifier.width(6.dp))
            Text(texto, style = MaterialTheme.typography.labelSmall, color = color)
        }
    }
}

@Composable
fun EstadoChip(estado: EstadoTicket, modifier: Modifier = Modifier) {
    EtiquetaColor(estado.etiqueta, colorEstado(estado), modifier)
}