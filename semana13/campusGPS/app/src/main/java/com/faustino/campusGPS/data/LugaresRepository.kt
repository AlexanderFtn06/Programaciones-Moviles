package com.faustino.campusGPS.data

import com.faustino.campusGPS.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}