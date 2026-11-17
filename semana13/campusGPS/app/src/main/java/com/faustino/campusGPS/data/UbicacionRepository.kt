package com.faustino.campusGPS.data

import com.faustino.campusGPS.model.Posicion

class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación")

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun playServicesDisponible(): Boolean
}