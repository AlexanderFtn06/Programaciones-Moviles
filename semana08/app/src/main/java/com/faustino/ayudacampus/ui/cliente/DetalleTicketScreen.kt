package com.faustino.ayudacampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.AyudaTextField
import com.faustino.ayudacampus.ui.components.BurbujaComentario
import com.faustino.ayudacampus.ui.components.EstadoChip
import com.faustino.ayudacampus.ui.components.Estrellas
import com.faustino.ayudacampus.ui.components.PrioridadChip
import com.faustino.ayudacampus.ui.components.StepperTicket
import com.faustino.ayudacampus.ui.components.iconoCategoria
import com.faustino.ayudacampus.ui.components.numeroTicket
import com.faustino.ayudacampus.ui.components.tiempoTranscurrido
import com.faustino.ayudacampus.ui.theme.ColorAlta
import com.faustino.ayudacampus.ui.theme.ColorResuelto
import com.faustino.ayudacampus.viewmodel.TicketDetalleViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetalleTicketScreen(
    vm: TicketDetalleViewModel,
    onAtras: () -> Unit,
    barraInferior: @Composable () -> Unit
) {
    val detalle by vm.ticket.collectAsState()
    val comentarios by vm.comentarios.collectAsState()
    val texto by vm.texto.collectAsState()
    val calificacion by vm.calificacion.collectAsState()
    val comentarioFinal by vm.comentarioFinal.collectAsState()
    val mensajeError by vm.error.collectAsState()

    val estado = detalle?.let { EstadoTicket.desde(it.ticket.estado) }
    val puedeComentar = estado == EstadoTicket.ABIERTO || estado == EstadoTicket.EN_PROCESO
    val tecladoVisible = WindowInsets.isImeVisible
    val listState = rememberLazyListState()

    // Al llegar un comentario nuevo, baja hasta el final de la conversación
    var cantidadPrevia by remember { mutableIntStateOf(-1) }
    LaunchedEffect(detalle != null, comentarios.size) {
        if (detalle == null) return@LaunchedEffect
        if (cantidadPrevia >= 0 && comentarios.size > cantidadPrevia) {
            listState.animateScrollToItem(maxOf(0, listState.layoutInfo.totalItemsCount - 1))
        }
        cantidadPrevia = comentarios.size
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = detalle?.let { "Ticket ${numeroTicket(it.ticket.id)}" } ?: "Ticket",
                onAtras = onAtras
            )
        },
        bottomBar = {
            // Sube con el teclado; la barra de navegación se oculta mientras escribes
            Column(Modifier.imePadding()) {
                if (puedeComentar) {
                    CajaComentario(texto, vm::cambiarTexto, vm::enviarComentario)
                }
                if (!tecladoVisible) barraInferior()
            }
        }
    ) { padding ->
        val d = detalle
        if (d == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { TarjetaResumen(d) }

                item {
                    StepperTicket(
                        EstadoTicket.desde(d.ticket.estado),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (estado == EstadoTicket.RESUELTO) {
                    item { AvisoResuelto() }
                }

                item {
                    Text(
                        "Conversación",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (comentarios.isEmpty()) {
                    item {
                        Text(
                            "Aún no hay comentarios",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(comentarios, key = { it.id }) { c ->
                    val propio = c.autorId == vm.usuarioId
                    BurbujaComentario(
                        texto = c.texto,
                        autor = if (propio) "Tú" else c.autorNombre,
                        fecha = c.fecha,
                        propio = propio,
                        rolAutor = Rol.desde(c.autorRol) ?: Rol.CLIENTE
                    )
                }

                if (estado == EstadoTicket.RESUELTO) {
                    item {
                        SeccionCalificar(
                            calificacion = calificacion,
                            onCalificar = vm::calificar,
                            comentarioFinal = comentarioFinal,
                            onComentarioFinal = vm::cambiarComentarioFinal,
                            mensajeError = mensajeError,
                            onCerrar = vm::confirmarYCerrar,
                            onReabrir = vm::reabrir
                        )
                    }
                }

                if (estado == EstadoTicket.CERRADO) {
                    item { TarjetaCerrado(d.ticket.calificacion ?: 0) }
                }

                if (estado != EstadoTicket.RESUELTO && mensajeError != null) {
                    item {
                        Text(
                            mensajeError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaResumen(d: TicketConDetalle) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(d.ticket.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                d.ticket.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EstadoChip(EstadoTicket.desde(d.ticket.estado))
                PrioridadChip(Prioridad.desde(d.ticket.prioridad))
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    iconoCategoria(d.categoria.icono),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    listOfNotNull(
                        d.categoria.nombre,
                        d.ticket.ubicacion.ifBlank { null },
                        tiempoTranscurrido(d.ticket.fecha)
                    ).joinToString(" · "),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AvisoResuelto() {
    Surface(
        color = ColorResuelto.copy(alpha = 0.12f),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = ColorResuelto)
            Spacer(Modifier.width(10.dp))
            Text(
                "Soporte marcó este ticket como resuelto. ¿El problema se solucionó?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SeccionCalificar(
    calificacion: Int,
    onCalificar: (Int) -> Unit,
    comentarioFinal: String,
    onComentarioFinal: (String) -> Unit,
    mensajeError: String?,
    onCerrar: () -> Unit,
    onReabrir: () -> Unit
) {
    Column {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Califica la atención", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(12.dp))
                Estrellas(valor = calificacion, tamano = 40.dp, onCambio = onCalificar)
                Spacer(Modifier.height(6.dp))
                Text(
                    if (calificacion == 0) "Toca una estrella" else "$calificacion de 5 estrellas",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        AyudaTextField(
            valor = comentarioFinal,
            onCambio = onComentarioFinal,
            etiqueta = "Comentario final (opcional)"
        )

        mensajeError?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(16.dp))
        AyudaButton("Confirmar y cerrar", onClick = onCerrar)
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = onReabrir,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.5.dp, ColorAlta),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Reabrir ticket", color = ColorAlta, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun TarjetaCerrado(calificacion: Int) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Ticket cerrado", style = MaterialTheme.typography.titleSmall)
                Text(
                    "Ya no se pueden agregar comentarios",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Estrellas(valor = calificacion, tamano = 18.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CajaComentario(
    texto: String,
    onCambio: (String) -> Unit,
    onEnviar: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = onCambio,
                placeholder = { Text("Escribe un comentario…") },
                shape = RoundedCornerShape(50),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(10.dp))
            FilledIconButton(
                onClick = onEnviar,
                enabled = texto.isNotBlank(),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.size(52.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar comentario")
            }
        }
    }
}