-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: db_veterinaria
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- ------------------------------------------------------
-- Eliminación de tablas en orden inverso para evitar conflictos de FK
-- ------------------------------------------------------
DROP TABLE IF EXISTS `citas`;
DROP TABLE IF EXISTS `productos`;
DROP TABLE IF EXISTS `mascotas`;
DROP TABLE IF EXISTS `clientes`;
DROP TABLE IF EXISTS `usuarios`;

--
-- Table structure for table `clientes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `dni` varchar(10) DEFAULT NULL,
  `telefono` varchar(10) DEFAULT NULL,
  `correo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Rous','Guevara','73679000','942654111','rousguevara@gmail.com'),(2,'Juan','Aguilar Rodriguez','15437811','934222177','juanaguilar@gmail.com');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mascotas`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mascotas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `especie` varchar(30) DEFAULT NULL,
  `raza` varchar(50) DEFAULT NULL,
  `dueno` varchar(100) DEFAULT NULL,
  `cliente_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mascotas_cliente_idx` (`cliente_id`),
  CONSTRAINT `fk_mascotas_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mascotas`
--

LOCK TABLES `mascotas` WRITE;
/*!40000 ALTER TABLE `mascotas` DISABLE KEYS */;
INSERT INTO `mascotas` VALUES (1,'Moti','Perro','Bichón Maltés','Rous Guevara',1),(2,'Bobby','Gato','Persa','Juan Aguilar Rodriguez',2);
/*!40000 ALTER TABLE `mascotas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `estado` tinyint(4) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `correo` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'admin@huellitasfelices.com','$2a$10$T8rM53rXretRVraI9HEVO.67bSLRbaPpP/W1paPNgx.PawlDFYXOy','ADMIN');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `citas`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente` varchar(100) NOT NULL,
  `mascota` varchar(100) NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `cliente_id` int(11) DEFAULT NULL,
  `mascota_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_citas_cliente_idx` (`cliente_id`),
  KEY `fk_citas_mascota_idx` (`mascota_id`),
  CONSTRAINT `fk_citas_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_citas_mascotas` FOREIGN KEY (`mascota_id`) REFERENCES `mascotas` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-28  0:33:40
