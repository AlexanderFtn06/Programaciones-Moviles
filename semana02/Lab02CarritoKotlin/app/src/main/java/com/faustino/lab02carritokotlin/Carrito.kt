package com.faustino.lab02carritokotlin

fun calcularSubtotal(productos: List<Product>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.calculateAmount()
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}
fun mostrarDetalle(productos: List<Product>) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in productos) {
        println(String.format("%d. %-20s x%d  S/ %8.2f",
            i, p.getName(), p.getQuantity(), p.calculateAmount()))
        i++
    }
    println("-----------------------------------------")
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}
fun buscarProducto(productos: List<Product>, nombre: String): Product? {
    for (p in productos) {
        if (p.getName() == nombre) {
            return p
        }
    }
    return null
}

fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    val nombreCliente = "Alexander  Faustino" // String (inferido)
    val carrito = mutableListOf<Product>() // lista vacía de productos
    println("Cliente: $nombreCliente")
    println()

    carrito.add(ElectronicProduct("Laptop", 3500.0, 1,12))
    carrito.add(ElectronicProduct("Mouse", 45.5, 2,6))
    carrito.add(ElectronicProduct("Teclado", 120.0, 1,6))
    carrito.add(ElectronicProduct("Monitor", 850.0, 1,12))
    carrito.add(FoodProduct("Snacks variados", 25.0, 3, true))

    for (producto in carrito) {
        println("Producto agregado: ${producto.getName()}")
    }
    println()
    mostrarDetalle(carrito)
    println("Cantidad de productos: ${carrito.size}")

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println()
    println(String.format("%-20s S/ %8.2f", "Subtotal:", subtotal))
    println(String.format("%-20s S/ %8.2f", "IGV (18%):", igv))
    println(String.format("%-20s S/ %8.2f", "TOTAL A PAGAR:", total))

    val masCaro = carrito.maxByOrNull { it.getPrice() }
    println("Producto mas caro: ${masCaro?.getName()} - S/ ${masCaro?.getPrice()}")

    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento

    if (descuento > 0) {
        println("Descuento aplicado: S/ ${String.format("%.2f", descuento)}")
    } else {
        println("Sin descuento aplicado")
    }
    println(String.format("%-20s S/ %8.2f", "TOTAL CON DESCUENTO:", totalConDescuento))

    println()
    println("Buscando el producto 'Mouse'...")
    val productoBuscado = buscarProducto(carrito, "Mouse")

    if (productoBuscado != null) {
        println("Producto encontrado: ${productoBuscado.getName()} - S/ ${productoBuscado.getPrice()}")
    } else {
        println("Producto no encontrado")
    }
    println()
    println("Eliminando el producto 'Teclado'...")
    carrito.removeIf { it.getName() == "Teclado" }

    // 3. Volver a mostrar el detalle actualizado
    println()
    mostrarDetalle(carrito)
    val nuevoSubtotal = calcularSubtotal(carrito)
    val nuevoIgv = calcularIGV(nuevoSubtotal)
    val nuevoTotal = calcularTotal(nuevoSubtotal, nuevoIgv)

    println(String.format("%-20s S/ %8.2f", "Subtotal:", nuevoSubtotal))
    println(String.format("%-20s S/ %8.2f", "IGV (18%):", nuevoIgv))
    println(String.format("%-20s S/ %8.2f", "TOTAL A PAGAR:", nuevoTotal))
}


