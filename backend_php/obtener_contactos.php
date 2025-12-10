<?php
require_once 'config.php';

// Obtener id_usuario
$json = file_get_contents('php://input');
$data = json_decode($json, true);

$id_usuario = isset($data['id_usuario']) ? $data['id_usuario'] : '';

if (empty($id_usuario)) {
    echo json_encode([
        'success' => false,
        'message' => 'ID de usuario requerido'
    ]);
    exit;
}

// Consultar contactos del usuario
$stmt = $conexion->prepare("SELECT id_contacto, codigo, nombre, direccion, telefono, correo, ruta_imagen FROM contactos WHERE id_usuario = ? ORDER BY nombre ASC");
$stmt->bind_param("i", $id_usuario);
$stmt->execute();
$resultado = $stmt->get_result();

$contactos = [];
while ($fila = $resultado->fetch_assoc()) {
    $contactos[] = $fila;
}

echo json_encode([
    'success' => true,
    'message' => 'Contactos obtenidos correctamente',
    'contactos' => $contactos
]);

$stmt->close();
$conexion->close();
?>
