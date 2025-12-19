-- ============================================
-- 将头像字段改为存储URL
-- ============================================

USE cloudnote;

-- 将 avatar 字段改为 VARCHAR(500)，存储文件路径
ALTER TABLE `user` 
MODIFY COLUMN `avatar` VARCHAR(500) COMMENT '用户头像URL';

-- 验证修改
DESC `user`;

-- 查看字段详细信息
SHOW FULL COLUMNS FROM `user` WHERE Field = 'avatar';

-- ============================================
-- 说明
-- ============================================
/*
现在 avatar 字段存储的是文件路径，例如：
- /uploads/avatars/abc123.jpg
- /uploads/avatars/def456.png

优点：
1. 数据库体积小
2. 查询速度快
3. 图片可以通过CDN加速
4. 支持多种图片格式
5. 易于管理和备份

文件存储位置：
- 开发环境: 项目根目录/uploads/avatars/
- 生产环境: 可配置为绝对路径，如 /var/www/uploads/avatars/
*/
