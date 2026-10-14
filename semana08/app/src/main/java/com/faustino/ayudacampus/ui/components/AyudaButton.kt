package com.faustino.ayudacampus.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AyudaButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    secundario: Boolean = false,
    icono: ImageVector? = null
) {
    val forma = RoundedCornerShape(14.dp)
    val mod = modifier.fillMaxWidth().height(52.dp)
    if (secundario) {
        OutlinedButton(onClick = onClick, modifier = mod, enabled = habilitado, shape = forma) {
            ContenidoBoton(texto, icono)
        }
    } else {
        Button(onClick = onClick, modifier = mod, enabled = habilitado, shape = forma) {
            ContenidoBoton(texto, icono)
        }
    }
}

@Composable
private fun ContenidoBoton(texto: String, icono: ImageVector?) {
    if (icono != null) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
    }
    Text(texto, style = MaterialTheme.typography.labelLarge)
}