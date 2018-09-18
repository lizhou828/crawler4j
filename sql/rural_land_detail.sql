/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50628
Source Host           : 192.168.222.43:3306
Source Database       : land

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-09-18 16:43:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rural_land_detail
-- ----------------------------
DROP TABLE IF EXISTS `rural_land_detail`;
CREATE TABLE `rural_land_detail` (
  `id` bigint(20) NOT NULL,
  `rural_land_id` int(11) NOT NULL,
  `rent` varchar(255) NOT NULL COMMENT '交易费用',
  `transfer_years` int(11) NOT NULL COMMENT '流转年限',
  `land_type` varchar(255) NOT NULL COMMENT '土地类型',
  `massif_area` varchar(255) NOT NULL COMMENT '地块面积',
  `

circulation_mode` varchar(255) NOT NULL COMMENT '流转方式',
  `number_plots` varchar(255) NOT NULL COMMENT '地块编号',
  `custom_tags` varchar(255) DEFAULT NULL COMMENT '自定义标签',
  `warrant` varchar(255) DEFAULT NULL COMMENT '权证',
  `warrant_time` varchar(255) DEFAULT NULL COMMENT '权证时间',
  `regional_location` varchar(255) DEFAULT NULL COMMENT '地区位置',
  `contacts_phone` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `contacts` varchar(255) DEFAULT NULL COMMENT '联系人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `rural_land_id_index` (`rural_land_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
