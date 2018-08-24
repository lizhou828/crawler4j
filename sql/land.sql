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
  `type` int(4) DEFAULT NULL COMMENT '���ͣ�1�бꡢ2������3���ơ�4�������桢5���������6�������棩',
  `title` varchar(255) NOT NULL COMMENT '����',
  `notice_num` varchar(255) NOT NULL COMMENT '�����',
  `publish_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `areaName` varchar(255) DEFAULT NULL COMMENT '���ڵ���',
  `areaId` int(11) DEFAULT NULL COMMENT '����id',
  `content` mediumtext COMMENT '��ҳ����',
  `href` varchar(255) NOT NULL COMMENT 'Դ��ַ',
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `publish_dept_name` varchar(255) DEFAULT NULL COMMENT '������λ����',
  PRIMARY KEY (`id`),
  UNIQUE KEY `remise_notice_href_index` (`href`) USING BTREE COMMENT 'ԭ���ӵ�ַ_Ψһ����'
) ENGINE=InnoDB AUTO_INCREMENT=674 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for remise_notice_detail
-- ----------------------------
DROP TABLE IF EXISTS `remise_notice_detail`;
CREATE TABLE `remise_notice_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '����',
  `notice_id` bigint(20) NOT NULL COMMENT '����id',
  `land_sn` varchar(255) DEFAULT NULL COMMENT '�ڵر��',
  `land_total_area` varchar(255) DEFAULT NULL COMMENT '�ڵ������',
  `land_location` varchar(255) DEFAULT NULL COMMENT '�ڵ�����',
  `sale_time` varchar(255) DEFAULT NULL COMMENT '��������',
  `plot_ratio` varchar(255) DEFAULT NULL COMMENT '�ݻ���',
  `building_density` varchar(255) DEFAULT NULL COMMENT '�����ܶ�',
  `greening_rate` varchar(255) DEFAULT NULL COMMENT '�̻��� ',
  `building_limited_height` varchar(255) DEFAULT NULL COMMENT '�����޸�',
  `land_use_details` varchar(255) DEFAULT NULL COMMENT '������;��ϸ',
  `investment_intensity` varchar(255) DEFAULT NULL COMMENT 'Ͷ��ǿ��',
  `cash_deposit` varchar(255) DEFAULT NULL COMMENT '��֤��',
  `valuation_report_num` varchar(255) DEFAULT NULL COMMENT '���۱��汸����',
  `current_land_conditions` varchar(255) DEFAULT NULL COMMENT '��״��������',
  `starting_price` varchar(255) DEFAULT NULL COMMENT '��ʼ��',
  `price_increase` varchar(255) DEFAULT NULL COMMENT '�Ӽ۷���',
  `open_start_time` varchar(255) DEFAULT NULL COMMENT '���ƿ�ʼʱ��',
  `open_end_time` varchar(255) DEFAULT NULL COMMENT '���ƽ�ֹʱ��',
  `remark` text COMMENT '��ע',
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=753 DEFAULT CHARSET=utf8;
