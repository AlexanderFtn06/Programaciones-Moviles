package com.faustino.ayudacampus.ui.soporte

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.AyudaTextField
import com.faustino.ayudacampus.ui.components.ICONOS_CATEGORIA
import com.faustino.ayudacampus.ui.components.iconoCategoria
import com.faustino.ayudacampus.ui.theme.ColorAlta
import com.faustino.ayudacampus.viewmodel.CategoriasViewModel

/** categoriaId = null -> Nueva categoría; con id -> Editar categoría. */
@Composable
fun FormularioCategoriaScreen(
    vm: CategoriasViewModel,
    categoriaId: Int?,
    onCerrar: () -> Unit
) {
    val categorias by vm.categorias.collectAsState()
    val conteos by vm.conteos.collectAsState()
    val mensajeError by vm.error.collectAsState()
    val listo by vm.listo.collectAsState()

    val esEdicion = categoriaId != null
    val existente = categoriaId?.let { id -> categorias.firstOrNull { it.id == id } }

    // Estado del formulario (lo que el usuario escribe y elige)
    var nombre by rememberSaveable { mutableStateOf("") }
    var icono by rememberSaveable { mutableStateOf("tag") }
    var activa by rememberSaveable { mutableStateOf(true) }
    var cargado by rememberSaveable { mutableStateOf(false) }

    // Al editar, llena el formulario una sola vez con los datos guardados
    LaunchedEffect(existente) {
        if (!cargado && existente != null) {
            nombre = existente.nombre
            icono = existente.icono
            activa = existente.activa
            cargado = true
        }
    }

    // Guardado o eliminado: volvemos a la lista
    LaunchedEffect(listo) {
        if (listo) onCerrar()
    }

    val tickets = existente?.let { conteos[it.nombre] } ?: 0

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = if (esEdicion) "Editar categoría" else "Nueva categoría",
                onAtras = onCerrar
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            AyudaTextField(
                valor = nombre,
                onCambio = { nombre = it; vm.limpiarError() },
                etiqueta = "Nombre de la categoría"
            )

            Titulo("Icono", Modifier.padding(top = 20.dp))

            // Cuadrícula de 3 x 2
            ICONOS_CATEGORIA.chunked(3).forEach { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    fila.forEach { ic ->
                        CeldaIcono(
                            icono = ic,
                            seleccionado = ic == icono,
                            onClick = { icono = ic },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(6.dp))

            // Activa / inactiva
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Categoría activa", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Los estudiantes podrán elegirla",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = activa, onCheckedChange = { activa = it })
                }
            }

            Titulo("Vista previa", Modifier.padding(top = 20.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(iconoCategoria(icono), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            nombre.ifBlank { "Nombre de la categoría" },
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            if (tickets == 1) "1 ticket" else "$tickets tickets",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            mensajeError?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(20.dp))

            AyudaButton(
                texto = "Guardar categoría",
                onClick = {
                    vm.guardar(
                        Categoria(
                            id = categoriaId ?: 0,
                            nombre = nombre,
                            icono = icono,
                            activa = activa
                        )
                    )
                }
            )

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = onCerrar,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Cancelar", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
            }

            // Solo al editar: eliminar (el ViewModel aplica la regla de los tickets)
            if (esEdicion && existente != null) {
                Spacer(Modifier.height(6.dp))
                TextButton(
                    onClick = { vm.eliminar(existente) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Eliminar categoría", color = ColorAlta, style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun CeldaIcono(
    icono: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(14.dp)
    Box(
        modifier
            .height(64.dp)
            .clip(forma)
            .background(
                if (seleccionado) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .border(
                width = if (seleccionado) 2.dp else 1.dp,
                color = if (seleccionado) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant,
                shape = forma
            )
            .selectable(selected = seleccionado, role = Role.RadioButton, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            iconoCategoria(icono),
            contentDescription = icono,
            tint = if (seleccionado) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}