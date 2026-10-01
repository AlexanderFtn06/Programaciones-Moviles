package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 7: Pedido confirmado (mockup "Cliente").
 * No guarda estado: recibe los datos del pedido ya listos y avisa
 * hacia arriba qué botón se tocó.
 */
@Composable
fun ConfirmacionScreen(
    numeroPedido: Int,
    total: Double,
    direccion: String,
    referencia: String,
    onVerEstado: () -> Unit,
    onVolverInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = VerdeBodega,
            modifier = Modifier.size(96.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "¡Pedido realizado!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tu pedido está siendo preparado y será entregado pronto",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(28.dp))

        ResumenPedido(
            numeroPedido = numeroPedido,
            total = total,
            direccion = direccion,
            referencia = referencia
        )

        Spacer(Modifier.height(28.dp))

        OutlinedButton(
            onClick = onVerEstado,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.5.dp, VerdeBodega),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeBodega)
        ) {
            Text(text = "Ver estado del pedido", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))

        BotonSecundario(texto = "Volver al inicio", onClick = onVolverInicio)

        Spacer(Modifier.height(24.dp))
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun ResumenPedido(
    numeroPedido: Int,
    total: Double,
    direccion: String,
    referencia: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrisClaro, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Pedido #$numeroPedido",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "S/ %.2f".format(total),
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(text = "Dirección", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = direccion)
        if (referencia.isNotBlank()) {
            Text(text = "($referencia)", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ConfirmacionPreview() {
    BodegaTheme {
        ConfirmacionScreen(
            numeroPedido = 1024,
            total = 25.90,
            direccion = "Av. Los Olivos 123",
            referencia = "Frente al parque",
            onVerEstado = {},
            onVolverInicio = {}
        )
    }
}
