package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.CLIENTE_DEMO
import com.tecsup.mibodega.ui.cliente.modelo.CONTRASENA_DEMO
import com.tecsup.mibodega.ui.cliente.modelo.Cliente
import com.tecsup.mibodega.ui.cliente.modelo.TELEFONO_DEMO
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega


/**
 * Pantalla de Iniciar sesión.
 * Guarda su propio estado de formulario; al ingresar entrega el teléfono ya listo.
 */


@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onIngresar: (telefono: String, contrasena: String) -> Boolean,
    onIrARegistro: () -> Unit
) {
    var telefono by remember { mutableStateOf("") }
    val telefonoValido = telefono.length == 9
    var contrasena by remember { mutableStateOf("") }
    var hayError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoLogin(onVolver = onVolver)

        Spacer(Modifier.height(32.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { texto ->
                telefono = texto.filter { it.isDigit() }.take(9)
                hayError = false
            },
            placeholder = "987654321",
            teclado = KeyboardType.Phone
        )
        Text(
            text = "Ingresa los 9 dígitos de tu número",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Contraseña",
            valor = contrasena,
            onValorCambia = {
                contrasena = it
                hayError = false
            },
            placeholder = "••••••••",
            teclado = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        if (hayError) {
            Text(
                text = "Teléfono o contraseña incorrectos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Ingresar",
            habilitado = telefonoValido && contrasena.isNotEmpty(),
            onClick = { hayError = !onIngresar(telefono, contrasena) }
        )

        Spacer(Modifier.height(12.dp))

        TextButton(
            onClick = onIrARegistro,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = VerdeBodega,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun EncabezadoLogin(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

