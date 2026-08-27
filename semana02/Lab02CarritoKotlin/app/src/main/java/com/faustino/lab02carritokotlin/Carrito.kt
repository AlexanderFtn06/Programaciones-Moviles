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

fun leerProducto(): Product {
    println()
    print("Nombre del producto: ")
    val nombre = readLine() ?: ""

    print("Precio: ")
    val precio = readLine()?.toDoubleOrNull() ?: 0.0

    print("Cantidad: ")
    val cantidad = readLine()?.toIntOrNull() ?: 1

    print("Tipo (1 = Electronico, 2 = Alimento): ")
    val tipo = readLine()?.toIntOrNull() ?: 1

    return if (tipo == 2) {
        print("Es perecible? (s/n): ")
        val esPerecible = readLine()?.trim()?.equals("s", ignoreCase = true) ?: false
        FoodProduct(nombre, precio, cantidad, esPerecible)
    } else {
        print("Meses de garantia: ")
        val garantia = readLine()?.toIntOrNull() ?: 0
        ElectronicProduct(nombre, precio, cantidad, garantia)
    }
}

fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    print("Nombre del cliente: ")
    val nombreCliente = readLine() ?: "Cliente"
    val carrito = mutableListOf<Product>()
    println("Cliente: $nombreCliente")
    println()

    print("Cuantos productos desea ingresar (minimo 1)? ")
    var cantidadProductos = readLine()?.toIntOrNull() ?: 1
    if (cantidadProductos < 1) {
        println("Se requiere minimo 1 producto, se usara 1.")
        cantidadProductos = 1
    }

    for (n in 1..cantidadProductos) {
        println("--- Producto $n ---")
        carrito.add(leerProducto())
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
    print("Nombre del producto a buscar: ")
    val nombreABuscar = readLine() ?: ""
    val productoBuscado = buscarProducto(carrito, nombreABuscar)

    if (productoBuscado != null) {
        println("Producto encontrado: ${productoBuscado.getName()} - S/ ${productoBuscado.getPrice()}")
    } else {
        println("Producto no encontrado")
    }

    println()
    print("Nombre del producto a eliminar: ")
    val nombreAEliminar = readLine() ?: ""
    val eliminado = carrito.removeIf { it.getName() == nombreAEliminar }
    if (eliminado) {
        println("Producto eliminado correctamente.")
    } else {
        println("Producto no encontrado, no se elimino nada.")
    }
}


