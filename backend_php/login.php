<?php
require_once 'config.php';

// Obtener datos del POST
$json = file_get_contents('php://input');
$data = json_decode($json, true);

$nombre_usuario = isset($data['nombre_usuario']) ? $data['nombre_usuario'] : '';
$contrasena = isset($data['contrasena']) ? $data['contrasena'] : '';

// Validar que los campos no estén vacíos
if (empty($nombre_usuario) || empty($contrasena)) {
    echo json_encode([
        'success' => false,
        'message' => 'Usuario y contraseña son requeridos'
    ]);
    exit;
}

// Preparar consulta
$stmt = $conexion->prepare("SELECT id_usuario, nombre_usuario, contrasena FROM usuarios WHERE nombre_usuario = ?");
$stmt->bind_param("s", $nombre_usuario);
$stmt->execute();
$resultado = $stmt->get_result();

if ($resultado->num_rows > 0) {
    $usuario = $resultado->fetch_assoc();

    // Verificar contraseña
    if (password_verify($contrasena, $usuario['contrasena'])) {
        echo json_encode([
            'success' => true,
            'message' => 'Login exitoso',
            'id_usuario' => $usuario['id_usuario'],
            'nombre_usuario' => $usuario['nombre_usuario']
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => 'Contraseña incorrecta'
        ]);
    }
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Usuario no encontrado'
    ]);
}

$stmt->close();
$conexion->close();
?>
