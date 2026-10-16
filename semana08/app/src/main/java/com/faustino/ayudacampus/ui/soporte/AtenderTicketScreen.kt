package com.faustino.ayudacampus.ui.soporte

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faustino.ayudacampus.data.local.relation.TicketConDetalle
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.data.model.Rol
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.BurbujaComentario
import com.faustino.ayudacampus.ui.components.Estrellas
import com.faustino.ayudacampus.ui.components.numeroTicket
import com.faustino.ayudacampus.ui.theme.colorEstado
import com.faustino.ayudacampus.ui.theme.colorPrioridad
import com.faustino.ayudacampus.viewmodel.AtenderViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AtenderTicketScreen(
    vm: AtenderViewModel,
    onAtras: () -> Unit,
    barraInferior: @Composable () -> Unit
) {
    val detalle by vm.ticket.collectAsState()
    val comentarios by vm.comentarios.collectAsState()
    val estadoSel by vm.estadoSeleccionado.collectAsState()
    val prioridadSel by vm.prioridadSeleccionada.collectAsState()
    val hayCambios by vm.hayCambios.collectAsState()
    val texto by vm.texto.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    val cerrado = detalle?.ticket?.estado == EstadoTicket.CERRADO.name
    val tecladoVisible = WindowInsets.isImeVisible
    val snackbar = remember { SnackbarHostState() }

    // Mensajes cortos: "Cambios guardados", "El ticket está cerrado"
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            AppTopBar(
                titulo = detalle?.let { "Atender ticket ${numeroTicket(it.ticket.id)}" } ?: "Atender ticket",
                onAtras = onAtras
            )
        },
        bottomBar = {
            Column(Modifier.imePadding()) {
                if (detalle != null && !cerrado) {
                    CajaRespuesta(texto, vm::cambiarTexto, vm::responder)
                    if (!tecladoVisible) {
                        AyudaButton(
                            texto = "Guardar cambios",
                            onClick = vm::guardarCambios,
                            habilitado = hayCambios,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
                        )
                    }
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { TarjetaTicket(d) }

                // ---------- Estado: Soporte solo elige En proceso o Resuelto ----------
                item {
                    Column {
                        Titulo("Estado")
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            EstadoTicket.entries.forEach { e ->
                                Pastilla(
                                    texto = e.etiqueta,
                                    seleccionada = estadoSel == e,
                                    color = colorEstado(e),
                                    habilitada = !cerrado &&
                                            (e == EstadoTicket.EN_PROCESO || e == EstadoTicket.RESUELTO),
                                    onClick = { vm.elegirEstado(e) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // ---------- Prioridad ----------
                item {
                    Column {
                        Titulo("Prioridad")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Prioridad.entries.forEach { p ->
                                Pastilla(
                                    texto = p.etiqueta,
                                    seleccionada = prioridadSel == p,
                                    color = colorPrioridad(p),
                                    habilitada = !cerrado,
                                    onClick = { vm.elegirPrioridad(p) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                item { Titulo("Conversación") }

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
                        autor = if (propio) "Tú" else nombreCorto(c.autorNombre),
                        fecha = c.fecha,
                        propio = propio,
                        rolAutor = Rol.desde(c.autorRol) ?: Rol.CLIENTE
                    )
                }

                if (cerrado) {
                    item { AvisoCerrado(d.ticket.calificacion ?: 0) }
                }
            }
        }
    }
}

@Composable
private fun TarjetaTicket(d: TicketConDetalle) {
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
                listOfNotNull(
                    d.cliente.nombre,
                    d.ticket.ubicacion.ifBlank { null },
                    d.categoria.nombre
                ).joinToString(" · "),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(d.ticket.descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun Titulo(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

/** Pastilla seleccionable que se rellena con el color de su estado o prioridad. */
@Composable
private fun Pastilla(
    texto: String,
    seleccionada: Boolean,
    color: Color,
    habilitada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(50)
    Box(
        modifier
            .height(40.dp)
            .alpha(if (habilitada || seleccionada) 1f else 0.45f)
            .clip(forma)
            .background(if (seleccionada) color else MaterialTheme.colorScheme.surface)
            .border(1.dp, if (seleccionada) color else MaterialTheme.colorScheme.outlineVariant, forma)
            .selectable(
                selected = seleccionada,
                enabled = habilitada,
                role = Role.RadioButton,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
            color = if (seleccionada) Color.White else MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}

@Composable
private fun AvisoCerrado(calificacion: Int) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Cerrado por el estudiante", style = MaterialTheme.typography.titleSmall)
                Text(
                    "Ya no se puede responder ni cambiar",
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
private fun CajaRespuesta(
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
                placeholder = { Text("Responder al cliente…") },
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
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar respuesta")
            }
        }
    }
}

/** "Juan Pérez" -> "Juan P." */
private fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size >= 2) "${partes[0]} ${partes[1].first()}." else nombre
}