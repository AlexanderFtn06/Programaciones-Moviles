package com.faustino.ayudacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val RECUPERAR_CLAVE = "recuperar_clave"

    // Estudiante
    const val MIS_TICKETS = "mis_tickets"
    const val NUEVO_TICKET = "nuevo_ticket"
    const val DETALLE_TICKET = "detalle_ticket/{id}"
    const val PERFIL = "perfil"
    fun detalleTicket(id: Int) = "detalle_ticket/$id"

    // Soporte TI
    const val PANEL = "panel"
    const val BANDEJA = "bandeja"
    const val ATENDER_TICKET = "atender_ticket/{id}"
    const val CATEGORIAS = "categorias"
    const val FORMULARIO_CATEGORIA = "formulario_categoria?id={id}"
    const val AJUSTES = "ajustes"
    fun atenderTicket(id: Int) = "atender_ticket/$id"
    fun formularioCategoria(id: Int? = null) =
        if (id == null) "formulario_categoria" else "formulario_categoria?id=$id"
}