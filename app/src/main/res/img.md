# Instrucciones para Aplicación de Contactos

## 1. BASE DE DATOS (PHPMyAdmin)

### Tabla: usuarios
Crea una tabla para almacenar los usuarios del sistema con estos campos:
- id_usuario (entero, autoincremental, clave primaria)
- nombre_usuario (texto, único)
- contraseña (texto, encriptado con hash)
- fecha_registro (fecha y hora)

### Tabla: contactos
Crea una tabla para almacenar los contactos con estos campos:
- id_contacto (entero, autoincremental, clave primaria)
- codigo (texto o entero único)
- nombre (texto)
- direccion (texto)
- telefono (texto)
- correo (texto)
- ruta_imagen (texto - guarda la ruta donde se almacenará la imagen)
- id_usuario (entero, clave foránea que referencia a usuarios.id_usuario)

**Relación:** La tabla contactos debe tener una relación de muchos a uno con usuarios mediante id_usuario. Esto asegura que cada contacto pertenezca a un usuario específico.

---

## 2. BACKEND / API (PHP)

Necesitas crear varios archivos PHP que funcionen como servicios web:

### login.php
- Recibe usuario y contraseña desde la app
- Busca en la tabla usuarios si existe ese usuario
- Verifica que la contraseña coincida (compara el hash)
- Si es correcto, devuelve un mensaje de éxito junto con el id_usuario
- Si es incorrecto, devuelve un mensaje de error

### obtener_contactos.php
- Recibe el id_usuario desde la app
- Consulta todos los contactos de la tabla contactos donde id_usuario coincida
- Devuelve la lista de contactos en formato JSON

### crear_contacto.php
- Recibe todos los datos del contacto más el id_usuario
- Recibe la imagen en formato Base64 o como archivo
- Guarda la imagen en una carpeta del servidor y obtiene su ruta
- Inserta un nuevo registro en la tabla contactos con todos los datos
- Devuelve mensaje de éxito o error

### actualizar_contacto.php
- Recibe el id_contacto y los nuevos datos
- Si hay una nueva imagen, guarda la nueva y elimina la anterior
- Actualiza el registro en la tabla contactos
- Devuelve mensaje de éxito o error

### eliminar_contacto.php
- Recibe el id_contacto
- Elimina la imagen del servidor si existe
- Elimina el registro de la tabla contactos
- Devuelve mensaje de éxito o error

---

## 3. INTERFAZ DE LOGIN

### Diseño visual:
- Coloca un logo o título de la aplicación en la parte superior
- Crea un campo de texto para "Usuario" con un icono de persona al lado izquierdo
- Crea un campo de texto para "Contraseña" (con texto oculto) con un icono de candado
- Coloca un botón grande "Iniciar Sesión" con icono de flecha o check
- Usa colores modernos: fondo claro o degradado, campos con bordes redondeados
- Añade un pequeño texto debajo por si el usuario quiere registrarse (opcional)

### Funcionalidad:
- Al presionar "Iniciar Sesión", valida que ambos campos no estén vacíos
- Envía los datos al archivo login.php
- Si la respuesta es exitosa, guarda el id_usuario en las preferencias o sesión de la app
- Navega a la pantalla del Menú
- Si hay error, muestra un mensaje de error con Toast o AlertDialog

---

## 4. INTERFAZ DE MENÚ

### Diseño visual:
- En la parte superior muestra un encabezado con el nombre del usuario logueado
- Muestra un saludo personalizado con icono de usuario
- Crea dos botones grandes y atractivos:

  **Botón 1: "Registro de Contactos"**
    - Icono de persona con signo "+"
    - Color distintivo (por ejemplo, azul)
    - Texto descriptivo debajo: "Agregar y gestionar contactos"

  **Botón 2: "Ver Contactos"**
    - Icono de lista o libreta
    - Color distintivo (por ejemplo, verde)
    - Texto descriptivo debajo: "Visualizar todos mis contactos"

- Opcional: botón de cerrar sesión en la parte superior derecha

### Funcionalidad:
- Al presionar "Registro de Contactos", navega a la pantalla de Registro
- Al presionar "Ver Contactos", navega a la pantalla del Visor
- Si hay botón de cerrar sesión, limpia las preferencias y regresa al Login

---

## 5. INTERFAZ DE REGISTRO

### Diseño visual:
- Título en la parte superior: "Gestión de Contactos"
- En el centro superior, coloca un ImageView circular grande para la foto del contacto
- Dentro o al lado del ImageView, un botón pequeño con icono de cámara para seleccionar imagen
- Debajo del ImageView, crea campos de texto para:
    - Código (puede ser autocompletado o manual)
    - Nombre (con icono de persona)
    - Dirección (con icono de casa/ubicación)
    - Teléfono (con icono de teléfono, formato numérico)
    - Correo (con icono de email, validación de formato)
- Todos los campos con bordes redondeados y iconos a la izquierda
- En la parte inferior, crea una barra con 4 botones:
    - **Nuevo:** icono "+" (limpia todos los campos)
    - **Guardar:** icono de diskette o check (guarda/crea el contacto)
    - **Actualizar:** icono de lápiz (actualiza contacto existente)
    - **Eliminar:** icono de basurero (elimina el contacto)

### Funcionalidad:

**Para el ImageView:**
- Al presionar el botón de cámara, abre un selector para elegir imagen de galería o cámara
- Una vez seleccionada, muestra la imagen en el ImageView
- Convierte la imagen a Base64 o prepárala para enviar al servidor

**Botón Nuevo:**
- Limpia todos los campos
- Limpia el ImageView (pone imagen por defecto)
- Prepara el formulario para crear un nuevo contacto
- Genera un nuevo código automático o deja que el usuario lo ingrese

**Botón Guardar:**
- Valida que todos los campos obligatorios estén llenos
- Valida el formato del correo y teléfono
- Envía los datos junto con la imagen y el id_usuario al archivo crear_contacto.php
- Muestra mensaje de éxito o error
- Si es exitoso, limpia el formulario

**Botón Actualizar:**
- Solo se habilita cuando se ha seleccionado un contacto existente (desde el Visor)
- Envía los datos modificados al archivo actualizar_contacto.php
- Muestra mensaje de confirmación

**Botón Eliminar:**
- Solo se habilita cuando se ha seleccionado un contacto existente
- Muestra un diálogo de confirmación preguntando si está seguro
- Si confirma, envía el id_contacto al archivo eliminar_contacto.php
- Muestra mensaje de éxito y limpia el formulario

---

## 6. INTERFAZ DE VISOR

### Diseño visual:
- Título en la parte superior: "Mis Contactos"
- Crea un ListView o RecyclerView que muestre todos los contactos
- Cada elemento de la lista debe mostrar:
    - Imagen del contacto (circular) a la izquierda
    - Nombre del contacto en grande
    - Teléfono en texto más pequeño debajo del nombre
    - Icono de correo con el email
    - Una flecha o icono a la derecha indicando que es seleccionable
- Usa un diseño tipo card (tarjeta) para cada contacto con sombras y bordes redondeados
- Si no hay contactos, muestra un mensaje centrado: "No tienes contactos registrados"

### Funcionalidad:
- Al abrir esta pantalla, automáticamente carga todos los contactos
- Envía el id_usuario al archivo obtener_contactos.php
- Recibe la lista de contactos y los muestra en el RecyclerView
- Al hacer clic en un contacto:
    - Guarda los datos del contacto seleccionado
    - Navega de regreso a la pantalla de Registro
    - Auto-completa todos los campos con los datos del contacto seleccionado
    - Habilita los botones de Actualizar y Eliminar
- Opcional: añade un botón flotante (+) en la esquina inferior derecha para crear nuevo contacto (navega a Registro con campos vacíos)

---

## 7. ESTILOS Y DISEÑO GENERAL

### Paleta de colores:
- Define un color primario (ejemplo: azul #2196F3)
- Define un color secundario (ejemplo: verde #4CAF50)
- Define un color de acento (ejemplo: naranja #FF9800)
- Usa fondos claros o degradados suaves
- Usa texto oscuro para contraste

### Iconos:
- Usa una librería de iconos como Material Icons o Font Awesome
- Todos los botones deben tener iconos descriptivos
- Los campos de texto deben tener iconos a la izquierda

### Componentes:
- Todos los botones con bordes redondeados
- Campos de texto con bordes sutiles y redondeados
- Cards con elevación (sombras) para la lista de contactos
- Imágenes de contactos siempre circulares
- Animaciones suaves al navegar entre pantallas

### Responsive:
- Los elementos deben adaptarse a diferentes tamaños de pantalla
- Usa márgenes y padding consistentes
- Las imágenes deben mantener proporción

---

## 8. FLUJO GENERAL DE LA APLICACIÓN

1. Usuario abre la app → ve el Login
2. Ingresa credenciales → valida con la base de datos
3. Si es correcto → guarda id_usuario y va al Menú
4. En el Menú puede elegir:
    - **Registro:** Para crear/editar/eliminar contactos
    - **Visor:** Para ver la lista de todos sus contactos
5. En Registro puede:
    - Crear nuevos contactos
    - Si viene desde Visor con un contacto seleccionado, puede actualizarlo o eliminarlo
6. En Visor puede:
    - Ver todos sus contactos
    - Seleccionar uno para editarlo (regresa a Registro con datos cargados)
7. Todos los cambios se reflejan en la base de datos vinculados al usuario logueado

---

¿Quieres que profundice en alguna sección específica o necesitas más detalles sobre alguna parte?