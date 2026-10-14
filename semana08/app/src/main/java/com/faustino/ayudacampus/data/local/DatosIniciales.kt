package com.faustino.ayudacampus.data.local

import androidx.room.withTransaction
import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.data.local.entity.Comentario
import com.faustino.ayudacampus.data.local.entity.Ticket
import com.faustino.ayudacampus.data.local.entity.Usuario
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad
import com.faustino.ayudacampus.data.model.Rol

object DatosIniciales {

    private const val HORA = 3600 * 1000L
    private const val MINUTO = 60 * 1000L

    suspend fun sembrar(db: AyudaDatabase) {
        // Solo la primera vez
        if (db.usuarioDao().contar() > 0) return

        val ahora = System.currentTimeMillis()

        // Todo o nada: si algo falla, no queda la base a medias
        db.withTransaction {
            if (db.categoriaDao().contar() == 0) {
                db.categoriaDao().insertarTodas(categorias)
            }
            db.usuarioDao().insertarTodos(usuarios)
            tickets(ahora).forEach { db.ticketDao().insertarTicket(it) }
            comentarios(ahora).forEach { db.ticketDao().insertarComentario(it) }
        }
    }

    // ---------- 5 categorías ----------
    private val categorias = listOf(
        Categoria(id = 1, nombre = "Hardware", icono = "pc"),
        Categoria(id = 2, nombre = "Software", icono = "code"),
        Categoria(id = 3, nombre = "Red / Wi-Fi", icono = "wifi"),
        Categoria(id = 4, nombre = "Cuenta", icono = "key"),
        Categoria(id = 5, nombre = "Impresión", icono = "printer")
    )

    // ---------- 1 estudiante y 1 soporte (datos ficticios) ----------
    private val juan = Usuario(
        id = 1, nombre = "Juan Pérez", correo = "juan@campus.test",
        clave = "1234", rol = Rol.CLIENTE.name
    )
    private val soporte = Usuario(
        id = 2, nombre = "Soporte TI", correo = "soporte@campus.test",
        clave = "admin", rol = Rol.ADMIN.name
    )
    private val usuarios = listOf(juan, soporte)

    // ---------- 6 tickets en distintos estados y prioridades ----------
    private fun tickets(ahora: Long) = listOf(
        // ALTA + ABIERTO con más de 1 hora -> cuenta en la alerta del panel
        Ticket(
            id = 1, clienteId = juan.id, categoriaId = 3,
            titulo = "El Wi-Fi se desconecta en la biblioteca",
            descripcion = "Cada 10 minutos pierdo la conexión y tengo que volver a ingresar.",
            ubicacion = "Biblioteca, 2do piso",
            prioridad = Prioridad.ALTA.name, estado = EstadoTicket.ABIERTO.name,
            fecha = ahora - 3 * HORA
        ),
        Ticket(
            id = 2, clienteId = juan.id, categoriaId = 1,
            titulo = "La PC del laboratorio 4 no enciende",
            descripcion = "Presiono el botón de encendido y no hace nada, ni luces.",
            ubicacion = "Lab. 4 - PC 12",
            prioridad = Prioridad.ALTA.name, estado = EstadoTicket.EN_PROCESO.name,
            fecha = ahora - 26 * HORA
        ),
        Ticket(
            id = 3, clienteId = juan.id, categoriaId = 4,
            titulo = "No puedo entrar al aula virtual",
            descripcion = "Me aparece credenciales inválidas aunque la clave es correcta.",
            ubicacion = "Remoto",
            prioridad = Prioridad.MEDIA.name, estado = EstadoTicket.RESUELTO.name,
            fecha = ahora - 50 * HORA
        ),
        Ticket(
            id = 4, clienteId = juan.id, categoriaId = 2,
            titulo = "Instalar Android Studio en el lab 2",
            descripcion = "Necesitamos Android Studio para el curso de móviles.",
            ubicacion = "Lab. 2",
            prioridad = Prioridad.BAJA.name, estado = EstadoTicket.CERRADO.name,
            fecha = ahora - 120 * HORA,
            fechaCierre = ahora - 100 * HORA, calificacion = 5
        ),
        Ticket(
            id = 5, clienteId = juan.id, categoriaId = 5,
            titulo = "La impresora del pabellón B atasca hojas",
            descripcion = "Se traba en la bandeja 2 con cualquier tipo de papel.",
            ubicacion = "Pabellón B, 1er piso",
            prioridad = Prioridad.MEDIA.name, estado = EstadoTicket.ABIERTO.name,
            fecha = ahora - 30 * MINUTO
        ),
        // ALTA + ABIERTO pero reciente -> todavía NO cuenta en la alerta
        Ticket(
            id = 6, clienteId = juan.id, categoriaId = 1,
            titulo = "Proyector del aula 301 sin señal HDMI",
            descripcion = "El proyector enciende pero no detecta la laptop por HDMI.",
            ubicacion = "Aula 301",
            prioridad = Prioridad.ALTA.name, estado = EstadoTicket.ABIERTO.name,
            fecha = ahora - 20 * MINUTO
        )
    )

    // ---------- 4 comentarios ----------
    private fun comentarios(ahora: Long) = listOf(
        Comentario(
            ticketId = 2, autorId = juan.id, autorNombre = juan.nombre, autorRol = juan.rol,
            texto = "Ya probé con otro enchufe y sigue sin encender.",
            fecha = ahora - 25 * HORA
        ),
        Comentario(
            ticketId = 2, autorId = soporte.id, autorNombre = soporte.nombre, autorRol = soporte.rol,
            texto = "Vamos a revisar la fuente de poder hoy en la tarde.",
            fecha = ahora - 24 * HORA
        ),
        Comentario(
            ticketId = 3, autorId = soporte.id, autorNombre = soporte.nombre, autorRol = soporte.rol,
            texto = "Restablecimos tu contraseña. Prueba con la que te llegó al correo.",
            fecha = ahora - 46 * HORA
        ),
        Comentario(
            ticketId = 1, autorId = juan.id, autorNombre = juan.nombre, autorRol = juan.rol,
            texto = "Pasa sobre todo en las mesas del fondo.",
            fecha = ahora - 2 * HORA
        )
    )
}