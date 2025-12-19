-- ============================================
-- 修复头像字段大小问题
-- ============================================

USE cloudnote;

-- 将 avatar 字段从 TEXT 改为 MEDIUMTEXT
-- TEXT: 最大 64KB (65,535 字节)
-- MEDIUMTEXT: 最大 16MB (16,777,215 字节)
-- LONGTEXT: 最大 4GB (4,294,967,295 字节)

ALTER TABLE `user` 
MODIFY COLUMN `avatar` MEDIUMTEXT COMMENT '用户头像（Base64或URL）';

-- 验证修改
DESC `user`;

-- 查看字段详细信息
SHOW FULL COLUMNS FROM `user` WHERE Field = 'avatar';

-- ============================================
-- 说明
-- ============================================
/*
MEDIUMTEXT 可以存储约 16MB 的数据，足够存储：
- 小图片（< 100KB）: 压缩后的头像
- 中等图片（100KB - 500KB）: 普通质量头像
- 大图片（500KB - 2MB）: 高质量头像

如果还是报错，可以改为 LONGTEXT：
ALTER TABLE `user` 
MODIFY COLUMN `avatar` LONGTEXT COMMENT '用户头像（Base64或URL）';
*/
