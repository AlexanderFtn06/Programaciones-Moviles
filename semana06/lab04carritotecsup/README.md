# Laboratorio 06 — TECSUP Store: DropdownMenu y NavigationDrawer

## Capturas

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/5829c90b-7422-41d7-a348-c63f7ed8cf6e" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/373f3555-04be-46a3-9f2f-52834f1ee74e" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/74f20f0b-ac34-4795-9462-9d8bc29a8aa8" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/d5671c21-6568-4ace-b480-0d676b3172fd" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/4d6b7760-564e-429a-be4e-90fa909d4751" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/70ab460e-1f2b-4562-8b8c-a4efb22df58e" />



## Preguntas de reflexión

### 1. ¿Por qué el DropdownMenu se declara dentro de un Box junto al ícono que lo activa, y no en cualquier parte de la pantalla?

Porque el `DropdownMenu` se posiciona respecto a su composable padre. Al declararlo dentro del mismo `Box` que el `IconButton`, el menú toma ese `Box` como ancla y se despliega justo debajo del ícono de 3 puntos. Si estuviera en otro lugar, por ejemplo al final de la pantalla, se abriría anclado a esa posición y no junto al botón que lo activó.

Además, el estado `expanded` vive dentro de cada tarjeta, así que cada producto controla su propio menú y abrir uno no afecta a los demás.

### 2. ¿Qué diferencia de alcance hay entre las opciones del DropdownMenu (afectan solo a un producto) y las del NavigationDrawer (afectan a toda la app)?

Las opciones del `DropdownMenu` están dentro de la tarjeta, que recibe un producto específico. Por eso "Favoritos", "Compartir" y "Reportar" actúan únicamente sobre ese producto y su efecto es local.

El `NavigationDrawer` se declara en `AppNavegacion`, envolviendo al `Scaffold` y al `NavHost`. Por eso está disponible desde cualquier pantalla y sus opciones cambian el destino de navegación de toda la app (Inicio, Mis pedidos, Favoritos, Perfil), además de reflejar en cuál pantalla estamos mediante el ítem resaltado.

### 3. ¿Cómo tuviste que estructurar tu código para que el contador de favoritos del drawer "se entere" de lo que pasa en el DropdownMenu de cada producto?

Tuve que subir el estado a un ancestro común del drawer y de las pantallas, que es AppNavegacion. Ahí viven la lista favoritos (con los nombres de los productos, porque Producto no tiene id) y la lista nuevosFavoritos, junto con la función onToggleFavorito, que agrega o quita un producto.

Las tarjetas dejaron de guardar el estado de favorito y ahora lo reciben como parámetros: esFavorito y onFavorito. La opción "Favoritos" del DropdownMenu solo avisa hacia arriba con onFavorito(). AppNavegacion actualiza la lista y Compose recompone lo que depende de ella. AppDrawer recibe la cantidad como parámetro y la muestra en el Badge, sin conocer el menú de ningún producto. El flujo es unidireccional: el evento sube desde el menú y el estado baja hacia el drawer y las pantallas.

### 4. ¿Qué tuviste que corregir del código que te generó la IA para la mejora del badge de favoritos?

1. El contador seguía contando productos eliminados. Los favoritos se guardan por nombre, así que al eliminar en Mis pedidos un producto que desaparecía de Inicio, el badge lo seguía contando. Lo corregí quitándolo de favoritos solo cuando realmente sale del catálogo, usando el resultado de removeAll. Así los productos de ejemplo, que siguen en Inicio, no pierden su favorito.
2. El badge no representaba lo que ya había visto el usuario. La primera versión mostraba siempre el total. Lo cambié para que cuente solo los favoritos nuevos desde la última visita: se reinicia al entrar a la pestaña Favoritos y reaparece al marcar otro producto.

## Observaciones

1. Las rutas de navegación estaban escritas como textos sueltos en varios lugares de AppNavegacion. Se centralizaron en una sealed class Screen para evitar errores de tipeo y facilitar agregar pantallas.

2. La lista de pedidos se perdía al cambiar de pestaña porque vivía dentro de MyOrdersScreen. Se movió a AppNavegacion, lo que además permitió que Inicio muestre los productos agregados y los quite al eliminarlos.

3. El contenido del drawer se extrajo a AppDrawer.kt para que AppNavegacion solo coordine la navegación, y los datos del usuario se centralizaron en Usuario.kt para usarlos en el encabezado y en Perfil.


## Conclusiones

1. El `DropdownMenu` y el `NavigationDrawer` resuelven problemas distintos: uno ofrece acciones sobre un elemento puntual y el otro organiza la navegación principal de toda la app. Separar el contenido del drawer, las rutas y las pantallas en archivos propios deja el código más ordenado y fácil de mantener.
2. En Compose, el estado debe vivir en el ancestro común más bajo que lo necesite. Elevarlo a `AppNavegacion` fue lo que permitió conservar los pedidos entre pestañas y compartir datos entre pantallas sin duplicarlos.
3. Trabajar con commits pequeños y descriptivos, uno por cada hito, facilitó probar cada cambio y volver atrás cuando una decisión de diseño no funcionaba. La comparación con la Fase 2 se desarrolla en el README de la rama `mejora-ia`.
