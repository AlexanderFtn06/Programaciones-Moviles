package com.faustino.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.TicketCard
import com.faustino.ayudacampus.ui.theme.ColorAlta
import com.faustino.ayudacampus.viewmodel.BandejaViewModel
import com.faustino.ayudacampus.viewmodel.FiltroEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandejaScreen(
    vm: BandejaViewModel,
    onAtras: () -> Unit,
    onAbrirTicket: (Int) -> Unit,
    barraInferior: @Composable () -> Unit
) {
    val tickets by vm.tickets.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val soloAlta by vm.soloAlta.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Tickets", onAtras = onAtras) },
        bottomBar = barraInferior
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Filtro que viene de la alerta del panel (se puede quitar)
                if (soloAlta) {
                    item {
                        InputChip(
                            selected = true,
                            onClick = vm::quitarSoloAlta,
                            label = { Text("Prioridad alta") },
                            trailingIcon = {
                                Icon(Icons.Filled.Close, contentDescription = "Quitar filtro", modifier = Modifier.size(16.dp))
                            },
                            shape = RoundedCornerShape(50),
                            colors = InputChipDefaults.inputChipColors(
                                selectedContainerColor = ColorAlta.copy(alpha = 0.15f),
                                selectedLabelColor = ColorAlta,
                                selectedTrailingIconColor = ColorAlta
                            )
                        )
                    }
                }
                items(FiltroEstado.entries) { f ->
                    FilterChip(
                        selected = f == filtro,
                        onClick = { vm.seleccionarFiltro(f) },
                        label = { Text(f.etiqueta) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            if (tickets.isEmpty()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.Inbox,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("No hay tickets aquí", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tickets, key = { it.ticket.id }) { t ->
                        TicketCard(
                            numero = t.ticket.id,
                            titulo = t.ticket.titulo,
                            detalle = nombreCorto(t.cliente.nombre),    // "Juan P."
                            icono = t.categoria.icono,
                            estado = EstadoTicket.desde(t.ticket.estado),
                            prioridad = Prioridad.desde(t.ticket.prioridad),
                            fecha = t.ticket.fecha,
                            onClick = { onAbrirTicket(t.ticket.id) }
                        )
                    }
                }
            }
        }
    }
}

/** "Juan Pérez" -> "Juan P." */
private fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size >= 2) "${partes[0]} ${partes[1].first()}." else nombre
}