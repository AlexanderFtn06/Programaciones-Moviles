package com.faustino.centrotareas

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.datastore.preferences.preferencesDataStore
import com.faustino.centrotareas.data.TareasRepositoryLocal
import com.faustino.centrotareas.ui.PantallaTareas
import com.faustino.centrotareas.ui.theme.CentroTareasTheme
import com.faustino.centrotareas.viewmodel.TareasViewModel
import com.faustino.centrotareas.viewmodel.TareasViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "historial")

class MainActivity : ComponentActivity() {

    private val vm: TareasViewModel by viewModels {
        TareasViewModelFactory(TareasRepositoryLocal(applicationContext.dataStore))
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