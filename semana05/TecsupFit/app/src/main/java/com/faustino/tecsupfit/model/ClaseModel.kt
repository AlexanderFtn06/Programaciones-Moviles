package com.faustino.tecsupfit.model

data class ClaseModel(
    val id: Int,
    val nombre: String,
    val horario: String,
    val instructor: String,
    val categoria: String,
    val cuposDisponibles: Int,
    val descripcion: String
)