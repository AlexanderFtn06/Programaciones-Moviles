package com.faustino.ayudacampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.faustino.ayudacampus.data.local.dao.CategoriaDao
import com.faustino.ayudacampus.data.local.entity.Categoria

@Database(
    entities = [Categoria::class /* TODO (Paso 3): agrega las otras 3 */],
    version = 1,
    exportSchema = false
)
abstract class AyudaDatabase : RoomDatabase() {

    abstract fun categoriaDao(): CategoriaDao
    // TODO (Paso 3): usuarioDao() y ticketDao()

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