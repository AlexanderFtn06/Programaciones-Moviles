package com.faustino.campusGPS.data

import com.faustino.campusGPS.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
}