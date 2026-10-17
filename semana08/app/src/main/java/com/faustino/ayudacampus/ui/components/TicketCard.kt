package com.faustino.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.ui.theme.ColorAbierto
import com.faustino.ayudacampus.ui.theme.ColorAlta
import com.faustino.ayudacampus.ui.theme.colorPrioridad
import com.faustino.ayudacampus.viewmodel.EstadoSla

/** Iconos válidos para una categoría. */
val ICONOS_CATEGORIA = listOf("pc", "code", "wifi", "key", "printer", "tag")

fun iconoCategoria(icono: String): ImageVector = when (icono) {
    "pc" -> Icons.Outlined.Computer
    "code" -> Icons.Outlined.Code
    "wifi" -> Icons.Outlined.Wifi
    "key" -> Icons.Outlined.Key
    "printer" -> Icons.Outlined.Print
    else -> Icons.Outlined.LocalOffer
}

/** "#0012" */
fun numeroTicket(id: Int): String = "#%04d".format(id)

/** "Ahora", "Hace 5 min", "Hace 3 h", "Ayer", "Hace 4 d" */
fun tiempoTranscurrido(millis: Long): String {
    val min = (System.currentTimeMillis() - millis) / 60_000
    return when {
        min < 1 -> "Ahora"
        min < 60 -> "Hace $min min"
        min < 60 * 24 -> "Hace ${min / 60} h"
        min < 60 * 48 -> "Ayer"
        else -> "Hace ${min / (60 * 24)} d"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketCard(
    numero: Int,
    titulo: String,
    detalle: String,          // Estudiante: ubicación · Soporte: nombre del cliente
    icono: String,
    estado: EstadoTicket,
    prioridad: Prioridad,
    fecha: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    comentarios: Int = 0,
    sla: EstadoSla? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            // Franja de color según la prioridad
            Box(
                Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(colorPrioridad(prioridad))
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(14.dp)
            ) {
                Row {
                    Box(
                        Modifier
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            iconoCategoria(icono),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            titulo,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "${numeroTicket(numero)} · $detalle",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EstadoChip(estado)
                    PrioridadChip(prioridad)
                    Spacer(Modifier.weight(1f))
                    if (comentarios > 0) {
                        Icon(
                            Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comentarios",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "$comentarios",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        tiempoTranscurrido(fecha),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (sla != null) {
                    Spacer(Modifier.height(8.dp))
                    val colorSla = when {
                        sla.vencido -> ColorAlta
                        sla.porVencer -> ColorAbierto
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = colorSla
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            sla.texto,
                            style = MaterialTheme.typography.labelSmall,
                            color = colorSla
                        )
                    }
                }
            }
        }
    }
}