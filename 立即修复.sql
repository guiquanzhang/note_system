-- ============================================
-- 立即修复图片 URL
-- ============================================

USE cloudnote;

-- 查看修复前的内容
SELECT note_id, title, content 
FROM note 
WHERE note_id = 23;

-- 修复：将 undefined/uploads/ 替换为 /uploads/
UPDATE note 
SET content = REPLACE(content, 'undefined/uploads/', '/uploads/')
WHERE note_id = 23;

-- 修复：将 /api/undefined/uploads/ 替换为 /uploads/
UPDATE note 
SET content = REPLACE(content, '/api/undefined/uploads/', '/uploads/')
WHERE note_id = 23;

-- 修复：将 src="/api/uploads/ 替换为 src="/uploads/
UPDATE note 
SET content = REPLACE(content, 'src="/api/uploads/', 'src="/uploads/')
WHERE note_id = 23;

-- 查看修复后的内容
SELECT note_id, title, content 
FROM note 
WHERE note_id = 23;

-- ============================================
-- 修复所有用户的笔记（如果有其他笔记也有问题）
-- ============================================

-- 修复所有包含 undefined 的图片 URL
UPDATE note 
SET content = REPLACE(content, 'undefined/uploads/', '/uploads/')
WHERE content LIKE '%undefined/uploads/%';

UPDATE note 
SET content = REPLACE(content, '/api/undefined/uploads/', '/uploads/')
WHERE content LIKE '%/api/undefined/uploads/%';

UPDATE note 
SET content = REPLACE(content, 'src="/api/uploads/', 'src="/uploads/')
WHERE content LIKE '%src="/api/uploads/%';

-- 查看所有修复后的笔记
SELECT note_id, title, SUBSTRING(content, 1, 200) as content_preview
FROM note 
WHERE content LIKE '%<img%'
ORDER BY created_at DESC;
