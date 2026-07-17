-- ============================================================
-- HUELLITAS FELICES - BASE DE DATOS PORTABLE
-- Compatible con MySQL 8.x y MariaDB 10.4+
-- ============================================================

CREATE DATABASE IF NOT EXISTS `db_veterinaria`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE `db_veterinaria`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `citas`;
DROP TABLE IF EXISTS `mascotas`;
DROP TABLE IF EXISTS `productos`;
DROP TABLE IF EXISTS `clientes`;
DROP TABLE IF EXISTS `usuarios`;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- TABLA: usuarios
-- ============================================================

CREATE TABLE `usuarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `correo` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `rol` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuarios_correo` (`correo`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;

-- Credenciales iniciales:
-- Correo: admin@huellitasfelices.com
-- Contraseña: admin123
INSERT INTO `usuarios` (`correo`, `password`, `rol`) VALUES
(
  'admin@huellitasfelices.com',
  '$2a$10$T8rM53rXretRVraI9HEVO.67bSLRbaPpP/W1paPNgx.PawlDFYXOy',
  'ADMIN'
);

-- ============================================================
-- TABLA: clientes
-- ============================================================

CREATE TABLE `clientes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `apellido` VARCHAR(50) NOT NULL,
  `dni` VARCHAR(10) DEFAULT NULL,
  `telefono` VARCHAR(10) DEFAULT NULL,
  `correo` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_clientes_dni` (`dni`),
  UNIQUE KEY `uk_clientes_correo` (`correo`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;

-- Datos ficticios de demostración
INSERT INTO `clientes`
(`nombre`, `apellido`, `dni`, `telefono`, `correo`) VALUES
('María', 'López', '12345678', '987654321', 'maria.lopez@example.com'),
('Carlos', 'Ramírez', '87654321', '912345678', 'carlos.ramirez@example.com');

-- ============================================================
-- TABLA: mascotas
-- ============================================================

CREATE TABLE `mascotas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `especie` VARCHAR(30) DEFAULT NULL,
  `raza` VARCHAR(50) DEFAULT NULL,
  `dueno` VARCHAR(100) DEFAULT NULL,
  `cliente_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mascotas_cliente_idx` (`cliente_id`),
  CONSTRAINT `fk_mascotas_cliente`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `clientes` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;

-- Datos ficticios de demostración
INSERT INTO `mascotas`
(`nombre`, `especie`, `raza`, `dueno`, `cliente_id`) VALUES
('Max', 'Perro', 'Labrador', 'María López', 1),
('Luna', 'Gato', 'Siamés', 'Carlos Ramírez', 2);

-- ============================================================
-- TABLA: productos
-- ============================================================

CREATE TABLE `productos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `estado` TINYINT NOT NULL DEFAULT 1,
  `precio` DECIMAL(10,2) NOT NULL,
  `stock` INT NOT NULL,
  `categoria` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `chk_productos_estado` CHECK (`estado` IN (0,1)),
  CONSTRAINT `chk_productos_precio` CHECK (`precio` >= 0),
  CONSTRAINT `chk_productos_stock` CHECK (`stock` >= 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;

-- Datos ficticios de demostración
INSERT INTO `productos`
(`nombre`, `estado`, `precio`, `stock`, `categoria`) VALUES
('Shampoo antipulgas', 1, 25.90, 15, 'Higiene'),
('Alimento premium para perro', 1, 89.50, 10, 'Alimentos'),
('Collar ajustable', 1, 18.00, 20, 'Accesorios');

-- ============================================================
-- TABLA: citas
-- ============================================================

CREATE TABLE `citas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cliente` VARCHAR(100) NOT NULL,
  `mascota` VARCHAR(100) NOT NULL,
  `fecha` DATE NOT NULL,
  `hora` TIME NOT NULL,
  `motivo` VARCHAR(255) DEFAULT NULL,
  `cliente_id` INT DEFAULT NULL,
  `mascota_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_citas_cliente_idx` (`cliente_id`),
  KEY `fk_citas_mascota_idx` (`mascota_id`),
  CONSTRAINT `fk_citas_cliente`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `clientes` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_mascota`
    FOREIGN KEY (`mascota_id`)
    REFERENCES `mascotas` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;

-- Datos ficticios de demostración
INSERT INTO `citas`
(`cliente`, `mascota`, `fecha`, `hora`, `motivo`, `cliente_id`, `mascota_id`) VALUES
('María López', 'Max', '2026-07-20', '10:00:00', 'Consulta general', 1, 1),
('Carlos Ramírez', 'Luna', '2026-07-21', '15:30:00', 'Vacunación anual', 2, 2);

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================
