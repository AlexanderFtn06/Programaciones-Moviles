package com.faustino.campusGPS.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.faustino.campusGPS.model.LugarConDistancia
import com.faustino.campusGPS.model.UbicacionUi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    permisoConcedido: Boolean,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado.posicion.latitud, estado.posicion.longitud),
                    16f
                )
            )
        }
    }

    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugar != null) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), 17f)
            )
        }
    }

    val properties = remember(permisoConcedido) {
        MapProperties(isMyLocationEnabled = permisoConcedido)
    }
    val uiSettings = remember(permisoConcedido) {
        MapUiSettings(myLocationButtonEnabled = permisoConcedido)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = properties,
        uiSettings = uiSettings
    ) {
        if (estado is UbicacionUi.Lista) {
            val miMarcador = rememberUpdatedMarkerState(
                position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            )
            Marker(state = miMarcador, title = "Estás aquí")
            LaunchedEffect(miMarcador) { miMarcador.showInfoWindow() }
        }

        lugares.forEach { item ->
            key(item.lugar.id) {
                val marcador = rememberUpdatedMarkerState(
                    position = LatLng(item.lugar.latitud, item.lugar.longitud)
                )
                Marker(
                    state = marcador,
                    title = item.lugar.nombre,
                    snippet = item.lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (item.lugar.id == masCercanoId) BitmapDescriptorFactory.HUE_GREEN
                        else BitmapDescriptorFactory.HUE_AZURE
                    )
                )
                LaunchedEffect(seleccionado) {
                    if (item.lugar.id == seleccionado) marcador.showInfoWindow()
                }
            }
        }
    }
}