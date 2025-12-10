<?php
require_once 'config.php';

// Obtener datos
$json = file_get_contents('php://input');
$data = json_decode($json, true);

$id_contacto = isset($data['id_contacto']) ? $data['id_contacto'] : '';

// Validar campo requerido
if (empty($id_contacto)) {
    echo json_encode([
        'success' => false,
        'message' => 'ID de contacto requerido'
    ]);
    exit;
}

// Obtener ruta de imagen
$stmt = $conexion->prepare("SELECT ruta_imagen FROM contactos WHERE id_contacto = ?");
$stmt->bind_param("i", $id_contacto);
$stmt->execute();
$resultado = $stmt->get_result();

if ($resultado->num_rows > 0) {
    $contacto = $resultado->fetch_assoc();

    // Eliminar imagen si existe
    if (!empty($contacto['ruta_imagen']) && file_exists($contacto['ruta_imagen'])) {
        unlink($contacto['ruta_imagen']);
    }

    // Eliminar contacto
    $stmt = $conexion->prepare("DELETE FROM contactos WHERE id_contacto = ?");
    $stmt->bind_param("i", $id_contacto);

    if ($stmt->execute()) {
        echo json_encode([
            'success' => true,
            'message' => 'Contacto eliminado exitosamente'
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => 'Error al eliminar contacto: ' . $stmt->error
        ]);
    }
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Contacto no encontrado'
    ]);
}

$stmt->close();
$conexion->close();
?>
