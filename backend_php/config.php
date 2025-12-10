<?php
// Configuración de la base de datos
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE');
header('Access-Control-Allow-Headers: Content-Type');

// Configuración de la conexión
$host = "localhost";
$usuario_db = "root"; // Cambiar según tu configuración
$password_db = ""; // Cambiar según tu configuración
$nombre_db = "contactos_db";

// Crear conexión
$conexion = new mysqli($host, $usuario_db, $password_db, $nombre_db);

// Verificar conexión
if ($conexion->connect_error) {
    die(json_encode([
        'success' => false,
        'message' => 'Error de conexión: ' . $conexion->connect_error
    ]));
}

// Establecer charset
$conexion->set_charset("utf8");
?>
