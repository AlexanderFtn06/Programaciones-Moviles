# Lab04 — LazyColumn

**Autor:** Alexander Faustino Quispe

## Capturas

### Estado vacío
<img width="293" height="601" alt="image" src="https://github.com/user-attachments/assets/d668597d-fc53-4c0a-8037-894b06391820" />

### Carrito con productos

<img width="302" height="634" alt="image" src="https://github.com/user-attachments/assets/97152252-5e58-482b-9e45-6f1f490fdb25" />

## Respuestas conceptuales

**(a) ¿Por qué `mutableStateListOf` y no una `MutableList` normal?**

En Compose, la UI se redibuja (recompone) solo cuando el sistema detecta que un State cambió. Una MutableList normal (como mutableListOf()) es una lista de Kotlin cualquiera, cuando le haces .add() o .remove(), la lista sí cambia por dentro, pero Compose no tiene forma de enterarse de ese cambio, porquea no es un State observable. Resultado: agregas un producto, la lista técnicamente creció, pero la pantalla no se actualiza sola.

**(b) ¿Por qué la lista se declara con `val` y aun así se le pueden agregar elementos?**

val en Kotlin impide reasignar la variable — o sea, no puedes hacer productos = otraLista después de declararla así. Pero val no dice nada sobre si el contenido del objeto al que apunta esa variable puede cambiar.
productos es una referencia constante a una lista mutable. La variable siempre apunta al mismo objeto lista, pero ese objeto lista internamente permite agregar y quitar elementos (.add(), .remove()) sin necesidad de crear una lista nueva y reasignarla.

**(c) ¿Qué hace `weight(1f)` en la LazyColumn?**

Dentro de un Column, cada elemento hijo normalmente ocupa solo el espacio (alto) que necesita su contenido. Si el LazyColumn no tuviera weight(1f), tendría que decirle una altura fija o dejaría que el Column completo creciera sin límite, empujando el panel de totales fuera de la pantalla si hay muchos productos.
weight(1f) le dice al LazyColumn: "ocupa todo el espacio vertical que sobre, después de que los demás elementos (formulario, botón) ya tomaron el suyo".
