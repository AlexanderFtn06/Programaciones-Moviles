package com.tecsup.mibodega.ui.cliente.screens.pedidos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.Rutas
import com.tecsup.mibodega.ui.cliente.modelo.PedidoConfirmado
import com.tecsup.mibodega.ui.componentes.BarraInferior
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Destino "Pedidos" de la barra inferior.
 * Muestra el historial de pedidos confirmados (el más reciente primero).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    pedidos: List<PedidoConfirmado>,
    onNavegar: (String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis pedidos", fontWeight = FontWeight.Bold) }) },
        bottomBar = { BarraInferior(rutaActual = Rutas.PEDIDOS, onNavegar = onNavegar) }
    ) { paddingInterno ->
        if (pedidos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes pedidos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.numero }) { pedido ->
                    FilaPedido(pedido)
                }
            }
        }
    }
}

@Composable
private fun FilaPedido(pedido: PedidoConfirmado) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrisClaro, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pedido #${pedido.numero}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "S/ %.2f".format(pedido.total),
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = pedido.direccion,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PedidosPreview() {
    BodegaTheme {
        PedidosScreen(
            pedidos = listOf(
                PedidoConfirmado(1025, 18.40, "Av. Los Olivos 123", "Frente al parque"),
                PedidoConfirmado(1024, 25.90, "Av. Los Olivos 123", "Frente al parque")
            ),
            onNavegar = {}
        )
    }
}
