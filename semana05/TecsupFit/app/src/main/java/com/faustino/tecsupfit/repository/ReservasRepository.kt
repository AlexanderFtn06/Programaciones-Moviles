package com.faustino.tecsupfit.repository

import androidx.compose.runtime.mutableStateListOf
import com.faustino.tecsupfit.model.ClaseModel
import com.faustino.tecsupfit.model.ReservaModel

object ReservasRepository {

    // Catálogo de clases disponibles (datos de ejemplo)
    val clases = mutableStateListOf(
        ClaseModel(
            id = 1,
            nombre = "Yoga",
            horario = "Hoy 6:00 PM",
            instructor = "Lucía Ramos",
            categoria = "Hoy",
            cuposDisponibles = 8,
            descripcion = "Sesión de yoga enfocada en flexibilidad y respiración."
        ),
        ClaseModel(
            id = 2,
            nombre = "Spinning",
            horario = "Hoy 7:30 PM",
            instructor = "Carlos Mendoza",
            categoria = "Hoy",
            cuposDisponibles = 5,
            descripcion = "Cardio intenso en bicicleta estática, ideal para quemar calorías."
        ),
        ClaseModel(
            id = 3,
            nombre = "Funcional",
            horario = "Miércoles 5:00 PM",
            instructor = "Andrea Solís",
            categoria = "Esta semana",
            cuposDisponibles = 10,
            descripcion = "Entrenamiento funcional con circuitos de fuerza y resistencia."
        ),
        ClaseModel(
            id = 4,
            nombre = "Boxeo",
            horario = "Viernes 6:30 PM",
            instructor = "Diego Torres",
            categoria = "Esta semana",
            cuposDisponibles = 6,
            descripcion = "Clase de boxeo técnico, trabaja fuerza, velocidad y coordinación."
        )
    )

    // Reservas confirmadas por el usuario
    val reservas = mutableStateListOf(
        ReservaModel(1, "Pilates", "Lunes 6:00 PM", "Completada"),
        ReservaModel(2, "Zumba", "Martes 7:00 PM", "Completada")
    )

    fun obtenerClasePorId(id: Int): ClaseModel? {
        return clases.find { it.id == id }
    }

    fun agregarReserva(clase: ClaseModel) {
        val nuevaReserva = ReservaModel(
            id = reservas.size + 1,
            claseNombre = clase.nombre,
            horario = clase.horario,
            estado = "Confirmada"
        )
        reservas.add(nuevaReserva)
    }
}