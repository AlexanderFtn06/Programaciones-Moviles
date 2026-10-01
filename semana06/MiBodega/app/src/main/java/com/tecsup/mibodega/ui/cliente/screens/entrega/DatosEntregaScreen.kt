package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

private val metodosPago = listOf("Efectivo al entregar", "Yape", "Plin")
/**
 * Pantalla 6: Datos de entrega y pago (mockup "Cliente").
 * Guarda su propio estado de formulario; al confirmar entrega los datos ya listos.
 */
@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit,
    onConfirmarPedido: (
        nombre: String,
        telefono: String,
        direccion: String,
        referencia: String,
        metodoPago: String
    ) -> Unit,
    nombreInicial: String = "",
    telefonoInicial: String = "",
    direccionInicial: String = "",
    referenciaInicial: String = ""
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var telefono by remember { mutableStateOf(telefonoInicial) }
    var direccion by remember { mutableStateOf(direccionInicial) }
    var referencia by remember { mutableStateOf(referenciaInicial) }
    var metodoPago by remember { mutableStateOf(metodosPago.first()) }

    val datosCompletos = nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoEntrega()

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Nombre",
            valor = nombre,
            onValorCambia = { nombre = it },
            placeholder = "Juan Pérez"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Dirección",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Av. Los Olivos 123"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Frente al parque"
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Método de pago",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        metodosPago.forEach { metodo ->
            FilaMetodoPago(

            )
        }

        Spacer(Modifier.height(24.dp))

        BotonPrimario(
            texto = "Confirmar pedido",
            habilitado = datosCompletos,
            onClick = { onConfirmarPedido(nombre, telefono, direccion, referencia, metodoPago) }
        )

        Spacer(Modifier.height(24.dp))
    }
}

