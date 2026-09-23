package com.tecsup.tecsupfit.model

data class ReservaModel(
    val id: Int,
    val claseNombre: String,
    val horario: String,
    val estado: String = "Confirmada"
)
