# 图片 URL 修复完成

## 问题原因

之前使用了错误的环境变量 `import.meta.env.VITE_API_BASE_URL`（未定义），导致图片 URL 变成：
```
http://localhost:5173/api/undefined/uploads/notes/xxx.jpg  ❌
```

## 修复方案

统一使用 `/api` 前缀（Vite 会自动代理到 `http://localhost:8080`）：

### 1. content.js - 显示图片
```javascript
// 将 /uploads/... 转换为 /api/uploads/...
const apiBaseUrl = '/api'
return `<img src="${apiBaseUrl}${normalizedSrc}">`
```

### 2. Edit.vue - 上传图片
```javascript
// 上传后拼接 /api 前缀
const fullUrl = '/api' + imageUrl  // /api/uploads/notes/xxx.jpg
```

### 3. Edit.vue - 保存笔记
```javascript
// 保存时将 /api/uploads/... 转换回 /uploads/...
return content.replace(/src=["'](\/api)(\/uploads\/[^"']+)["']/gi, 'src="$2"')
```

## 数据流程

### 上传图片
1. 用户选择图片
2. 上传到服务器 → 返回 `/uploads/notes/xxx.jpg`
3. 拼接 `/api` → `/api/uploads/notes/xxx.jpg`
4. 插入编辑器

### 保存笔记
1. 编辑器内容：`<img src="/api/uploads/notes/xxx.jpg">`
2. 保存前转换：`<img src="/uploads/notes/xxx.jpg">`
3. 存入数据库

### 显示笔记
1. 从数据库读取：`<img src="/uploads/notes/xxx.jpg">`
2. 显示前转换：`<img src="/api/uploads/notes/xxx.jpg">`
3. 浏览器加载：Vite 代理到 `http://localhost:8080/api/uploads/notes/xxx.jpg`

## 测试步骤

### 1. 刷新前端
按 **Ctrl+F5** 强制刷新浏览器

### 2. 测试新建笔记
1. 新建笔记
2. 插入图片
3. 保存
4. 查看详情 → **图片应该正常显示**

### 3. 测试编辑笔记
1. 编辑刚才的笔记
2. 编辑器中 → **图片应该正常显示**
3. 可以继续添加图片
4. 保存后查看 → **所有图片都正常显示**

### 4. 验证数据库
```sql
SELECT 
    note_id, 
    title, 
    SUBSTRING(content, 1, 200) as content_preview
FROM note 
WHERE user_id = 7 
  AND content LIKE '%<img%'
ORDER BY created_at DESC 
LIMIT 1;
```

**预期结果**：
```html
<img src="/uploads/notes/xxx.jpg">
```

**不应该包含**：
- ❌ `/api/uploads/...`（保存时已去除）
- ❌ `undefined`
- ❌ 完整 URL `http://...`

## 验证清单

- ✅ 图片上传成功
- ✅ 编辑器中图片显示正常
- ✅ 保存笔记成功
- ✅ 笔记详情页图片显示正常
- ✅ 数据库只存相对路径 `/uploads/notes/xxx.jpg`
- ✅ 浏览器加载 `/api/uploads/notes/xxx.jpg`（Vite 代理）
- ✅ 实际请求 `http://localhost:8080/api/uploads/notes/xxx.jpg`

## 关键点

1. **数据库存储**：`/uploads/notes/xxx.jpg`（相对路径，无 /api）
2. **前端显示**：`/api/uploads/notes/xxx.jpg`（有 /api，Vite 代理）
3. **Vite 代理**：`/api/*` → `http://localhost:8080/api/*`
4. **最终请求**：`http://localhost:8080/api/uploads/notes/xxx.jpg`

现在图片应该可以正常显示了！🎉
