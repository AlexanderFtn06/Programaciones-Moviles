package com.faustino.ayudacampus.ui.theme

import androidx.compose.ui.graphics.Color
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad

// Estudiante (cian)
val CianPrimario = Color(0xFF0891B2)
val CianContenedor = Color(0xFFCFFAFE)
val CianFondo = Color(0xFFF7FDFE)
val CianTextoContenedor = Color(0xFF164E63)
val CianVariante = Color(0xFFE6F4F7)

// Soporte TI (violeta)
val VioletaPrimario = Color(0xFF7C3AED)
val VioletaContenedor = Color(0xFFEDE4FF)
val VioletaFondo = Color(0xFFFAF8FF)
val VioletaTextoContenedor = Color(0xFF3B0764)
val VioletaVariante = Color(0xFFF1ECFB)

// Modo oscuro
val CianClaro = Color(0xFF67E8F9)
val CianContenedorOscuro = Color(0xFF155E75)
val VioletaClaro = Color(0xFFC4B5FD)
val VioletaContenedorOscuro = Color(0xFF5B21B6)
val FondoOscuro = Color(0xFF111418)
val SuperficieOscura = Color(0xFF1A1F24)
val VarianteOscura = Color(0xFF2A3038)

// Estado del ticket
val ColorAbierto = Color(0xFFD97706)
val ColorEnProceso = Color(0xFF2563EB)
val ColorResuelto = Color(0xFF16A34A)
val ColorCerrado = Color(0xFF6B7280)

// Prioridad
val ColorAlta = Color(0xFFDC2626)
val ColorMedia = Color(0xFFB45309)
val ColorBaja = Color(0xFF6B7280)

val ColorEstrella = Color(0xFFF59E0B)

fun colorEstado(estado: EstadoTicket): Color = when (estado) {
    EstadoTicket.ABIERTO -> ColorAbierto
    EstadoTicket.EN_PROCESO -> ColorEnProceso
    EstadoTicket.RESUELTO -> ColorResuelto
    EstadoTicket.CERRADO -> ColorCerrado
}

fun colorPrioridad(prioridad: Prioridad): Color = when (prioridad) {
    Prioridad.ALTA -> ColorAlta
    Prioridad.MEDIA -> ColorMedia
    Prioridad.BAJA -> ColorBaja
}