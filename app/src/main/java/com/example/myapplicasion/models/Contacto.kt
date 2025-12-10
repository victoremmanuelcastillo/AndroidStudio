package com.example.myapplicasion.models

data class Contacto(
    val id_contacto: Int = 0,
    val codigo: String = "",
    val nombre: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val correo: String = "",
    val ruta_imagen: String? = null,
    val id_usuario: Int = 0
)
