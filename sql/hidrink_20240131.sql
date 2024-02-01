DROP DATABASE IF EXISTS hidrink;
CREATE DATABASE hidrink;
USE hidrink;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: hidrink
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `ad`
--

DROP TABLE IF EXISTS `ad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ad` (
  `id` int NOT NULL AUTO_INCREMENT,
  `img` varchar(200) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `intro` varchar(300) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ad`
--

LOCK TABLES `ad` WRITE;
/*!40000 ALTER TABLE `ad` DISABLE KEYS */;
/*!40000 ALTER TABLE `ad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` tinyint NOT NULL DEFAULT '0',
  `username` varchar(10) DEFAULT NULL,
  `password` varchar(18) DEFAULT NULL,
  `name` varchar(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `admins_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (1,0,'Jackie','Zk44011334','zk','2023-11-01 00:00:00','2023-11-01 00:00:00'),(2,0,'SuperData','SuperData2','SuperData','2023-11-01 00:00:00','2023-11-01 00:00:00'),(3,0,'JavaWeb','SpringBoot2','Java','2023-11-01 00:00:00','2023-11-01 00:00:00');
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cus`
--

DROP TABLE IF EXISTS `cus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` tinyint NOT NULL DEFAULT '2',
  `username` varchar(10) NOT NULL,
  `password` varchar(18) NOT NULL,
  `img` varchar(200) DEFAULT NULL,
  `gender` int DEFAULT NULL COMMENT '0-男，1-女',
  `birthday` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `cus_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cus`
--

LOCK TABLES `cus` WRITE;
/*!40000 ALTER TABLE `cus` DISABLE KEYS */;
INSERT INTO `cus` VALUES (1,2,'AA_TTea','Aattee11','1.jpg',0,'2000-01-12 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(2,2,'Tomtom','TomisCat12','1.jpg',0,'2001-12-01 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(3,2,'User520','AattEw520','1.jpg',1,'2006-06-01 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(4,2,'ILOVEMike','Milkymiky1','1.jpg',1,'1989-05-01 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(5,2,'GDOUcomsci','Comsci666','1.jpg',1,'2021-09-01 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(6,2,'GDOUYJ','Library1314','1.jpg',0,'2021-09-11 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(7,2,'WinterIs','Coming111','1.jpg',0,'2019-02-01 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(8,2,'LoveStory','Fearless2023','1.jpg',1,'1999-02-01 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(9,2,'SpeakNow','SpeakNow2023','1.jpg',0,'1996-02-25 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(10,2,'Jerry666','JerryRat6','1.jpg',1,'2000-08-07 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00'),(11,2,'Lavendar','Haze2023','1.jpg',1,'2003-07-06 00:00:00','2023-11-01 00:00:00','2023-11-01 00:00:00');
/*!40000 ALTER TABLE `cus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dept`
--

DROP TABLE IF EXISTS `dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dept` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(5) DEFAULT NULL,
  `establish_time` datetime DEFAULT NULL,
  `emp_num` int DEFAULT '0',
  `intro` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `dept_pk` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dept`
--

LOCK TABLES `dept` WRITE;
/*!40000 ALTER TABLE `dept` DISABLE KEYS */;
INSERT INTO `dept` VALUES (1,'组织部','2023-11-01 00:00:00',4,'组织架构评估与设计；对总编制和各单位编制进行评估和规划；确定员工与公司整体价值观；确定绩效激励方案','2023-11-01 00:00:00','2023-11-18 13:42:22'),(2,'人事部','2023-11-01 00:00:00',6,'负责人力资源工作的规划，建立、执行招聘、培训、考勤、劳动纪律等人事程序或规章制度','2023-11-01 00:00:00','2023-11-18 13:43:35'),(3,'财务部','2023-11-01 00:00:00',2,'财务部','2023-11-01 00:00:00','2023-12-27 11:02:16'),(4,'销售部','2023-11-01 00:00:00',4,NULL,'2023-11-01 00:00:00','2023-11-01 00:00:00'),(13,'企划部','2023-11-18 00:00:00',0,'','2023-11-18 14:44:40','2023-12-23 16:56:23');
/*!40000 ALTER TABLE `dept` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tri_delete_dept` AFTER DELETE ON `dept` FOR EACH ROW BEGIN
		DELETE FROM emp 
			WHERE OLD.id = dept_id;
	END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `emp`
--

DROP TABLE IF EXISTS `emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emp` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` tinyint NOT NULL DEFAULT '1',
  `username` varchar(10) NOT NULL,
  `password` varchar(18) NOT NULL,
  `img` varchar(300) DEFAULT NULL,
  `gender` tinyint DEFAULT NULL COMMENT '0-男，1-女',
  `dept_id` int NOT NULL COMMENT '0-组织部，1-人事部，2-财务部，3-销售部',
  `entry_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`dept_id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `dept_id` (`dept_id`),
  CONSTRAINT `emp_ibfk_1` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emp`
--

LOCK TABLES `emp` WRITE;
/*!40000 ALTER TABLE `emp` DISABLE KEYS */;
INSERT INTO `emp` VALUES (1,1,'zhangsan','zhangsan3','http://localhost:8090/2834d86e-4f04-484c-bcc6-adcaa496c486.jpg',0,4,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:54:47'),(2,1,'wangwu','wangwu5','http://localhost:8090/a2dd1e8c-d2f1-4280-bf55-99bc4a1081b9.jpg',0,1,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:54:40'),(3,1,'lisi','lisii4','http://localhost:8090/c6e768c9-f510-4f07-ae3e-a04abe49ba41.jpg',1,2,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:54:32'),(4,1,'jiaren','jiaFirst1','http://localhost:8090/ce58d6d6-89f3-4378-b9e4-e83b2bc3f125.jpg',1,1,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:55:05'),(5,1,'antman','theant.','http://localhost:8090/11e5164e-a22d-4e9c-9b03-f922807a51d9.jpg',0,1,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:55:14'),(6,1,'spiderman','superspider','http://localhost:8090/3ab09cde-d31c-466b-bd3f-6107224270f8.jpg',0,1,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:55:25'),(7,1,'ironman','iamironman','http://localhost:8090/eb03f8b0-56f8-414c-9a96-acf2e49f3bde.jpg',0,2,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:55:35'),(8,1,'thundergod','sonofodin','http://localhost:8090/cc0e0408-2414-4f11-b64b-bb980df5e80d.jpg',0,2,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:55:46'),(9,1,'loki','brotherofthunder','http://localhost:8090/376949a3-3401-4024-bab0-835f51f77761.jpg',0,3,'2023-11-01 00:00:00','2023-11-01 00:00:00','2023-11-18 12:55:56'),(10,1,'Mysql8','MySQL80','http://localhost:8090/0ee2009e-9aa6-4245-bacc-98225ec63cf2.jpg',1,4,'2023-11-12 00:00:00','2023-11-12 00:00:00','2023-11-18 12:54:56'),(12,1,'look','look5555','http://localhost:8090/7def8f2f-e799-452e-901c-048c5a334b10.jpg',1,3,'2023-11-23 00:00:00','2023-11-18 14:21:09','2023-11-18 14:36:51'),(14,1,'newp2','111111','http://localhost:8090/ae9fb1fd-e97a-4c8b-9654-56ce9e8ffe07.jpg',1,4,'2022-09-27 00:00:00','2023-11-18 14:40:04','2023-12-27 10:57:21'),(17,1,'xiaoxiong','xiaoxiong','http://localhost:8090/180570e1-ecbe-4b57-9803-9b5f780c7133.png',0,2,'2023-12-27 00:00:00','2023-12-27 10:54:33','2023-12-27 10:54:42'),(19,1,'mysql','mysql',NULL,0,4,'2023-12-15 00:00:00','2023-12-27 11:04:30','2023-12-27 11:04:30'),(21,1,'MySQL666','MySQL666',NULL,0,2,NULL,'2024-01-14 00:50:12','2024-01-14 00:50:12'),(22,1,'MySQL66','MySQL66','http://localhost:8090/3d4de31c-5852-4e62-876a-5e69deaf74a9.png',0,2,NULL,'2024-01-14 00:50:51','2024-01-14 01:36:40');
/*!40000 ALTER TABLE `emp` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tri_insert_emp` AFTER INSERT ON `emp` FOR EACH ROW BEGIN
		UPDATE dept SET emp_num = emp_num + 1 
			WHERE NEW.dept_id = id;
	END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tri_update_emp` AFTER UPDATE ON `emp` FOR EACH ROW BEGIN
		UPDATE dept SET emp_num = emp_num + 1 
			WHERE NEW.dept_id = id;
		UPDATE dept SET emp_num = emp_num - 1
			WHERE OLD.dept_id = id;
	END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tri_delete_emp` AFTER DELETE ON `emp` FOR EACH ROW BEGIN
		UPDATE dept SET emp_num = emp_num - 1
			WHERE OLD.dept_id = id;
	END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `introduction` varchar(50) DEFAULT NULL,
  `price` decimal(5,2) DEFAULT '0.00',
  `stock` bigint DEFAULT '0',
  `sales_volume` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `url_picture` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` VALUES (1,'德国雷司令','白葡萄酒，口感清新，带有桃子和杏子味',80.00,79,59,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/1.png'),(2,'意大利巴罗洛葡萄酒','红葡萄酒，口感浓郁，带有黑色水果和巧克力味',300.00,10,34,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/2.png'),(3,'意大利桃红葡萄酒','玫瑰色葡萄酒，口感清新，带有草莓和葡萄柚味',79.00,50,114,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/3.png'),(4,'意大利奥尔尼葡萄酒','红葡萄酒，口感醇厚，带有黑色水果和香料味',129.00,100,135,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/4.png'),(5,'新西兰长相思','白葡萄酒，口感鲜活，带有青柠和柑橘味',129.00,1,33,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/5.png'),(6,'法国伯爵香槟','气泡白葡萄酒，口感丰富，带有焦糖和杏仁味',589.00,9,1,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/6.png'),(7,'法国香槟','气泡白葡萄酒，口感清爽，带有果香味道',229.00,1,15,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/7.png'),(8,'法国梅多克','红葡萄酒，口感醇厚，带有成熟水果和橡木味',199.00,19,12,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/8.png'),(9,'美国赤霞珠','红葡萄酒，口感浓郁，带有浓郁的黑莓和黑醋栗味',109.00,99,14,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/9.png'),(10,'美国西拉','红葡萄酒，口感浓郁，带有黑色水果和香料味',89.00,39,1,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/10.png'),(11,'西班牙卡瓦','气泡酒，口感清爽，带有苹果和柠檬味',130.00,50,11,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/11.png'),(12,'西班牙里奥哈','白葡萄酒，口感干爽，带有明显的橡木味',49.00,66,14,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/12.png'),(13,'西班牙桃红起泡酒','泡玫瑰葡萄酒，口感清新，带有草莓和覆盆子味',119.00,77,1,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/13.png'),(14,'苏格兰伏特加','伏特加酒，口感干净，带有淡淡的谷物味',149.00,16,65,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/14.png'),(15,'智利长相思','白葡萄酒，口感清新，带有热带水果味',39.00,100,100,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/15.png'),(16,'澳大利亚西拉','红葡萄酒，口感浓郁，带有黑莓和烤肉味',49.00,88,22,'2022-12-25 15:30:59','2023-12-25 15:30:59','../../assets/16.png');
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operate_log`
--

DROP TABLE IF EXISTS `operate_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operate_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `operate_user` int DEFAULT NULL,
  `operate_role` tinyint DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `class_name` varchar(100) DEFAULT NULL,
  `method_name` varchar(100) DEFAULT NULL,
  `method_params` varchar(1000) DEFAULT NULL,
  `return_value` varchar(2000) DEFAULT NULL,
  `cost_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- No data for table `operate_log`
--


--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cus_id` int NOT NULL,
  `goods_id` int NOT NULL,
  `purchase_volume` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` smallint DEFAULT NULL,
  PRIMARY KEY (`id`,`cus_id`,`goods_id`),
  KEY `cus_id` (`cus_id`),
  KEY `goods_id` (`goods_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`cus_id`) REFERENCES `cus` (`id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_sales_volume` AFTER INSERT ON `orders` FOR EACH ROW BEGIN

    -- 更新总销量

    UPDATE goods

    SET sales_volume = sales_volume + NEW.purchase_volume

    WHERE id = NEW.id;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping events for database 'myheytea'
--

--
-- Dumping routines for database 'myheytea'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-01 16:18:39
