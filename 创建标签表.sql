-- ============================================
-- 创建标签表
-- ============================================

USE cloudnote;

-- 创建标签表
CREATE TABLE IF NOT EXISTS `tag` (
  `tag_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
  `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
  `color` VARCHAR(20) DEFAULT NULL COMMENT '标签颜色',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `uk_user_name` (`user_id`, `name`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 创建笔记标签关联表
CREATE TABLE IF NOT EXISTS `note_tag` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `note_id` INT NOT NULL COMMENT '笔记ID',
  `tag_id` INT NOT NULL COMMENT '标签ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `uk_note_tag` (`note_id`, `tag_id`),
  KEY `idx_note_id` (`note_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='笔记标签关联表';

-- 查看表结构
DESC `tag`;
DESC `note_tag`;

-- ============================================
-- 说明：
-- 1. tag 表存储所有标签
-- 2. note_tag 表存储笔记和标签的多对多关系
-- 3. 用户可以预先创建标签，也可以在创建笔记时自动创建
-- 4. 每个用户的标签名称唯一
-- ============================================
