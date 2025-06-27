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
-- Table structure for table `std_anw_t`
--

DROP TABLE IF EXISTS `std_anw_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `std_anw_t` (
  `std_anw_t_idx` bigint NOT NULL AUTO_INCREMENT,
  `test_idx` bigint NOT NULL,
  `ques_num` bigint NOT NULL,
  `stdno` int NOT NULL,
  `s_anw` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`std_anw_t_idx`),
  KEY `std_anw_t_fk1` (`test_idx`),
  KEY `std_anw_t_fk2` (`stdno`),
  CONSTRAINT `std_anw_t_fk1` FOREIGN KEY (`test_idx`) REFERENCES `test_t` (`test_idx`),
  CONSTRAINT `std_anw_t_fk2` FOREIGN KEY (`stdno`) REFERENCES `std_t` (`stdno`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `std_anw_t`
--

LOCK TABLES `std_anw_t` WRITE;
/*!40000 ALTER TABLE `std_anw_t` DISABLE KEYS */;
INSERT INTO `std_anw_t` VALUES (70,9,1,1000,'매우 미흡'),(71,9,2,1000,'매우 미흡'),(72,9,3,1000,'매우 미흡'),(73,9,4,1000,'매우 미흡'),(74,9,5,1000,'매우 미흡'),(75,9,6,1000,'매우 미흡'),(76,9,7,1000,'매우 미흡'),(77,9,8,1000,'매우 미흡'),(78,9,9,1000,'매우 미흡'),(79,9,10,1000,'매우 미흡'),(80,9,11,1000,'매우 미흡'),(81,9,12,1000,'매우 미흡'),(82,9,13,1000,'매우 미흡'),(83,10,1,1000,'1'),(84,10,2,1000,'2'),(85,10,3,1000,'3');
/*!40000 ALTER TABLE `std_anw_t` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-27 15:54:43
