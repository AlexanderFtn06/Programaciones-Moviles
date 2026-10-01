package com.tecsup.mibodega.ui.cliente.screens.terminos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.BodegaTheme

private val secciones = listOf(
    "1. Uso de la aplicación" to
            "Mi Bodega permite a los clientes comprar productos de la bodega y recibirlos en su domicilio. Usar la app implica aceptar estos términos.",
    "2. Pedidos y entrega" to
            "Los pedidos se preparan después de confirmarlos y se entregan en la dirección indicada. El costo de delivery se muestra antes de confirmar.",
    "3. Pagos" to
            "El pago puede hacerse en efectivo al entregar, o con Yape o Plin, según la opción elegida al confirmar el pedido.",
    "4. Datos personales" to
            "Los datos que registras (nombre, teléfono y dirección) se usan para gestionar tus pedidos."
)

/**
 * Pantalla de Términos y condiciones.
 * No guarda estado: solo muestra el texto y avisa cuando se vuelve.
 */
@Composable
fun TerminosScreen(onVolver: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Términos y condiciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        secciones.forEach { (titulo, texto) ->
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TerminosPreview() {
    BodegaTheme {
        TerminosScreen(onVolver = {})
    }
}
