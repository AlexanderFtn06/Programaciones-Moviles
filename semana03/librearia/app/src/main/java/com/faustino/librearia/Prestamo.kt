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

const val MULTA_ALUMNO = 1.50
const val MULTA_DOCENTE = 2.00

val FORMATO_FECHA: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun obtenerMultaPorDia(tipoUsuario: String): Double {
    return if (tipoUsuario.equals("Docente", ignoreCase = true)) MULTA_DOCENTE else MULTA_ALUMNO
}
fun calcularMultaTotal(tipoUsuario: String, diasAtraso: Int): Double {
    val multaPorDia = obtenerMultaPorDia(tipoUsuario)
    var acumulado = 0.0
    for (dia in 1..diasAtraso) {
        acumulado += multaPorDia
    }
    return acumulado
}

fun estadoPrestamo(diasAtraso: Int): String {
    return if (diasAtraso > 0) "Devuelto con $diasAtraso dia(s) de atraso" else "Devuelto a tiempo"
}

fun mostrarTablaMultas(tipoUsuario: String, fechaDevolucionTexto: String, diasAtraso: Int) {
    val multaPorDia = obtenerMultaPorDia(tipoUsuario)
    val fechaDevolucion = LocalDate.parse(fechaDevolucionTexto, FORMATO_FECHA)
    println("Dia\tFecha\t\tMulta/Dia\tAcumulado")
    var acumulado = 0.0
    for (dia in 1..diasAtraso) {
        acumulado += multaPorDia
        val fechaDia = fechaDevolucion.plusDays(dia.toLong())
        println(String.format("%d\t%s\tS/ %.2f\tS/ %.2f", dia, fechaDia.format(FORMATO_FECHA), multaPorDia, acumulado))
    }
}
fun main() {
    println("=========================================")
    println(" SISTEMA DE MULTAS")
    println("=========================================")

    print("Titulo del libro: ")
    val titulo = readLine() ?: ""
    print("Tipo de usuario (Docente/Alumno): ")
    val tipoUsuario = readLine() ?: ""
    print("Fecha de prestamo (dd/MM/yyyy): ")
    val fechaPrestamo = readLine() ?: ""
    print("Fecha de devolucion (dd/MM/yyyy): ")
    val fechaDevolucion = readLine() ?: ""
    print("Fecha de entrega (dd/MM/yyyy): ")
    val fechaEntrega = readLine() ?: ""

    print("Dias de atraso: ")
    val diasAtraso = readLine()?.toIntOrNull() ?: 0
    val prestamo = Prestamo(titulo, tipoUsuario, fechaPrestamo, fechaDevolucion, fechaEntrega, diasAtraso)

    println()
    println("Titulo Libro     : ${prestamo.titulo}")
    println("Tipo de Usuario  : ${prestamo.tipoUsuario}")
    println("Fecha Prestamo   : ${prestamo.fechaPrestamo}")
    println("Fecha Devolucion : ${prestamo.fechaDevolucion}")
    println("Fecha Entrega    : ${prestamo.fechaEntrega}")
    println("Estado           : ${estadoPrestamo(prestamo.diasAtraso)}")
    println()
    mostrarTablaMultas(prestamo.tipoUsuario, prestamo.fechaDevolucion, prestamo.diasAtraso)
    val multaTotal = calcularMultaTotal(prestamo.tipoUsuario, prestamo.diasAtraso)
    println()
    println(String.format("%-20s S/ %.2f", "MULTA TOTAL:", multaTotal))
}