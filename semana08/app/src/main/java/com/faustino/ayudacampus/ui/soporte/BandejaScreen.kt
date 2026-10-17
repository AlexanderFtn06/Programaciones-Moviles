package com.faustino.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.EstadoVacio
import com.faustino.ayudacampus.ui.components.TicketCard
import com.faustino.ayudacampus.ui.theme.ColorAlta
import com.faustino.ayudacampus.ui.theme.colorPrioridad
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
    val filtros by vm.filtros.collectAsState()
    val categorias by vm.categorias.collectAsState()
    val hayFiltrosActivos by vm.hayFiltrosActivos.collectAsState()
    val slas by vm.slas.collectAsState()

    var expandidoCategoria by remember { mutableStateOf(false) }

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
            Spacer(Modifier.height(8.dp))

            // Buscador
            OutlinedTextField(
                value = filtros.texto,
                onValueChange = vm::buscar,
                placeholder = {
                    Text(
                        "Buscar ticket o estudiante…",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(10.dp))

            // Chips de Estado + Prioridad alta (si viene de alerta)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (filtros.soloAlta) {
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
                        selected = f == filtros.estado,
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

            Spacer(Modifier.height(8.dp))

            // Chips de Prioridad (Baja, Media, Alta) y botón Limpiar filtros
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(Prioridad.entries) { p ->
                    val seleccionada = p == filtros.prioridad
                    val colorP = colorPrioridad(p)
                    FilterChip(
                        selected = seleccionada,
                        onClick = { vm.elegirPrioridad(p) },
                        label = { Text(p.etiqueta) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            selectedContainerColor = colorP,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                if (hayFiltrosActivos) {
                    item {
                        TextButton(onClick = vm::limpiarFiltros) {
                            Text("Limpiar filtros", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Selector de Categoría (ExposedDropdownMenuBox)
            Column(Modifier.padding(horizontal = 20.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandidoCategoria,
                    onExpandedChange = { expandidoCategoria = it }
                ) {
                    val nombreCatActual = categorias.find { it.id == filtros.categoriaId }?.nombre ?: "Todas las categorías"
                    OutlinedTextField(
                        value = nombreCatActual,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCategoria) },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandidoCategoria,
                        onDismissRequest = { expandidoCategoria = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas las categorías") },
                            onClick = {
                                vm.elegirCategoria(null)
                                expandidoCategoria = false
                            }
                        )
                        categorias.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.nombre) },
                                onClick = {
                                    vm.elegirCategoria(cat.id)
                                    expandidoCategoria = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            if (tickets.isEmpty()) {
                EstadoVacio(
                    icono = Icons.Outlined.Inbox,
                    titulo = "No hay tickets con estos filtros",
                    mensaje = "Prueba con otra búsqueda o limpia los filtros",
                    textoAccion = "Limpiar filtros",
                    onAccion = vm::limpiarFiltros,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tickets, key = { it.ticket.id }) { t ->
                        TicketCard(
                            numero = t.ticket.id,
                            titulo = t.ticket.titulo,
                            detalle = nombreCorto(t.cliente.nombre),
                            icono = t.categoria.icono,
                            estado = EstadoTicket.desde(t.ticket.estado),
                            prioridad = Prioridad.desde(t.ticket.prioridad),
                            fecha = t.ticket.fecha,
                            onClick = { onAbrirTicket(t.ticket.id) },
                            sla = slas[t.ticket.id],
                            modifier = Modifier.animateItem()
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
