# Lab02 - Carrito de Compras (Kotlin)

Alexander Faustino

Programa de consola que simula un carrito de compras: agrega productos, calcula subtotal, IGV y total, aplica descuentos por monto, busca y elimina productos.

Este trabajo se desarrolló en dos ramas:

- *Rama-sin-IA*: versión funcional del carrito, usando funciones y una data class simple.
- *Rama-con-IA*: versión orientada a objetos, aplicando los 4 pilares de la POO (Abstracción, Encapsulamiento, Herencia y Polimorfismo).

## ¿Por qué nombre y precio son val pero cantidad es var? ¿Qué
pasaría si intentas cambiar el precio después de crear el producto?

Nombre y precio son val porque son datos que no deberian cambiar una vez creado el producto. Cantidad es var porque si puede variar (el cliente agrega o quita unidades). Si intento cambiar precio 
despues de crear el producto, el compilador marca error porque val es de solo lectura y no se puede reasignar.

## Rama-sin-IA: Version funcional

Carrito de compras usando una data class Producto y funciones (calcularSubtotal, calcularIGV, calcularTotal, mostrarDetalle, calcularDescuento, buscarProducto).

### Resultado de ejecucion (Rama-sin-IA)
<img width="416" height="810" alt="image" src="https://github.com/user-attachments/assets/a1833df1-a8ab-480f-920d-7669497a1e09" />

## Rama-con-IA: Version orientada a objeto
Promt:
Ahora actúa como desarrollador/programador y ayúdame hacer este código orientado a objetos utilizando los 4 principios Abstracción , encapsulamiento, herencia y polimorfismo, pero que sea parte por parte para hacer commits y subirlo a GitHub
Se aplicaron los 4 pilares de la POO:

### Resultado de ejecucion (Rama-con-IA)
<img width="479" height="844" alt="image" src="https://github.com/user-attachments/assets/6d798641-ea00-464f-bfc3-d0b998552104" />
