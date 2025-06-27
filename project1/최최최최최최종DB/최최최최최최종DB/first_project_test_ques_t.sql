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
-- Table structure for table `test_ques_t`
--

DROP TABLE IF EXISTS `test_ques_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_ques_t` (
  `ques_idx` bigint NOT NULL AUTO_INCREMENT,
  `ques_num` bigint NOT NULL,
  `ques_con` varchar(100) NOT NULL,
  `test_item_tnum` bigint NOT NULL,
  `test_ques_pit` bigint NOT NULL,
  `test_idx` bigint NOT NULL,
  PRIMARY KEY (`ques_idx`),
  KEY `test_ques_t_fk1` (`test_idx`),
  CONSTRAINT `test_ques_t_fk1` FOREIGN KEY (`test_idx`) REFERENCES `test_t` (`test_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_ques_t`
--

LOCK TABLES `test_ques_t` WRITE;
/*!40000 ALTER TABLE `test_ques_t` DISABLE KEYS */;
INSERT INTO `test_ques_t` VALUES (26,1,'개발에 필요한 JAVA SDK를 다운받고 설치하며 경로를 설정할 수 있다.',5,7,9),(27,2,'객체지향개념을 적용한 class를 작성하고 객체를 생성할 수 있다.',5,7,9),(28,3,'변수와 상수, 자료형, 연산자, 제어문을 사용할 수 있다.',5,7,9),(29,4,'다양한 method를 작성하고 호출할 수 있다.',5,7,9),(30,5,'일괄처리를 위한 배열을 생성하고 사용할 수 있다.',5,8,9),(31,6,'예외처리(Exception Handling)를 이해하며 코드에 적용할 수 있다.',5,8,9),(32,7,'java.util package class 및 Java CollectionFramework를 사용할 수 있다.',5,8,9),(33,8,'Windows Application 개발을 위한 JFC와 Event Handling을 할 수 있다.',5,8,9),(34,9,'데이터를 읽고 쓰기 위한 IO Stream을 사용할 수 있다.',5,8,9),(35,10,'스레드를 사용한 동시 업무처리를 할 수 있다,',5,8,9),(36,11,'외부 컴퓨터와 연결하기 위한 네트워크환경을 이해하며 네트워크 프로그래밍을 작성할 수 있다.',5,8,9),(37,12,'NIO를 이용한 대용량 파일 처리를 할 수 있다.',5,8,9),(38,13,'JDBC를 이용한 데이터베이스 연동할 수 있다.',5,8,9),(39,1,'1',4,33,10),(40,2,'2',3,33,10),(41,3,'3',3,34,10),(42,1,'답은 1번',4,20,13),(43,2,'답은 2번',5,20,13),(44,3,'답은 3번',5,20,13),(45,4,'답은 4번',5,20,13),(46,5,'5번이 답입니다',5,20,13);
/*!40000 ALTER TABLE `test_ques_t` ENABLE KEYS */;
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
