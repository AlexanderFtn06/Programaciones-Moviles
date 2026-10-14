package com.faustino.ayudacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.data.local.entity.Comentario
import com.faustino.ayudacampus.data.local.entity.Ticket
import com.faustino.ayudacampus.data.local.entity.Usuario

/** Un ticket con todo lo que la pantalla necesita mostrar. */
data class TicketConDetalle(
    @Embedded
    val ticket: Ticket,

    // 1 ticket -> N comentarios
    @Relation(parentColumn = "id", entityColumn = "ticketId")
    val comentarios: List<Comentario>,

    // ticket.categoriaId -> categorias.id
    @Relation(parentColumn = "categoriaId", entityColumn = "id")
    val categoria: Categoria,

    // ticket.clienteId -> usuarios.id
    @Relation(parentColumn = "clienteId", entityColumn = "id")
    val cliente: Usuario
)