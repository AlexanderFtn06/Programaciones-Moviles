package com.faustino.lab02carritokotlin

class FoodProduct(
    name: String,
    price: Double,
    quantity: Int,
    private val isPerishable: Boolean
) : Product(name, price, quantity) {

    override fun calculateAmount(): Double {
        return getPrice() * getQuantity()
    }

    override fun showInfo(): String {
        val tipo = if (isPerishable) "Perecible" else "No perecible"
        return super.showInfo() + " ($tipo)"
    }
}