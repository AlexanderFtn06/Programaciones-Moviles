package com.tecsup.mibodega.ui.cliente.modelo
import com.tecsup.mibodega.R
/**
 * Datos de ejemplo (fake) para mostrar la UI sin base de datos.
 * Cuando conecten Room o una API, este archivo se reemplaza por
 * un Repository real, pero las pantallas no cambian porque ya
 * reciben una List<Producto> como parámetro.
 */
val listaCategorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks")
const val TELEFONO_DEMO = "906259697"
const val CONTRASENA_DEMO = "12345678"

val CLIENTE_DEMO = Cliente(
    nombre = "Alexander Faustino",
    telefono = TELEFONO_DEMO,
    direccion = "Av. Metropolitana - ceres medio",
    referencia = "Frente al parque",
    contraseña = CONTRASENA_DEMO
)
val listaProductosFake = listOf(
    Producto(
        id = 1,
        nombre = "Arroz Costeño",
        descripcion = "Arroz extra, grano largo, ideal para el día a día.",
        precio = 4.50,
        categoria = "Abarrotes",
        imagen = R.drawable.prod_arroz_costeno
    ),
    Producto(
        id = 2,
        nombre = "Aceite Primor",
        descripcion = "Aceite vegetal 1 L, alto en vitamina E.",
        precio = 8.90,
        categoria = "Abarrotes",
        imagen = R.drawable.prod_aceite_primor
    ),
    Producto(
        id = 3,
        nombre = "Leche Gloria",
        descripcion = "Leche evaporada entera 1 L.",
        precio = 5.20,
        categoria = "Abarrotes",
        imagen = R.drawable.prod_leche_gloria
    ),
    Producto(
        id = 4,
        nombre = "Galleta Oreo",
        descripcion = "Galletas de chocolate rellenas 126 g.",
        precio = 3.50,
        categoria = "Snacks",
        imagen = R.drawable.prod_galleta_oreo
    ),
    Producto(
        id = 5,
        nombre = "Coca-Cola Original",
        descripcion = "Bebida gaseosa sabor cola. Ideal para compartir en familia.",
        precio = 6.50,
        categoria = "Bebidas",
        imagen = R.drawable.prod_coca_cola
    ),
    Producto(
        id = 6,
        nombre = "Inca Kola 1.5 L",
        descripcion = "Gaseosa sabor a hierba luisa, la bebida de sabor nacional.",
        precio = 7.00,
        categoria = "Bebidas",
        imagen = R.drawable.prod_inca_kola
    ),
    Producto(
        id = 7,
        nombre = "Agua San Luis 625 ml",
        descripcion = "Agua de mesa sin gas, ideal para llevar.",
        precio = 1.50,
        categoria = "Bebidas",
        imagen = R.drawable.prod_agua_san_luis
    ),
    Producto(
        id = 8,
        nombre = "Néctar Frugos 1 L",
        descripcion = "Néctar de durazno listo para tomar.",
        precio = 5.80,
        categoria = "Bebidas",
        imagen = R.drawable.prod_frugos
    ),
    Producto(
        id = 9,
        nombre = "Azúcar Cartavio 1 kg",
        descripcion = "Azúcar rubia de caña, ideal para postres y bebidas.",
        precio = 4.30,
        categoria = "Abarrotes",
        imagen = R.drawable.prod_azucar_cartavio
    ),
    Producto(
        id = 10,
        nombre = "Lay's Clásicas 140 g",
        descripcion = "Papas fritas crocantes con sal, para compartir.",
        precio = 5.50,
        categoria = "Snacks",
        imagen = R.drawable.prod_lays
    ),
    Producto(
        id = 11,
        nombre = "Chocolate Sublime",
        descripcion = "Chocolate con leche y maní, barra individual.",
        precio = 1.50,
        categoria = "Snacks",
        imagen = R.drawable.prod_sublime
    ),
    Producto(
        id = 12,
        nombre = "Chizitos 85 g",
        descripcion = "Piqueo de maíz sabor a queso.",
        precio = 2.00,
        categoria = "Snacks",
        imagen = R.drawable.prod_chizitos
    )
)

