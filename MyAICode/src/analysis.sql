-- noinspection SqlDialectInspectionForFile

/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : analysis

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 13/06/2022 13:25:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS `issue_source`;
DROP TABLE IF EXISTS `pattern_lk`;
DROP TABLE IF EXISTS `a_user_info`;
drop table if exists `knowledge`;
-- ----------------------------
-- Table structure for a_rule
-- ----------------------------
DROP TABLE IF EXISTS `a_rule`;
CREATE TABLE `a_rule`  (
                           `rule_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                           `rule_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `pattern_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `priority` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `kingdom` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `function_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
                           `state` int(11) DEFAULT NULL,
                           `version_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           PRIMARY KEY (`rule_id`) USING BTREE,
                           INDEX `version_id`(`version_id`) USING BTREE,
                           CONSTRAINT `a_rule_ibfk_1` FOREIGN KEY (`version_id`) REFERENCES `a_version` (`version_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_template
-- ----------------------------
DROP TABLE IF EXISTS `a_template`;
CREATE TABLE `a_template`  (
                               `template_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                               `template_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                               `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                               `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
                               `state` int(11) DEFAULT NULL,
                               `user_id` int(11) DEFAULT NULL,
                               PRIMARY KEY (`template_id`) USING BTREE,
                               INDEX `user_id`(`user_id`) USING BTREE,
                               CONSTRAINT `a_template_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `a_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_user
-- ----------------------------
DROP TABLE IF EXISTS `a_user`;
CREATE TABLE `a_user`  (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `a_code`;
CREATE TABLE `a_code`  (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `code_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                           `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        primary key (`id`) using BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `a_code`values(1,'demoCode1','This is a description.');
INSERT INTO `a_code`values(2,'demoCode2','This is another description.');
INSERT INTO `a_code`values(3,'demoCode3','This is a third description.');


DROP TABLE IF EXISTS `a_employee`;
create table `a_employee` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `employee_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
)ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_version
-- ----------------------------
DROP TABLE IF EXISTS `a_version`;
CREATE TABLE `a_version`  (
                              `version_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `version_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                              `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                              `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
                              `project_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                              `last_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                              `code_id` int(11) default null,
                              `folder_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                              `code_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                              PRIMARY KEY (`version_id`) USING BTREE,
                              INDEX `project_id`(`project_id`) USING BTREE,
                              CONSTRAINT `a_version_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                              constraint `a_version_ibfk_2` foreign key (`code_id`) references `a_code` (`id`) on delete restrict on update restrict
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for issue_basic
-- ----------------------------
DROP TABLE IF EXISTS `issue_basic`;
CREATE TABLE `issue_basic`  (
                                `issue_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `pattern_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `priority` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `kingdom` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `employee_id` int(11) default null,
                                `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `start_line` int(11) DEFAULT NULL,
                                `snippet` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
                                `target_function` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `version_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `flag` int(11) DEFAULT NULL,
                                `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `func_start_line`int(11) DEFAULT NULL,
                                `func_end_line`int(11) DEFAULT NULL,
                                PRIMARY KEY (`issue_id`) USING BTREE,
                                INDEX `pattern_id`(`pattern_id`) USING BTREE,
                                INDEX `version_id`(`version_id`) USING BTREE,
                                CONSTRAINT `issue_basic_ibfk_1` FOREIGN KEY (`pattern_id`) REFERENCES `pattern_info` (`pattern_info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                CONSTRAINT `issue_basic_ibfk_2` FOREIGN KEY (`version_id`) REFERENCES `a_version` (`version_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                CONSTRAINT `issue_basic_ibfk_3` FOREIGN KEY (`employee_id`) REFERENCES `a_employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pattern_info
-- ----------------------------
DROP TABLE IF EXISTS `pattern_info`;
CREATE TABLE `pattern_info`  (
  `pattern_info_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `explanation` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  `recommendation` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  `tip` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  `pattern_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `t_num` int(11) DEFAULT NULL,
  `f_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`pattern_info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pattern_statistics
-- ----------------------------
DROP TABLE IF EXISTS `pattern_statistics`;
CREATE TABLE `pattern_statistics`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue_num` int(11) DEFAULT NULL,
  `v_p_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `v_p_id`(`v_p_id`) USING BTREE,
  CONSTRAINT `pattern_statistics_ibfk_1` FOREIGN KEY (`v_p_id`) REFERENCES `version_pattern_rel` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for priority_statistics
-- ----------------------------
DROP TABLE IF EXISTS `priority_statistics`;
CREATE TABLE `priority_statistics`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `low_num` int(11) DEFAULT NULL,
  `medium_num` int(11) DEFAULT NULL,
  `high_num` int(11) DEFAULT NULL,
  `critical_num` int(11) DEFAULT NULL,
  `version_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `version_id`(`version_id`) USING BTREE,
  CONSTRAINT `priority_statistics_ibfk_1` FOREIGN KEY (`version_id`) REFERENCES `a_version` (`version_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `project_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `project_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `user_id` int(11) DEFAULT NULL,
  `show` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`project_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `a_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for version_pattern_rel
-- ----------------------------
DROP TABLE IF EXISTS `version_pattern_rel`;
CREATE TABLE `version_pattern_rel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `pattern_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `version_id`(`version_id`) USING BTREE,
  INDEX `pattern_id`(`pattern_id`) USING BTREE,
  CONSTRAINT `version_pattern_rel_ibfk_1` FOREIGN KEY (`version_id`) REFERENCES `a_version` (`version_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `version_pattern_rel_ibfk_2` FOREIGN KEY (`pattern_id`) REFERENCES `pattern_info` (`pattern_info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO`a_user` values (1,'zhuyifan',123,'13813989978','571341080@qq.com');
# insert into `project` values('97f8e190de4d496ebf784686d0d0ed41', 'xxl-job', '...', '2022-03-14 09:01:26', 1,null);
# INSERT INTO `a_version` VALUES ('demo', 'initial', 'data/jsfnsc/xxl-job/xxl-job-1.3.1.xml', '2022-05-12 07:56:11', '97f8e190de4d496ebf784686d0d0ed41', '2352e27f5ec942af9b1b27391a3881bd',null,'using code');
# INSERT INTO `issue_basic` values ('')
