-- ============================================
-- 快速修复脚本
-- ============================================

USE cloudnote;

-- ============================================
-- 1. 修复 avatar 字段（如果还没执行）
-- ============================================
ALTER TABLE `user` 
MODIFY COLUMN `avatar` VARCHAR(500) COMMENT '用户头像URL';

-- ============================================
-- 2. 检查并修复用户数据
-- ============================================

-- 查看所有用户
SELECT user_id, username, email, nickname, avatar, created_at FROM user;

-- 如果没有用户，创建测试用户（密码: 123456，已BCrypt加密）
-- INSERT INTO user (username, password, email, nickname) VALUES 
-- ('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHCYAKMnBYNDRwsYLfKRCm', 'test@example.com', '测试用户');

-- ============================================
-- 3. 检查并修复分类数据
-- ============================================

-- 查看所有分类
SELECT * FROM category;

-- 如果没有分类，为每个用户创建默认分类
INSERT INTO category (name, user_id)
SELECT '工作', user_id FROM user WHERE NOT EXISTS (
    SELECT 1 FROM category WHERE name = '工作' AND category.user_id = user.user_id
);

INSERT INTO category (name, user_id)
SELECT '学习', user_id FROM user WHERE NOT EXISTS (
    SELECT 1 FROM category WHERE name = '学习' AND category.user_id = user.user_id
);

INSERT INTO category (name, user_id)
SELECT '生活', user_id FROM user WHERE NOT EXISTS (
    SELECT 1 FROM category WHERE name = '生活' AND category.user_id = user.user_id
);

-- ============================================
-- 4. 检查笔记软删除字段
-- ============================================

-- 确保所有笔记的 deleted 字段为 0
UPDATE note SET deleted = 0 WHERE deleted IS NULL OR deleted != 0;

-- ============================================
-- 5. 验证修复结果
-- ============================================

-- 查看表结构
DESC user;
DESC category;
DESC note;

-- 查看数据统计
SELECT 
    '用户数' AS 项目,
    COUNT(*) AS 数量
FROM user
UNION ALL
SELECT 
    '分类数',
    COUNT(*)
FROM category
UNION ALL
SELECT 
    '笔记数（未删除）',
    COUNT(*)
FROM note
WHERE deleted = 0;

-- ============================================
-- 完成
-- ============================================
SELECT '✅ 数据库修复完成！' AS 状态;
