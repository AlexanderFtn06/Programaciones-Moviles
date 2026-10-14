package com.faustino.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "comentarios",
    foreignKeys = [
        // N comentarios -> 1 ticket. Si se borra el ticket, se borran sus comentarios
        ForeignKey(
            entity = Ticket::class,
            parentColumns = ["id"],
            childColumns = ["ticketId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ticketId")]
)
data class Comentario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketId: Int,
    val autorId: Int,
    val autorNombre: String,    // "foto" del momento: se muestra sin buscar en otra tabla
    val autorRol: String,       // Rol.name: CLIENTE | ADMIN
    val texto: String,
    val fecha: Long = System.currentTimeMillis()
)