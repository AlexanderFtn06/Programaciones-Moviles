package com.faustino.murocampus.model

import kotlinx.serialization.Serializable

// [E] Ejemplo resuelto
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

// [G] Paso 2: completa Post
@Serializable
data class Post(
    val id: Int
    // TODO: agrega userId, title y body (mismos nombres y tipos que el JSON)
)

// TODO: Comentario(postId, id, name, email, body)
// TODO: NuevoPost(userId, title, body)  (sin id)