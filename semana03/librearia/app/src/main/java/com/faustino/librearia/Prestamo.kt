package com.faustino.libreria
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Prestamo(
    val titulo: String,
    val tipoUsuario: String,
    val fechaPrestamo: String,
    val fechaDevolucion: String,
    val fechaEntrega: String,
    val diasAtraso: Int
)

const val MULTA_POR_DIA = 1.50

val FORMATO_FECHA: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun calcularMultaTotal(diasAtraso: Int): Double {
    var acumulado = 0.0
    for (dia in 1..diasAtraso) {
        acumulado += MULTA_POR_DIA
    }
    return acumulado
}

fun estadoPrestamo(diasAtraso: Int): String {
    return if (diasAtraso > 0) "Devuelto con $diasAtraso dia(s) de atraso" else "Devuelto a tiempo"
}

fun mostrarTablaMultas(fechaDevolucionTexto: String, diasAtraso: Int) {
    val fechaDevolucion = LocalDate.parse(fechaDevolucionTexto, FORMATO_FECHA)
    println("Dia\tFecha\t\tMulta/Dia\tAcumulado")
    var acumulado = 0.0
    for (dia in 1..diasAtraso) {
        acumulado += MULTA_POR_DIA
        val fechaDia = fechaDevolucion.plusDays(dia.toLong())
        println(String.format("%d\t%s\tS/ %.2f\tS/ %.2f", dia, fechaDia.format(FORMATO_FECHA), MULTA_POR_DIA, acumulado))
    }
}