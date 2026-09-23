package com.faustino.tecsupfit.model

data class Horario(
    val hora: String,
    val cuposDisponibles: Int
)

data class ClaseGym(
    val id: Int,
    val nombre: String,
    val instructor: String,
    val dia: String, // "Hoy" o "Esta semana"
    val horarios: List<Horario>
)

data class Reserva(
    val clase: ClaseGym,
    val horario: Horario,
    val estado: String = "Confirmada" // "Confirmada" o "Completada"
)

object ClasesRepository {
    val clases = listOf(
        ClaseGym(1, "Spinning", "Carla Ruiz", "Hoy",
            listOf(Horario("7:00 am", 5), Horario("6:00 pm", 3), Horario("8:00 pm", 8))
        ),
        ClaseGym(2, "Yoga", "Mateo Salas", "Hoy",
            listOf(Horario("9:00 am", 10), Horario("5:00 pm", 6), Horario("7:00 pm", 4))
        ),
        ClaseGym(3, "CrossFit", "Diego Paredes", "Esta semana",
            listOf(Horario("6:00 am", 2), Horario("12:00 pm", 7), Horario("6:30 pm", 5))
        ),
        ClaseGym(4, "Zumba", "Valeria Nuñez", "Esta semana",
            listOf(Horario("10:00 am", 9), Horario("4:00 pm", 6), Horario("7:30 pm", 3))
        )
    )

    fun obtenerClasePorId(id: Int): ClaseGym? = clases.find { it.id == id }
}