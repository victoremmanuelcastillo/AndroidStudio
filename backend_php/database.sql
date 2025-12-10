-- Script para crear la base de datos y tablas

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS contactos_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE contactos_db;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_nombre_usuario (nombre_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de contactos
CREATE TABLE IF NOT EXISTS contactos (
    id_contacto INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50),
    nombre VARCHAR(100) NOT NULL,
    direccion TEXT,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(100),
    ruta_imagen TEXT,
    id_usuario INT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    INDEX idx_id_usuario (id_usuario),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar usuarios de prueba (contraseña: "12345678" para ambos)
INSERT INTO usuarios (nombre_usuario, contrasena) VALUES
('admin', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
('usuario1', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi');

-- Insertar contactos de ejemplo para admin (id_usuario = 1)
INSERT INTO contactos (codigo, nombre, direccion, telefono, correo, id_usuario) VALUES
('C001', 'Juan Pérez', 'Calle Principal 123', '555-0001', 'juan@email.com', 1),
('C002', 'María García', 'Avenida Central 456', '555-0002', 'maria@email.com', 1),
('C003', 'Carlos López', 'Boulevard Norte 789', '555-0003', 'carlos@email.com', 1);

-- Mostrar las tablas creadas
SHOW TABLES;
