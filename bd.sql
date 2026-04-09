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
  `fisio_id` bigint DEFAULT NULL,
  `servicio_id` bigint DEFAULT NULL,
  `paciente_id` bigint DEFAULT NULL,
  `recordatorio_enviado` bit(1) DEFAULT b'0',
  `notas` text,
  `estado` enum('NONE','LIBRE','PENDIENTE','CONFIRMADA','ANULADA','DISPONIBLE') DEFAULT NULL,
  `turno_id` bigint DEFAULT NULL,
  `fecha_hora` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bbwh4ytnwechvo8kjw8p6pwdb` (`turno_id`),
  KEY `fk_citas_fisio` (`fisio_id`),
  KEY `fk_citas_servicio` (`servicio_id`),
  KEY `fk_citas_paciente` (`paciente_id`),
  CONSTRAINT `FK7guqrif5gn9g6jmyxltyhwary` FOREIGN KEY (`turno_id`) REFERENCES `turnos` (`id`),
  CONSTRAINT `fk_citas_fisio` FOREIGN KEY (`fisio_id`) REFERENCES `fisio` (`id`),
  CONSTRAINT `fk_citas_paciente` FOREIGN KEY (`paciente_id`) REFERENCES `pacientes` (`id`),
  CONSTRAINT `fk_citas_servicio` FOREIGN KEY (`servicio_id`) REFERENCES `servicios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `citas`
--

LOCK TABLES `citas` WRITE;
/*!40000 ALTER TABLE `citas` DISABLE KEYS */;
INSERT INTO `citas` VALUES (1,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 14:00:00.000000'),(2,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 14:40:00.000000'),(3,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 15:20:00.000000'),(4,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 16:00:00.000000'),(5,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 16:40:00.000000'),(6,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 17:20:00.000000'),(7,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 18:00:00.000000'),(8,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 18:40:00.000000'),(9,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 19:20:00.000000'),(10,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 08:00:00.000000'),(11,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 08:40:00.000000'),(12,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 09:20:00.000000'),(13,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 10:00:00.000000'),(14,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 10:40:00.000000'),(15,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 11:20:00.000000'),(16,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 14:30:00.000000'),(17,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 15:10:00.000000'),(18,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 15:50:00.000000'),(19,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 16:30:00.000000'),(20,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 17:10:00.000000'),(21,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 17:50:00.000000'),(22,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 18:30:00.000000'),(23,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-16 19:10:00.000000'),(24,2,1,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 08:00:00.000000'),(25,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 08:40:00.000000'),(26,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 09:20:00.000000'),(27,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 10:00:00.000000'),(28,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 10:40:00.000000'),(29,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 11:20:00.000000'),(30,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 12:00:00.000000'),(31,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 12:40:00.000000'),(32,2,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 13:20:00.000000'),(33,1,NULL,NULL,_binary '\0',NULL,'ANULADA',NULL,'2026-03-18 08:00:00.000000'),(34,1,NULL,NULL,_binary '\0',NULL,'ANULADA',NULL,'2026-03-18 08:40:00.000000'),(35,1,NULL,NULL,_binary '\0',NULL,'ANULADA',NULL,'2026-03-18 09:20:00.000000'),(36,1,NULL,NULL,_binary '\0',NULL,'ANULADA',NULL,'2026-03-18 10:00:00.000000'),(37,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 10:40:00.000000'),(38,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 11:20:00.000000'),(39,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 14:30:00.000000'),(40,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 15:10:00.000000'),(41,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 15:50:00.000000'),(42,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 16:30:00.000000'),(43,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 17:10:00.000000'),(44,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 17:50:00.000000'),(45,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 18:30:00.000000'),(46,1,NULL,NULL,_binary '\0',NULL,'LIBRE',NULL,'2026-03-18 19:10:00.000000');
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
-- Table structure for table `fisio`
--

DROP TABLE IF EXISTS `fisio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fisio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `numero_colegiado` varchar(50) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL,
  `usuario_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fisio_usuario` (`usuario_id`),
  CONSTRAINT `fk_fisio_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fisio`
--

LOCK TABLES `fisio` WRITE;
/*!40000 ALTER TABLE `fisio` DISABLE KEYS */;
INSERT INTO `fisio` VALUES (1,'Lucía Garza','Fisioterapia General','COL123','admin123','ADMIN',1),(2,'Ánxela','Rehabilitación Deportiva','COL456','admin456','ADMIN',2);
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
  `email` varchar(150) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `token_recuperacion` varchar(255) DEFAULT NULL,
  `acepto_rgpd` tinyint(1) DEFAULT '0',
  `fecha_registro` datetime DEFAULT NULL,
  `usuario_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hol387x0ourgruynyqewdhv37` (`dni`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_paciente_usuario` (`usuario_id`),
  CONSTRAINT `fk_paciente_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1013 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pacientes`
--

LOCK TABLES `pacientes` WRITE;
/*!40000 ALTER TABLE `pacientes` DISABLE KEYS */;
INSERT INTO `pacientes` VALUES (1004,'LÓPEZ PI',NULL,'444777888M','MANUEL','6566565656','lu-garza@hotmail.com',NULL,NULL,0,'2026-03-17 18:56:43',NULL),(1005,'FUMEGA DAMIAN',NULL,'76720814J','DIEGO','645820649','difuda_@hotmail.com',NULL,NULL,0,'2026-03-17 23:28:42',NULL);
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
  `precio` decimal(38,2) DEFAULT NULL,
  `duracion_minutos` int DEFAULT '40',
  `activo` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicios`
--

LOCK TABLES `servicios` WRITE;
/*!40000 ALTER TABLE `servicios` DISABLE KEYS */;
INSERT INTO `servicios` VALUES (1,'Tratamiento Fisioterapia','Sesión de terapia manual',30.00,40,_binary '');
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
  `username` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(50) NOT NULL,
  `otp_code` varchar(6) DEFAULT NULL,
  `activo` bit(1) DEFAULT b'1',
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(150) NOT NULL,
  `token_recuperacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'lucia@clinica.com','123456','ADMIN',NULL,_binary '','2026-03-15 01:07:54','',NULL),(2,'anxela@clinica.com','123456','ADMIN',NULL,_binary '','2026-03-15 01:07:54','',NULL),(3,'difuda_@hotmail.com','OTP_USER','INVITADO',NULL,_binary '','2026-03-16 20:53:24','difuda_@hotmail.com',NULL),(4,'lu-garza@hotmail.com','OTP_USER','INVITADO',NULL,_binary '','2026-03-16 20:56:12','lu-garza@hotmail.com',NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

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

--
-- Table structure for table `noticia`
--

DROP TABLE IF EXISTS `noticia`;
CREATE TABLE `noticia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) NOT NULL,
  `contenido` text NOT NULL,
  `fecha_publicacion` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
