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
import com.faustino.ayudacampus.ui.theme.colorPrioridad

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

/** "hace 5 min", "hace 3 h", "hace 2 d" */
fun tiempoTranscurrido(millis: Long): String {
    val min = (System.currentTimeMillis() - millis) / 60_000
    return when {
        min < 1 -> "ahora"
        min < 60 -> "hace $min min"
        min < 60 * 24 -> "hace ${min / 60} h"
        else -> "hace ${min / (60 * 24)} d"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketCard(
    numero: Int,
    titulo: String,
    categoria: String,
    icono: String,
    estado: EstadoTicket,
    prioridad: Prioridad,
    fecha: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cliente: String? = null,        // Soporte muestra quién lo reportó
    comentarios: Int = 0
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
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            contentDescription = categoria,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "#$numero · $categoria",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            titulo,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        tiempoTranscurrido(fecha),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (cliente != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Reportado por $cliente",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(10.dp))
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
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "$comentarios",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}