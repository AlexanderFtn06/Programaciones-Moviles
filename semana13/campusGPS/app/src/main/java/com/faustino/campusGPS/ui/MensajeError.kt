package com.faustino.campusGPS.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.faustino.campusGPS.model.TipoError
import com.faustino.campusGPS.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = error.mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        when (error.tipo) {
            TipoError.SIN_PERMISO -> OutlinedButton(
                onClick = onAbrirAjustes,
                modifier = Modifier.padding(top = 12.dp)
            ) { Text("Abrir ajustes") }

            TipoError.GPS_APAGADO, TipoError.OTRO -> OutlinedButton(
                onClick = onReintentar,
                modifier = Modifier.padding(top = 12.dp)
            ) { Text("Reintentar") }

            TipoError.SIN_PLAY_SERVICES -> {}
        }
    }
}