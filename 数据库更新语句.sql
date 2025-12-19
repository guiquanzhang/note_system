-- ============================================
-- 云笔记系统数据库更新语句
-- 版本: v1.1.0
-- 更新日期: 2024-12-20
-- ============================================

USE cloudnote;

-- ============================================
-- 1. 用户表增强 - 添加头像和昵称字段
-- ============================================

-- 添加头像字段（存储Base64或URL）
ALTER TABLE `user` 
ADD COLUMN `avatar` TEXT COMMENT '用户头像（Base64或URL）' AFTER `email`;

-- 添加昵称字段
ALTER TABLE `user` 
ADD COLUMN `nickname` VARCHAR(50) COMMENT '用户昵称' AFTER `username`;

-- ============================================
-- 2. 笔记表增强 - 添加软删除支持
-- ============================================

-- 添加删除标记字段
ALTER TABLE `note` 
ADD COLUMN `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0=正常，1=已删除' AFTER `tags`;

-- 添加删除时间字段
ALTER TABLE `note` 
ADD COLUMN `deleted_at` TIMESTAMP NULL COMMENT '删除时间' AFTER `deleted`;

-- 添加索引以提高查询性能
ALTER TABLE `note` 
ADD INDEX idx_deleted (`deleted`);

-- ============================================
-- 3. 验证更新结果
-- ============================================

-- 查看user表结构
DESC `user`;

-- 查看note表结构
DESC `note`;

-- ============================================
-- 4. 更新后的完整表结构
-- ============================================

/*
用户表 (user) - 更新后
+------------+--------------+------+-----+-------------------+
| Field      | Type         | Null | Key | Default           |
+------------+--------------+------+-----+-------------------+
| user_id    | int          | NO   | PRI | NULL              |
| username   | varchar(100) | NO   | UNI | NULL              |
| nickname   | varchar(50)  | YES  |     | NULL              | ← 新增
| password   | varchar(255) | NO   |     | NULL              |
| email      | varchar(100) | YES  |     | NULL              |
| avatar     | text         | YES  |     | NULL              | ← 新增
| created_at | timestamp    | YES  |     | CURRENT_TIMESTAMP |
| updated_at | timestamp    | YES  |     | CURRENT_TIMESTAMP |
+------------+--------------+------+-----+-------------------+

笔记表 (note) - 更新后
+-------------+--------------+------+-----+-------------------+
| Field       | Type         | Null | Key | Default           |
+-------------+--------------+------+-----+-------------------+
| note_id     | int          | NO   | PRI | NULL              |
| title       | varchar(255) | NO   |     | NULL              |
| content     | text         | YES  |     | NULL              |
| user_id     | int          | NO   | MUL | NULL              |
| category_id | int          | YES  | MUL | NULL              |
| tags        | varchar(255) | YES  |     | NULL              |
| deleted     | tinyint      | YES  | MUL | 0                 | ← 新增
| deleted_at  | timestamp    | YES  |     | NULL              | ← 新增
| created_at  | timestamp    | YES  |     | CURRENT_TIMESTAMP |
| updated_at  | timestamp    | YES  |     | CURRENT_TIMESTAMP |
+-------------+--------------+------+-----+-------------------+
*/

-- ============================================
-- 5. 数据迁移（如果需要）
-- ============================================

-- 为现有用户设置默认昵称（使用用户名）
UPDATE `user` SET `nickname` = `username` WHERE `nickname` IS NULL;

-- 确保所有现有笔记的deleted字段为0
UPDATE `note` SET `deleted` = 0 WHERE `deleted` IS NULL;

-- ============================================
-- 6. 回滚语句（如果需要撤销更新）
-- ============================================

/*
-- 删除新增的字段
ALTER TABLE `user` DROP COLUMN `avatar`;
ALTER TABLE `user` DROP COLUMN `nickname`;
ALTER TABLE `note` DROP COLUMN `deleted`;
ALTER TABLE `note` DROP COLUMN `deleted_at`;
ALTER TABLE `note` DROP INDEX idx_deleted;
*/

-- ============================================
-- 更新完成
-- ============================================
