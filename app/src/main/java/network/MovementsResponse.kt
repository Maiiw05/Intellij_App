package com.example.appiumint.network

// respuesta para la API de movimientos
data class MovementsResponse(
    val success: Boolean,
    val data: List<Movement>?
)
