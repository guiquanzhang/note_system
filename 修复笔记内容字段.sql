-- ============================================
-- 修复笔记内容字段 - 支持存储大量图片
-- ============================================

USE cloudnote;

-- 将 content 字段从 TEXT 改为 LONGTEXT
-- TEXT: 最大 65,535 字节 (约 64KB)
-- LONGTEXT: 最大 4,294,967,295 字节 (约 4GB)
ALTER TABLE `note` 
MODIFY COLUMN `content` LONGTEXT COMMENT '笔记内容（支持富文本和图片）';

-- 验证修改结果
DESC `note`;

-- 查看 content 字段的详细信息
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'cloudnote'
  AND TABLE_NAME = 'note'
  AND COLUMN_NAME = 'content';

-- ============================================
-- 修复完成
-- ============================================
