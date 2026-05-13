-- 数据分析功能迁移脚本
-- 为现有数据库添加软删除字段

USE cloudnote;

-- 检查并添加 deleted 字段
ALTER TABLE `note` 
ADD COLUMN IF NOT EXISTS `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除' AFTER `tags`;

-- 检查并添加 deleted_at 字段
ALTER TABLE `note` 
ADD COLUMN IF NOT EXISTS `deleted_at` TIMESTAMP NULL COMMENT '删除时间' AFTER `deleted`;

-- 添加索引以提高查询性能
ALTER TABLE `note` 
ADD INDEX IF NOT EXISTS `idx_deleted` (`deleted`);

-- 更新现有数据，确保 deleted 字段有默认值
UPDATE `note` SET `deleted` = 0 WHERE `deleted` IS NULL;

-- 提示
SELECT '数据库迁移完成！已添加软删除字段。' AS message;
