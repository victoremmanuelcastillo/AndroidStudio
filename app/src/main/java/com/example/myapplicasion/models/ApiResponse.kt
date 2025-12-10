package com.example.myapplicasion.models

data class ApiResponse(
    val success: Boolean = false,
    val message: String = "",
    val id_usuario: Int = 0,
    val nombre_usuario: String = "",
    val id_contacto: Int = 0,
    val contactos: List<Contacto>? = null
)
