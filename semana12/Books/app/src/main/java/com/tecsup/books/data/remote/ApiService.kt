package com.tecsup.books.data.remote

import com.tecsup.books.data.remote.dto.BusquedaDto
import com.tecsup.books.data.remote.dto.DetalleLibroDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search.json")
    suspend fun listar(
        @Query("subject") subject: String,
        @Query("language") language: String,
        @Query("limit") limit: Int
    ): BusquedaDto

    @GET("works/{id}.json")
    suspend fun detalle(@Path("id") id: String): DetalleLibroDto
}