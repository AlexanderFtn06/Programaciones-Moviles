# Mi Bodega — App Cliente

Tarea Laboratorio 6 · Programación en Móviles 

## Capturas

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/b24a6771-cf2a-4eba-ba8e-1e01b6c50642" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/62f6d5b9-0690-4a1e-aecf-a076ba47b1a2" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/0302eb00-7e53-439c-a457-c706955506e2" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/065cf10f-9569-4d74-b488-746cd075e83f" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/4a8a4354-2fe8-4ebc-821d-6c807a8d7129" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/67ddcd3a-fafd-416c-bee2-8ca14a28a822" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/99dc76ac-4d75-4591-b5a6-bff0116ab4dc" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/06b91415-adc6-4c2d-a8b1-065dfecd5f01" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/e00eee85-4492-42e1-855d-7fedaf9286e5" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/a85d6b80-6da5-40ca-8cf6-8ed94c5453e8" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/61894939-c9c6-48d7-8eb3-f530c8ea9377" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/2b9d7107-9ef6-4c17-bcdd-081408af4aef" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/f128baeb-9a49-40f9-85b9-30358b36d95f" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/6a8e09c2-43db-41a4-859a-0aef5380f7ed" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/470fc29e-fb87-4132-94e8-3183152db961" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/88e9047d-cdab-4cfc-a357-ff1934dd95db" />



## Preguntas de reflexión

### 1. ¿Por qué Producto.kt y MainActivity.kt se entregaron completos, y las pantallas no? ¿Qué tienen en común los archivos que sí se dejaron como esqueleto?

Porque, como indica el enunciado, no son el objetivo de aprendizaje. `Producto.kt` es solo un modelo de datos y `MainActivity.kt` solo arranca la app y carga el tema; ninguno enseña los temas de la semana.

Los archivos que se dejaron como esqueleto tienen en común que son los que **aplican esos temas**: navegación con rutas y parámetros, `NavigationBar`, listas perezosas, estado y formularios. Son pantallas y archivos de navegación, donde hay que tomar decisiones de diseño. En mi caso, lo que realmente quedaba por hacer eran las pantallas de Datos de entrega y Confirmación (que estaban vacías), los `TODO` de `ClienteApp.kt` y del detalle, y hacer que la barra inferior navegara.

### 2. ¿Cómo lograste que el filtro de categoría (LazyRow) y el cálculo del carrito reaccionen automáticamente sin que tú "actualices" nada a mano?

Con estado de Compose y valores derivados.

- **Filtro:** en `InicioScreen`, `categoriaSeleccionada` y `textoBusqueda` son estados (`remember { mutableStateOf(...) }`), y `productosFiltrados` se calcula directamente a partir de ellos. Al tocar un chip cambia el estado, Compose recompone la pantalla y la lista se vuelve a filtrar. No hay ninguna función de "actualizar".
- **Carrito:** la lista vive en `ClienteApp` como estado, y cada cambio crea una lista nueva (`map`, `filterNot`, `+`) que se reasigna, lo que dispara la recomposición. El subtotal y el total no se guardan: `CarritoScreen` los calcula con `carrito.sumOf { ... }` cada vez que se dibuja. Por eso no existe un botón "recalcular" y el contador del ícono del carrito en Inicio también se actualiza solo.

### 3. ¿Qué diferencia notaste entre navigate() normal (Inicio→Detalle) y el que usa popUpTo (Datos de entrega→Confirmación)?

Con `navigate()` normal, la nueva pantalla se apila encima de las anteriores. Al ir de Inicio a Detalle el historial queda `Inicio → Detalle`, y el botón atrás vuelve a Inicio, que es lo que se espera.

Con `popUpTo(Rutas.INICIO)` primero se quitan del historial todas las pantallas que están encima de Inicio y luego se agrega la nueva. Al confirmar el pedido, el historial pasa de `Inicio → Carrito → Datos de entrega` a `Inicio → Confirmación`. Así, el botón atrás desde la confirmación no regresa al carrito ni al formulario de pago, lo que evitaría confirmar el mismo pedido dos veces. En el registro uso la variante `popUpTo(Rutas.BIENVENIDA) { inclusive = true }` para que tampoco se pueda volver a la bienvenida.

### 4. ¿Qué tuviste que corregir del código que te generó la IA para el buscador en tiempo real?

Corresponde a la Fase 2 y se responde en el PROMTPS.md de la rama de esa fase.

### 5. Compara el NavigationDrawer del Laboratorio 6 con el NavigationBar de esta tarea: ¿en qué caso usarías cada uno en un proyecto propio?

El `NavigationDrawer` es un menú lateral que se oculta. Admite muchas opciones y no ocupa espacio mientras está cerrado, pero exige un paso extra (abrirlo). El `NavigationBar` es una barra inferior siempre visible, pensada para 3 a 5 destinos principales a los que se llega con un solo toque del pulgar.

- **Usaría `NavigationBar`** en una app de uso diario con pocos destinos principales, como Mi Bodega: Inicio, Categorías, Pedidos y Perfil se usan constantemente y conviene tenerlos a la vista.
- **Usaría `NavigationDrawer`** cuando hay muchas secciones o acciones secundarias que no se usan todo el tiempo (ajustes, ayuda, cerrar sesión), como en la TECSUP Store del Lab 6, o en paneles de administración con varias secciones.

También se pueden combinar: barra inferior para lo principal y drawer para lo secundario.

## Observaciones

1. **Los nombres del enunciado no coinciden con el esqueleto.** El enunciado menciona `Rutas.kt`, `AppNavegacion.kt`, `PantallaLogin.kt` y `PantallaCrearCuenta.kt`, pero el repositorio trae la navegación dentro de `ClienteApp.kt` y las pantallas con otros nombres (`BienvenidaScreen`, `RegistroScreen`). Trabajé sobre la estructura real del esqueleto y separé `Rutas` en su propio archivo.
2. **El buscador de Inicio ya venía implementado.** El esqueleto ya filtraba por categoría y por texto a la vez, que es justamente la mejora obligatoria de la Fase 2. Por eso se implemento otras mejoras.
3. **Algunos TODO pedían pantallas que no están en el mockup(imagenes del resultado).**  El esqueleto dejaba pendientes el inicio de sesión (con la nota "aún no está en el mockup") y los términos y condiciones. Como la app no tiene base de datos, resolví el inicio de sesión de forma simulada (teléfono de 9 dígitos) y los términos con un texto de ejemplo.

## Conclusiones

1. **Partir de un esqueleto ahorra el trabajo repetitivo, pero exige leer antes de escribir.** Pude concentrarme en la navegación, el estado y los formularios sin armar el proyecto desde cero, pero primero tuve que entender cómo estaba organizado el código de otra persona y qué faltaba realmente.
2. **Mantener el estado en un solo lugar (`ClienteApp`) hizo el código flexible.** Como el carrito, el cliente y el historial de pedidos viven arriba y las pantallas solo reciben datos y funciones, pude agregar Pedidos y Perfil al final sin reescribir lo anterior.
3. **Trabajar con commits pequeños, uno por hito, facilitó probar y corregir.** Cada pantalla o flujo quedó en su propio commit, así los errores se ubicaban rápido. La comparación del proceso con la Fase 2 se desarrolla en el README de esa rama.
