package com.example.appiumint.network

data class Movement(
    val fecha_transaccion: String,
    val monto: Double,
    val tipo_operacion: String,
    val descripcion: String
)
