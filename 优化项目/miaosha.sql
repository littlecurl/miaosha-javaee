-- MySQL dump 10.13  Distrib 5.7.24, for Win64 (x86_64)
--
-- Host: localhost    Database: miaosha
-- ------------------------------------------------------
-- Server version	5.7.24-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `miaosha`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `miaosha` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `miaosha`;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '商品名称',
  `price` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品价格，double类型',
  `description` varchar(500) NOT NULL DEFAULT '' COMMENT '商品描述',
  `sales` int(11) NOT NULL DEFAULT '0' COMMENT '商品销量',
  `img_url` varchar(500) NOT NULL DEFAULT '' COMMENT '图片链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (18,'iPhone',1529.00,'最好用的手机',20,'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2529243003,3824091008&fm=15&gp=0.jpg'),(19,'iPhone',1529.00,'最好用的手机',5,'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2529243003,3824091008&fm=15&gp=0.jpg'),(20,'iPhone',1529.00,'最好用的手机',7,'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2529243003,3824091008&fm=15&gp=0.jpg'),(21,'大哥大',199.00,'别闹，手机中的战斗机！',0,'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3451589917,2529190060&fm=26&gp=0.jpg'),(22,'大哥大',199.00,'别闹，手机中的战斗机！',0,'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3451589917,2529190060&fm=26&gp=0.jpg'),(23,'大哥大',199.00,'别闹，手机中的战斗机！',0,'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3451589917,2529190060&fm=26&gp=0.jpg'),(24,'诺基亚',19.09,'真正的王者',0,'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3293582389,828777081&fm=26&gp=0.jpg'),(25,'诺基亚',19.09,'真正的王者',0,'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3293582389,828777081&fm=26&gp=0.jpg'),(26,'诺基亚',19.09,'真正的王者',0,'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3293582389,828777081&fm=26&gp=0.jpg');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_stock`
--

DROP TABLE IF EXISTS `item_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存',
  `item_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_id_unique_index` (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_stock`
--

LOCK TABLES `item_stock` WRITE;
/*!40000 ALTER TABLE `item_stock` DISABLE KEYS */;
INSERT INTO `item_stock` VALUES (18,16,18),(19,20,19),(20,18,20),(21,2562,21),(22,2562,22),(23,2562,23),(24,326,24),(25,326,25),(26,326,26);
/*!40000 ALTER TABLE `item_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_info`
--

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_info` (
  `id` varchar(32) NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `item_id` int(11) NOT NULL DEFAULT '0',
  `item_price` double NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL DEFAULT '0',
  `order_price` double NOT NULL DEFAULT '0',
  `promo_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_info`
--

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;
INSERT INTO `order_info` VALUES ('2019021300000000',28,1,0,1,0,0),('2019021300000100',28,1,0,1,0,0),('2019021300000200',28,1,0,1,0,0),('2019021300000300',28,1,666,1,666,0),('2019021300000400',28,1,666,1,666,0),('2019021300000500',28,1,666,1,666,0),('2019021300000600',28,1,666,1,666,0),('2019021400000700',28,1,666,1,666,0),('2019021400000800',28,1,666,1,666,0),('2019072000000000',45,18,1529,1,1529,0),('2019072000000100',45,18,1529,1,1529,0),('2019072000000200',45,18,180,1,180,18),('2019072000000300',45,18,180,1,180,18),('2019072000000400',46,18,1529,1,1529,0),('2019072000000500',46,18,180,1,180,18),('2019072100000600',45,19,1529,1,1529,0),('2019072100000700',47,19,1529,1,1529,0),('2019072100000800',47,20,1529,1,1529,0),('2019072100000900',45,20,1529,1,1529,0),('2019072100001000',45,20,1529,1,1529,0),('2019072100001100',45,20,1529,1,1529,0),('2019072100001200',45,20,1529,1,1529,0),('2019072100001300',45,20,1529,1,1529,0),('2019072100001400',45,19,1529,1,1529,0),('2019072100001500',45,20,1529,1,1529,0),('2019072100001600',45,18,1529,1,1529,0),('2019072100001700',45,19,1529,1,1529,0),('2019072300001800',45,19,1529,1,1529,0),('2019072300001900',45,18,180,1,180,18),('2019072300002000',45,18,180,1,180,18),('2019072300002100',45,18,180,1,180,18),('2019072300002200',45,18,180,1,180,18),('2019072300002300',45,18,180,1,180,18),('2019072300002400',45,18,180,1,180,18),('2019072300002500',45,18,180,1,180,18),('2019072300002600',45,18,180,1,180,18),('2019072300002700',45,18,180,1,180,18),('2019072300002800',45,18,180,1,180,18),('2019072300002900',45,18,180,1,180,18),('2019072300003000',45,18,180,1,180,18),('2019072300003100',45,18,180,1,180,18);
/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promo`
--

DROP TABLE IF EXISTS `promo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `promo_name` varchar(255) NOT NULL DEFAULT '',
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `item_id` int(16) NOT NULL DEFAULT '0',
  `promo_item_price` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promo`
--

LOCK TABLES `promo` WRITE;
/*!40000 ALTER TABLE `promo` DISABLE KEYS */;
INSERT INTO `promo` VALUES (1,'iphon抢购活动','2019-02-14 19:51:59','2019-02-16 16:14:19',1,100),(18,'iphone抢购活动','2019-07-20 14:45:04','2019-07-24 16:15:14',18,180);
/*!40000 ALTER TABLE `promo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequence_info`
--

DROP TABLE IF EXISTS `sequence_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_info` (
  `name` varchar(255) NOT NULL DEFAULT '',
  `current_value` int(11) NOT NULL DEFAULT '0',
  `step` int(11) DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequence_info`
--

LOCK TABLES `sequence_info` WRITE;
/*!40000 ALTER TABLE `sequence_info` DISABLE KEYS */;
INSERT INTO `sequence_info` VALUES ('order_info',32,1);
/*!40000 ALTER TABLE `sequence_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL DEFAULT '',
  `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '//1男性，2女性',
  `age` int(2) NOT NULL DEFAULT '18',
  `telphone` varchar(11) NOT NULL DEFAULT '',
  `register_mode` varchar(16) NOT NULL DEFAULT '' COMMENT '//by phone, by wechat, by alipay',
  `third_part_id` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `telphone` (`telphone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,'asdfjhlk',1,18,'15432165465','byphone',''),(28,'刘亚恒',1,23,'15733093033','byphone',''),(29,'152',2,11,'15733093011','byphone',''),(30,'littlecurl',1,15,'15733093099','byphone',''),(31,'littlecurl',1,15,'15789465130','byphone',''),(32,'123456',1,22,'15777777777','byphone',''),(33,'littlecurl',1,15,'15111111111','byphone',''),(34,'qwe',1,123,'15888888888','byphone',''),(35,'qwe',1,15,'15555555555','byphone',''),(36,'111',1,88,'13345678888','byphone',''),(37,'12',1,18,'17111111111','byphone',''),(38,'123456',1,88,'18111111111','byphone',''),(39,'191',1,19,'19111111111','byphone',''),(40,'193',1,18,'19333333333','byphone',''),(41,'qq',1,11,'18011111111','byphone',''),(45,'刘亚恒',1,18,'15933093033','byphone',''),(46,'刘亚恒',1,14,'17987654321','byphone',''),(47,'tomcat',1,23,'13123456789','byphone','');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_password`
--

DROP TABLE IF EXISTS `user_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_password` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `encrypt_password` varchar(128) NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_password`
--

LOCK TABLES `user_password` WRITE;
/*!40000 ALTER TABLE `user_password` DISABLE KEYS */;
INSERT INTO `user_password` VALUES (20,'8otxyRDJyhtwHNqGV94Puw==',28),(21,'4QrcOUm6Wau+VuBX8g+IPg==',29),(22,'8otxyRDJyhtwHNqGV94Puw==',30),(23,'8otxyRDJyhtwHNqGV94Puw==',31),(24,'4QrcOUm6Wau+VuBX8g+IPg==',32),(25,'4QrcOUm6Wau+VuBX8g+IPg==',33),(26,'4QrcOUm6Wau+VuBX8g+IPg==',34),(27,'4QrcOUm6Wau+VuBX8g+IPg==',35),(28,'JfnnlDI7RTiF9RgfG2JNCw==',36),(29,'4QrcOUm6Wau+VuBX8g+IPg==',37),(30,'4QrcOUm6Wau+VuBX8g+IPg==',38),(31,'4QrcOUm6Wau+VuBX8g+IPg==',39),(32,'4QrcOUm6Wau+VuBX8g+IPg==',40),(33,'4QrcOUm6Wau+VuBX8g+IPg==',41),(34,'4QrcOUm6Wau+VuBX8g+IPg==',1),(35,'4QrcOUm6Wau+VuBX8g+IPg==',45),(36,'4QrcOUm6Wau+VuBX8g+IPg==',46),(37,'4QrcOUm6Wau+VuBX8g+IPg==',47);
/*!40000 ALTER TABLE `user_password` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-23 17:51:47
