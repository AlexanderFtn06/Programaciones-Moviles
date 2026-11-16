package com.tecsup.campusGPS.data

import com.tecsup.campusGPS.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}