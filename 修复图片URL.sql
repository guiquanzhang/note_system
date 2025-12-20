-- ============================================
-- 修复笔记中的图片 URL
-- 将错误的 URL 修复为正确的相对路径
-- ============================================

USE cloudnote;

-- 1. 查看当前有问题的笔记
SELECT 
    note_id, 
    title, 
    SUBSTRING(content, 1, 300) as content_preview
FROM note 
WHERE content LIKE '%<img%'
  AND user_id = 7
ORDER BY created_at DESC;

-- 2. 修复包含 undefined 的 URL
-- 将 /api/undefined/uploads/... 修复为 /uploads/...
UPDATE note 
SET content = REPLACE(content, '/api/undefined/uploads/', '/uploads/')
WHERE content LIKE '%/api/undefined/uploads/%'
  AND user_id = 7;

-- 3. 修复包含完整域名的 URL
-- 将 http://localhost:5173/api/uploads/... 修复为 /uploads/...
UPDATE note 
SET content = REPLACE(content, 'http://localhost:5173/api/uploads/', '/uploads/')
WHERE content LIKE '%http://localhost:5173/api/uploads/%'
  AND user_id = 7;

-- 4. 修复包含 /api/ 前缀的 URL（保存时应该去除）
-- 将 /api/uploads/... 修复为 /uploads/...
UPDATE note 
SET content = REPLACE(content, 'src="/api/uploads/', 'src="/uploads/')
WHERE content LIKE '%src="/api/uploads/%'
  AND user_id = 7;

UPDATE note 
SET content = REPLACE(content, "src='/api/uploads/", "src='/uploads/")
WHERE content LIKE "%src='/api/uploads/%"
  AND user_id = 7;

-- 5. 验证修复结果
SELECT 
    note_id, 
    title, 
    SUBSTRING(content, 1, 300) as content_preview
FROM note 
WHERE content LIKE '%<img%'
  AND user_id = 7
ORDER BY created_at DESC;

-- ============================================
-- 修复完成
-- 预期结果：所有图片 URL 应该是 /uploads/notes/xxx.jpg
-- ============================================
