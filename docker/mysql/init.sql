-- MySQL dump 10.13  Distrib 5.7.44, for Linux (x86_64)
--
-- Host: localhost    Database: iast
-- ------------------------------------------------------
-- Server version	5.7.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+08:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent`
(
    `agent_id`         varchar(32) NOT NULL,
    `os`               varchar(10)          DEFAULT NULL,
    `hostname`         varchar(100)         DEFAULT NULL,
    `server_path`      varchar(255)         DEFAULT NULL,
    `timestamp`        datetime             DEFAULT NULL,
    `state`            int(11)              DEFAULT '0',
    `project_name`     varchar(50) NOT NULL,
    `jdk_version`      varchar(10)          DEFAULT NULL,
    `version`          varchar(10)          DEFAULT NULL COMMENT 'agent版本',
    `process`          varchar(10)          DEFAULT NULL,
    `detection_status` int(11)     NOT NULL DEFAULT '1',
    memory_usage     double        null,
    cpu_usage        double        null,
    PRIMARY KEY (`agent_id`),
    KEY `agent_application_id_fk` (`project_name`),
    KEY `agent_host_name_IDX` (`hostname`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blacklist`
--

DROP TABLE IF EXISTS `blacklist`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blacklist`
(
    `id`   int(11)                           NOT NULL AUTO_INCREMENT,
    `name` varchar(128) CHARACTER SET latin1 NOT NULL,
    UNIQUE KEY `blacklist_pk` (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instruction`
--

DROP TABLE IF EXISTS `policy`;
create table policy
(
    id               int auto_increment
        primary key,
    agent_id         varchar(32)          not null,
    agent_enabled    tinyint(1) default 1 null,
    detect_enabled   tinyint(1) default 1 null,
    max_memory_usage int        default 0 null comment '是否已读',
    max_cpu_usage    int                  null,
    modified_time    datetime             null,
    constraint policy_unique
        unique (agent_id),
    constraint policy_agent_FK
        foreign key (agent_id) references agent (agent_id)
            on delete cascade
)
    charset = utf8;

--
-- Table structure for table `notify`
--

DROP TABLE IF EXISTS `notify`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notify`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `title`       varchar(100) NOT NULL COMMENT '通知标题',
    `datetime`    datetime     NOT NULL COMMENT '通知时间',
    `description` varchar(100)          DEFAULT NULL,
    `is_read`     int(11)      NOT NULL DEFAULT '0' COMMENT '是否已读',
    `report_id`   int(11)      NOT NULL,
    PRIMARY KEY (`id`),
    KEY `notify_report_FK` (`report_id`),
    CONSTRAINT `notify_report_FK` FOREIGN KEY (`report_id`) REFERENCES `report` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operation_logs`
--

DROP TABLE IF EXISTS `operation_logs`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation_logs`
(
    `id`              int(11)                         NOT NULL AUTO_INCREMENT,
    `operation_type`  varchar(50) CHARACTER SET utf8  NOT NULL COMMENT '操作类型（如：INSERT, UPDATE, DELETE, SELECT）',
    `operator_name`   varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '操作人用户名',
    `operation_time`  timestamp                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `content`         text CHARACTER SET utf8 COMMENT '操作内容简要描述',
    `operation_param` text CHARACTER SET utf8 COMMENT '修改内容详情或额外信息',
    `ip_address`      varchar(45) CHARACTER SET utf8           DEFAULT NULL COMMENT '操作时的IP地址',
    `device_info`     text CHARACTER SET utf8 COMMENT '设备信息（如：User-Agent等）',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 64
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `parent_id`   bigint(20)   DEFAULT NULL,
    `name`        varchar(64)  NOT NULL,
    `enname`      varchar(64)  NOT NULL,
    `url`         varchar(255) NOT NULL,
    `description` varchar(200) DEFAULT NULL,
    `created`     datetime     NOT NULL,
    `updated`     datetime     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project`
(
    `id`    int(11)     NOT NULL AUTO_INCREMENT,
    `name`  varchar(20) NOT NULL COMMENT '应用名',
    `tag`   varchar(1024)        DEFAULT NULL COMMENT '应用标签',
    `level` int(11)     NOT NULL DEFAULT '1' COMMENT '重要性',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8 COMMENT ='应用';
/*!40101 SET character_set_client = @saved_cs_client */;
insert into project(id, name, level) values (1, "default", 2);

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report`
(
    `id`                 int(11)                                         NOT NULL AUTO_INCREMENT,
    `agent_id`           varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
    `request_body`       text,
    `response_body`      text,
    `finding_data`       text,
    `last_timestamp`     datetime                                                 DEFAULT NULL,
    `level`              int(11)                                                  DEFAULT NULL,
    `duplicate_key`      varchar(32)                                     NOT NULL DEFAULT '"md5"',
    `url`                varchar(100)                                             DEFAULT NULL,
    `request_method`     varchar(5)                                               DEFAULT NULL,
    `vulnerable_type`    varchar(20)                                     NOT NULL DEFAULT '0',
    `response_header`    text,
    `request_header`     text,
    `status_code`        int(11)                                                  DEFAULT NULL,
    `protocol`           varchar(10)                                              DEFAULT NULL,
    `uri`                varchar(100)                                             DEFAULT NULL,
    `vulnerable_type_zh` varchar(100)                                             DEFAULT NULL,
    `first_timestamp`    datetime                                                 DEFAULT NULL,
    `status_id`          int(11)                                         NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `duplicate_key` (`duplicate_key`),
    KEY `report_vulnerability_status_FK` (`status_id`),
    CONSTRAINT `report_vulnerability_status_FK` FOREIGN KEY (`status_id`) REFERENCES `vulnerability_status` (`status_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT,
    `parent_id`   bigint(20)   DEFAULT NULL,
    `name`        varchar(64) NOT NULL,
    `enname`      varchar(64) NOT NULL,
    `description` varchar(200) DEFAULT NULL,
    `created`     datetime    NOT NULL,
    `updated`     datetime    NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `role_id`       bigint(20) NOT NULL,
    `permission_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user`
(
    `id`       bigint(20)  NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(64) NOT NULL COMMENT '用户密码',
    `phone`    varchar(20)          DEFAULT NULL COMMENT '用户手机号',
    `email`    varchar(50)          DEFAULT NULL COMMENT '用户emali',
    `created`  datetime    NOT NULL,
    `updated`  datetime             DEFAULT NULL,
    `state`    int(11)     NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8 COMMENT ='登录用户';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `user`
VALUES (1, 'admin', '$2a$10$9ZhDOBp.sRKat4l14ygu/.LscxrMUcDAfeVOEPiYwbcRkoB09gCmi', NULL, NULL, '2023-12-21 13:14:12',
        NULL, 1);

--
-- Table structure for table `vulnerability_status`
--

DROP TABLE IF EXISTS `vulnerability_status`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vulnerability_status`
(
    `status_id`   int(11)      NOT NULL AUTO_INCREMENT,
    `status_name` varchar(100) NOT NULL,
    `is_del`      int(11)      NOT NULL DEFAULT '1',
    PRIMARY KEY (`status_id`),
    UNIQUE KEY `vulnerability_status_unique` (`status_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;
INSERT INTO iast.vulnerability_status
(status_id, status_name, is_del)
VALUES(1, '上报', 0);
INSERT INTO iast.vulnerability_status
(status_id, status_name, is_del)
VALUES(2, '修复中', 0);
INSERT INTO iast.vulnerability_status
(status_id, status_name, is_del)
VALUES(3, '验证中', 0);
INSERT INTO iast.vulnerability_status
(status_id, status_name, is_del)
VALUES(4, '修复完成', 0);
INSERT INTO iast.vulnerability_status
(status_id, status_name, is_del)
VALUES(5, '误报', 0);
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;


-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;

-- SimpleIAST Database Permissions Configuration
-- Grant all privileges to iast user on iast database
GRANT ALL PRIVILEGES ON iast.* TO 'iast'@'%';
FLUSH PRIVILEGES;
