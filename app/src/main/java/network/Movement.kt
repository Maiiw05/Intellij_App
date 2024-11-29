package com.example.appiumint.network

data class Movement(
    val fecha_transaccion: String, // Fecha del movimiento
    val monto: Double,             // Monto del movimiento
    val nombre: String             // Tipo de operación (e.g., "Transferencia", "Retiro")
)
