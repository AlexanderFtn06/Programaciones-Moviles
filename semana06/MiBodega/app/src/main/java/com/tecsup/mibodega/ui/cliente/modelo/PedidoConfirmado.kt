package com.tecsup.mibodega.ui.cliente.modelo

data class PedidoConfirmado(
    val numero: Int,
    val total: Double,
    val direccion: String,
    val referencia: String
)