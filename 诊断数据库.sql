-- ============================================
-- 数据库诊断脚本
-- ============================================

USE cloudnote;

-- 1. 检查表是否存在
SHOW TABLES;

-- 2. 检查 user 表结构
DESC user;

-- 3. 检查 category 表结构
DESC category;

-- 4. 检查 note 表结构
DESC note;

-- 5. 查看用户数据
SELECT user_id, username, email, nickname, 
       LEFT(avatar, 50) AS avatar_preview,
       created_at 
FROM user;

-- 6. 查看分类数据
SELECT * FROM category;

-- 7. 查看笔记数据（前5条）
SELECT note_id, title, user_id, category_id, deleted, created_at 
FROM note 
LIMIT 5;

-- 8. 统计数据
SELECT 
    (SELECT COUNT(*) FROM user) AS 用户数,
    (SELECT COUNT(*) FROM category) AS 分类数,
    (SELECT COUNT(*) FROM note WHERE deleted = 0 OR deleted IS NULL) AS 笔记数;

-- ============================================
-- 如果 avatar 字段还是 TEXT 类型，执行以下修复
-- ============================================
-- ALTER TABLE `user` MODIFY COLUMN `avatar` VARCHAR(500) COMMENT '用户头像URL';

-- ============================================
-- 如果没有分类数据，执行以下插入
-- ============================================
-- INSERT INTO category (name, user_id) VALUES ('工作', 1);
-- INSERT INTO category (name, user_id) VALUES ('学习', 1);
-- INSERT INTO category (name, user_id) VALUES ('生活', 1);
