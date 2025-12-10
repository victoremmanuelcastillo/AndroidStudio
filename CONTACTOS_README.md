# Sistema de Gestión de Contactos - Guía de Instalación

Este documento contiene las instrucciones completas para configurar y ejecutar el sistema de gestión de contactos con backend PHP/MySQL y aplicación Android.

## 📋 Requisitos Previos

### Backend
- XAMPP (o similar: WAMP, MAMP) con:
  - Apache 2.4 o superior
  - PHP 7.4 o superior
  - MySQL 5.7 o superior
  - phpMyAdmin

### Android
- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Dispositivo Android con API 24+ o emulador

---

## 🔧 Parte 1: Configuración del Backend (PHP + MySQL)

### 1.1 Instalar XAMPP

1. Descarga XAMPP desde: https://www.apachefriends.org/
2. Instala XAMPP en tu PC
3. Inicia Apache y MySQL desde el panel de control de XAMPP

### 1.2 Crear la Base de Datos

1. Abre tu navegador y ve a: `http://localhost/phpmyadmin`
2. Haz clic en "Nueva" para crear una nueva base de datos
3. Abre el archivo `backend_php/database.sql` de este proyecto
4. Copia todo el contenido del archivo
5. En phpMyAdmin, ve a la pestaña "SQL"
6. Pega el contenido copiado y haz clic en "Continuar"

Esto creará:
- Base de datos: `contactos_db`
- Tabla: `usuarios` (con 2 usuarios de prueba)
- Tabla: `contactos` (con 3 contactos de ejemplo)

### 1.3 Configurar los Archivos PHP

1. Copia la carpeta `backend_php` a la carpeta `htdocs` de XAMPP
   - Ruta típica en Windows: `C:\xampp\htdocs\backend_php`
   - Ruta típica en Mac: `/Applications/XAMPP/htdocs/backend_php`

2. Abre el archivo `backend_php/config.php` y verifica la configuración:
   ```php
   $host = "localhost";
   $usuario_db = "root";        // Tu usuario de MySQL
   $password_db = "";           // Tu contraseña de MySQL (generalmente vacía)
   $nombre_db = "contactos_db";
   ```

3. Verifica que los archivos PHP estén accesibles:
   - Abre tu navegador
   - Ve a: `http://localhost/backend_php/config.php`
   - No deberías ver errores

### 1.4 Usuarios de Prueba

El script SQL crea dos usuarios para pruebas:

| Usuario   | Contraseña |
|-----------|------------|
| admin     | 12345678   |
| usuario1  | 12345678   |

---

## 📱 Parte 2: Configuración de la Aplicación Android

### 2.1 Abrir el Proyecto

1. Abre Android Studio
2. Abre este proyecto
3. Espera a que se sincronicen las dependencias de Gradle

### 2.2 Configurar la URL del Servidor

**IMPORTANTE:** Debes configurar la URL correcta según tu caso.

Abre el archivo:
```
app/src/main/java/com/example/myapplicasion/api/ContactosApiService.kt
```

Busca la línea:
```kotlin
private val baseUrl = "http://10.0.2.2/backend_php/"
```

Cambia la URL según tu caso:

#### Si usas el EMULADOR de Android Studio:
```kotlin
private val baseUrl = "http://10.0.2.2/backend_php/"
```
(La IP 10.0.2.2 representa localhost en el emulador)

#### Si usas un DISPOSITIVO FÍSICO conectado a la misma red WiFi:
1. Encuentra tu IP local:
   - Windows: Abre CMD y ejecuta `ipconfig`, busca "Dirección IPv4"
   - Mac/Linux: Abre Terminal y ejecuta `ifconfig`, busca "inet"

2. Usa esa IP, ejemplo:
```kotlin
private val baseUrl = "http://192.168.1.100/backend_php/"
```

### 2.3 Ejecutar la Aplicación

1. Conecta un dispositivo Android o inicia un emulador
2. Haz clic en el botón "Run" (▶️) en Android Studio
3. Espera a que la app se instale y se abra

---

## 🚀 Parte 3: Usar la Aplicación

### 3.1 Acceder al Sistema de Contactos

1. En el menú principal de la app, busca el botón **"Contactos PHP"** (Botón 18)
2. Haz clic en él

### 3.2 Iniciar Sesión

1. Usa las credenciales de prueba:
   - **Usuario:** admin
   - **Contraseña:** 12345678

2. Haz clic en "Iniciar Sesión"

### 3.3 Menú de Contactos

Verás dos opciones:

#### 📝 Registro de Contactos
- Crear nuevos contactos
- Editar contactos existentes
- Eliminar contactos
- Agregar fotos a los contactos

#### 📋 Ver Contactos
- Ver lista de todos tus contactos
- Hacer clic en un contacto para editarlo

### 3.4 Crear un Nuevo Contacto

1. Ve a "Registro de Contactos"
2. Haz clic en "Seleccionar Imagen" para agregar una foto (opcional)
3. Llena los campos:
   - **Código:** Autocompletado (puedes cambiarlo)
   - **Nombre:** Requerido
   - **Dirección:** Opcional
   - **Teléfono:** Requerido
   - **Correo:** Opcional (debe ser válido)
4. Haz clic en "Guardar"

### 3.5 Editar o Eliminar un Contacto

1. Ve a "Ver Contactos"
2. Haz clic en el contacto que deseas editar
3. Se abrirá el formulario con los datos cargados
4. Modifica los campos necesarios
5. Haz clic en:
   - **"Actualizar"** para guardar los cambios
   - **"Eliminar"** para eliminar el contacto (pide confirmación)
   - **"Nuevo"** para limpiar el formulario

---

## 🔍 Solución de Problemas

### Problema: "Error de conexión" en la app

**Solución:**
1. Verifica que Apache y MySQL estén corriendo en XAMPP
2. Verifica que la URL en `ContactosApiService.kt` sea correcta
3. Si usas dispositivo físico, asegúrate de estar en la misma red WiFi
4. Prueba acceder desde el navegador del dispositivo a: `http://TU_IP/backend_php/config.php`

### Problema: "Usuario no encontrado"

**Solución:**
1. Verifica que la base de datos se haya creado correctamente
2. Abre phpMyAdmin y verifica que la tabla `usuarios` tenga datos
3. Asegúrate de usar: admin / 12345678

### Problema: "No se pueden cargar las imágenes"

**Solución:**
1. Verifica que la carpeta `backend_php/imagenes_contactos/` exista
2. Dale permisos de escritura a esa carpeta
3. En Windows: Clic derecho → Propiedades → Seguridad → Editar

### Problema: La app no se conecta desde dispositivo físico

**Solución:**
1. Verifica que el firewall de Windows permita conexiones al puerto 80
2. En XAMPP, configura Apache para permitir conexiones externas
3. Asegúrate de que ambos (PC y dispositivo) estén en la misma red WiFi

---

## 📂 Estructura del Proyecto

```
MyApplicasion/
├── backend_php/                          # Backend PHP
│   ├── config.php                        # Configuración BD
│   ├── database.sql                      # Script de BD
│   ├── login.php                         # Login
│   ├── obtener_contactos.php            # Obtener contactos
│   ├── crear_contacto.php               # Crear contacto
│   ├── actualizar_contacto.php          # Actualizar contacto
│   ├── eliminar_contacto.php            # Eliminar contacto
│   └── imagenes_contactos/              # Carpeta de imágenes
│
└── app/src/main/
    ├── java/com/example/myapplicasion/
    │   ├── api/
    │   │   └── ContactosApiService.kt   # Servicio API
    │   ├── models/
    │   │   ├── Usuario.kt               # Modelo Usuario
    │   │   ├── Contacto.kt              # Modelo Contacto
    │   │   └── ApiResponse.kt           # Modelo Respuesta
    │   ├── adapters/
    │   │   └── ContactosAdapter.kt      # Adapter RecyclerView
    │   ├── LoginContactosActivity.kt    # Login
    │   ├── MenuContactosActivity.kt     # Menú principal
    │   ├── RegistroContactoActivity.kt  # CRUD contactos
    │   └── VisorContactosActivity.kt    # Lista contactos
    │
    └── res/layout/
        ├── activity_login_contactos.xml
        ├── activity_menu_contactos.xml
        ├── activity_registro_contacto.xml
        ├── activity_visor_contactos.xml
        └── item_contacto.xml
```

---

## 🎯 Características Implementadas

✅ Sistema de login con usuarios
✅ CRUD completo de contactos
✅ Subida de imágenes
✅ Validación de formularios
✅ Manejo de sesiones
✅ Lista de contactos con RecyclerView
✅ Diseño moderno con Material Design
✅ Gestión de errores

---

## 📝 Notas Adicionales

### Seguridad
- Las contraseñas están hasheadas con `password_hash()` de PHP
- Los archivos PHP usan prepared statements para prevenir SQL injection
- El sistema valida todos los campos antes de guardar

### Extensibilidad
- Puedes agregar más campos a los contactos modificando:
  1. La tabla en `database.sql`
  2. El modelo `Contacto.kt`
  3. Los archivos PHP
  4. Los layouts XML

### Base de Datos
- Para crear más usuarios, usa este código PHP:
```php
$password = password_hash("tu_password", PASSWORD_DEFAULT);
// Inserta en la tabla usuarios con el password hasheado
```

---

## 🆘 Soporte

Si tienes problemas:
1. Revisa la sección "Solución de Problemas"
2. Verifica los logs de Apache en `xampp/apache/logs/error.log`
3. Usa Logcat en Android Studio para ver errores de la app

---

## ✅ Checklist de Instalación

- [ ] XAMPP instalado y corriendo
- [ ] Base de datos creada con el script SQL
- [ ] Archivos PHP copiados a htdocs
- [ ] Carpeta imagenes_contactos/ creada con permisos
- [ ] URL configurada en ContactosApiService.kt
- [ ] App compilada sin errores
- [ ] Login exitoso con admin/12345678
- [ ] Contactos de prueba visibles
- [ ] Creación de nuevo contacto funciona
- [ ] Edición de contacto funciona
- [ ] Eliminación de contacto funciona

¡Listo! Ya tienes tu sistema de gestión de contactos funcionando.
