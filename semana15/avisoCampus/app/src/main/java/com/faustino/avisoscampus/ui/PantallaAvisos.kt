package com.faustino.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faustino.avisoscampus.notificacion.Notificador
import com.faustino.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostró, avisar al ViewModel
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Tarjeta("Permiso de notificaciones") {
                Text("Sin pedir")
                Button(onClick = { /* TODO Paso 2 */ }) { Text("Permitir notificaciones") }
            }
            Tarjeta("Avisos") {
                Button(onClick = { /* TODO Paso 3 */ }) { Text("Mostrar aviso") }
            }
            Tarjeta("Recordatorio") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { /* TODO Paso 5 */ }) { Text("Recordar en 10 s") }
                    OutlinedButton(onClick = { /* TODO Paso 5 */ }) { Text("Cancelar") }
                }
            }
            Tarjeta("Receptor de anuncios") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = false, onCheckedChange = { /* TODO Paso 6 */ })
                    Text("Escuchar el modo avión", modifier = Modifier.padding(start = 8.dp))
                }
            }
            Tarjeta("Historial") {
                Text("Sin avisos todavía")
            }
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}