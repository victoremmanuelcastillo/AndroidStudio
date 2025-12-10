package com.example.myapplicasion

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicasion.api.ContactosApiService
import com.example.myapplicasion.models.Contacto
import java.io.ByteArrayOutputStream

class RegistroContactoActivity : AppCompatActivity() {

    private lateinit var ivContacto: ImageView
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var etCodigo: EditText
    private lateinit var etNombre: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etCorreo: EditText
    private lateinit var btnNuevo: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button

    private lateinit var apiService: ContactosApiService
    private var imagenBase64: String? = null
    private var contactoActual: Contacto? = null
    private var idUsuario: Int = 0

    private val seleccionarImagenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let { cargarImagenDesdeUri(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_contacto)

        // Inicializar vistas
        ivContacto = findViewById(R.id.ivContacto)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        etCodigo = findViewById(R.id.etCodigo)
        etNombre = findViewById(R.id.etNombre)
        etDireccion = findViewById(R.id.etDireccion)
        etTelefono = findViewById(R.id.etTelefono)
        etCorreo = findViewById(R.id.etCorreo)
        btnNuevo = findViewById(R.id.btnNuevo)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnEliminar = findViewById(R.id.btnEliminar)

        // Inicializar API
        apiService = ContactosApiService(this)

        // Obtener id de usuario
        val prefs = getSharedPreferences("ContactosPrefs", Context.MODE_PRIVATE)
        idUsuario = prefs.getInt("id_usuario", 0)

        // Verificar si viene un contacto para editar
        cargarContactoDeIntent()

        // Si no hay contacto para editar, generar código automático
        if (contactoActual == null) {
            generarCodigoAutomatico()
        }

        // Configurar listeners
        btnSeleccionarImagen.setOnClickListener {
            seleccionarImagen()
        }

        btnNuevo.setOnClickListener {
            limpiarFormulario()
        }

        btnGuardar.setOnClickListener {
            guardarContacto()
        }

        btnActualizar.setOnClickListener {
            actualizarContacto()
        }

        btnEliminar.setOnClickListener {
            confirmarEliminacion()
        }
    }

    private fun cargarContactoDeIntent() {
        val contactoId = intent.getIntExtra("id_contacto", 0)
        if (contactoId > 0) {
            val codigo = intent.getStringExtra("codigo") ?: ""
            val nombre = intent.getStringExtra("nombre") ?: ""
            val direccion = intent.getStringExtra("direccion") ?: ""
            val telefono = intent.getStringExtra("telefono") ?: ""
            val correo = intent.getStringExtra("correo") ?: ""

            contactoActual = Contacto(
                id_contacto = contactoId,
                codigo = codigo,
                nombre = nombre,
                direccion = direccion,
                telefono = telefono,
                correo = correo,
                id_usuario = idUsuario
            )

            cargarDatosEnFormulario(contactoActual!!)
        }
    }

    private fun cargarDatosEnFormulario(contacto: Contacto) {
        etCodigo.setText(contacto.codigo)
        etNombre.setText(contacto.nombre)
        etDireccion.setText(contacto.direccion)
        etTelefono.setText(contacto.telefono)
        etCorreo.setText(contacto.correo)

        // Habilitar botones de actualizar y eliminar
        btnActualizar.isEnabled = true
        btnEliminar.isEnabled = true
        btnGuardar.isEnabled = false
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        seleccionarImagenLauncher.launch(intent)
    }

    private fun cargarImagenDesdeUri(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            ivContacto.setImageBitmap(bitmap)

            // Convertir a Base64
            imagenBase64 = convertirBitmapABase64(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar imagen: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertirBitmapABase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun generarCodigoAutomatico() {
        val codigoAuto = "C" + System.currentTimeMillis().toString().takeLast(6)
        etCodigo.setText(codigoAuto)
    }

    private fun limpiarFormulario() {
        etCodigo.setText("")
        etNombre.setText("")
        etDireccion.setText("")
        etTelefono.setText("")
        etCorreo.setText("")
        ivContacto.setImageResource(android.R.drawable.ic_menu_camera)

        imagenBase64 = null
        contactoActual = null

        btnActualizar.isEnabled = false
        btnEliminar.isEnabled = false
        btnGuardar.isEnabled = true

        // Generar código automático
        generarCodigoAutomatico()
    }

    private fun guardarContacto() {
        val codigo = etCodigo.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val correo = etCorreo.text.toString().trim()

        // Validaciones
        if (nombre.isEmpty()) {
            etNombre.error = "El nombre es requerido"
            etNombre.requestFocus()
            return
        }

        if (telefono.isEmpty()) {
            etTelefono.error = "El teléfono es requerido"
            etTelefono.requestFocus()
            return
        }

        if (correo.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.error = "Correo inválido"
            etCorreo.requestFocus()
            return
        }

        // Crear contacto
        val contacto = Contacto(
            codigo = codigo,
            nombre = nombre,
            direccion = direccion,
            telefono = telefono,
            correo = correo,
            id_usuario = idUsuario
        )

        btnGuardar.isEnabled = false
        btnGuardar.text = "Guardando..."

        apiService.crearContacto(
            contacto = contacto,
            imagenBase64 = imagenBase64,
            onSuccess = { mensaje ->
                btnGuardar.isEnabled = true
                btnGuardar.text = "Guardar"
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                limpiarFormulario()
            },
            onError = { error ->
                btnGuardar.isEnabled = true
                btnGuardar.text = "Guardar"
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun actualizarContacto() {
        if (contactoActual == null) return

        val codigo = etCodigo.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val correo = etCorreo.text.toString().trim()

        // Validaciones
        if (nombre.isEmpty()) {
            etNombre.error = "El nombre es requerido"
            etNombre.requestFocus()
            return
        }

        if (telefono.isEmpty()) {
            etTelefono.error = "El teléfono es requerido"
            etTelefono.requestFocus()
            return
        }

        if (correo.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.error = "Correo inválido"
            etCorreo.requestFocus()
            return
        }

        // Actualizar contacto
        val contactoActualizado = contactoActual!!.copy(
            codigo = codigo,
            nombre = nombre,
            direccion = direccion,
            telefono = telefono,
            correo = correo
        )

        btnActualizar.isEnabled = false
        btnActualizar.text = "Actualizando..."

        apiService.actualizarContacto(
            contacto = contactoActualizado,
            imagenBase64 = imagenBase64,
            onSuccess = { mensaje ->
                btnActualizar.isEnabled = true
                btnActualizar.text = "Actualizar"
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                finish()
            },
            onError = { error ->
                btnActualizar.isEnabled = true
                btnActualizar.text = "Actualizar"
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun confirmarEliminacion() {
        if (contactoActual == null) return

        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este contacto?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarContacto()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarContacto() {
        if (contactoActual == null) return

        btnEliminar.isEnabled = false
        btnEliminar.text = "Eliminando..."

        apiService.eliminarContacto(
            idContacto = contactoActual!!.id_contacto,
            onSuccess = { mensaje ->
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                finish()
            },
            onError = { error ->
                btnEliminar.isEnabled = true
                btnEliminar.text = "Eliminar"
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }
}
