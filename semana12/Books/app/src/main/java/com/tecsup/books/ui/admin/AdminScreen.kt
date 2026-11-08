package com.tecsup.books.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminScreen(
    ui: AdminUiState,
    onSincronizar: () -> Unit,
    onLimpiarCache: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Panel Admin",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            GraficaBarras(
                totalGuardados = ui.totalGuardados,
                totalFavoritos = ui.totalFavoritos,
                totalUsuarios = ui.totalUsuarios
            )
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            GraficaDona(
                totalGuardados = ui.totalGuardados,
                totalFavoritos = ui.totalFavoritos
            )
        }

        Button(
            onClick = onSincronizar,
            enabled = !ui.sincronizando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (ui.sincronizando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Sincronizar")
            }
        }

        OutlinedButton(
            onClick = onLimpiarCache,
            enabled = !ui.sincronizando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar caché")
        }

        ui.mensaje?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}