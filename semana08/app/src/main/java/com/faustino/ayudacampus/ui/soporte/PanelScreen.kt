package com.faustino.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.ui.components.graficos.DatoGrafico
import com.faustino.ayudacampus.ui.components.graficos.GraficoBarras
import com.faustino.ayudacampus.ui.components.graficos.GraficoDona
import com.faustino.ayudacampus.ui.theme.ColorAbierto
import com.faustino.ayudacampus.ui.theme.ColorAlta
import com.faustino.ayudacampus.ui.theme.ColorCerrado
import com.faustino.ayudacampus.ui.theme.ColorEnProceso
import com.faustino.ayudacampus.ui.theme.ColorResuelto
import com.faustino.ayudacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerAltas: () -> Unit,
    barraInferior: @Composable () -> Unit
) {
    val resumen by vm.resumen.collectAsState()
    val porCategoria by vm.porCategoria.collectAsState()
    val altas by vm.altasSinAtender.collectAsState()
    val tiempoPromedio by vm.tiempoPromedio.collectAsState()

    // Datos para la dona (colores del tema por estado)
    val datosEstado = listOf(
        DatoGrafico("Abierto", resumen.abiertos.toFloat(), ColorAbierto),
        DatoGrafico("En proceso", resumen.enProceso.toFloat(), ColorEnProceso),
        DatoGrafico("Resuelto", resumen.resueltos.toFloat(), ColorResuelto),
        DatoGrafico("Cerrado", resumen.cerrados.toFloat(), ColorCerrado)
    )

    // Datos para las barras: la categoría con más tickets se resalta
    val primario = MaterialTheme.colorScheme.primary
    val maximo = porCategoria.maxOfOrNull { it.cantidad } ?: 0
    val datosCategoria = porCategoria.map { c ->
        DatoGrafico(
            etiqueta = c.nombre.substringBefore(" /"),          // "Red / Wi-Fi" -> "Red"
            valor = c.cantidad.toFloat(),
            color = if (maximo > 0 && c.cantidad == maximo) primario else primario.copy(alpha = 0.35f)
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = barraInferior
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Encabezado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Panel de soporte",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "Mesa de ayuda TI · Hoy",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Contadores
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TarjetaContador("Abiertos", resumen.abiertos, ColorAbierto, Modifier.weight(1f))
                TarjetaContador("En proceso", resumen.enProceso, ColorEnProceso, Modifier.weight(1f))
                TarjetaContador("Resueltos", resumen.resueltos, ColorResuelto, Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            TarjetaBlanca {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Tiempo promedio de resolución",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            tiempoPromedio,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            TituloSeccion("Tickets por estado")
            TarjetaBlanca { GraficoDona(datosEstado) }

            TituloSeccion("Tickets por categoría")
            TarjetaBlanca { GraficoBarras(datosCategoria) }

            // Alerta: ALTA + ABIERTO con más de 1 hora
            if (altas > 0) {
                Spacer(Modifier.height(16.dp))
                AlertaAltas(cantidad = altas, onVer = onVerAltas)
            }

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
        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
    )
}

@Composable
private fun TarjetaContador(etiqueta: String, valor: Int, color: Color, modifier: Modifier) {
    Surface(
        color = color.copy(alpha = 0.14f),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$valor", style = MaterialTheme.typography.headlineSmall, color = color)
            Text(etiqueta, style = MaterialTheme.typography.labelSmall, color = color)
        }
    }
}

@Composable
private fun TarjetaBlanca(contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), content = contenido)
    }
}

@Composable
private fun AlertaAltas(cantidad: Int, onVer: () -> Unit) {
    Surface(
        color = ColorAlta.copy(alpha = 0.12f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Warning, contentDescription = null, tint = ColorAlta)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    if (cantidad == 1) "1 ticket de prioridad Alta"
                    else "$cantidad tickets de prioridad Alta",
                    style = MaterialTheme.typography.titleSmall,
                    color = ColorAlta
                )
                Text(
                    "sin atender hace más de 1 hora",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                onClick = onVer,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorAlta,
                    contentColor = Color.White
                )
            ) {
                Text("Ver")
            }
        }
    }
}