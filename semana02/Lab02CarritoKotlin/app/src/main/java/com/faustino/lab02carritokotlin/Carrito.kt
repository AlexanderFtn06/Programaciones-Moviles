package com.faustino.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    val nombreCliente = "Alexander  Faustino" // String (inferido)
    val carrito = mutableListOf<Producto>() // lista vacía de productos
    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop", 3500.0, 1))
    carrito.add(Producto("Mouse", 45.5, 2))
    carrito.add(Producto("Teclado", 120.0, 1))
    carrito.add(Producto("Monitor", 850.0, 1))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
}

