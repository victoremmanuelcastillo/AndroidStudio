package com.example.myapplicasion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PrincipalActivity : AppCompatActivity() {

    private lateinit var btnCerrarSesion: Button
    private lateinit var tvBienvenida: TextView
    private lateinit var layoutImagenUsuario: LinearLayout
    private lateinit var layoutSeleccionarImagen: LinearLayout
    private lateinit var ivImagenUsuario: ImageView
    private lateinit var tvNombreImagen: TextView

    private var usuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        // Obtener el usuario
        usuario = intent.getStringExtra("usuario") ?: ""

        // Inicializar vistas
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        tvBienvenida = findViewById(R.id.tvBienvenida)
        layoutImagenUsuario = findViewById(R.id.layoutImagenUsuario)
        layoutSeleccionarImagen = findViewById(R.id.layoutSeleccionarImagen)
        ivImagenUsuario = findViewById(R.id.ivImagenUsuario)
        tvNombreImagen = findViewById(R.id.tvNombreImagen)

        // Mostrar bienvenida
        tvBienvenida.text = "Bienvenido, $usuario"

        // Verificar si el usuario ya tiene una imagen seleccionada
        val sharedPref = getSharedPreferences("UsersData", Context.MODE_PRIVATE)
        val imagenId = sharedPref.getInt("user_${usuario}_imagen", -1)

        if (imagenId != -1) {
            // Ya tiene imagen seleccionada, mostrarla
            mostrarImagenUsuario(imagenId)
        } else {
            // No tiene imagen, mostrar opciones para seleccionar
            mostrarOpcionesImagen()
        }

        // Botón de cerrar sesión
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun mostrarImagenUsuario(imagenId: Int) {
        layoutImagenUsuario.visibility = View.VISIBLE
        layoutSeleccionarImagen.visibility = View.GONE

        ivImagenUsuario.setImageResource(imagenId)
        tvNombreImagen.text = obtenerNombreImagen(imagenId)
    }

    private fun mostrarOpcionesImagen() {
        layoutImagenUsuario.visibility = View.GONE
        layoutSeleccionarImagen.visibility = View.VISIBLE

        // Configurar las imágenes opcionales con los avatares creados
        val imagenesOpciones = listOf(
            R.id.ivOpcion1 to R.drawable.avatar_1,
            R.id.ivOpcion2 to R.drawable.avatar_2,
            R.id.ivOpcion3 to R.drawable.avatar_3,
            R.id.ivOpcion4 to R.drawable.avatar_4,
            R.id.ivOpcion5 to R.drawable.avatar_5,
            R.id.ivOpcion6 to R.drawable.avatar_6
        )

        imagenesOpciones.forEach { (viewId, imageResId) ->
            findViewById<ImageView>(viewId).setOnClickListener {
                seleccionarImagen(imageResId)
            }
        }
    }

    private fun seleccionarImagen(imagenId: Int) {
        // Guardar la imagen del usuario
        val sharedPref = getSharedPreferences("UsersData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("user_${usuario}_imagen", imagenId)
            apply()
        }

        Toast.makeText(this, "Imagen seleccionada exitosamente", Toast.LENGTH_SHORT).show()

        // Mostrar la imagen seleccionada
        mostrarImagenUsuario(imagenId)
    }

    private fun obtenerNombreImagen(imagenId: Int): String {
        return when (imagenId) {
            R.drawable.avatar_1 -> "Avatar Azul"
            R.drawable.avatar_2 -> "Avatar Verde"
            R.drawable.avatar_3 -> "Avatar Rojo"
            R.drawable.avatar_4 -> "Avatar Morado"
            R.drawable.avatar_5 -> "Avatar Naranja"
            R.drawable.avatar_6 -> "Avatar Cian"
            else -> "Avatar del usuario"
        }
    }

    private fun cerrarSesion() {
        // Eliminar la sesión activa
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("usuario_activo")
            apply()
        }

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

        // Volver a la pantalla de login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
