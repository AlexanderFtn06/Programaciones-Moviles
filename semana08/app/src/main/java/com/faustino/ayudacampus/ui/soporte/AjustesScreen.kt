package com.faustino.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faustino.ayudacampus.ui.components.AppTopBar
import com.faustino.ayudacampus.ui.components.AvisoDataStore
import com.faustino.ayudacampus.ui.components.BotonCerrarSesion
import com.faustino.ayudacampus.ui.components.FilaPreferencia
import com.faustino.ayudacampus.ui.components.GrupoPreferencias
import com.faustino.ayudacampus.ui.components.TarjetaCambiarPerfil
import com.faustino.ayudacampus.ui.components.TarjetaUsuario
import com.faustino.ayudacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(
    sesionVm: SesionViewModel,
    onAtras: () -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    barraInferior: @Composable () -> Unit
) {
    val usuario by sesionVm.usuarioActual.collectAsState()
    val oscuro by sesionVm.modoOscuro.collectAsState()
    val notificaciones by sesionVm.notificaciones.collectAsState()
    val recibirTickets by sesionVm.recibirTickets.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Ajustes", onAtras = onAtras) },
        bottomBar = barraInferior
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            TarjetaUsuario(
                nombre = usuario?.nombre ?: "",
                correo = usuario?.correo ?: "",
                etiqueta = "Perfil activo"
            )

            Spacer(Modifier.height(16.dp))
            TarjetaCambiarPerfil(destino = "Estudiante", onClick = onCambiarPerfil)

            Text(
                "Preferencias",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )
            GrupoPreferencias {
                FilaPreferencia(
                    titulo = "Modo oscuro",
                    descripcion = "Cambia el tema de la app",
                    activo = oscuro,
                    onCambio = sesionVm::cambiarModoOscuro
                )
                FilaPreferencia(
                    titulo = "Notificaciones",
                    descripcion = "Avisar tickets nuevos",
                    activo = notificaciones,
                    onCambio = sesionVm::cambiarNotificaciones,
                    mostrarDivisor = true
                )
                FilaPreferencia(
                    titulo = "Recibir tickets",
                    descripcion = "Abrir o cerrar la mesa de ayuda",
                    activo = recibirTickets,
                    onCambio = sesionVm::cambiarRecibirTickets,
                    mostrarDivisor = true
                )
            }

            Spacer(Modifier.height(12.dp))
            AvisoDataStore()

            Spacer(Modifier.height(20.dp))
            BotonCerrarSesion(onCerrarSesion)
            Spacer(Modifier.height(16.dp))
        }
    }
}