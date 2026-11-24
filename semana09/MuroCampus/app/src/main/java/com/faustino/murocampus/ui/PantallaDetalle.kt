package com.faustino.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faustino.murocampus.viewmodel.PostsViewModel
import com.faustino.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val estadoBorrado by vm.borrado.collectAsStateWithLifecycle()
    val estadoEdicion by vm.edicion.collectAsStateWithLifecycle()

    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }
    var mostrarDialogoEditar by rememberSaveable { mutableStateOf(false) }
    var textoTitulo by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(estadoBorrado) {
        if (estadoBorrado is UiState.Exito) {
            vm.reiniciarBorrado()
            onVolver()
        }
    }

    LaunchedEffect(estadoEdicion) {
        if (estadoEdicion is UiState.Exito) {
            mostrarDialogoEditar = false
            vm.reiniciarEdicion()
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                if (estadoBorrado != UiState.Cargando) mostrarDialogo = false
            },
            title = { Text("Eliminar publicación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta publicación?") },
            confirmButton = {
                Button(
                    onClick = { vm.borrar(id) },
                    enabled = estadoBorrado != UiState.Cargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { mostrarDialogo = false },
                    enabled = estadoBorrado != UiState.Cargando
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (mostrarDialogoEditar) {
        AlertDialog(
            onDismissRequest = {
                if (estadoEdicion != UiState.Cargando) {
                    mostrarDialogoEditar = false
                    vm.reiniciarEdicion()
                }
            },
            title = { Text("Editar título") },
            text = {
                Column {
                    OutlinedTextField(
                        value = textoTitulo,
                        onValueChange = { textoTitulo = it },
                        label = { Text("Título") },
                        singleLine = true,
                        enabled = estadoEdicion != UiState.Cargando,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (estadoEdicion is UiState.Error) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = (estadoEdicion as UiState.Error).mensaje,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { vm.editarTitulo(id, textoTitulo.trim()) },
                    enabled = textoTitulo.trim().isNotBlank() && estadoEdicion != UiState.Cargando
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        mostrarDialogoEditar = false
                        vm.reiniciarEdicion()
                    },
                    enabled = estadoEdicion != UiState.Cargando
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    when (val e = estado) {
        UiState.Cargando -> Cargando(modifier)

        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.abrir(id) },
            modifier = modifier
        )

        is UiState.Exito -> {
            val detalle = e.datos
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // La publicación, dentro de su tarjeta
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFD5DCE6)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                text = detalle.post.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = detalle.post.body.replace("\n", " "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        textoTitulo = detalle.post.title
                                        vm.reiniciarEdicion()
                                        mostrarDialogoEditar = true
                                    },
                                    enabled = estadoBorrado != UiState.Cargando && estadoEdicion != UiState.Cargando,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Editar")
                                }
                                Button(
                                    onClick = { mostrarDialogo = true },
                                    enabled = estadoBorrado != UiState.Cargando && estadoEdicion != UiState.Cargando,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }

                if (estadoBorrado is UiState.Error) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = (estadoBorrado as UiState.Error).mensaje,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Comentarios (${detalle.comentarios.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                items(detalle.comentarios, key = { it.id }) { comentario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                            Text(
                                text = comentario.email,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = comentario.body.replace("\n", " "),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}