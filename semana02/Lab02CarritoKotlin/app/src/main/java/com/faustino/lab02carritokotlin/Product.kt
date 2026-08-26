package com.faustino.lab02carritokotlin

abstract class Product(
    private val name: String,
    private val price: Double,
    private var quantity: Int
) {
    fun getName(): String = name
    fun getPrice(): Double = price
    fun getQuantity(): Int = quantity
    fun setQuantity(newQuantity: Int) {
        quantity = newQuantity
    }

    // Método abstracto: cada tipo de producto define cómo calcular su importe
    abstract fun calculateAmount(): Double

    // Método abierto para que las subclases lo sobreescriban (polimorfismo)
    open fun showInfo(): String {
        return String.format("%-20s x%d  S/ %8.2f", name, quantity, calculateAmount())
    }
}