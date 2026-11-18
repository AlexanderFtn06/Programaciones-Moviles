package com.faustino.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    fun progresoTarea(): Flow<Float>
    suspend fun contarPrimos(hasta: Int, onProgreso: (Float) -> Unit): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
    fun historial(): Flow<List<String>>
    suspend fun guardarResultado(texto: String)
}