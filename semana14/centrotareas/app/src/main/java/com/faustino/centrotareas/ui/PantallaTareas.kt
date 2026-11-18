package com.faustino.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.faustino.centrotareas.model.PrimosUi
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.material3.Switch
import androidx.compose.material3.CircularProgressIndicator
import com.faustino.centrotareas.model.CargaUi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faustino.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    var toques by rememberSaveable { mutableIntStateOf(0) }
    val primos by vm.primos.collectAsState()
    val primosProgreso by vm.primosProgreso.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    var texto by rememberSaveable { mutableStateOf("500000") }
    var hastaConsultado by rememberSaveable { mutableIntStateOf(0) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ---- Tarjeta 1: Tarea con progreso (pasos 1 a 4)
            Tarjeta("Tarea con progreso") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { toques++ }) { Text("Tócame: $toques") }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { vm.tareaLarga() },
                            enabled = !corriendo
                        ) { Text("Iniciar") }
                        OutlinedButton(
                            onClick = { vm.cancelar() },
                            enabled = corriendo
                        ) { Text("Cancelar") }
                    }
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("${(progreso * 100).toInt()} %")
                    Text(mensaje)
                }
            }

            // ---- Tarjeta 2: Contar números primos (paso 5)
            Tarjeta("Contar números primos") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = texto,
                        onValueChange = { texto = it.filter { c -> c.isDigit() } },
                        label = { Text("Hasta el número") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                val n = texto.toIntOrNull()
                                if (n != null && n >= 2) {   // ignora texto no numérico o < 2
                                    hastaConsultado = n
                                    vm.contar(n)
                                }
                            },
                            enabled = primos !is PrimosUi.Calculando
                        ) { Text("Contar") }
                        OutlinedButton(
                            onClick = { vm.cancelarPrimos() },
                            enabled = primos is PrimosUi.Calculando
                        ) { Text("Cancelar") }
                    }
                    when (val p = primos) {
                        PrimosUi.Inicial -> {}
                        PrimosUi.Calculando -> {
                            LinearProgressIndicator(
                                progress = { primosProgreso },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text("${(primosProgreso * 100).toInt()} %")
                            Text("Calculando...")
                        }
                        is PrimosUi.Listo -> RecuadroVerde {
                            Text(
                                "Primos hasta $hastaConsultado: ${p.cantidad}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Text("Tiempo: ${p.milisegundos} ms", color = Color(0xFF1B5E20))
                        }
                        PrimosUi.Cancelado -> Text("Cancelado")
                    }
                }
            }

            // ---- Tarjeta 3: Dos cargas (pasos 6 y 7)
            Tarjeta("Dos cargas") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                    ) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = { vm.cambiarSimularError(it) }
                        )
                        Text("Simular error")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { vm.cargarUnaPorUna() },
                            enabled = carga !is CargaUi.Cargando
                        ) { Text("Una por una") }
                        Button(
                            onClick = { vm.cargarALaVez() },
                            enabled = carga !is CargaUi.Cargando
                        ) { Text("A la vez") }
                    }
                    when (val c = carga) {
                        CargaUi.Inicial -> {}
                        CargaUi.Cargando -> CircularProgressIndicator()
                        is CargaUi.Exito -> RecuadroVerde {
                            Text(
                                "Modo: ${c.modo}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Text("Perfil: ${c.perfil}", color = Color(0xFF1B5E20))
                            Text("Pedidos: ${c.pedidos}", color = Color(0xFF1B5E20))
                            Text("Tiempo: ${c.milisegundos} ms", color = Color(0xFF1B5E20))
                        }
                        is CargaUi.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.errorContainer,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    c.mensaje,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                            OutlinedButton(onClick = { vm.cargarALaVez() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                    Text(
                        "«Una por una» tarda ≈ 5000 ms",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Tiempo máximo: ${TareasViewModel.TIEMPO_MAXIMO_MS / 1000} s",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}

@Composable
private fun RecuadroVerde(contenido: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE3F1E0), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = contenido
    )
}