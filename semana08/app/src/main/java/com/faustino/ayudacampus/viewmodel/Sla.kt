package com.faustino.ayudacampus.viewmodel

import com.faustino.ayudacampus.data.local.entity.Ticket
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad

data class EstadoSla(
    val texto: String,
    val vencido: Boolean,
    val porVencer: Boolean
)

fun horasSla(prioridad: Prioridad): Int = when (prioridad) {
    Prioridad.ALTA -> 4
    Prioridad.MEDIA -> 24
    Prioridad.BAJA -> 72
}

private fun formatearDuracion(minutos: Long): String {
    if (minutos <= 0) return "menos de 1 min"
    val dias = minutos / (60 * 24)
    val totalHoras = minutos / 60
    val remHoras = totalHoras % 24
    val remMins = minutos % 60

    return when {
        dias > 0 -> if (remHoras > 0) "$dias d $remHoras h" else "$dias d"
        totalHoras > 0 -> if (remMins > 0) "$totalHoras h $remMins min" else "$totalHoras h"
        else -> "$minutos min"
    }
}

/** Calcula el estado SLA del ticket. Retorna null si no está ABIERTO ni EN_PROCESO. */
fun calcularSla(ticket: Ticket, ahora: Long): EstadoSla? {
    val estado = EstadoTicket.desde(ticket.estado)
    if (estado != EstadoTicket.ABIERTO && estado != EstadoTicket.EN_PROCESO) {
        return null
    }

    val prio = Prioridad.desde(ticket.prioridad)
    val totalMillis = horasSla(prio) * 3600 * 1000L
    val limite = ticket.fecha + totalMillis
    val diff = limite - ahora

    return if (diff > 0) {
        val minRestantes = diff / (60 * 1000L)
        val texto = "Quedan ${formatearDuracion(minRestantes)}"
        val porVencer = diff < (totalMillis * 0.25)
        EstadoSla(texto = texto, vencido = false, porVencer = porVencer)
    } else {
        val diffAbs = -diff
        val minVencidos = diffAbs / (60 * 1000L)
        val texto = "Vencido hace ${formatearDuracion(minVencidos)}"
        EstadoSla(texto = texto, vencido = true, porVencer = false)
    }
}
