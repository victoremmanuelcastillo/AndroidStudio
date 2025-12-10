package com.example.myapplicasion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicasion.adapters.ContactosAdapter
import com.example.myapplicasion.api.ContactosApiService
import com.example.myapplicasion.models.Contacto
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VisorContactosActivity : AppCompatActivity() {

    private lateinit var rvContactos: RecyclerView
    private lateinit var tvSinContactos: TextView
    private lateinit var tvCantidadContactos: TextView
    private lateinit var fabNuevoContacto: FloatingActionButton

    private lateinit var apiService: ContactosApiService
    private lateinit var adapter: ContactosAdapter
    private var idUsuario: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visor_contactos)

        // Inicializar vistas
        rvContactos = findViewById(R.id.rvContactos)
        tvSinContactos = findViewById(R.id.tvSinContactos)
        tvCantidadContactos = findViewById(R.id.tvCantidadContactos)
        fabNuevoContacto = findViewById(R.id.fabNuevoContacto)

        // Inicializar API
        apiService = ContactosApiService(this)

        // Obtener id de usuario
        val prefs = getSharedPreferences("ContactosPrefs", Context.MODE_PRIVATE)
        idUsuario = prefs.getInt("id_usuario", 0)

        // Configurar RecyclerView
        rvContactos.layoutManager = LinearLayoutManager(this)
        adapter = ContactosAdapter(emptyList()) { contacto ->
            abrirEdicionContacto(contacto)
        }
        rvContactos.adapter = adapter

        // Configurar FAB
        fabNuevoContacto.setOnClickListener {
            val intent = Intent(this, RegistroContactoActivity::class.java)
            startActivity(intent)
        }

        // Cargar contactos
        cargarContactos()
    }

    override fun onResume() {
        super.onResume()
        cargarContactos()
    }

    private fun cargarContactos() {
        apiService.obtenerContactos(
            idUsuario = idUsuario,
            onSuccess = { contactos ->
                if (contactos.isEmpty()) {
                    rvContactos.visibility = View.GONE
                    tvSinContactos.visibility = View.VISIBLE
                    tvCantidadContactos.text = "0 contactos"
                } else {
                    rvContactos.visibility = View.VISIBLE
                    tvSinContactos.visibility = View.GONE
                    tvCantidadContactos.text = "${contactos.size} contacto(s)"
                    adapter.actualizarLista(contactos)
                }
            },
            onError = { error ->
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun abrirEdicionContacto(contacto: Contacto) {
        val intent = Intent(this, RegistroContactoActivity::class.java).apply {
            putExtra("id_contacto", contacto.id_contacto)
            putExtra("codigo", contacto.codigo)
            putExtra("nombre", contacto.nombre)
            putExtra("direccion", contacto.direccion)
            putExtra("telefono", contacto.telefono)
            putExtra("correo", contacto.correo)
        }
        startActivity(intent)
    }
}
