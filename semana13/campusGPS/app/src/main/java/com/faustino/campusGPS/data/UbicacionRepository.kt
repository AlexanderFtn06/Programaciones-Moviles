package com.tecsup.campusGPS.data

import com.tecsup.campusGPS.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
}