-- MySQL dump 10.13  Distrib 5.7.44, for Linux (x86_64)
--
-- Host: localhost    Database: iast
-- ------------------------------------------------------
-- Server version	5.7.44

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
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent` (
  `agent_id` varchar(32) NOT NULL,
  `os` varchar(10) DEFAULT NULL,
  `host_name` varchar(100) DEFAULT NULL,
  `server_path` varchar(255) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `state` int(11) DEFAULT '0',
  `app_id` int(11) NOT NULL,
  `jdk_version` varchar(10) DEFAULT NULL,
  `version` varchar(10) DEFAULT NULL COMMENT 'agent版本',
  PRIMARY KEY (`agent_id`),
  KEY `agent_application_id_fk` (`app_id`),
  KEY `agent_host_name_IDX` (`host_name`) USING BTREE,
  CONSTRAINT `agent_application_id_fk` FOREIGN KEY (`app_id`) REFERENCES `application` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '应用名',
  `tag` varchar(1024) DEFAULT NULL COMMENT '应用标签',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '重要性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='应用';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blacklist`
--
insert into application(id,name,level) values(1,"default",2);
DROP TABLE IF EXISTS `blacklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blacklist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET latin1 NOT NULL,
  UNIQUE KEY `blacklist_pk` (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instruction`
--

DROP TABLE IF EXISTS `instruction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instruction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(32) CHARACTER SET latin1 DEFAULT NULL,
  `name` varchar(10) CHARACTER SET latin1 DEFAULT NULL,
  `value` varchar(10) CHARACTER SET latin1 DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  PRIMARY KEY (`id`),
  UNIQUE KEY `instruction_pk` (`agent_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notify`
--

DROP TABLE IF EXISTS `notify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '通知标题',
  `datetime` datetime NOT NULL COMMENT '通知时间',
  `description` varchar(100) DEFAULT NULL,
  `is_read` int(11) NOT NULL DEFAULT '0' COMMENT '是否已读',
  `report_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `notify_report_FK` FOREIGN KEY (`report_id`) REFERENCES `report` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operation_logs`
--

DROP TABLE IF EXISTS `operation_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operation_type` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '操作类型（如：INSERT, UPDATE, DELETE, SELECT）',
  `operator_name` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '操作人用户名',
  `operation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `content` text CHARACTER SET utf8 COMMENT '操作内容简要描述',
  `extra_details` text CHARACTER SET utf8 COMMENT '修改内容详情或额外信息',
  `ip_address` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作时的IP地址',
  `device_info` text CHARACTER SET utf8 COMMENT '设备信息（如：User-Agent等）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `enname` varchar(64) NOT NULL,
  `url` varchar(255) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `request_body` text,
  `response_body` text,
  `finding_data` text,
  `timestamp` datetime DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `duplicate_key` varchar(32) NOT NULL DEFAULT '"md5"',
  `url` varchar(100) DEFAULT NULL,
  `request_method` varchar(5) DEFAULT NULL,
  `vulnerable_type` varchar(20) NOT NULL DEFAULT '0',
  `response_header` text,
  `request_header` text,
  `status_code` int(11) DEFAULT NULL,
  `protocol` varchar(10) DEFAULT NULL,
  `uri` varchar(100) DEFAULT NULL,
  `vulnerable_type_zh` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `duplicate_key` (`duplicate_key`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `enname` varchar(64) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '用户密码',
  `phone` varchar(20) DEFAULT NULL COMMENT '用户手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '用户emali',
  `created` datetime NOT NULL,
  `updated` datetime DEFAULT NULL,
  `state` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='登录用户';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','$2a$10$9ZhDOBp.sRKat4l14ygu/.LscxrMUcDAfeVOEPiYwbcRkoB09gCmi',NULL,NULL,'2023-12-21 13:14:12',NULL,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
