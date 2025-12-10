package com.example.myapplicasion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MenuContactosActivity : AppCompatActivity() {

    private lateinit var tvBienvenida: TextView
    private lateinit var btnCerrarSesion: Button
    private lateinit var cardRegistro: CardView
    private lateinit var cardVisor: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_contactos)

        // Inicializar vistas
        tvBienvenida = findViewById(R.id.tvBienvenida)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        cardRegistro = findViewById(R.id.cardRegistro)
        cardVisor = findViewById(R.id.cardVisor)

        // Obtener datos de sesión
        val prefs = getSharedPreferences("ContactosPrefs", Context.MODE_PRIVATE)
        val nombreUsuario = prefs.getString("nombre_usuario", "Usuario")

        // Configurar bienvenida
        tvBienvenida.text = "Bienvenido, $nombreUsuario"

        // Configurar listeners
        cardRegistro.setOnClickListener {
            val intent = Intent(this, RegistroContactoActivity::class.java)
            startActivity(intent)
        }

        cardVisor.setOnClickListener {
            val intent = Intent(this, VisorContactosActivity::class.java)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion() {
        // Limpiar preferencias
        val prefs = getSharedPreferences("ContactosPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        // Volver al login
        val intent = Intent(this, LoginContactosActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
