-- ============================================
-- 回收站功能快速修复脚本
-- ============================================

USE cloudnote;

-- 1. 检查 deleted 字段
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    COLUMN_DEFAULT, 
    IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'cloudnote' 
  AND TABLE_NAME = 'note' 
  AND COLUMN_NAME IN ('deleted', 'deleted_at');

-- 2. 确保字段类型正确
ALTER TABLE note 
MODIFY COLUMN deleted TINYINT DEFAULT 0 COMMENT '是否删除：0=正常，1=已删除';

ALTER TABLE note 
MODIFY COLUMN deleted_at TIMESTAMP NULL COMMENT '删除时间';

-- 3. 修复现有数据（将 NULL 设置为 0）
UPDATE note SET deleted = 0 WHERE deleted IS NULL;

-- 4. 创建一条测试数据（用你的 user_id）
INSERT INTO note (title, content, user_id, deleted, deleted_at, created_at, updated_at) 
VALUES ('测试回收站笔记', '这是一条用于测试回收站功能的笔记', 1, 1, NOW(), NOW(), NOW());

-- 5. 验证结果
SELECT '=== 所有笔记 ===' AS info;
SELECT note_id, title, user_id, deleted, deleted_at FROM note ORDER BY note_id DESC LIMIT 5;

SELECT '=== 已删除笔记（回收站）===' AS info;
SELECT note_id, title, user_id, deleted, deleted_at FROM note WHERE deleted = 1;

SELECT '=== 正常笔记 ===' AS info;
SELECT note_id, title, user_id, deleted, deleted_at FROM note WHERE deleted = 0 LIMIT 5;

-- ============================================
-- 执行完成
-- ============================================
