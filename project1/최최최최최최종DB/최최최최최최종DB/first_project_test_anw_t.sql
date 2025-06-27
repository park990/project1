-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: first_project
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `test_anw_t`
--

DROP TABLE IF EXISTS `test_anw_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_anw_t` (
  `test_anw_t_idx` bigint NOT NULL AUTO_INCREMENT,
  `test_idx` bigint NOT NULL,
  `ques_num` bigint NOT NULL,
  `c_anw` varchar(45) NOT NULL,
  PRIMARY KEY (`test_anw_t_idx`),
  KEY `test_anw_t_fk1` (`test_idx`),
  CONSTRAINT `test_anw_t_fk1` FOREIGN KEY (`test_idx`) REFERENCES `test_t` (`test_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_anw_t`
--

LOCK TABLES `test_anw_t` WRITE;
/*!40000 ALTER TABLE `test_anw_t` DISABLE KEYS */;
INSERT INTO `test_anw_t` VALUES (26,9,1,'매우 미흡'),(27,9,2,'매우 미흡'),(28,9,3,'매우 미흡'),(29,9,4,'매우 미흡'),(30,9,5,'매우 미흡'),(31,9,6,'매우 미흡'),(32,9,7,'매우 미흡'),(33,9,8,'매우 미흡'),(34,9,9,'매우 미흡'),(35,9,10,'매우 미흡'),(36,9,11,'매우 미흡'),(37,9,12,'매우 미흡'),(38,9,13,'매우 미흡'),(39,10,1,'1'),(40,10,2,'2'),(41,10,3,'3'),(42,13,1,'ㄱ'),(43,13,2,'2'),(44,13,3,'3'),(45,13,4,'4'),(46,13,5,'55');
/*!40000 ALTER TABLE `test_anw_t` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-27 15:54:44
