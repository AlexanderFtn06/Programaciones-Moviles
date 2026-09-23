package com.faustino.tecsupfit.model

data class ClaseModel(
    val id: Int,
    val nombre: String,
    val dia: String,
    val horario: String,
    val sala: String,
    val duracionMin: Int,
    val instructor: String,
    val categoria: String,
    val cuposDisponibles: Int,
    val cuposTotales: Int,
    val descripcion: String,
    val horariosDisponibles: List<String>

)