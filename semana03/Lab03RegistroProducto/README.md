# Lab03 - Registro de Producto

**Nombre:** Alexander Faustino

## Descripción

Aplicación desarrollada con Jetpack Compose que permite registrar un producto ingresando su nombre, precio y cantidad. 
Al presionar el botón **AGREGAR PRODUCTO**, se muestra una tarjeta (Card) con el resumen del producto y el importe total 
calculado (precio × cantidad), junto con un mensaje de confirmación.
La pantalla gestiona su estado con `remember` y `mutableStateOf`, y reutiliza herramientas del Lab 02 
(`toDoubleOrNull`, `toIntOrNull`, operador Elvis `?:` y `String.format`/`.format` con 2 decimales) para leer 
y mostrar los datos de forma segura.

## Capturas

### Pantalla inicial (formulario vacío)
<img width="738" height="1311" alt="WhatsApp Image 2026-09-02 at 7 32 18 PM" src="https://github.com/user-attachments/assets/a56f4093-befd-4a3b-bf36-852091b73785" />


### Después de presionar AGREGAR PRODUCTO
<img width="738" height="1318" alt="WhatsApp Image 2026-09-02 at 7 32 06 PM" src="https://github.com/user-attachments/assets/3452dc19-8b20-46e3-8fae-def8838865b8" />

## Reflexión: ¿qué pasaría si declaras las variables de los campos SIN remember?

Sin remember, el texto que escribo en los campos se borra cada vez que la pantalla se recompone (por ejemplo, al girar el celular).
Esto pasa porque mutableStateO crea el estado reactivo, pero solo remember hace que ese estado se conserve entre recomposiciones; 
sin él, la variable vuelve a su valor inicial cada vez que la función se ejecuta de nuevo.

## Mejora con IA

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí (y por qué) |
|---|---|---|
| Actua como un desarrollador de software mobile ya que tengo un composable PantallaRegistro en Jetpack Compose (código abajo) que registra nombre, precio y cantidad. Agrega SOLO esto:<br>1. Si algún campo está vacío al presionar "AGREGAR PRODUCTO", no muestres la Card: muestra en su lugar un texto de error en rojo tipo "Completa todos los campos".<br>2. Agrega un botón "LIMPIAR" debajo del botón actual que vacíe los 3 campos y oculte la Card/error.<br>No cambies el diseño existente, ni el cálculo del importe, ni el mensaje verde de confirmación cuando los datos sí son válidos, ni los nombres de las variables de estado.<br>Código: [se adjuntó el archivo MainActivity.kt completo] | Una variable de estado nueva `mensajeError`, la validación `isBlank()` en los 3 campos dentro del `onClick` de AGREGAR, un `OutlinedButton` "LIMPIAR" que vacía los campos y oculta la Card/error, y un `Text` en rojo (`MaterialTheme.colorScheme.error`) que se muestra cuando `mensajeError` no está vacío. | Acepté toda la estructura general porque cumplía lo pedido y no tocó el diseño existente.<br>**Corregí** la validación: el código original solo revisaba `isBlank()`, así que si escribías letras en Precio (ej. "abc"), no se detectaba como error y la Card mostraba "S/ 0.00" en silencio.<br>Lo probé, confirmé el problema, y ajusté la condición para también validar con `toDoubleOrNull()` / `toIntOrNull()` antes de calcular. |


### Código generado por Gemini (evidencia)

```kotlin
// CAMBIO 1: Nueva variable de estado para controlar el mensaje de error
var mensajeError by remember { mutableStateOf("") }

Button(
    onClick = {
        // CAMBIO 2: Validar si algún campo está vacío al presionar el botón
        if (nombre.isBlank() || precio.isBlank() || cantidad.isBlank()) {
            mostrarResumen = false // Oculta la Card si estaba visible
            mensajeError = "Completa todos los campos" // Muestra el texto de error
        } else {
            mensajeError = "" // Limpia el mensaje de error si la entrada es válida
            val p = precio.toDoubleOrNull() ?: 0.0
            val c = cantidad.toIntOrNull() ?: 0
            importeTotal = p * c
            mostrarResumen = true
        }
    },
    modifier = Modifier.fillMaxWidth()
)

// CAMBIO 3: Botón LIMPIAR agregado justo debajo de "AGREGAR PRODUCTO"
Spacer(modifier = Modifier.height(8.dp))
OutlinedButton(
    onClick = {
        // Vacía los 3 campos de texto
        nombre = ""
        precio = ""
        cantidad = ""
        // Oculta la Card y el mensaje de error
        mostrarResumen = false
        mensajeError = ""
    },
    modifier = Modifier.fillMaxWidth()
) {
    Text(text = "LIMPIAR")
}

// CAMBIO 4: Muestra el mensaje de error en color rojo si corresponde
if (mensajeError.isNotEmpty()) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = mensajeError,
        color = MaterialTheme.colorScheme.error, // Color rojo estándar del tema
        style = MaterialTheme.typography.bodyMedium
    )
}
```
### Capturas del codigo generado por la ia(sin corregir)
<img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/0882635d-c5f4-4030-a929-da9017ac9dec" />

### Capturas del codigo corregido generado por la ia
<img width="738" height="1600" alt="WhatsApp Image 2026-09-02 at 8 14 01 PM" src="https://github.com/user-attachments/assets/55a2374e-6268-4c80-862d-9055e32ac405" />

