package com.faustino.campusGPS.data

import android.annotation.SuppressLint
import android.content.Context
import com.faustino.campusGPS.model.Posicion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(private val context: Context) : UbicacionRepository {

    private val cliente = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val ubicacion = cliente
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .await()
            ?: throw UbicacionNoDisponibleException()
        Posicion(ubicacion.latitude, ubicacion.longitude)
    }

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
}