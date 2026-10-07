package com.tecsup.mibodega.ui.cliente

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.CLIENTE_DEMO
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.PedidoConfirmado
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.COSTO_DELIVERY
import androidx.compose.ui.platform.LocalContext
import com.tecsup.mibodega.ui.cliente.modelo.Cliente
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.cliente.screens.terminos.TerminosScreen

/**
 * "Director de orquesta" de la app cliente:
 * - Tiene el NavHost con las rutas de cada pantalla.
 * - Tiene el estado del carrito (List<ItemCarrito>), que se reparte
 *   hacia abajo a Inicio, Detalle, Carrito y Entrega.
 * Ninguna Screen navega sola ni modifica el carrito directamente:
 * todas reciben funciones (lambdas) desde aquí (state hoisting).
 */


@Composable
fun ClienteApp() {
    val navController = rememberNavController()
    val contexto = LocalContext.current

    // El carrito vive aquí arriba, no en ninguna Screen.
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }

    // Datos del cliente registrado; se usan para rellenar Datos de entrega.
    var cliente by remember { mutableStateOf(Cliente()) }

    // Ultimo pedido confirmado y contador para numerar los pedidos
    var historial by remember { mutableStateOf<List<PedidoConfirmado>>(emptyList()) }
    var contadorPedidos by remember { mutableStateOf(1024) }

    // Ids de los productos marcados como favoritos desde el detalle.
    var favoritos by remember { mutableStateOf<Set<Int>>(emptySet()) }

    var cuentas by remember { mutableStateOf(listOf(CLIENTE_DEMO)) }

    var categoriaInicial by remember { mutableStateOf("Todos") }

    val navegarBarra: (String) -> Unit = { ruta ->
        if (ruta == Rutas.INICIO) categoriaInicial = "Todos"
        navController.navigate(ruta) {
            popUpTo(Rutas.INICIO)
            launchSingleTop = true
        }
    }


    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                onTerminos = { navController.navigate(Rutas.TERMINOS) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onCrearCuenta = { nombre, telefono, direccion, referencia, contrasena ->
                    val nueva = Cliente(nombre, telefono, direccion, referencia, contrasena)
                    cuentas = cuentas.filterNot { it.telefono == telefono } + nueva
                    Toast.makeText(
                        contexto,
                        "Cuenta creada. Inicia sesión para continuar",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.BIENVENIDA)
                    }
                }
            )
        }
        composable(Rutas.LOGIN) {
            LoginScreen(
                onVolver = { navController.popBackStack() },
                onIngresar = { telefono, contrasena ->
                    val cuenta = cuentas.find { it.telefono == telefono && it.contraseña == contrasena }
                    if (cuenta != null) {
                        cliente = cuenta
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    }
                    cuenta != null
                },
                onIrARegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.TERMINOS) {
            TerminosScreen(onVolver = { navController.popBackStack() })
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                },
                onNavegar = navegarBarra,
                categoriaInicial = categoriaInicial
            )
        }
        // Destino Categorías
        composable(Rutas.CATEGORIAS) {
            CategoriasScreen(
                onCategoriaClick = { categoria ->
                    categoriaInicial = categoria
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO)
                        launchSingleTop = true
                    }
                },
                onNavegar = navegarBarra
            )
        }

        // Destino Pedidos
        composable(Rutas.PEDIDOS) {
            PedidosScreen(
                pedidos = historial,
                onNavegar = navegarBarra
            )
        }

        // NUEVO: Destino "Perfil
        composable(Rutas.PERFIL) {
            PerfilScreen(
                cliente = cliente,
                onNavegar = navegarBarra
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            val producto = listaProductosFake.first { it.id == productoId }

            DetalleProductoScreen(
                producto = producto,
                esFavorito = producto.id in favoritos,
                onVolver = { navController.popBackStack() },
                onFavorito = {
                    val agregado = producto.id !in favoritos
                    favoritos = if (agregado) favoritos + producto.id else favoritos - producto.id
                    Toast.makeText(
                        contexto,
                        if (agregado) "${producto.nombre} agregado a favoritos"
                        else "${producto.nombre} quitado de favoritos",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                    carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                onVolver = { navController.popBackStack() },
                onIncrementar = { producto ->
                    carrito = carrito.map {
                        if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                    }
                },
                onDecrementar = { producto ->
                    carrito = carrito.mapNotNull {
                        when {
                            it.producto.id != producto.id -> it
                            it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                            else -> null // si llega a 0, se elimina de la lista
                        }
                    }
                },
                onEliminar = { producto ->
                    carrito = carrito.filterNot { it.producto.id == producto.id }
                },
                onContinuarPedido = {
                    if (carrito.isNotEmpty()) navController.navigate(Rutas.ENTREGA)
                }
            )
        }
        // Pantalla 6, Datos de entrega
        composable(Rutas.ENTREGA) {
            DatosEntregaScreen(
                onVolver = { navController.popBackStack() },
                nombreInicial = cliente.nombre,
                telefonoInicial = cliente.telefono,
                direccionInicial = cliente.direccion,
                referenciaInicial = cliente.referencia,
                onConfirmarPedido = { _, _, direccion, referencia, _ ->
                    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
                    val nuevoPedido = PedidoConfirmado(
                        numero = contadorPedidos,
                        total = subtotal + COSTO_DELIVERY,
                        direccion = direccion,
                        referencia = referencia
                    )
                    historial = listOf(nuevoPedido) + historial
                    contadorPedidos++
                    carrito = emptyList()

                    // popUpTo: saca Carrito y Datos de entrega del historial,
                    // así "atrás" desde la confirmación no regresa a ellos.
                    navController.navigate(Rutas.CONFIRMACION) {
                        popUpTo(Rutas.INICIO)
                    }
                }
            )
        }

        // Pantalla 7, Pedido confirmado
        composable(Rutas.CONFIRMACION) {
            historial.firstOrNull()?.let { p ->
                ConfirmacionScreen(
                    numeroPedido = p.numero,
                    total = p.total,
                    direccion = p.direccion,
                    referencia = p.referencia,
                    onVerEstado = {
                        Toast.makeText(
                            contexto,
                            "Tu pedido #${p.numero} está en preparación",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onVolverInicio = {
                        navController.popBackStack(Rutas.INICIO, inclusive = false)
                    }
                )
            }
        }
    }
}

/**
 * Si el producto ya está en el carrito, le suma la cantidad;
 * si no, lo agrega como un ItemCarrito nuevo.
 */
private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}