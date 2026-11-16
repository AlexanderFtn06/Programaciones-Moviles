package com.faustino.campusGPS.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faustino.campusGPS.model.UbicacionUi
import com.faustino.campusGPS.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("CampusGPS") }) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { vm.buscar() },
                enabled = estado !is UbicacionUi.Buscando
            ) { Text("Obtener mi ubicación") }

            Spacer(Modifier.height(16.dp))

            when (val e = estado) {
                UbicacionUi.Inicial -> {}
                UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista ->
                    Text("Lat: ${e.posicion.latitud}\nLon: ${e.posicion.longitud}")
                is UbicacionUi.Error ->
                    Text(e.mensaje, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}