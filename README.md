# Programaciones-Moviles
¿Por qué nombre y precio son val pero cantidad es var? ¿Qué pasaría si intentas cambiar el precio después de crear el producto?

Nombre y precio son val porque son datos que no deberían cambiar una vez creado el producto. Cantidad es var porque sí puede variar (el cliente agrega o quita unidades). Si intento cambiar precio después de crear el producto, el compilador marca error de compilación porque val es de solo lectura y no se puede reasignar.
