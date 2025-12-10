package com.example.myapplicasion.api

import android.content.Context
import android.util.Base64
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicasion.models.ApiResponse
import com.example.myapplicasion.models.Contacto
import com.google.gson.Gson
import org.json.JSONObject

class ContactosApiService(private val context: Context) {

    // IMPORTANTE: Cambiar esta URL según tu configuración
    // Para emulador Android: http://10.0.2.2/backend_php/
    // Para dispositivo físico con ADB reverse: http://localhost:8080/backend_php/
    private val baseUrl = "http://localhost:8080/backend_php/"

    private val requestQueue = Volley.newRequestQueue(context)
    private val gson = Gson()

    // Login
    fun login(
        nombreUsuario: String,
        contrasena: String,
        onSuccess: (ApiResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = baseUrl + "login.php"

        val jsonBody = JSONObject().apply {
            put("nombre_usuario", nombreUsuario)
            put("contrasena", contrasena)
        }

        val request = object : JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                val apiResponse = gson.fromJson(response.toString(), ApiResponse::class.java)
                onSuccess(apiResponse)
            },
            { error ->
                val errorMessage = when {
                    error.networkResponse == null -> "No se puede conectar al servidor. Verifica:\n1. Que Apache esté corriendo\n2. Que estés en la misma red WiFi\n3. La IP del servidor: $baseUrl"
                    error.networkResponse.statusCode == 404 -> "Servidor no encontrado (404)"
                    else -> error.message ?: "Error desconocido"
                }
                onError(errorMessage)
            }
        ) {
            override fun getRetryPolicy() = com.android.volley.DefaultRetryPolicy(
                30000, // 30 segundos de timeout
                0, // no reintentar
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        }

        requestQueue.add(request)
    }

    // Obtener contactos
    fun obtenerContactos(
        idUsuario: Int,
        onSuccess: (List<Contacto>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = baseUrl + "obtener_contactos.php"

        val jsonBody = JSONObject().apply {
            put("id_usuario", idUsuario)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                val apiResponse = gson.fromJson(response.toString(), ApiResponse::class.java)
                if (apiResponse.success) {
                    onSuccess(apiResponse.contactos ?: emptyList())
                } else {
                    onError(apiResponse.message)
                }
            },
            { error ->
                onError(error.message ?: "Error de conexión")
            }
        )

        requestQueue.add(request)
    }

    // Crear contacto
    fun crearContacto(
        contacto: Contacto,
        imagenBase64: String?,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = baseUrl + "crear_contacto.php"

        val jsonBody = JSONObject().apply {
            put("id_usuario", contacto.id_usuario)
            put("codigo", contacto.codigo)
            put("nombre", contacto.nombre)
            put("direccion", contacto.direccion)
            put("telefono", contacto.telefono)
            put("correo", contacto.correo)
            if (!imagenBase64.isNullOrEmpty()) {
                put("imagen", imagenBase64)
            }
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                val apiResponse = gson.fromJson(response.toString(), ApiResponse::class.java)
                if (apiResponse.success) {
                    onSuccess(apiResponse.message)
                } else {
                    onError(apiResponse.message)
                }
            },
            { error ->
                onError(error.message ?: "Error de conexión")
            }
        )

        requestQueue.add(request)
    }

    // Actualizar contacto
    fun actualizarContacto(
        contacto: Contacto,
        imagenBase64: String?,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = baseUrl + "actualizar_contacto.php"

        val jsonBody = JSONObject().apply {
            put("id_contacto", contacto.id_contacto)
            put("codigo", contacto.codigo)
            put("nombre", contacto.nombre)
            put("direccion", contacto.direccion)
            put("telefono", contacto.telefono)
            put("correo", contacto.correo)
            if (!imagenBase64.isNullOrEmpty()) {
                put("imagen", imagenBase64)
            }
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                val apiResponse = gson.fromJson(response.toString(), ApiResponse::class.java)
                if (apiResponse.success) {
                    onSuccess(apiResponse.message)
                } else {
                    onError(apiResponse.message)
                }
            },
            { error ->
                onError(error.message ?: "Error de conexión")
            }
        )

        requestQueue.add(request)
    }

    // Eliminar contacto
    fun eliminarContacto(
        idContacto: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = baseUrl + "eliminar_contacto.php"

        val jsonBody = JSONObject().apply {
            put("id_contacto", idContacto)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                val apiResponse = gson.fromJson(response.toString(), ApiResponse::class.java)
                if (apiResponse.success) {
                    onSuccess(apiResponse.message)
                } else {
                    onError(apiResponse.message)
                }
            },
            { error ->
                onError(error.message ?: "Error de conexión")
            }
        )

        requestQueue.add(request)
    }
}
