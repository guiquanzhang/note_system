/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : cloudnote

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 15/05/2026 15:48:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `user_id` int NOT NULL COMMENT '用户ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
  `note_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '笔记标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `user_id` int NOT NULL COMMENT '用户ID',
  `category_id` int NULL DEFAULT NULL COMMENT '分类ID',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签，逗号分隔',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0=正常，1=已删除',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`note_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_deleted`(`deleted` ASC) USING BTREE,
  FULLTEXT INDEX `idx_title_content`(`title`, `content`),
  CONSTRAINT `note_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `note_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '笔记表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of note
-- ----------------------------

-- ----------------------------
-- Table structure for note_comment
-- ----------------------------
DROP TABLE IF EXISTS `note_comment`;
CREATE TABLE `note_comment`  (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `note_id` int NOT NULL COMMENT '笔记ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_note_id`(`note_id` ASC) USING BTREE,
  CONSTRAINT `note_comment_ibfk_1` FOREIGN KEY (`note_id`) REFERENCES `note` (`note_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `note_comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of note_comment
-- ----------------------------

-- ----------------------------
-- Table structure for note_tag
-- ----------------------------
DROP TABLE IF EXISTS `note_tag`;
CREATE TABLE `note_tag`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `note_id` int NOT NULL COMMENT '笔记ID',
  `tag_id` int NOT NULL COMMENT '标签ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_note_tag`(`note_id` ASC, `tag_id` ASC) USING BTREE,
  INDEX `idx_note_id`(`note_id` ASC) USING BTREE,
  INDEX `idx_tag_id`(`tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '笔记标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of note_tag
-- ----------------------------
INSERT INTO `note_tag` VALUES (4, 32, 4, '2025-12-25 00:01:52');
INSERT INTO `note_tag` VALUES (5, 33, 5, '2025-12-27 10:48:20');
INSERT INTO `note_tag` VALUES (6, 36, 6, '2026-01-11 23:49:57');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签颜色',
  `user_id` int NOT NULL COMMENT '用户ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tag_id`) USING BTREE,
  UNIQUE INDEX `uk_user_name`(`user_id` ASC, `name` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (4, '1', NULL, 9, '2025-12-25 00:01:41', '2025-12-25 00:01:41');
INSERT INTO `tag` VALUES (5, '高等数学', '#00f2fe', 10, '2025-12-27 10:47:46', '2025-12-27 10:47:46');
INSERT INTO `tag` VALUES (6, '11', NULL, 12, '2026-01-11 23:49:57', '2026-01-11 23:49:57');
INSERT INTO `tag` VALUES (7, 'asdasda', '#fee140', 13, '2026-04-15 16:36:53', '2026-04-15 16:36:53');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '11111111', NULL, '$2a$10$89NAUj5kRk8swkW0NXVVeOUcjFGjJbDNPXAK8.TLDymFYtWRKHTIG', '11111111@qq.com', NULL, '2026-04-15 17:16:39', '2026-04-15 17:16:39');
INSERT INTO `user` VALUES (2, '12345678', NULL, '$2a$10$b/4NGo1MSg.TT8KQnva0PuDcn3MbxPVcdwvaAy/bswwD5UsJ8bvdq', '12345678@qq.comqq.com', NULL, '2026-05-14 16:44:03', '2026-05-14 16:44:03');

SET FOREIGN_KEY_CHECKS = 1;
