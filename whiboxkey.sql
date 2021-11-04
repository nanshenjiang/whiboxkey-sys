/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : whiboxkey

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 04/11/2021 19:43:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for client_key
-- ----------------------------
DROP TABLE IF EXISTS `client_key`;
CREATE TABLE `client_key`  (
  `client_id` bigint(0) NOT NULL,
  `key_id` bigint(0) NOT NULL,
  UNIQUE INDEX `UK_an5r75i0q4aj7rtah0bdy6r2a`(`key_id`) USING BTREE,
  INDEX `FK3wbxf20i54w37fsx1wtwjrnmu`(`client_id`) USING BTREE,
  CONSTRAINT `FK3wbxf20i54w37fsx1wtwjrnmu` FOREIGN KEY (`client_id`) REFERENCES `gateway_client` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKfe6objggtj4h9qewxeppfd39f` FOREIGN KEY (`key_id`) REFERENCES `key_msg` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gateway_client
-- ----------------------------
DROP TABLE IF EXISTS `gateway_client`;
CREATE TABLE `gateway_client`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `gateway_server_serial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `serial` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `vaild` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_il2m9jkehackdgxg2q0nxojh7`(`serial`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gateway_server
-- ----------------------------
DROP TABLE IF EXISTS `gateway_server`;
CREATE TABLE `gateway_server`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `serial` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `vaild` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_hqwpneoi3049qytqtjr1jjwl7`(`serial`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gateway_server_client
-- ----------------------------
DROP TABLE IF EXISTS `gateway_server_client`;
CREATE TABLE `gateway_server_client`  (
  `server_id` bigint(0) NOT NULL,
  `client_id` bigint(0) NOT NULL,
  UNIQUE INDEX `UK_bq4hc5pihdwumc8sumieh3j7e`(`client_id`) USING BTREE,
  INDEX `FK8fpn0vu3ncg9cg98pr99or3qn`(`server_id`) USING BTREE,
  CONSTRAINT `FK7ja7cixvpjojslj4y5yj7rki2` FOREIGN KEY (`client_id`) REFERENCES `gateway_client` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK8fpn0vu3ncg9cg98pr99or3qn` FOREIGN KEY (`server_id`) REFERENCES `gateway_server` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for key_msg
-- ----------------------------
DROP TABLE IF EXISTS `key_msg`;
CREATE TABLE `key_msg`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `duration` int(0) NOT NULL,
  `up_or_down` bit(1) NOT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `whibox_alg_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for key_wbkey
-- ----------------------------
DROP TABLE IF EXISTS `key_wbkey`;
CREATE TABLE `key_wbkey`  (
  `key_id` bigint(0) NOT NULL,
  `wb_key_id` bigint(0) NOT NULL,
  UNIQUE INDEX `UK_3lmb7o4032tio7sfm1hwb3e1u`(`wb_key_id`) USING BTREE,
  INDEX `FK21t5oej54rfwc0pp9bov7nern`(`key_id`) USING BTREE,
  CONSTRAINT `FK21t5oej54rfwc0pp9bov7nern` FOREIGN KEY (`key_id`) REFERENCES `key_msg` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKs7c29fomt4kw1k93844ww9uoa` FOREIGN KEY (`wb_key_id`) REFERENCES `whibox_key` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for whibox_key
-- ----------------------------
DROP TABLE IF EXISTS `whibox_key`;
CREATE TABLE `whibox_key`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `black_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `effective_time` datetime(6) NULL DEFAULT NULL,
  `key_fpath` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pass` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `version` bigint(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_djsx0gt208xigsvd8xcxeyoqd`(`pass`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
