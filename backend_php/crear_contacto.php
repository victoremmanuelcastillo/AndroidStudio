<?php
require_once 'config.php';

// Obtener datos
$json = file_get_contents('php://input');
$data = json_decode($json, true);

$id_usuario = isset($data['id_usuario']) ? $data['id_usuario'] : '';
$codigo = isset($data['codigo']) ? $data['codigo'] : '';
$nombre = isset($data['nombre']) ? $data['nombre'] : '';
$direccion = isset($data['direccion']) ? $data['direccion'] : '';
$telefono = isset($data['telefono']) ? $data['telefono'] : '';
$correo = isset($data['correo']) ? $data['correo'] : '';
$imagen_base64 = isset($data['imagen']) ? $data['imagen'] : '';

// Validar campos requeridos
if (empty($id_usuario) || empty($nombre) || empty($telefono)) {
    echo json_encode([
        'success' => false,
        'message' => 'Campos requeridos: id_usuario, nombre, telefono'
    ]);
    exit;
}

// Procesar imagen si existe
$ruta_imagen = '';
if (!empty($imagen_base64)) {
    // Crear directorio si no existe
    $directorio = 'imagenes_contactos/';
    if (!file_exists($directorio)) {
        mkdir($directorio, 0777, true);
    }

    // Decodificar imagen
    $imagen_data = base64_decode($imagen_base64);
    $nombre_archivo = 'contacto_' . time() . '_' . uniqid() . '.jpg';
    $ruta_completa = $directorio . $nombre_archivo;

    if (file_put_contents($ruta_completa, $imagen_data)) {
        $ruta_imagen = $ruta_completa;
    }
}

// Insertar contacto
$stmt = $conexion->prepare("INSERT INTO contactos (codigo, nombre, direccion, telefono, correo, ruta_imagen, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssssi", $codigo, $nombre, $direccion, $telefono, $correo, $ruta_imagen, $id_usuario);

if ($stmt->execute()) {
    echo json_encode([
        'success' => true,
        'message' => 'Contacto creado exitosamente',
        'id_contacto' => $stmt->insert_id
    ]);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Error al crear contacto: ' . $stmt->error
    ]);
}

$stmt->close();
$conexion->close();
?>
