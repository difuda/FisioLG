CREATE DATABASE  IF NOT EXISTS `fisiolg` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `fisiolg`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: fisiolg
-- ------------------------------------------------------
-- Server version	8.0.40

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

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_hora` datetime NOT NULL,
  `estado_id` bigint NOT NULL,
  `fisio_id` bigint DEFAULT NULL,
  `servicio_id` bigint DEFAULT NULL,
  `paciente_id` bigint DEFAULT NULL,
  `recordatorio_enviado` bit(1) DEFAULT b'0',
  `notas_clinicas` text,
  `estado` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_citas_estado` (`estado_id`),
  KEY `fk_citas_fisio` (`fisio_id`),
  KEY `fk_citas_servicio` (`servicio_id`),
  CONSTRAINT `fk_citas_estado` FOREIGN KEY (`estado_id`) REFERENCES `estados_cita` (`id`),
  CONSTRAINT `fk_citas_fisio` FOREIGN KEY (`fisio_id`) REFERENCES `fisio` (`id`),
  CONSTRAINT `fk_citas_servicio` FOREIGN KEY (`servicio_id`) REFERENCES `servicios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `citas`
--

LOCK TABLES `citas` WRITE;
/*!40000 ALTER TABLE `citas` DISABLE KEYS */;
INSERT INTO `citas` VALUES (1,'2026-03-09 14:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(2,'2026-03-09 14:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(3,'2026-03-09 15:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(4,'2026-03-09 16:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(5,'2026-03-09 16:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(6,'2026-03-09 17:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(7,'2026-03-09 18:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(8,'2026-03-09 18:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(9,'2026-03-09 19:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(10,'2026-03-09 08:00:00',1,2,NULL,1,_binary '\0',NULL,'LIBRE'),(11,'2026-03-09 08:40:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(12,'2026-03-09 09:20:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(13,'2026-03-09 10:00:00',1,2,NULL,1000,_binary '\0',NULL,'LIBRE'),(14,'2026-03-09 10:40:00',1,2,NULL,1,_binary '\0',NULL,'LIBRE'),(15,'2026-03-09 11:20:00',1,2,NULL,1,_binary '\0',NULL,'LIBRE'),(16,'2026-03-09 14:30:00',1,2,NULL,1,_binary '\0',NULL,'LIBRE'),(17,'2026-03-09 15:10:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(18,'2026-03-09 15:50:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(19,'2026-03-09 16:30:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(20,'2026-03-09 17:10:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(21,'2026-03-09 17:50:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(22,'2026-03-09 18:30:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(23,'2026-03-05 14:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(24,'2026-03-05 14:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(25,'2026-03-05 15:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(26,'2026-03-05 16:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(27,'2026-03-05 16:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(28,'2026-03-05 17:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(29,'2026-03-05 18:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(30,'2026-03-05 18:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(31,'2026-03-05 19:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(32,'2026-03-05 08:00:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(33,'2026-03-05 08:40:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(34,'2026-03-05 09:20:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(35,'2026-03-05 10:00:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(36,'2026-03-05 10:40:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(37,'2026-03-05 11:20:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(38,'2026-03-05 14:30:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(39,'2026-03-05 15:10:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(40,'2026-03-05 15:50:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(41,'2026-03-05 16:30:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(42,'2026-03-05 17:10:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(43,'2026-03-05 17:50:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(44,'2026-03-05 18:30:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(45,'2026-03-25 08:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(46,'2026-03-25 08:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(47,'2026-03-25 09:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(48,'2026-03-25 10:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(49,'2026-03-25 10:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(50,'2026-03-25 11:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(51,'2026-03-25 12:00:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(52,'2026-03-25 12:40:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(53,'2026-03-25 13:20:00',1,1,NULL,NULL,_binary '\0',NULL,'LIBRE'),(54,'2026-03-25 08:00:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(55,'2026-03-25 08:40:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(56,'2026-03-25 09:20:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(57,'2026-03-25 10:00:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(58,'2026-03-25 10:40:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(59,'2026-03-25 11:20:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(60,'2026-03-25 14:30:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(61,'2026-03-25 15:10:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(62,'2026-03-25 15:50:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(63,'2026-03-25 16:30:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(64,'2026-03-25 17:10:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(65,'2026-03-25 17:50:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE'),(66,'2026-03-25 18:30:00',1,2,NULL,NULL,_binary '\0',NULL,'LIBRE');
/*!40000 ALTER TABLE `citas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disponibilidad`
--

DROP TABLE IF EXISTS `disponibilidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disponibilidad` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fisio_id` bigint DEFAULT NULL,
  `dia_semana` int NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dispo_fisio` (`fisio_id`),
  CONSTRAINT `fk_dispo_fisio` FOREIGN KEY (`fisio_id`) REFERENCES `fisio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disponibilidad`
--

LOCK TABLES `disponibilidad` WRITE;
/*!40000 ALTER TABLE `disponibilidad` DISABLE KEYS */;
/*!40000 ALTER TABLE `disponibilidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estados_cita`
--

DROP TABLE IF EXISTS `estados_cita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estados_cita` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estados_cita`
--

LOCK TABLES `estados_cita` WRITE;
/*!40000 ALTER TABLE `estados_cita` DISABLE KEYS */;
INSERT INTO `estados_cita` VALUES (1,'PENDIENTE'),(2,'CONFIRMADA'),(3,'ANULADA'),(4,'DISPONIBLE'),(5,'DISPONIBLE');
/*!40000 ALTER TABLE `estados_cita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fisio`
--

DROP TABLE IF EXISTS `fisio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fisio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `colegiado` varchar(50) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fisio`
--

LOCK TABLES `fisio` WRITE;
/*!40000 ALTER TABLE `fisio` DISABLE KEYS */;
INSERT INTO `fisio` VALUES (1,'Lucía Garza','Fisioterapia General','COL123','admin123','ADMIN'),(2,'Ánxela','Rehabilitación Deportiva','COL456','admin456','ADMIN');
/*!40000 ALTER TABLE `fisio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pacientes`
--

DROP TABLE IF EXISTS `pacientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pacientes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hol387x0ourgruynyqewdhv37` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pacientes`
--

LOCK TABLES `pacientes` WRITE;
/*!40000 ALTER TABLE `pacientes` DISABLE KEYS */;
INSERT INTO `pacientes` VALUES (1,'Paciente TFG',NULL,NULL,'Juan',NULL),(999,'Sin Asignar','Clínica','00000000T','Disponible','000000000'),(1000,NULL,NULL,NULL,'Alex Pato Galiña','645820649');
/*!40000 ALTER TABLE `pacientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicios`
--

DROP TABLE IF EXISTS `servicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `precio` decimal(10,2) DEFAULT '30.00',
  `duracion_minutos` int DEFAULT '60',
  `activo` int DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicios`
--

LOCK TABLES `servicios` WRITE;
/*!40000 ALTER TABLE `servicios` DISABLE KEYS */;
INSERT INTO `servicios` VALUES (1,'Tratamiento Fisioterapia','Sesión de terapia manual',30.00,40,1);
/*!40000 ALTER TABLE `servicios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `turnos`
--

DROP TABLE IF EXISTS `turnos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `turnos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` enum('PENDIENTE','CONFIRMADA','COMPLETADA','CANCELADA') DEFAULT NULL,
  `fecha_hora` datetime(6) DEFAULT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `fisio_id` bigint DEFAULT NULL,
  `paciente_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcpxls5usbf8jdosk3sk7w4fpj` (`fisio_id`),
  KEY `FK4okcqr9iqt2iw6xhy1ppsmo3l` (`paciente_id`),
  CONSTRAINT `FK4okcqr9iqt2iw6xhy1ppsmo3l` FOREIGN KEY (`paciente_id`) REFERENCES `pacientes` (`id`),
  CONSTRAINT `FKcpxls5usbf8jdosk3sk7w4fpj` FOREIGN KEY (`fisio_id`) REFERENCES `fisio` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turnos`
--

LOCK TABLES `turnos` WRITE;
/*!40000 ALTER TABLE `turnos` DISABLE KEYS */;
INSERT INTO `turnos` VALUES (2,'PENDIENTE','2026-02-26 19:21:05.000000','hy',NULL,NULL),(3,'PENDIENTE','2026-02-26 19:21:34.000000','hg',NULL,NULL);
/*!40000 ALTER TABLE `turnos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(100) DEFAULT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('PACIENTE','FISIOTERAPEUTA','ADMINISTRADOR') DEFAULT 'PACIENTE',
  `activo` bit(1) DEFAULT b'1',
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Lucia','Garza','fisioterapialuciagarza@gmail.com','MartinyTiago140816','ADMINISTRADOR',_binary '','2026-03-01 18:14:35');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'fisiolg'
--

--
-- Dumping routines for database 'fisiolg'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-08 19:01:44
