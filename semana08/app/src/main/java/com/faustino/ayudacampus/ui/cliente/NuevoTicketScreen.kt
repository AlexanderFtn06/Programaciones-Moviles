package com.faustino.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.outlined.Info
import com.faustino.ayudacampus.ui.theme.ColorAlta
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.AyudaTextField
import com.faustino.ayudacampus.ui.components.iconoCategoria
import com.faustino.ayudacampus.ui.theme.colorPrioridad
import com.faustino.ayudacampus.viewmodel.NuevoTicketViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NuevoTicketScreen(
    vm: NuevoTicketViewModel,
    onAtras: () -> Unit,
    onEnviado: () -> Unit,
    barraInferior: @Composable () -> Unit
) {
    val form by vm.form.collectAsState()
    val categorias by vm.categorias.collectAsState()
    val enviado by vm.enviado.collectAsState()
    val recibir by vm.recibirTickets.collectAsState()

    // Cuando el ViewModel confirma que se guardó, volvemos a Mis tickets
    LaunchedEffect(enviado) {
        if (enviado) onEnviado()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Nuevo ticket", onAtras = onAtras) },
        bottomBar = barraInferior
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            if (!recibir) {
                AvisoMesaCerrada()
                Spacer(Modifier.height(8.dp))
            }
            // ---------- Categoría (solo activas) ----------
            TituloSeccion("Categoría")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { c ->
                    FilterChip(
                        selected = form.categoriaId == c.id,
                        onClick = { vm.elegirCategoria(c.id) },
                        label = { Text(c.nombre) },
                        leadingIcon = {
                            Icon(
                                iconoCategoria(c.icono),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
            // Error junto al campo (no en un Toast)
            form.errorCategoria?.let { TextoError(it) }

            Spacer(Modifier.height(16.dp))

            // ---------- Título y ubicación ----------
            AyudaTextField(
                valor = form.titulo,
                onCambio = vm::cambiarTitulo,
                etiqueta = "Título",
                error = form.errorTitulo
            )
            Spacer(Modifier.height(12.dp))
            AyudaTextField(
                valor = form.ubicacion,
                onCambio = vm::cambiarUbicacion,
                etiqueta = "Ubicación",
                icono = Icons.Outlined.LocationOn
            )

            Spacer(Modifier.height(8.dp))

            // ---------- Prioridad (Media por defecto) ----------
            TituloSeccion("Prioridad")
            SelectorPrioridad(seleccionada = form.prioridad, onElegir = vm::cambiarPrioridad)

            Spacer(Modifier.height(16.dp))

            // ---------- Descripción ----------
            AyudaTextField(
                valor = form.descripcion,
                onCambio = vm::cambiarDescripcion,
                etiqueta = "Descripción",
                error = form.errorDescripcion,
                lineas = 4
            )

            Spacer(Modifier.height(12.dp))

            // ---------- Tiempo estimado según la prioridad ----------
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Tiempo de respuesta estimado: ${vm.tiempoRespuesta(form.prioridad)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            AyudaButton(
                texto = if (form.enviando) "Enviando…" else "Enviar ticket",
                onClick = vm::enviar,
                habilitado = !form.enviando && recibir
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    )
}

@Composable
private fun TextoError(texto: String) {
    Text(
        texto,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(top = 6.dp, start = 4.dp)
    )
}

/** Baja / Media / Alta: el elegido se rellena con el color de su prioridad. */
@Composable
private fun SelectorPrioridad(seleccionada: Prioridad, onElegir: (Prioridad) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Prioridad.entries.forEach { p ->
            val activo = p == seleccionada
            val color = colorPrioridad(p)
            val forma = RoundedCornerShape(14.dp)
            Box(
                Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(forma)
                    .background(if (activo) color else MaterialTheme.colorScheme.surface)
                    .border(1.dp, if (activo) color else MaterialTheme.colorScheme.outlineVariant, forma)
                    .selectable(
                        selected = activo,
                        onClick = { onElegir(p) },
                        role = Role.RadioButton
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    p.etiqueta,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (activo) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
@Composable
private fun AvisoMesaCerrada() {
    Surface(
        color = ColorAlta.copy(alpha = 0.12f),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Info, contentDescription = null, tint = ColorAlta)
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Mesa de ayuda cerrada", style = MaterialTheme.typography.titleSmall, color = ColorAlta)
                Text(
                    "Soporte TI no está recibiendo tickets por ahora. Intenta más tarde.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}