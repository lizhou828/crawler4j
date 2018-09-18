/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50628
Source Host           : 192.168.222.43:3306
Source Database       : land

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-09-18 16:41:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for second_land_detail
-- ----------------------------
DROP TABLE IF EXISTS `second_land_detail`;
CREATE TABLE `second_land_detail` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `second_land_id` int(10) NOT NULL COMMENT '二手土地id',
  `land_type` int(2) DEFAULT NULL COMMENT '地块类型（1宅基地，2工商用地）',
  `land_code` varchar(255) DEFAULT NULL COMMENT '土地编码',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `usage` varchar(255) DEFAULT NULL COMMENT '用途',
  `land_price` varchar(255) DEFAULT NULL COMMENT '土地价格',
  `transfer_type` varchar(255) DEFAULT NULL COMMENT '流转类型',
  `transfer_time` varchar(255) DEFAULT NULL COMMENT '流转年限',
  `total_area` varchar(255) DEFAULT NULL COMMENT '土地面积',
  `regional_location` varchar(255) DEFAULT NULL COMMENT '地区位置',
  `contacts` varchar(255) DEFAULT NULL COMMENT '联系人',
  `contacts_phone` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `create_time` datetime DEFAULT NULL,
  `from` varchar(255) DEFAULT '' COMMENT '信息来源',
  PRIMARY KEY (`id`),
  KEY `second_land_id_index` (`second_land_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
