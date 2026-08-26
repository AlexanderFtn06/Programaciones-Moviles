package com.faustino.lab02carritokotlin

class ElectronicProduct(
    name: String,
    price: Double,
    quantity: Int,
    private val warrantyMonths: Int
) : Product(name, price, quantity) {

    override fun calculateAmount(): Double {
        return getPrice() * getQuantity()
    }

    override fun showInfo(): String {
        return super.showInfo() + " (Garantia: $warrantyMonths meses)"
    }
}