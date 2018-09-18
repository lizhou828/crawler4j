/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50628
Source Host           : 192.168.222.43:3306
Source Database       : land

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-09-18 16:41:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for second_land
-- ----------------------------
DROP TABLE IF EXISTS `second_land`;
CREATE TABLE `second_land` (
  `id` int(10) NOT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `href` varchar(255) NOT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `href_index` (`href`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
