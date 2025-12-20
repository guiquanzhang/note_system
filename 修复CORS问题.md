# 🔧 修复 CORS 问题

## 问题原因

删除笔记时出现 CORS 错误：
```
Access to fetch at 'http://localhost:8080/api/note/12' from origin 'http://localhost:5173' 
has been blocked by CORS policy: Response to preflight request doesn't pass access control check: 
No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

**原因**：后端 WebConfig 中没有配置 CORS（跨域资源共享）。

---

## 已修复

在 `src/main/java/com/cloudnote/config/WebConfig.java` 中添加了 CORS 配置：

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    // 配置跨域
    registry.addMapping("/**")
            .allowedOriginPatterns("*")  // 允许所有来源
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的方法
            .allowedHeaders("*")  // 允许的请求头
            .allowCredentials(true)  // 允许携带凭证
            .maxAge(3600);  // 预检请求的有效期（秒）
}
```

---

## 🚀 重启后端

**必须重启后端才能生效！**

### 步骤：
1. 在 IDEA 中停止 CloudNoteApplication
2. 点击 **Build → Rebuild Project**
3. 重新运行 CloudNoteApplication
4. 等待看到 "Started CloudNoteApplication"

---

## ✅ 验证修复

重启后端后，在浏览器控制台执行：

```javascript
// 测试删除接口
const token = localStorage.getItem('token')

fetch('http://localhost:8080/api/note/12', {
  method: 'DELETE',
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(res => res.json())
.then(data => {
  console.log('删除成功:', data)
})
.catch(err => {
  console.error('删除失败:', err)
})
```

**预期结果**：
- 不再出现 CORS 错误
- 返回：`{"code":200,"message":"操作成功","data":null}`

---

## 🧪 完整测试流程

### 1. 重启后端
按照上面的步骤重启

### 2. 删除一条笔记
在笔记列表中删除一条笔记

### 3. 验证笔记列表
笔记应该从列表中消失

### 4. 查看回收站
访问回收站，应该能看到刚删除的笔记

### 5. 测试恢复
在回收站中点击"恢复"，笔记应该回到列表中

---

## 📝 CORS 配置说明

### allowedOriginPatterns("*")
- 允许所有来源访问
- 开发环境使用
- 生产环境应该指定具体域名

### allowedMethods
- GET：查询
- POST：创建
- PUT：更新
- DELETE：删除
- OPTIONS：预检请求

### allowCredentials(true)
- 允许携带 Cookie 和 Authorization 头
- 必须设置为 true，否则 JWT Token 无法传递

### maxAge(3600)
- 预检请求的缓存时间（秒）
- 减少 OPTIONS 请求次数

---

## 🎉 修复后的功能

重启后端后，以下功能应该全部正常：

- ✅ 删除笔记（软删除）
- ✅ 笔记从列表中消失
- ✅ 笔记出现在回收站
- ✅ 恢复笔记
- ✅ 永久删除笔记
- ✅ 清空回收站

---

**重启后端后，所有功能都应该正常工作了！** 🚀
