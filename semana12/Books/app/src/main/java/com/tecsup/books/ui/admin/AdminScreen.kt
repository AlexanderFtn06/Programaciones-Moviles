package com.tecsup.books.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Panel Admin",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Libros guardados: ${ui.totalGuardados}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Favoritos: ${ui.totalFavoritos}",
            style = MaterialTheme.typography.bodyLarge
        )

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
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}