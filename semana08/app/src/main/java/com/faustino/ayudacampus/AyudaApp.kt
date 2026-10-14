package com.faustino.ayudacampus

import android.app.Application
import com.faustino.ayudacampus.data.local.AyudaDatabase
import com.faustino.ayudacampus.data.local.DatosIniciales
import com.faustino.ayudacampus.data.repository.AyudaRepository
import com.faustino.ayudacampus.data.repository.AyudaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AyudaApp : Application() {

    val database: AyudaDatabase by lazy { AyudaDatabase.obtener(this) }

    // Los ViewModels solo conocen la interface, nunca la implementación
    val repository: AyudaRepository by lazy { AyudaRepositoryRoom(database) }

    // TODO (Paso 5): val preferencias by lazy { PreferenciasStore(this) }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        appScope.launch { DatosIniciales.sembrar(database) }
    }
}