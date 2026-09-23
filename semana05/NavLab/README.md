# Lab04 — LazyColumn

**Autor:** Alexander Faustino Quispe

## Capturas sin la mejora con la IA de Android Studio

<img width="738" height="1600" alt="WhatsApp Image 2026-09-21 at 11 39 56 PM" src="https://github.com/user-attachments/assets/9d22ea44-51ae-48fc-93aa-7de10cc932bd" />

<img width="738" height="1600" alt="WhatsApp Image 2026-09-21 at 11 39 56 PM (1)" src="https://github.com/user-attachments/assets/f25d8426-d7bb-4317-b2b7-6ff1db7a4a15" />

<img width="738" height="1600" alt="WhatsApp Image 2026-09-21 at 11 39 56 PM (2)" src="https://github.com/user-attachments/assets/3f6db3bf-cd43-4888-a45c-4a6ce0e39258" />

<img width="738" height="1600" alt="WhatsApp Image 2026-09-21 at 11 39 56 PM (3)" src="https://github.com/user-attachments/assets/4e30e019-65d8-4195-a5fc-541ec9720b07" />

## Capturas con la mejora de IA la Android Studio(Gemini)

<img width="738" height="1600" alt="WhatsApp Image 2026-09-22 at 7 48 08 PM" src="https://github.com/user-attachments/assets/a331da55-6735-44d6-a34c-5ccc7de21871" />

<img width="738" height="1600" alt="WhatsApp Image 2026-09-22 at 7 48 08 PM (1)" src="https://github.com/user-attachments/assets/a9fa0880-6c23-456b-8280-8458ccfca960" />

<img width="738" height="1600" alt="WhatsApp Image 2026-09-22 at 7 48 08 PM (2)" src="https://github.com/user-attachments/assets/3d683b72-0c5e-4ead-9797-17c57486f8f8" />

<img width="738" height="1600" alt="WhatsApp Image 2026-09-22 at 7 50 34 PM" src="https://github.com/user-attachments/assets/a8ddafff-7336-43b8-a73c-7cb31269cd3e" />

<img width="738" height="1600" alt="WhatsApp Image 2026-09-22 at 7 48 08 PM (3)" src="https://github.com/user-attachments/assets/07c1b1d3-9a3f-4a06-ab02-777e43f37624" />

### Prompt
Actua como desarrollador de apliaciones moviles y rediseña mi app Jetpack Compose (Material 3): degradado morado 
(
#4A2E83) a lavanda (
#C9B8F0), esquinas redondeadas (16-20dp), iconos 
en círculos lavanda claro. No rompas mi navegación actual (Screen.kt, 
AppNavigation.kt, Home/List/Detail/ProfileScreen).

LOGIN (nueva, Screen.Login como startDestination): card centrada, 
título "Portal Académico", campos Correo/Contraseña con iconos, botón 
morado "INICIAR SESIÓN", link "¿Olvidaste tu contraseña?".

Lógica (lista fija de usuarios, sin backend):
- alexander@tecsup.edu.pe / 12345678 / nombre "Alexander"
- afaustino@tecsup.edu.pe / 87654321 / nombre "Faustino"

Validar en orden (error en rojo bajo el campo, solo tras enviar, sin 
navegar si falla):
1. Campo vacío → "Este campo es obligatorio"
2. Correo con formato inválido (Patterns.EMAIL_ADDRESS) → "Correo inválido"
3. Contraseña < 8 caracteres → "La contraseña debe tener al menos 8 caracteres"
4. Correo no está en la lista → "Usuario no encontrado"
5. Contraseña no coincide → "Contraseña incorrecta"
6. Todo correcto → navegar a Home pasando el "nombre" como argumento 
   (igual que Screen.Detail recibe itemId)

"¿Olvidaste tu contraseña?" clickeable → Snackbar "Se enviará un enlace 
de recuperación a tu correo institucional".

HOME: degradado morado, "Bienvenido, [nombre]" (del argumento recibido, 
no fijo), dos cards blancas (Directorio y Perfil), link rojo "Cerrar 
Sesión" que vuelve a Login limpiando el back stack.
LIST → "Directorio de Alumnos": lista con foto circular, nombre bold, 
carrera en morado, chevron ">".
DETAIL → "Expediente Académico": header degradado con foto grande, 
card con filas icono+label+valor (ID, correo, facultad) y Biografía.
PROFILE → "Configuración de Perfil": mismo header, secciones 
"INFORMACIÓN PERSONAL" y "ACADÉMICO", botón rojo "Cerrar Sesión" 
(vuelve a Login limpiando back stack).

Usa Scaffold, TopAppBar, Card, Brush.verticalGradient. Mantén nombres 
de archivos y funciones actuales, solo cambia estilos y agrega la 
lógica de login descrita.




