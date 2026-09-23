package com.faustino.tecsupfit.model

data class ReservaModel(
    val id: Int,
    val claseId: Int,
    val claseNombre: String,
    val horario: String,
    val estado: String = "Confirmada"
)
