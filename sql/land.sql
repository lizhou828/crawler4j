/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50628
Source Host           : localhost:3306
Source Database       : land

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-08-24 12:42:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for remise_notice
-- ----------------------------
DROP TABLE IF EXISTS `remise_notice`;
CREATE TABLE `remise_notice` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `type` int(4) DEFAULT NULL COMMENT '类型（1招标、2拍卖、3挂牌、4公开公告、5公告调整、6其他公告）',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `notice_num` varchar(255) NOT NULL COMMENT '公告号',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `areaName` varchar(255) DEFAULT NULL COMMENT '所在地区',
  `areaId` int(11) DEFAULT NULL COMMENT '地区id',
  `content` mediumtext COMMENT '网页内容',
  `href` varchar(255) NOT NULL COMMENT '源地址',
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `publish_dept_name` varchar(255) DEFAULT NULL COMMENT '发布单位名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `remise_notice_href_index` (`href`) USING BTREE COMMENT '原链接地址_唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=674 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for remise_notice_detail
-- ----------------------------
DROP TABLE IF EXISTS `remise_notice_detail`;
CREATE TABLE `remise_notice_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_id` bigint(20) NOT NULL COMMENT '公告id',
  `land_sn` varchar(255) DEFAULT NULL COMMENT '宗地编号',
  `land_total_area` varchar(255) DEFAULT NULL COMMENT '宗地总面积',
  `land_location` varchar(255) DEFAULT NULL COMMENT '宗地坐落',
  `sale_time` varchar(255) DEFAULT NULL COMMENT '出让年限',
  `plot_ratio` varchar(255) DEFAULT NULL COMMENT '容积率',
  `building_density` varchar(255) DEFAULT NULL COMMENT '建筑密度',
  `greening_rate` varchar(255) DEFAULT NULL COMMENT '绿化率 ',
  `building_limited_height` varchar(255) DEFAULT NULL COMMENT '建筑限高',
  `land_use_details` varchar(255) DEFAULT NULL COMMENT '土地用途明细',
  `investment_intensity` varchar(255) DEFAULT NULL COMMENT '投资强度',
  `cash_deposit` varchar(255) DEFAULT NULL COMMENT '保证金',
  `valuation_report_num` varchar(255) DEFAULT NULL COMMENT '估价报告备案号',
  `current_land_conditions` varchar(255) DEFAULT NULL COMMENT '现状土地条件',
  `starting_price` varchar(255) DEFAULT NULL COMMENT '起始价',
  `price_increase` varchar(255) DEFAULT NULL COMMENT '加价幅度',
  `open_start_time` varchar(255) DEFAULT NULL COMMENT '挂牌开始时间',
  `open_end_time` varchar(255) DEFAULT NULL COMMENT '挂牌截止时间',
  `remark` text COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=753 DEFAULT CHARSET=utf8;
