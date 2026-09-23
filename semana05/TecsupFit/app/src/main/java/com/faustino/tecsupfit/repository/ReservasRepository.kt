package com.faustino.tecsupfit.repository

import androidx.compose.runtime.mutableStateListOf
import com.faustino.tecsupfit.model.ClaseModel
import com.faustino.tecsupfit.model.ReservaModel

object ReservasRepository {

    // Catálogo de clases disponibles (datos de ejemplo)
    // Catálogo de clases disponibles (datos de ejemplo)
    val clases = mutableStateListOf(
        ClaseModel(
            id = 1,
            nombre = "Yoga funcional",
            dia = "Hoy",
            horario = "7:00 am",
            sala = "Sala 2",
            duracionMin = 60,
            instructor = "Lucía Ramos",
            categoria = "Hoy",
            cuposDisponibles = 8,
            cuposTotales = 15,
            descripcion = "Sesión de yoga con enfoque en flexibilidad, equilibrio y respiración.",
            horariosDisponibles = listOf("7:00 am", "9:00 am", "12:00 pm")
        ),
        ClaseModel(
            id = 2,
            nombre = "Cross Training",
            dia = "Hoy",
            horario = "6:00 pm",
            sala = "Sala 1",
            duracionMin = 45,
            instructor = "Diego Torres",
            categoria = "Hoy",
            cuposDisponibles = 8,
            cuposTotales = 12,
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            horariosDisponibles = listOf("6:00 pm", "7:00 pm", "8:00 pm")
        ),
        ClaseModel(
            id = 3,
            nombre = "Spinning",
            dia = "Hoy",
            horario = "7:30 pm",
            sala = "Sala 3",
            duracionMin = 45,
            instructor = "Carlos Mendoza",
            categoria = "Hoy",
            cuposDisponibles = 5,
            cuposTotales = 20,
            descripcion = "Cardio intenso en bicicleta estática, ideal para quemar calorías.",
            horariosDisponibles = listOf("7:30 pm", "8:30 pm")
        ),
        ClaseModel(
            id = 4,
            nombre = "Funcional",
            dia = "Miércoles",
            horario = "5:00 pm",
            sala = "Sala 1",
            duracionMin = 50,
            instructor = "Andrea Solís",
            categoria = "Esta semana",
            cuposDisponibles = 10,
            cuposTotales = 16,
            descripcion = "Entrenamiento funcional con circuitos de fuerza y resistencia.",
            horariosDisponibles = listOf("5:00 pm", "6:00 pm")
        ),
        ClaseModel(
            id = 5,
            nombre = "Boxeo",
            dia = "Viernes",
            horario = "6:30 pm",
            sala = "Sala 2",
            duracionMin = 60,
            instructor = "Marco Paredes",
            categoria = "Esta semana",
            cuposDisponibles = 6,
            cuposTotales = 12,
            descripcion = "Clase de boxeo técnico, trabaja fuerza, velocidad y coordinación.",
            horariosDisponibles = listOf("6:30 pm", "7:30 pm")
        )
    )

    // Reservas confirmadas por el usuario
    val reservas = mutableStateListOf(
        ReservaModel(1, "Yoga funcional", "Ayer, 7:00 am", "Completada"),
        ReservaModel(2, "Spinning", "Lunes, 7:30 pm", "Completada")
    )

    fun obtenerClasePorId(id: Int): ClaseModel? {
        return clases.find { it.id == id }
    }

    fun agregarReserva(clase: ClaseModel, turno: String) {
        val nuevaReserva = ReservaModel(
            id = reservas.size + 1,
            claseNombre = clase.nombre,
            horario = "${clase.dia}, $turno",
            estado = "Confirmada"
        )
        reservas.add(nuevaReserva)
    }
    fun completarReserva(id: Int) {
        val index = reservas.indexOfFirst { it.id == id }
        if (index != -1) {
            reservas[index] = reservas[index].copy(estado = "Completada")
        }
    }
}