package com.faustino.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faustino.murocampus.viewmodel.PostsViewModel
import com.faustino.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val estado by vm.creacion.collectAsStateWithLifecycle()
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }

    // Éxito: se limpia el formulario. Error: se conserva lo escrito.
    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { vm.publicar(titulo.trim(), contenido.trim()) },
            enabled = titulo.isNotBlank() &&
                    contenido.isNotBlank() &&
                    estado != UiState.Cargando
        ) {
            Text("Publicar")
        }

        when (val e = estado) {
            null -> Unit

            UiState.Cargando -> Text("Publicando...")

            is UiState.Exito -> CajaMensaje(
                texto = "Publicado con id ${e.datos.id}",
                fondo = Color(0xFFE6F4EA),
                borde = Color(0xFF2E9E4F),
                colorTexto = Color(0xFF1B5E20)
            )

            is UiState.Error -> CajaMensaje(
                texto = e.mensaje,
                fondo = MaterialTheme.colorScheme.errorContainer,
                borde = MaterialTheme.colorScheme.error,
                colorTexto = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

// Recuadro con fondo de color y borde punteado
@Composable
private fun CajaMensaje(
    texto: String,
    fondo: Color,
    borde: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(fondo, RoundedCornerShape(10.dp))
            .drawBehind {
                val grosor = 2.dp.toPx()
                drawRoundRect(
                    color = borde,
                    topLeft = Offset(grosor / 2, grosor / 2),
                    size = Size(size.width - grosor, size.height - grosor),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                    style = Stroke(
                        width = grosor,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
                    )
                )
            }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = colorTexto,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}