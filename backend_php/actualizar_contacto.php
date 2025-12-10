<?php
require_once 'config.php';

// Obtener datos
$json = file_get_contents('php://input');
$data = json_decode($json, true);

$id_contacto = isset($data['id_contacto']) ? $data['id_contacto'] : '';
$codigo = isset($data['codigo']) ? $data['codigo'] : '';
$nombre = isset($data['nombre']) ? $data['nombre'] : '';
$direccion = isset($data['direccion']) ? $data['direccion'] : '';
$telefono = isset($data['telefono']) ? $data['telefono'] : '';
$correo = isset($data['correo']) ? $data['correo'] : '';
$imagen_base64 = isset($data['imagen']) ? $data['imagen'] : '';

// Validar campos requeridos
if (empty($id_contacto) || empty($nombre) || empty($telefono)) {
    echo json_encode([
        'success' => false,
        'message' => 'Campos requeridos: id_contacto, nombre, telefono'
    ]);
    exit;
}

// Obtener ruta de imagen anterior
$stmt = $conexion->prepare("SELECT ruta_imagen FROM contactos WHERE id_contacto = ?");
$stmt->bind_param("i", $id_contacto);
$stmt->execute();
$resultado = $stmt->get_result();
$contacto_anterior = $resultado->fetch_assoc();
$ruta_imagen = $contacto_anterior['ruta_imagen'];

// Procesar nueva imagen si existe
if (!empty($imagen_base64)) {
    // Eliminar imagen anterior si existe
    if (!empty($ruta_imagen) && file_exists($ruta_imagen)) {
        unlink($ruta_imagen);
    }

    // Crear directorio si no existe
    $directorio = 'imagenes_contactos/';
    if (!file_exists($directorio)) {
        mkdir($directorio, 0777, true);
    }

    // Guardar nueva imagen
    $imagen_data = base64_decode($imagen_base64);
    $nombre_archivo = 'contacto_' . time() . '_' . uniqid() . '.jpg';
    $ruta_completa = $directorio . $nombre_archivo;

    if (file_put_contents($ruta_completa, $imagen_data)) {
        $ruta_imagen = $ruta_completa;
    }
}

// Actualizar contacto
$stmt = $conexion->prepare("UPDATE contactos SET codigo = ?, nombre = ?, direccion = ?, telefono = ?, correo = ?, ruta_imagen = ? WHERE id_contacto = ?");
$stmt->bind_param("ssssssi", $codigo, $nombre, $direccion, $telefono, $correo, $ruta_imagen, $id_contacto);

if ($stmt->execute()) {
    echo json_encode([
        'success' => true,
        'message' => 'Contacto actualizado exitosamente'
    ]);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Error al actualizar contacto: ' . $stmt->error
    ]);
}

$stmt->close();
$conexion->close();
?>
