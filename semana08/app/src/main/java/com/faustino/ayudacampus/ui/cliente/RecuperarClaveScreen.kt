package com.faustino.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.AyudaTextField
import com.faustino.ayudacampus.viewmodel.RecuperarClaveViewModel

@Composable
fun RecuperarClaveScreen(
    vm: RecuperarClaveViewModel,
    onAtras: () -> Unit,
    onListo: () -> Unit
) {
    val form by vm.form.collectAsState()
    val listo by vm.listo.collectAsState()

    LaunchedEffect(listo) {
        if (listo) onListo()
    }

    Scaffold(
        topBar = { AppTopBar("Recuperar contraseña", onAtras = onAtras) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                if (form.paso == 1) "Confirma tu identidad con el correo y el nombre con que te registraste"
                else "Hola, ${form.nombre}. Crea tu nueva contraseña",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            if (form.paso == 1) {
                AyudaTextField(
                    valor = form.correo,
                    onCambio = vm::cambiarCorreo,
                    etiqueta = "Correo",
                    error = form.errorCorreo,
                    icono = Icons.Outlined.Email,
                    teclado = KeyboardType.Email
                )
                Spacer(Modifier.height(12.dp))
                AyudaTextField(
                    valor = form.nombre,
                    onCambio = vm::cambiarNombre,
                    etiqueta = "Nombre completo",
                    error = form.errorNombre,
                    icono = Icons.Outlined.Person
                )
                Spacer(Modifier.height(24.dp))
                AyudaButton(
                    texto = if (form.enviando) "Verificando…" else "Continuar",
                    onClick = vm::verificar,
                    habilitado = !form.enviando
                )
            } else {
                AyudaTextField(
                    valor = form.nuevaClave,
                    onCambio = vm::cambiarNuevaClave,
                    etiqueta = "Nueva contraseña",
                    error = form.errorNuevaClave,
                    icono = Icons.Outlined.Lock,
                    esClave = true
                )
                Spacer(Modifier.height(12.dp))
                AyudaTextField(
                    valor = form.confirmarClave,
                    onCambio = vm::cambiarConfirmarClave,
                    etiqueta = "Confirmar contraseña",
                    error = form.errorConfirmarClave,
                    icono = Icons.Outlined.Lock,
                    esClave = true
                )
                Spacer(Modifier.height(24.dp))
                AyudaButton(
                    texto = if (form.enviando) "Guardando…" else "Guardar contraseña",
                    onClick = vm::guardar,
                    habilitado = !form.enviando
                )
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = vm::volverAlPaso1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "Cambiar correo",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
