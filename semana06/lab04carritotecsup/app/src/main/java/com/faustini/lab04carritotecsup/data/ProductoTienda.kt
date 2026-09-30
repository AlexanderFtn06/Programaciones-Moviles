package com.faustini.lab04carritotecsup.data

data class ProductoTienda(
    val id: Int,
    val nombre: String,
    val precio: Double
)

val productosEjemplo = listOf(
    ProductoTienda(1, "Audífonos", 89.0),
    ProductoTienda(2, "Smartwatch", 199.0),
    ProductoTienda(3, "Funda celular", 25.0),
    ProductoTienda(4, "Cargador USB-C", 39.9),
    ProductoTienda(5, "Mouse inalámbrico", 59.0),
    ProductoTienda(6, "Teclado mecánico", 149.0)
)