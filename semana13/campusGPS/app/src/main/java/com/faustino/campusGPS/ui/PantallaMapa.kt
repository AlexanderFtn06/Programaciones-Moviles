package com.faustino.campusGPS.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.faustino.campusGPS.model.UbicacionUi
import com.faustino.campusGPS.util.formatearDistancia
import com.faustino.campusGPS.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val categorias by vm.categorias.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val lugaresFiltrados by vm.lugaresFiltrados.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val cercano by vm.masCercano.collectAsState()
    val radioMetros by vm.radioMetros.collectAsState()
    val idsEnRadio by vm.idsEnRadio.collectAsState()
    val contexto = LocalContext.current

    fun tienePermisos(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    var permisoConcedido by remember { mutableStateOf(tienePermisos()) }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            permisoConcedido = true
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun alPulsar() {
        if (tienePermisos()) {
            permisoConcedido = true
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun abrirAjustes() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", contexto.packageName, null)
        }
        contexto.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (estado is UbicacionUi.Error) {
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { alPulsar() },
                    enabled = estado !is UbicacionUi.Buscando,
                    modifier = Modifier.padding(top = 12.dp).widthIn(min = 220.dp)
                ) {
                    Text("Obtener mi ubicación")
                }

                when (val e = estado) {
                    is UbicacionUi.Error -> MensajeError(
                        error = e,
                        onAbrirAjustes = { abrirAjustes() },
                        onReintentar = { alPulsar() },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    else -> {}
                }
            } else {
                MapaUbicacion(
                    estado = estado,
                    lugares = lugaresFiltrados,
                    seleccionado = seleccionado,
                    masCercanoId = cercano?.id,
                    permisoConcedido = permisoConcedido,
                    radioMetros = radioMetros,
                    modifier = Modifier.weight(1f).fillMaxWidth().heightIn(min = 160.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { alPulsar() },
                        enabled = estado !is UbicacionUi.Buscando,
                        modifier = Modifier.padding(top = 8.dp).widthIn(min = 220.dp)
                    ) {
                        Text("Obtener mi ubicación")
                    }

                    when (estado) {
                        UbicacionUi.Inicial -> {}
                        UbicacionUi.Buscando -> {}
                        is UbicacionUi.Lista -> {}
                        else -> {}
                    }

                    OutlinedButton(
                        onClick = { cercano?.let { vm.seleccionar(it.id) } },
                        enabled = cercano != null,
                        modifier = Modifier.padding(top = 4.dp)
                    ) { Text("Ir al más cercano") }

                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (cercano != null) {
                            val c = cercano!!
                            val metros = lugaresFiltrados.firstOrNull { it.lugar.id == c.id }?.metros
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Más cercano: ")
                                    }
                                    append(c.nombre)
                                    if (metros != null) append(" (${formatearDistancia(metros)})")
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }

                    val radios = listOf(100, 200, 500)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(radios) { r ->
                            FilterChip(
                                selected = radioMetros == r,
                                onClick = { vm.cambiarRadio(r) },
                                label = { Text("$r m") },
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = categoriaSeleccionada == null,
                                onClick = { vm.seleccionarCategoria(null) },
                                label = { Text("Todos") }
                            )
                        }
                        items(categorias) { cat ->
                            FilterChip(
                                selected = categoriaSeleccionada == cat,
                                onClick = { vm.seleccionarCategoria(cat) },
                                label = { Text(cat) }
                            )
                        }
                    }

                    ListaLugares(
                        lugares = lugaresFiltrados,
                        seleccionado = seleccionado,
                        onClick = { vm.seleccionar(it) },
                        cercanoId = cercano?.id,
                        idsEnRadio = idsEnRadio,
                        modifier = Modifier.height(184.dp).fillMaxWidth()
                    )
                }
            }
        }
    }
}
