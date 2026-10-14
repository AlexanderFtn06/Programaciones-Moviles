package com.faustino.ayudacampus.data.model

/** Resultado de la consulta con LEFT JOIN del panel (Paso 2). */
data class ConteoCategoria(
    val nombre: String,
    val icono: String,
    val cantidad: Int
)