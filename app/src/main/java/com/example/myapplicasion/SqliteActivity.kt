package com.example.myapplicasion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SqliteActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var rvContactos: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var contactoAdapter: ContactoAdapter

    private var contactoSeleccionado: Contacto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etTelefono = findViewById(R.id.etTelefono)
        btnAgregar = findViewById(R.id.btnAgregar)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        rvContactos = findViewById(R.id.rvContactos)

        // Inicializar base de datos
        databaseHelper = DatabaseHelper(this)

        // Configurar RecyclerView
        setupRecyclerView()

        // Configurar listeners
        btnAgregar.setOnClickListener { agregarContacto() }
        btnActualizar.setOnClickListener { actualizarContacto() }
        btnEliminar.setOnClickListener { eliminarContacto() }
        btnLimpiar.setOnClickListener { limpiarCampos() }

        // Cargar contactos
        cargarContactos()
    }

    private fun setupRecyclerView() {
        contactoAdapter = ContactoAdapter(emptyList()) { contacto ->
            seleccionarContacto(contacto)
        }
        rvContactos.apply {
            layoutManager = LinearLayoutManager(this@SqliteActivity)
            adapter = contactoAdapter
        }
    }

    private fun agregarContacto() {
        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        if (validarCampos(nombre, email, telefono)) {
            val id = databaseHelper.insertarContacto(nombre, email, telefono)
            if (id > 0) {
                Toast.makeText(this, "Contacto agregado exitosamente", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                cargarContactos()
            } else {
                Toast.makeText(this, "Error al agregar contacto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarContacto() {
        val contacto = contactoSeleccionado
        if (contacto == null) {
            Toast.makeText(this, "Selecciona un contacto para actualizar", Toast.LENGTH_SHORT).show()
            return
        }

        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        if (validarCampos(nombre, email, telefono)) {
            val result = databaseHelper.actualizarContacto(contacto.id, nombre, email, telefono)
            if (result > 0) {
                Toast.makeText(this, "Contacto actualizado exitosamente", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                cargarContactos()
            } else {
                Toast.makeText(this, "Error al actualizar contacto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarContacto() {
        val contacto = contactoSeleccionado
        if (contacto == null) {
            Toast.makeText(this, "Selecciona un contacto para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        val result = databaseHelper.eliminarContacto(contacto.id)
        if (result > 0) {
            Toast.makeText(this, "Contacto eliminado exitosamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
            cargarContactos()
        } else {
            Toast.makeText(this, "Error al eliminar contacto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun seleccionarContacto(contacto: Contacto) {
        contactoSeleccionado = contacto
        etNombre.setText(contacto.nombre)
        etEmail.setText(contacto.email)
        etTelefono.setText(contacto.telefono)
        Toast.makeText(this, "Contacto seleccionado: ${contacto.nombre}", Toast.LENGTH_SHORT).show()
    }

    private fun limpiarCampos() {
        etNombre.text.clear()
        etEmail.text.clear()
        etTelefono.text.clear()
        contactoSeleccionado = null
        etNombre.requestFocus()
    }

    private fun cargarContactos() {
        val contactos = databaseHelper.obtenerContactos()
        contactoAdapter.updateData(contactos)
    }

    private fun validarCampos(nombre: String, email: String, telefono: String): Boolean {
        if (nombre.isEmpty()) {
            etNombre.error = "El nombre es requerido"
            etNombre.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            etEmail.error = "El email es requerido"
            etEmail.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Email inválido"
            etEmail.requestFocus()
            return false
        }

        if (telefono.isEmpty()) {
            etTelefono.error = "El teléfono es requerido"
            etTelefono.requestFocus()
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }
}
