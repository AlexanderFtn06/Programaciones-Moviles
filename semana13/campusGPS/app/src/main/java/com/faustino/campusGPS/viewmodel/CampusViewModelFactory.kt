package com.tecsup.campusGPS.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.campusGPS.data.LugaresRepository
import com.tecsup.campusGPS.data.UbicacionRepository

class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MapaViewModel(ubicacionRepo, lugaresRepo) as T
}