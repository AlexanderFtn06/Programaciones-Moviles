package com.faustino.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.ui.components.AyudaButton
import com.faustino.ayudacampus.ui.components.AyudaTextField
import com.faustino.ayudacampus.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    vm: RegistroViewModel,
    onRegistrado: (Usuario) -> Unit,
    onIrLogin: () -> Unit
) {
    val form by vm.form.collectAsState()
    val registrado by vm.registrado.collectAsState()

    LaunchedEffect(registrado) {
        registrado?.let(onRegistrado)
    }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Regístrate como estudiante",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            AyudaTextField(
                valor = form.nombre,
                onCambio = vm::cambiarNombre,
                etiqueta = "Nombre completo",
                error = form.errorNombre,
                icono = Icons.Outlined.Person
            )
            Spacer(Modifier.height(12.dp))
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
                valor = form.clave,
                onCambio = vm::cambiarClave,
                etiqueta = "Contraseña",
                error = form.errorClave,
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
                texto = if (form.enviando) "Creando…" else "Crear cuenta",
                onClick = vm::registrar,
                habilitado = !form.enviando
            )

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "¿Ya tienes cuenta?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onIrLogin) {
                    Text(
                        "Inicia sesión",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
