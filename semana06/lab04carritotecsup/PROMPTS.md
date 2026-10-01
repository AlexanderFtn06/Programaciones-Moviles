# PROMPTS.md — Fase 2 (rama `mejora-ia-Lab06`)

Asistente de IA utilizado: Claude.

---
## Prompt 1 — Registrar favoritos desde el DropdownMenu

**Prompt:**

> Actua como desarrollador de aplicaciones moviles ya que tengo una app en Jetpack Compose con un DropdownMenu en cada tarjeta de producto (`ProductCard` y `TarjetaProducto`) que incluye la opción "Favoritos", y un NavigationDrawer en `AppNavegacion`. Quiero que la opción "Favoritos" registre el producto como favorito, que se pueda quitar al tocarla de nuevo, y que el estado viva en `AppNavegacion` para compartirlo entre pantallas.

**Respuesta de la IA:** propuso elevar el estado a `AppNavegacion` y pasarlo hacia las pantallas con una función para alternar. Las tarjetas reciben `esFavorito` y `onFavorito`, y la opción del menú cambia su texto e ícono según el estado.

**Correcciones y observaciones:** los favoritos se guardan por nombre en minúsculas, porque `Producto` no tiene `id`. Esa decisión dejó un caso sin cubrir: al eliminar un producto agregado, seguía contando como favorito. Se resolvió en el Prompt 3.



## Prompt 2 — Badge con contador en el drawer

**Prompt:**

> Sigue actuando como desarrollador de aplicaciones moviles y agrega un `Badge` con la cantidad de favoritos al ítem "Favoritos" del NavigationDrawer (`ModalDrawerSheet`). Debe ocultarse cuando la cantidad sea 0.

**Respuesta de la IA:** agregó el parámetro `cantidadFavoritos` a `AppDrawer` y usó el slot `badge` de `NavigationDrawerItem`, solo para la ruta de Favoritos y solo si hay al menos un favorito.

**Correcciones y observaciones:** según la versión de Material3, `Badge` puede requerir `@OptIn(ExperimentalMaterial3Api::class)` sobre `AppDrawer`; en ese caso se agrega la anotación.

---

## Prompt 3 — Pestaña Favoritos y contador correcto al eliminar

**Prompt:**

>Sigue actuando como desarrollador de aplicaciones moviles ya que quiero que la pestaña Favoritos muestre los productos marcados como favoritos y que desaparezcan de ahí al quitarlos. Además, si elimino un producto agregado en Mis pedidos y desaparece de Inicio, el contador de favoritos sigue contándolo. Corrige para que se quite de favoritos, sin afectar a los productos de ejemplo que se mantienen en Inicio.

**Respuesta de la IA:** creó `FavoritosScreen`, que recibe los productos favoritos filtrados del catálogo y los muestra con su menú ⋮ (desde ahí también se pueden quitar). Además limitó la limpieza al eliminar al caso en que el producto realmente sale del catálogo, usando el resultado booleano de `removeAll`.

**Correcciones y observaciones:** de esta forma, un producto de ejemplo (por ejemplo "Audífonos") conserva su favorito aunque se agregue y elimine un pedido con el mismo nombre.

---

## Prompt 4 — Ocultar el badge al visitar Favoritos

**Prompt:**

> Sigue actuando como desarrollador de aplicaciones moviles ya que cuando el usuario entre a la pestaña Favoritos, el badge del drawer debe desaparecer porque ya vio sus favoritos. Si luego marca otro producto como favorito, debe volver a aparecer contando solo los nuevos.

**Respuesta de la IA:** separó el total de favoritos de los favoritos "nuevos" (no vistos). El badge ahora cuenta solo los nuevos, y la lista se vacía al entrar a la ruta de Favoritos.


**Correcciones y observaciones:** el badge deja de reflejar el total de favoritos y pasa a indicar cuántos son nuevos desde la última visita; el total completo se ve en la pestaña Favoritos. Al quitar o eliminar un producto también se retira de los nuevos.

## Capturas

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/077f9703-8834-46ea-9809-97df03a0c4d7" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/ae53f4b3-fb10-4fef-83e4-93b30c79f135" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/4c4ed73b-f32c-4cf4-b3fc-babaebd7f029" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/0d0dc17e-ff14-42bf-a904-793fa081bf69" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/f957fe7d-c69b-4f84-9078-c1e61fe8fd4e" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/97948a14-54e7-4a85-8b66-4b2f4848dab6" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/f303cc76-3d74-492a-84ae-0687e137e3e1" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/bc317e96-ce88-4872-9e50-0dce547912ff" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/8022c70d-3487-4b61-b89b-ffa97b85f4c9" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/314bb786-96f8-4118-85fe-1621f5021d2a" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/ef9bc5dd-3aee-413a-8d57-55685c298658" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/ce1c669e-2cdf-43cd-b51e-5728b15b0ca4" />

