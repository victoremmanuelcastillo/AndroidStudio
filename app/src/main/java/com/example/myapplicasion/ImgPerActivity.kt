package com.example.myapplicasion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class ImgPerActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnEncodeToBase64: Button
    private lateinit var btnDecodeFromBase64: Button
    private lateinit var btnSendWithVolley: Button
    private lateinit var tvResult: TextView

    private var encodedImageString: String = ""
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imgper)

        // Inicializar vistas
        imageView = findViewById(R.id.imageView)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnEncodeToBase64 = findViewById(R.id.btnEncodeToBase64)
        btnDecodeFromBase64 = findViewById(R.id.btnDecodeFromBase64)
        btnSendWithVolley = findViewById(R.id.btnSendWithVolley)
        tvResult = findViewById(R.id.tvResult)

        // Inicializar Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this)

        // Configurar listeners
        btnSelectImage.setOnClickListener {
            // Cargar una imagen de ejemplo desde recursos
            // En este caso, usaremos el launcher icon como ejemplo
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            imageView.setImageBitmap(bitmap)
            tvResult.text = "Imagen cargada desde recursos"
        }

        btnEncodeToBase64.setOnClickListener {
            // Codificar la imagen actual a Base64
            try {
                encodedImageString = encodeResourceToBase64(this, R.mipmap.ic_launcher)
                tvResult.text = "Imagen codificada a Base64:\n${encodedImageString.take(100)}..."
                Toast.makeText(this, "Imagen codificada exitosamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                tvResult.text = "Error al codificar: ${e.message}"
            }
        }

        btnDecodeFromBase64.setOnClickListener {
            // Decodificar Base64 a imagen
            try {
                if (encodedImageString.isNotEmpty()) {
                    val bitmap = decodeBase64(encodedImageString)
                    imageView.setImageBitmap(bitmap)
                    tvResult.text = "Imagen decodificada desde Base64"
                    Toast.makeText(this, "Imagen decodificada exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    tvResult.text = "Primero codifica una imagen a Base64"
                }
            } catch (e: Exception) {
                tvResult.text = "Error al decodificar: ${e.message}"
            }
        }

        btnSendWithVolley.setOnClickListener {
            // Enviar imagen con Volley
            if (encodedImageString.isNotEmpty()) {
                sendImageWithVolley(encodedImageString)
            } else {
                tvResult.text = "Primero codifica una imagen a Base64"
                Toast.makeText(this, "Codifica una imagen primero", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para codificar imagen de recursos a Base64
    fun encodeResourceToBase64(context: Context, resId: Int): String {
        // 1. Obtener el bitmap desde recursos
        val bitmap = BitmapFactory.decodeResource(context.resources, resId)

        // 2. Redimensionar (opcional)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 800, 600, true)

        // 3. Comprimir a JPEG
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

        // 4. Convertir a Base64
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    // Función para decodificar Base64 a Bitmap
    fun decodeBase64(encodedImage: String): Bitmap {
        val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    // Función para enviar imagen con Volley
    private fun sendImageWithVolley(base64Image: String) {
        // URL de ejemplo - debes cambiar esto por tu endpoint real
        val url = "https://jsonplaceholder.typicode.com/posts"

        // Crear objeto JSON con la imagen
        val jsonBody = JSONObject()
        jsonBody.put("title", "Imagen Base64")
        jsonBody.put("body", base64Image.take(100)) // Enviamos solo los primeros 100 caracteres como ejemplo
        jsonBody.put("userId", 1)

        // Crear request
        val jsonRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                tvResult.text = "Respuesta de Volley:\n${response.toString()}"
                Toast.makeText(this, "Imagen enviada exitosamente", Toast.LENGTH_SHORT).show()
            },
            { error ->
                tvResult.text = "Error en Volley: ${error.message}"
                Toast.makeText(this, "Error al enviar imagen", Toast.LENGTH_SHORT).show()
            }
        )

        // Agregar request a la cola
        requestQueue.add(jsonRequest)
        tvResult.text = "Enviando imagen con Volley..."
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancelar todas las peticiones pendientes cuando se destruya la actividad
        requestQueue.cancelAll(this)
    }
}
