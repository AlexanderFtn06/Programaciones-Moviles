package com.faustino.campusGPS

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faustino.campusGPS.ui.theme.CampusGPSTheme
import com.faustino.campusGPS.data.LugaresRepositoryLocal
import com.faustino.campusGPS.data.UbicacionRepositoryGoogle
import com.faustino.campusGPS.ui.PantallaMapa
import com.faustino.campusGPS.viewmodel.CampusViewModelFactory

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