# PROMPTS.md — Fase 2 (rama mejora-ia-Lab06-tarea)

Mi Bodega 

**Asistente de IA principal:** Claude

## Mejoras desarrolladas

1. **Inicio de sesión con contraseña**, validado contra un usuario fijo en el código. Al coincidir, se cargan los datos completos del usuario demo para que Perfil y Datos de entrega aparezcan llenos.
2. **`AlertDialog` de confirmación antes de eliminar un producto del carrito**, tanto con el ícono de basura como al bajar la cantidad de 1 a 0 con el botón "−".

**Usuario demo:**  Nombre `Alexander Faustino`· teléfono `906259697` · contraseña `12345678`· direccion `Av. Metropolitana - ceres medio` · referencia `Frente al parque`


---

## Prompt 1 — Campo de contraseña en el login

**Commit:** `Labo6-Tarea-ia: Agregar campo de contraseña al login`

**Prompt:**

> Actua como desarrollador de aplicaciones moviles ya que tengo una `LoginScreen` en Jetpack Compose con un `CampoTexto` personalizado (etiqueta, valor, onValorCambia, placeholder, teclado) y un teléfono de 9 dígitos. Quiero agregar un campo de contraseña que oculte los caracteres. Mi `CampoTexto` no tiene `visualTransformation`. Aquí está el código de `LoginScreen.kt` y `CampoTexto.kt`: "CODIGO"

(La IA trabajó a partir de mi repositorio público.)

**Respuesta:** La IA propuso agregar un parámetro opcional `visualTransformation` (por defecto `VisualTransformation.None`) a `CampoTexto`, para no afectar sus otros usos en Registro y Datos de entrega. En `LoginScreen` agregó el estado `contraseña` y un segundo `CampoTexto` con `KeyboardType.Password` y `PasswordVisualTransformation()`, y sugirió habilitar el botón solo con teléfono válido y contraseña no vacía.

**Correcciones que hice:** Ninguna, compiló y funcionó todo lo nuevo

---

## Prompt 2 — Validar credenciales fijas y cargar los datos del usuario

**Commit:** `Labo6-Tarea-ia: Validar credenciales fijas y cargar datos del usuario demo`

**Prompt:**

> Sigue actuando como desarrollador de aplicaciones moviles. Continuando con lo anterior mi `LoginScreen` ahora tiene teléfono y contraseña. Quiero validar contra un usuario fijo en el código (teléfono 9906259697, contraseña 12345678). Si coincide, debe entrar a Inicio con los datos completos de ese usuario (nombre, teléfono, dirección, referencia) cargados en el estado `cliente` de `ClienteApp`, para que Perfil y Datos de entrega se vean llenos. Si no coincide, no debe navegar.

(La IA trabajó a partir de mi repositorio público.)

**Respuesta (resumen):**

- Primera propuesta: constantes `TELEFONO_DEMO` y `CONTRASENA_DEMO`, una función `credencialesValidas()` y llamar a `onIngresar(telefono)` solo si coinciden.
- Tras mi primera corrección: se agregó `CLIENTE_DEMO` (un `Cliente` completo), `onIngresar` pasó a entregar un `Cliente` y en `ClienteApp` se asigna `cliente = clienteLogueado` antes de navegar a Inicio.
- Tras mi segunda corrección: las constantes y `CLIENTE_DEMO` se movieron a `DatosFake.kt` y `LoginScreen.kt` los importa.

**Correccion que hice:**

**Ubicación de los datos de ejemplo.** La IA dejó las credenciales y `CLIENTE_DEMO` dentro de `LoginScreen.kt`. Los moví a `DatosFake.kt`, donde ya viven los datos de ejemplo de la app, para separar los datos de la interfaz.

---

## Prompt 3 — AlertDialog antes de eliminar del carrito

**Commit:** `Labo6-Tarea-ia: Agrega un AlertDialog antes de eliminar del carrito`

**Prompt:**

> Sigue actuando como desarrollador de aplicaciones moviles y continuando con lo anterior, tengo una `CarritoScreen` en Jetpack Compose que recibe `onEliminar: (Producto) -> Unit` y lo llama directo desde el ícono de basura de cada fila. Quiero que antes de eliminar aparezca un `AlertDialog` de confirmación con botones Cancelar y Eliminar. El carrito vive en `ClienteApp`, así que no quiero tocarlo. Aquí está mi `CarritoScreen.kt`: "CODIGO"

(La IA trabajó a partir de mi repositorio público.)

**Respuesta (resumen):** La IA propuso guardar en `CarritoScreen` un estado local `productoAEliminar` (`Producto?`). El ícono de basura solo asigna ese estado; mientras no sea `null` se muestra un `AlertDialog` con Cancelar y Eliminar. Solo al confirmar se llama a `onEliminar(producto)`, por lo que `ClienteApp` no se modificó.

**Correcciones que hice:** Ninguna. Compiló y funcionó sin cambios; probé que Cancelar, tocar fuera del diálogo y el botón atrás no eliminan, y que Eliminar actualiza subtotal y total.

---

## Prompt 4 — Personalizar el diálogo y confirmar al bajar de 1 a 0

**Commit:** `Labo6-Tarea-ia: Personalizar el AlertDialog y confirmar también al reducir la cantidad a 0 aparezca el AlertDialog`

**Prompt**:

> Continuando con lo anterior, el diálogo ya funciona. Quiero personalizarlo: que el mensaje incluya el nombre del producto, que tenga un ícono de basura y que el botón "Eliminar" use el color de error del tema. Al tocar "−" con cantidad 1, `ClienteApp` elimina el producto sin avisar (usa `mapNotNull`). Quiero que en ese caso se abra el mismo diálogo de confirmación en vez de borrarlo directo.

(La IA trabajó a partir de mi repositorio público)

**Respuesta (resumen):**

- Personalización: ícono `Icons.Default.Delete` con `iconContentColor` de error, mensaje con el nombre del producto y botón "Eliminar" con `ButtonDefaults.textButtonColors` en color de error.
- Botón "−": interceptarlo en `FilaCarrito` con `if (item.cantidad == 1)` para abrir el mismo diálogo en vez de llamar a `onDecrementar`.

**Correcciones que hice:**

**La condición `if (item.cantidad == 1)` nunca se ejecutaba.** La IA no revisó `SelectorCantidad.kt`, donde el botón "−" se deshabilita cuando `cantidad <= minimo` (con `minimo = 1` por defecto). Con cantidad 1 el botón estaba gris y no se podía tocar. Lo resolví al pasar `minimo = 0` en la llamada a `SelectorCantidad` del carrito, y también cambie el valor por defecto de `minimo` a 0 en `SelectorCantidad.kt`.

---
## Pregunta
¿Qué tuviste que corregir del código que te generó la IA para el buscador en tiempo real?

El buscador en tiempo real ya estaba hecho en la Fase 1(el codigo esqueleto ya lo tenia), productosFiltrados combina el texto de búsqueda y la categoría seleccionada con &&, e ignora mayúsculas. Por eso, en la Fase 2 no le pedí a la IA que lo generara, y usé la IA para otras dos mejoras: el login con contraseña y usuario fijo, y un AlertDialog de confirmación al eliminar productos del carrito. Lo que tuve que corregir del código que me generó fue lo siguiente:

1. **Login**: ubicación de los datos de ejemplo. La IA dejó las credenciales y el usuario demo dentro de LoginScreen.kt. Los moví a DatosFake.kt, donde ya vivían los datos de ejemplo, para separar los datos de la interfaz.
2. **Carrito**: la intercepción del botón "−" no se ejecutaba. La IA propuso abrir el diálogo con if (item.cantidad == 1), pero no revisó SelectorCantidad, que deshabilita el botón cuando la cantidad es igual al mínimo (1). Lo resolví pasando minimo = 0 en la llamada del carrito.

## Capturas

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/bfa5e977-9fcb-41a1-9268-3016bacc99e1" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/3fd5fed7-a208-456f-bb31-4d6c9cd49495" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/59294335-5cea-44eb-9d14-dabccc84fcce" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/662ca056-ddad-4e0e-a3a7-7e129d016394" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/b8d85773-a07a-4d90-868c-251958b5b6bf" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/848ab033-8715-4585-94b0-60775b320e9f" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/204e0b9e-6209-4836-b57f-cc79884f2418" />

<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/7a5bfb12-1ed8-4e6e-9e89-70791aa62adb" />


