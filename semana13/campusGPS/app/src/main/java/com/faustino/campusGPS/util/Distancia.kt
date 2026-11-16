package com.faustino.campusGPS.util

import android.location.Location
import com.faustino.campusGPS.model.Lugar
import com.faustino.campusGPS.model.Posicion

fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}