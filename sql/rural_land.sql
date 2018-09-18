/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50628
Source Host           : 192.168.222.43:3306
Source Database       : land

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-09-18 16:43:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rural_land
-- ----------------------------
DROP TABLE IF EXISTS `rural_land`;
CREATE TABLE `rural_land` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL COMMENT '标题',
  `href` varchar(255) NOT NULL COMMENT '源链接',
  `content` mediumtext NOT NULL COMMENT '内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `href_index` (`href`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
