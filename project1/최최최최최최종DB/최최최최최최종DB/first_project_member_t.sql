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
-- Table structure for table `member_t`
--

DROP TABLE IF EXISTS `member_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_t` (
  `mem_idx` int NOT NULL AUTO_INCREMENT,
  `mem_id` varchar(50) NOT NULL,
  `mem_pw` varchar(50) NOT NULL,
  `stdno` int DEFAULT NULL,
  `adno` int DEFAULT NULL,
  `mem_quit` char(1) NOT NULL DEFAULT 'N',
  `mem_joindate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mem_role` char(1) NOT NULL DEFAULT 'S',
  `mem_name` varchar(50) NOT NULL,
  `mem_gender` char(1) NOT NULL,
  `mem_phone` varchar(20) NOT NULL,
  `mem_birth` varchar(10) NOT NULL,
  `mem_bkCode` varchar(50) NOT NULL,
  `mem_email` varchar(100) NOT NULL,
  `mem_address` varchar(255) NOT NULL,
  `mem_admin_inputOrNot` char(1) DEFAULT '0',
  PRIMARY KEY (`mem_idx`),
  UNIQUE KEY `mem_id` (`mem_id`),
  KEY `mem_t_std_fk` (`stdno`),
  KEY `mem_t_ad_fk` (`adno`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_t`
--

LOCK TABLES `member_t` WRITE;
/*!40000 ALTER TABLE `member_t` DISABLE KEYS */;
INSERT INTO `member_t` VALUES (1,'2000','2000',NULL,2000,'N','2025-03-23 12:28:27','A','오경주','M','010-2000-2000','1965-01-01','2000','2000ad@email.com','강남','1'),(2,'1000','1000',1000,NULL,'Y','2025-03-26 14:03:07','S','손윤서','F','010-1000-1000','1999-09-14','1000','1000st@email.com','강남','0'),(5,'111','111',NULL,2002,'N','2025-06-27 15:36:48','A','박준형','M','01055281841','1999-11-12','8N3fs6E4f2','11111111','경기도','0'),(6,'3000','3000',1002,NULL,'N','2025-06-27 15:48:32','S','이도','M','01033333333','1984-05-04','G774c3m0b','3333','대한민국','0'),(7,'4000','4000',1003,NULL,'N','2025-06-27 15:49:11','S','홍길동','M','01044444444','1968-04-05','Y9O5M71fj','4444','대한민국','0'),(8,'5000','5000',1004,NULL,'N','2025-06-27 15:50:07','S','황도','F','01055555555','1980-06-02','S6n13C4','5555','대한민국','0');
/*!40000 ALTER TABLE `member_t` ENABLE KEYS */;
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
