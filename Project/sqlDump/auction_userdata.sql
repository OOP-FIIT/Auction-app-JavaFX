-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: auction
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `userdata`
--

DROP TABLE IF EXISTS `userdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userdata` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `mode` varchar(10) DEFAULT NULL,
  `balance` int DEFAULT NULL,
  `license` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userdata`
--

LOCK TABLES `userdata` WRITE;
/*!40000 ALTER TABLE `userdata` DISABLE KEYS */;
INSERT INTO `userdata` VALUES (2,'new','pass','misha0510@gmail.com','Pro',73325,'8f08b217fcae01d4c8d4d08844ed73145da67b737a05ca0f88f5db8eabb4a524'),(3,'/','/','misha0510@gmail.com','Pro',101,'fd0b9c11e2a143021ca0cd481fc6fa59f2534bb4c19740ef5005d81ccf1363ab'),(4,'one','pass','misha0510@gmail.com','Pro',38875,'e66565edb0b6839ff84dc7e11074f0e359618ae53901632c3ba8f92b5bfc8600'),(5,'two','pass','misha0510@gmail.com','Buyer',5350,'f9c78fab3747379ff94bc7fc03b478d3518d347829d71bcf542b8d92e9b47fb3'),(6,'three','pass','misha0510@gmail.com','Auctioner',22000,'20195f83c196d17325fdfd91a5bcf6837a88d9c4b0a66832871fada4f2cc4afb'),(7,'nnn','pass',NULL,'Pro',10000,NULL),(8,'mmm','pass','misha0510@gmail.com','Pro',40922,'3b2a9ecfed198dd4848638ccc7a1e110f2ff1ae51a543fdce552ed4a3268c4bf'),(9,'user','pass','misha0510@gmail.com','Seller',9324,NULL),(10,'MarkusAurelius','goodpassword','misha0510@gmail.com','Auctioner',86469,NULL);
/*!40000 ALTER TABLE `userdata` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-15 18:47:04
