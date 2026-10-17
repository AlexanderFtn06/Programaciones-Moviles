package com.faustino.ayudacampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.faustino.ayudacampus.data.local.dao.CategoriaDao
import com.faustino.ayudacampus.data.local.dao.TicketDao
import com.faustino.ayudacampus.data.local.dao.UsuarioDao
import com.faustino.ayudacampus.data.local.entity.Categoria
import com.faustino.ayudacampus.data.local.entity.Comentario
import com.faustino.ayudacampus.data.local.entity.Ticket
import com.faustino.ayudacampus.data.local.entity.Usuario

@Database(
    entities = [Categoria::class, Usuario::class, Ticket::class, Comentario::class],
    version = 3,            // subió de 2 a 3 porque cambió el esquema
    exportSchema = false
)
abstract class AyudaDatabase : RoomDatabase() {

    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ticketDao(): TicketDao

    companion object {
        @Volatile
        private var INSTANCE: AyudaDatabase? = null

        // Singleton: una sola instancia para toda la app
        fun obtener(context: Context): AyudaDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AyudaDatabase::class.java,
                    "ayudacampus.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}