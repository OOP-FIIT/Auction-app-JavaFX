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
-- Table structure for table `bid`
--

DROP TABLE IF EXISTS `bid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bid` (
  `id` int NOT NULL AUTO_INCREMENT,
  `buyer_id` int DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `lot_id` int DEFAULT NULL,
  `bid` int DEFAULT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lots`
--

DROP TABLE IF EXISTS `lots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lots` (
  `id` int NOT NULL AUTO_INCREMENT,
  `seller_id` int DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `description` varchar(10000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lots`
--

LOCK TABLES `lots` WRITE;
/*!40000 ALTER TABLE `lots` DISABLE KEYS */;
INSERT INTO `lots` VALUES (18,7,'GOOGLE search','2022-05-15 19:50:48','Place near the 1 page of search result, best plase to promote your goods. 1 week long'),(19,7,'YOUTUBE.com ','2022-05-15 19:54:04','advertisement place in the middle of video'),(20,7,'GOOGLE MAPS','2022-05-15 19:56:19','promote your bussiness witn our app, your label become greater on the maps, you will be shown as first result if somebody tries to find you wia search in GOOGLE MAPS '),(21,7,'GOOGLE COMPLETE PACK','2022-05-15 19:57:53','get all available places in our products and your servise will be moset popular in the workd');
/*!40000 ALTER TABLE `lots` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userdata`
--

LOCK TABLES `userdata` WRITE;
/*!40000 ALTER TABLE `userdata` DISABLE KEYS */;
INSERT INTO `userdata` VALUES (2,'new','pass','misha0510@gmail.com','Pro',73325,'8f08b217fcae01d4c8d4d08844ed73145da67b737a05ca0f88f5db8eabb4a524'),(3,'/','/','misha0510@gmail.com','Pro',101,'fd0b9c11e2a143021ca0cd481fc6fa59f2534bb4c19740ef5005d81ccf1363ab'),(4,'one','pass','misha0510@gmail.com','Pro',42875,'e66565edb0b6839ff84dc7e11074f0e359618ae53901632c3ba8f92b5bfc8600'),(5,'two','pass','misha0510@gmail.com','Buyer',5350,'f9c78fab3747379ff94bc7fc03b478d3518d347829d71bcf542b8d92e9b47fb3'),(6,'three','pass','misha0510@gmail.com','Auctioner',22000,'20195f83c196d17325fdfd91a5bcf6837a88d9c4b0a66832871fada4f2cc4afb'),(7,'nnn','pass','misha0510@gmail.com','Pro',21000,'9132c6a3e6770227545289ba2da719a0c7cd393da9b9281ec645490b861236bb'),(8,'mmm','pass','misha0510@gmail.com','Pro',40922,'3b2a9ecfed198dd4848638ccc7a1e110f2ff1ae51a543fdce552ed4a3268c4bf'),(9,'user','pass','misha0510@gmail.com','Seller',9324,NULL),(10,'MarkusAurelius','goodpassword','misha0510@gmail.com','Auctioner',86469,NULL),(11,'newAcc','new','misha0510@gmail.com','Pro',22928,'053056d2a670a9f3e71e4438486d02b5a8446354cc371bfb3f0b7d26c28b2351');
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

-- Dump completed on 2022-05-16  4:21:22
