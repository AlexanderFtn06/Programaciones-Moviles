package com.faustino.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}
fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

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

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println()
    println("Subtotal: S/ $subtotal")
    println("IGV (18%): S/ $igv")
    println("TOTAL A PAGAR: S/ $total")

}


