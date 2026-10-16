package com.faustino.ayudacampus.ui.soporte

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.ui.components.iconoCategoria
import com.faustino.ayudacampus.viewmodel.CategoriasViewModel

// La View solo observa el estado y avisa los eventos al ViewModel
@Composable
fun CategoriasScreen(vm: CategoriasViewModel, onEditar: (Int) -> Unit) {
    val categorias by vm.categorias.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categorias, key = { it.id }) { c ->
            CategoriaItem(c, onToggle = { vm.alternar(c) }, onEditar = { onEditar(c.id) })
        }
    }
}

@Composable
private fun CategoriaItem(c: Categoria, onToggle: () -> Unit, onEditar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    iconoCategoria(c.icono),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(c.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    if (c.activa) "Activa" else "Inactiva",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Outlined.Edit, contentDescription = "Editar ${c.nombre}")
            }
            Switch(checked = c.activa, onCheckedChange = { onToggle() })
        }
    }
}