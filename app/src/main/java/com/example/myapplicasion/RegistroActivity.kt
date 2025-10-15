package com.example.myapplicasion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

/**
 * ACTIVIDAD DE REGISTRO
 *
 * Esta actividad permite a los nuevos usuarios crear una cuenta.
 *
 * Flujo:
 * 1. Usuario llena: usuario, contraseña, repetir contraseña
 * 2. Sistema valida los datos
 * 3. Sistema guarda el usuario en SharedPreferences
 * 4. Sistema crea sesión automática
 * 5. Sistema lleva al usuario a PrincipalActivity
 */
class RegistroActivity : AppCompatActivity() {

    // ═══════════════════════════════════════════════════════════
    // DECLARACIÓN DE VARIABLES
    // ═══════════════════════════════════════════════════════════

    // lateinit = "late initialize" - Se inicializará después, en onCreate()
    private lateinit var etUsuario: TextInputEditText      // Campo de texto para usuario
    private lateinit var etPassword: TextInputEditText     // Campo de texto para contraseña
    private lateinit var etRePassword: TextInputEditText   // Campo de texto para repetir contraseña
    private lateinit var btnGuardar: Button                // Botón de guardar

    // ═══════════════════════════════════════════════════════════
    // MÉTODO PRINCIPAL - SE EJECUTA AL ABRIR LA ACTIVIDAD
    // ═══════════════════════════════════════════════════════════

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargar el diseño XML de la pantalla de registro
        setContentView(R.layout.activity_registro)

        // ───────────────────────────────────────────────────────
        // PASO 1: Conectar las variables con los elementos del XML
        // ───────────────────────────────────────────────────────
        etUsuario = findViewById(R.id.etUsuarioRegistro)        // Buscar el campo de usuario por su ID
        etPassword = findViewById(R.id.etPasswordRegistro)      // Buscar el campo de contraseña
        etRePassword = findViewById(R.id.etRePasswordRegistro)  // Buscar el campo de repetir contraseña
        btnGuardar = findViewById(R.id.btnGuardar)              // Buscar el botón de guardar

        // ───────────────────────────────────────────────────────
        // PASO 2: Configurar el evento de clic del botón Guardar
        // ───────────────────────────────────────────────────────
        btnGuardar.setOnClickListener {

            // Obtener el texto de los campos y eliminar espacios al inicio/final con trim()
            val usuario = etUsuario.text.toString().trim()      // Ej: "juan"
            val password = etPassword.text.toString().trim()    // Ej: "123456"
            val rePassword = etRePassword.text.toString().trim() // Ej: "123456"

            // ┌─────────────────────────────────────────────────┐
            // │ VALIDACIÓN 1: Verificar que no haya campos vacíos │
            // └─────────────────────────────────────────────────┘
            if (usuario.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                // Mostrar mensaje de error al usuario
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                // return@setOnClickListener = salir del listener (detener ejecución)
                return@setOnClickListener
            }

            // ┌─────────────────────────────────────────────────┐
            // │ VALIDACIÓN 2: Verificar que las contraseñas coincidan │
            // └─────────────────────────────────────────────────┘
            if (password != rePassword) {
                // Las contraseñas no son iguales
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // Detener ejecución
            }

            // ┌─────────────────────────────────────────────────┐
            // │ VALIDACIÓN 3: Verificar que el usuario no exista ya │
            // └─────────────────────────────────────────────────┘
            if (usuarioExiste(usuario)) {
                // Ya hay alguien registrado con ese nombre de usuario
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // Detener ejecución
            }

            // ═══════════════════════════════════════════════════
            // ✅ TODAS LAS VALIDACIONES PASARON - PROCEDER A GUARDAR
            // ═══════════════════════════════════════════════════

            // PASO 1: Guardar el usuario en la base de datos (SharedPreferences)
            registrarUsuario(usuario, password)

            // PASO 2: Crear sesión automática (para que no tenga que hacer login)
            guardarSesion(usuario)

            // PASO 3: Mostrar mensaje de éxito
            Toast.makeText(this, "registrado exitosamente", Toast.LENGTH_SHORT).show()

            // PASO 4: Navegar a la pantalla principal
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.putExtra("usuario", usuario)  // Enviar el nombre de usuario
            startActivity(intent)  // Abrir PrincipalActivity
            finish()  // Cerrar RegistroActivity (no puede volver atrás)
        }
    }

    // ═══════════════════════════════════════════════════════════
    // FUNCIÓN: Verificar si el usuario ya existe
    // ═══════════════════════════════════════════════════════════
    /**
     * Busca en SharedPreferences si ya existe un usuario con ese nombre
     *
     * @param usuario El nombre de usuario a buscar (ej: "juan")
     * @return true si existe, false si no existe
     *
     * Ejemplo:
     * usuarioExiste("juan") → true si ya hay un "juan" registrado
     * usuarioExiste("maria") → false si no hay ninguna "maria"
     */
    private fun usuarioExiste(usuario: String): Boolean {
        // Abrir el archivo "UsersData" de SharedPreferences (modo lectura)
        val sharedPref = getSharedPreferences("UsersData", Context.MODE_PRIVATE)

        // Verificar si existe la clave "user_juan_pass" (por ejemplo)
        // contains() retorna true si la clave existe, false si no
        return sharedPref.contains("user_${usuario}_pass")

        // Ejemplo:
        // Si usuario = "juan", busca: "user_juan_pass"
        // Si existe → retorna true
        // Si no existe → retorna false
    }

    // ═══════════════════════════════════════════════════════════
    // FUNCIÓN: Registrar un nuevo usuario
    // ═══════════════════════════════════════════════════════════
    /**
     * Guarda los datos del usuario en SharedPreferences
     *
     * @param usuario Nombre de usuario (ej: "juan")
     * @param password Contraseña (ej: "123456")
     *
     * Guarda en memoria:
     * - user_juan_pass = "123456"
     * - user_juan_nombre_completo = "usuario1"
     * - contador_usuarios = 1
     */
    private fun registrarUsuario(usuario: String, password: String) {
        // Abrir el archivo "UsersData" de SharedPreferences
        val sharedPref = getSharedPreferences("UsersData", Context.MODE_PRIVATE)

        // ───────────────────────────────────────────────────────
        // Obtener el contador de usuarios y sumarle 1
        // ───────────────────────────────────────────────────────
        // getInt("contador_usuarios", 0) busca la clave "contador_usuarios"
        // Si no existe, retorna 0 como valor por defecto
        // Luego le suma 1 para el nuevo usuario
        val contadorUsuarios = sharedPref.getInt("contador_usuarios", 0) + 1

        // Ejemplo de cómo funciona:
        // Primera vez: getInt() retorna 0, +1 = 1 → usuario1
        // Segunda vez: getInt() retorna 1, +1 = 2 → usuario2
        // Tercera vez: getInt() retorna 2, +1 = 3 → usuario3

        // ───────────────────────────────────────────────────────
        // Guardar datos en SharedPreferences
        // ───────────────────────────────────────────────────────
        with(sharedPref.edit()) {  // edit() = modo de edición

            // 1. Guardar la contraseña
            // Formato: "user_[nombre]_pass" = contraseña
            // Ejemplo: "user_juan_pass" = "123456"
            putString("user_${usuario}_pass", password)

            // 2. Guardar el nombre completo (usuario1, usuario2, etc.)
            // Formato: "user_[nombre]_nombre_completo" = "usuario[número]"
            // Ejemplo: "user_juan_nombre_completo" = "usuario1"
            putString("user_${usuario}_nombre_completo", "usuario$contadorUsuarios")

            // 3. Actualizar el contador de usuarios
            // Ejemplo: "contador_usuarios" = 1
            putInt("contador_usuarios", contadorUsuarios)

            // apply() = Guardar los cambios de forma asíncrona (en background)
            // No bloquea la interfaz, es rápido
            apply()
        }

        // RESULTADO EN MEMORIA (ejemplo con usuario="juan", password="123456"):
        // ┌────────────────────────────────────────────────┐
        // │ UsersData.xml                                  │
        // ├────────────────────────────────────────────────┤
        // │ user_juan_pass = "123456"                      │
        // │ user_juan_nombre_completo = "usuario1"         │
        // │ contador_usuarios = 1                          │
        // └────────────────────────────────────────────────┘
    }

    // ═══════════════════════════════════════════════════════════
    // FUNCIÓN: Guardar la sesión activa del usuario
    // ═══════════════════════════════════════════════════════════
    /**
     * Crea una sesión automática para que el usuario no tenga que hacer login
     *
     * @param usuario Nombre del usuario (ej: "juan")
     *
     * Guarda en memoria:
     * - usuario_activo = "juan"
     *
     * Esto permite que la próxima vez que abra la app,
     * LoginActivity detecte que hay una sesión y salte directo a PrincipalActivity
     */
    private fun guardarSesion(usuario: String) {
        // Abrir el archivo "UserSession" de SharedPreferences
        // Este archivo es DIFERENTE a "UsersData"
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        // Guardar el usuario activo
        with(sharedPref.edit()) {
            // Guardar la clave "usuario_activo" con el nombre del usuario
            // Ejemplo: "usuario_activo" = "juan"
            putString("usuario_activo", usuario)

            // Guardar los cambios
            apply()
        }

        // RESULTADO EN MEMORIA:
        // ┌────────────────────────────────────────────────┐
        // │ UserSession.xml                                │
        // ├────────────────────────────────────────────────┤
        // │ usuario_activo = "juan"                        │
        // └────────────────────────────────────────────────┘
        //
        // Ahora, cuando el usuario abra la app de nuevo,
        // LoginActivity verá que "usuario_activo" existe
        // y lo llevará directo a PrincipalActivity sin pedir login
    }
}
