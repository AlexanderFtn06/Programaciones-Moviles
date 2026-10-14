package com.faustino.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.faustino.ayudacampus.data.model.EstadoTicket
import com.faustino.ayudacampus.data.model.Prioridad

@Entity(
    tableName = "tickets",
    foreignKeys = [
        // N tickets -> 1 cliente
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"]
        ),
        // N tickets -> 1 categoría
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"]
        )
    ],
    // Cada columna FK necesita su Index (si no, Room muestra advertencias)
    indices = [Index("clienteId"), Index("categoriaId")]
)
data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int,
    val categoriaId: Int,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String = "",
    val prioridad: String = Prioridad.MEDIA.name,       // BAJA | MEDIA | ALTA
    val estado: String = EstadoTicket.ABIERTO.name,     // ABIERTO | EN_PROCESO | RESUELTO | CERRADO
    val fecha: Long = System.currentTimeMillis(),
    val fechaCierre: Long? = null,                      // se llena al cerrar
    val calificacion: Int? = null                       // 1 a 5, se llena al cerrar
)