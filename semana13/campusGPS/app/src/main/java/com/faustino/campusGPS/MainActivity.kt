package com.tecsup.campusGPS

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faustino.campusGPS.ui.theme.CampusGPSTheme
import com.tecsup.campusGPS.data.LugaresRepositoryLocal
import com.tecsup.campusGPS.data.UbicacionRepositoryGoogle
import com.tecsup.campusGPS.ui.PantallaMapa
import com.tecsup.campusGPS.viewmodel.CampusViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = CampusViewModelFactory(
            UbicacionRepositoryGoogle(applicationContext),
            LugaresRepositoryLocal()
        )

        setContent {
            CampusGPSTheme {
                PantallaMapa(vm = viewModel(factory = factory))
            }
        }
    }
}