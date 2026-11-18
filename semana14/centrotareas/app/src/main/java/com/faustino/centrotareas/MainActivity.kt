package com.faustino.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.faustino.centrotareas.data.TareasRepositoryLocal
import com.faustino.centrotareas.ui.PantallaTareas
import com.faustino.centrotareas.ui.theme.CentroTareasTheme
import com.faustino.centrotareas.viewmodel.TareasViewModel
import com.faustino.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: TareasViewModel by viewModels {
        TareasViewModelFactory(TareasRepositoryLocal())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme {
                PantallaTareas(vm)
            }
        }
    }
}