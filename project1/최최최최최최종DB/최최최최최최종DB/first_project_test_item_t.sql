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
-- Table structure for table `test_item_t`
--

DROP TABLE IF EXISTS `test_item_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_item_t` (
  `test_item_idx` int NOT NULL AUTO_INCREMENT,
  `ques_idx` int DEFAULT NULL,
  `test_item_num` int DEFAULT NULL,
  `ques_con` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`test_item_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_item_t`
--

LOCK TABLES `test_item_t` WRITE;
/*!40000 ALTER TABLE `test_item_t` DISABLE KEYS */;
INSERT INTO `test_item_t` VALUES (98,26,1,'매우 미흡'),(99,26,2,'미흡'),(100,26,3,'보통'),(101,26,4,'우수'),(102,26,5,'매우 우수'),(103,27,1,'매우 미흡'),(104,27,2,'미흡'),(105,27,3,'보통'),(106,27,4,'우수'),(107,27,5,'매우 우수'),(108,28,1,'매우 미흡'),(109,28,2,'미흡'),(110,28,3,'보통'),(111,28,4,'우수'),(112,28,5,'매우 우수'),(113,29,1,'매우 미흡'),(114,29,2,'미흡'),(115,29,3,'보통'),(116,29,4,'우수'),(117,29,5,'매우 우수'),(118,30,1,'매우 미흡'),(119,30,2,'미흡'),(120,30,3,'보통'),(121,30,4,'우수'),(122,30,5,'매우 우수'),(123,31,1,'매우 미흡'),(124,31,2,'미흡'),(125,31,3,'보통'),(126,31,4,'우수'),(127,31,5,'매우 우수'),(128,32,1,'매우 미흡'),(129,32,2,'미흡'),(130,32,3,'보통'),(131,32,4,'우수'),(132,32,5,'매우 우수'),(133,33,1,'매우 미흡'),(134,33,2,'미흡'),(135,33,3,'보통'),(136,33,4,'우수'),(137,33,5,'매우 우수'),(138,34,1,'매우 미흡'),(139,34,2,'미흡'),(140,34,3,'보통'),(141,34,4,'우수'),(142,34,5,'매우 우수'),(143,35,1,'매우 미흡'),(144,35,2,'미흡'),(145,35,3,'보통'),(146,35,4,'우수'),(147,35,5,'매우 우수'),(148,36,1,'매우 미흡'),(149,36,2,'미흡'),(150,36,3,'보통'),(151,36,4,'우수'),(152,36,5,'매우 우수'),(153,37,1,'매우 미흡'),(154,37,2,'미흡'),(155,37,3,'보통'),(156,37,4,'우수'),(157,37,5,'매우 우수'),(158,38,1,'매우 미흡'),(159,38,2,'미흡'),(160,38,3,'보통'),(161,38,4,'우수'),(162,38,5,'매우 우수'),(163,39,1,'1'),(164,39,2,'1'),(165,39,3,'1'),(166,39,4,'1'),(167,40,1,'2'),(168,40,2,'2'),(169,40,3,'2'),(170,41,1,'3'),(171,41,2,'3'),(172,41,3,'3'),(173,42,1,'ㄱ'),(174,42,2,'ㄴ'),(175,42,3,'ㄹㄷ'),(176,42,4,'ㅁ'),(177,43,1,'1'),(178,43,2,'2'),(179,43,3,'3'),(180,43,4,'4'),(181,43,5,'5'),(182,44,1,'1'),(183,44,2,'2'),(184,44,3,'3'),(185,44,4,'4'),(186,44,5,'5'),(187,45,1,'1'),(188,45,2,'2'),(189,45,3,'3'),(190,45,4,'4'),(191,45,5,'5'),(192,46,1,'11'),(193,46,2,'22'),(194,46,3,'33'),(195,46,4,'44'),(196,46,5,'55');
/*!40000 ALTER TABLE `test_item_t` ENABLE KEYS */;
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
