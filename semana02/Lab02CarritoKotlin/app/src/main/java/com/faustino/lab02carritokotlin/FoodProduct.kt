package com.faustino.lab02carritokotlin

class FoodProduct(
    name: String,
    price: Double,
    quantity: Int,
    private val isPerishable: Boolean
) : Product(name, price, quantity) {

    override fun calculateAmount(): Double {
        val base = getPrice() * getQuantity()
        // Si es perecible, se aplica un pequeño descuento por rotacion rapida de inventario
        return if (isPerishable) base * 0.95 else base
    }

    override fun showInfo(): String {
        val tipo = if (isPerishable) "Perecible" else "No perecible"
        return super.showInfo() + " ($tipo)"
    }
}