# CloudNote 云笔记系统
## 项目演示 PPT

---

## 第1页：项目概述

### CloudNote - 现代化云笔记管理系统

**项目简介**
- 基于 Spring Boot + Vue 3 的前后端分离项目
- 提供完整的笔记管理解决方案
- 支持富文本编辑、分类管理、标签系统、回收站等功能

**核心特性**
- 🔐 JWT 无状态认证
- 📝 富文本编辑器（Quill）
- 🏷️ 灵活的标签系统
- 🗑️ 回收站机制
- 📱 响应式设计
- 🎨 iPhone 风格 UI

**技术栈**
- 后端：Spring Boot 2.7.18 + MyBatis Plus + MySQL 8.0
- 前端：Vue 3 + Element Plus + Vite

---

## 第2页：系统架构

### 整体架构设计

```
┌─────────────────────────────────────────┐
│         前端层 (Vue 3 + Vite)           │
│  ┌──────────┐  ┌──────────┐  ┌────────┐│
│  │ 用户界面 │  │ 状态管理 │  │ 路由   ││
│  │ Element+ │  │  Pinia   │  │ Router ││
│  └──────────┘  └──────────┘  └────────┘│
└─────────────────┬───────────────────────┘
                  │ HTTP/JSON (Axios)
┌─────────────────┴───────────────────────┐
│      后端层 (Spring Boot)               │
│  ┌──────────────────────────────────┐   │
│  │  JWT 拦截器 (权限控制)           │   │
│  └──────────────────────────────────┘   │
│  ┌──────────────────────────────────┐   │
│  │  Controller (REST API)           │   │
│  └──────────────────────────────────┘   │
│  ┌──────────────────────────────────┐   │
│  │  Service (业务逻辑)              │   │
│  └──────────────────────────────────┘   │
│  ┌──────────────────────────────────┐   │
│  │  Mapper (MyBatis Plus)           │   │
│  └──────────────────────────────────┘   │
└─────────────────┬───────────────────────┘
                  │ JDBC
┌─────────────────┴───────────────────────┐
│         数据层 (MySQL 8.0)              │
│  user | note | category | tag | note_tag│
└─────────────────────────────────────────┘
```

**架构优势**
- 前后端分离，独立开发部署
- RESTful API 设计，接口清晰
- 分层架构，职责明确
- 易于扩展和维护

---

## 第3页：核心功能展示

### 功能模块

**1. 用户管理**
- 用户注册/登录
- JWT Token 认证
- 个人信息管理
- 头像上传

**2. 笔记管理**
- 笔记 CRUD 操作
- 富文本编辑（支持图片）
- 笔记搜索
- 分类管理

**3. 标签系统**
- 标签创建/编辑/删除
- 笔记标签关联
- 标签颜色自定义
- 标签筛选

**4. 回收站**
- 软删除机制
- 笔记恢复
- 永久删除
- 清空回收站

**5. 文件管理**
- 图片上传
- 文件存储
- 静态资源访问

---

## 第4页：技术难点 1 - 标签系统重构

### 问题背景
**原始设计缺陷**
- 标签存储在 note 表的 tags 字段（VARCHAR，逗号分隔）
- 标签管理页面创建的标签无法在笔记中使用
- 笔记中创建的标签不同步到标签管理
- 无法统计标签使用情况
- 难以维护和查询

### 解决方案
**重构为独立表 + 关联表**

```sql
-- 标签表
CREATE TABLE tag (
  tag_id INT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  color VARCHAR(20),
  user_id INT,
  UNIQUE KEY (user_id, name)
);

-- 笔记标签关联表
CREATE TABLE note_tag (
  id INT PRIMARY KEY,
  note_id INT,
  tag_id INT,
  UNIQUE KEY (note_id, tag_id)
);
```

**技术实现**
```java
// 笔记保存时自动处理标签
private void handleNoteTags(Integer noteId, List<Integer> tagIds) {
    // 1. 删除旧的关联关系
    noteTagMapper.delete(new LambdaQueryWrapper<NoteTag>()
        .eq(NoteTag::getNoteId, noteId));
    
    // 2. 批量插入新的关联关系
    if (tagIds != null && !tagIds.isEmpty()) {
        List<NoteTag> noteTags = new ArrayList<>();
        for (Integer tagId : tagIds) {
            NoteTag noteTag = new NoteTag();
            noteTag.setNoteId(noteId);
            noteTag.setTagId(tagId);
            noteTags.add(noteTag);
        }
        noteTagService.saveBatch(noteTags);
    }
}
```

**优势**
- ✅ 标签管理和笔记完全同步
- ✅ 支持标签统计和筛选
- ✅ 数据结构清晰，易于维护
- ✅ 符合数据库设计规范

---

## 第5页：技术难点 2 - 回收站软删除

### 问题背景
**MyBatis Plus 逻辑删除冲突**
- 配置了全局逻辑删除：`logic-delete-field: deleted`
- `updateById()` 方法无法更新 deleted 字段
- 恢复笔记时无法将 deleted 从 1 改为 0
- 查询已删除笔记时被自动过滤

### 解决方案
**使用自定义 SQL 绕过逻辑删除**

```java
// Mapper 层 - 自定义 SQL
@Mapper
public interface NoteMapper extends BaseMapper<Note> {
    
    // 恢复笔记（绕过逻辑删除）
    @Update("UPDATE note SET deleted = 0, deleted_at = NULL " +
            "WHERE note_id = #{noteId} AND user_id = #{userId}")
    int restoreNote(@Param("noteId") Integer noteId, 
                    @Param("userId") Integer userId);
    
    // 查询包含已删除的笔记
    @Select("SELECT * FROM note " +
            "WHERE note_id = #{noteId} AND user_id = #{userId}")
    Note selectByIdIncludeDeleted(@Param("noteId") Integer noteId, 
                                   @Param("userId") Integer userId);
    
    // 永久删除
    @Delete("DELETE FROM note " +
            "WHERE note_id = #{noteId} AND user_id = #{userId}")
    int permanentDelete(@Param("noteId") Integer noteId, 
                        @Param("userId") Integer userId);
}
```

**Service 层实现**
```java
// 恢复笔记
public void restore(Integer noteId) {
    Integer userId = UserContext.getUserId();
    
    // 使用自定义 SQL 查询（包含已删除）
    Note note = noteMapper.selectByIdIncludeDeleted(noteId, userId);
    if (note == null) {
        throw new RuntimeException("笔记不存在或无权限");
    }
    
    // 使用自定义 SQL 恢复
    int rows = noteMapper.restoreNote(noteId, userId);
    if (rows == 0) {
        throw new RuntimeException("恢复失败");
    }
}
```

**关键点**
- 🔑 使用 `@Update` 注解直接操作数据库
- 🔑 自定义查询方法绕过逻辑删除过滤
- 🔑 保持权限验证（user_id 条件）

---

## 第6页：技术难点 3 - 图片上传优化

### 问题背景
**Base64 存储的问题**
- 图片以 Base64 编码存储在 content 字段
- 数据库体积急剧增大
- 查询性能下降
- 单个笔记超过 TEXT 字段限制（64KB）
- 无法使用 CDN 加速

### 解决方案
**改为服务器文件存储**

**后端实现**
```java
@PostMapping("/upload/note-image")
public Result<String> uploadNoteImage(@RequestParam("file") MultipartFile file) {
    // 1. 验证文件类型
    String contentType = file.getContentType();
    if (!contentType.startsWith("image/")) {
        throw new RuntimeException("只能上传图片文件");
    }
    
    // 2. 生成唯一文件名
    String originalFilename = file.getOriginalFilename();
    String extension = originalFilename.substring(
        originalFilename.lastIndexOf("."));
    String filename = UUID.randomUUID().toString() + extension;
    
    // 3. 保存文件
    String relativePath = "/uploads/notes/" + filename;
    File dest = new File(uploadPath + "/notes", filename);
    file.transferTo(dest);
    
    // 4. 返回相对路径
    return Result.success(relativePath);
}
```

**前端实现**
```javascript
// 自定义图片上传处理器
const handleImageUpload = () => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'
  
  input.onchange = async () => {
    const file = input.files[0]
    const formData = new FormData()
    formData.append('file', file)
    
    // 上传到服务器
    const res = await uploadNoteImage(formData)
    const imageUrl = res.data  // /uploads/notes/xxx.jpg
    
    // 插入编辑器
    const quill = quillEditor.value.getQuill()
    const range = quill.getSelection()
    quill.insertEmbed(range.index, 'image', imageUrl)
  }
  
  input.click()
}
```

**数据库优化**
```sql
-- 将 content 字段改为 LONGTEXT
ALTER TABLE note 
MODIFY COLUMN content LONGTEXT COMMENT '笔记内容';
```

**优势**
- ✅ 数据库体积减小 90%+
- ✅ 查询性能提升
- ✅ 支持大量图片
- ✅ 可迁移到 OSS/CDN

---

## 第7页：技术难点 4 - JWT 认证与权限控制

### 认证流程设计

**JWT Token 生成**
```java
public String generateToken(Integer userId, String username) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);
    
    SecretKey key = Keys.hmacShaKeyFor(
        secret.getBytes(StandardCharsets.UTF_8));
    
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("username", username)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
}
```

**JWT 拦截器**
```java
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) {
        // 1. 获取 Token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }
        
        // 2. 验证 Token
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            return false;
        }
        
        // 3. 提取用户信息并存入上下文
        Integer userId = jwtUtil.getUserIdFromToken(token);
        UserContext.setUserId(userId);
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Object handler, Exception ex) {
        // 清除上下文（防止内存泄漏）
        UserContext.clear();
    }
}
```

**数据级权限控制**
```java
// 所有业务操作都验证数据所有权
public NoteVO getById(Integer noteId) {
    Integer userId = UserContext.getUserId();
    Note note = noteMapper.selectById(noteId);
    
    if (note == null) {
        throw new RuntimeException("笔记不存在");
    }
    
    // 验证所有权
    if (!note.getUserId().equals(userId)) {
        throw new RuntimeException("无权限访问");
    }
    
    return convertToVO(note);
}
```

**安全特性**
- 🔒 无状态认证，支持分布式
- 🔒 Token 有效期 7 天
- 🔒 所有接口验证 Token
- 🔒 数据级权限控制
- 🔒 ThreadLocal 隔离用户上下文

---

## 第8页：技术难点 5 - 全局异常处理

### 统一异常处理机制

**全局异常处理器**
```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // 业务异常
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("业务异常：", e);
        return Result.error(e.getMessage());
    }
    
    // 参数校验异常
    @ExceptionHandler({MethodArgumentNotValidException.class, 
                       BindException.class})
    public Result<Void> handleValidException(Exception e) {
        log.error("参数校验异常：", e);
        String message = "参数校验失败";
        
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = 
                (MethodArgumentNotValidException) e;
            if (ex.getBindingResult().hasErrors()) {
                message = ex.getBindingResult()
                    .getAllErrors().get(0).getDefaultMessage();
            }
        }
        
        return Result.error(400, message);
    }
    
    // 系统异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("系统异常，请联系管理员");
    }
}
```

**统一响应格式**
```java
@Data
public class Result<T> {
    private Integer code;    // 200-成功, 400-参数错误, 500-业务错误
    private String message;  // 提示信息
    private T data;          // 返回数据
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
}
```

**前端统一处理**
```javascript
// Axios 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    if (res.code !== 200) {
      ElMessage.error(res.message || '操作失败')
      
      // 401 未授权，跳转登录
      if (res.code === 401) {
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message))
    }
    
    return res
  },
  error => {
    ElMessage.error('网络错误')
    return Promise.reject(error)
  }
)
```

**优势**
- ✅ 统一的错误处理逻辑
- ✅ 友好的错误提示
- ✅ 完整的日志记录
- ✅ 前后端协同处理

---

## 第9页：性能优化与最佳实践

### 数据库优化

**索引优化**
```sql
-- 组合索引优化查询
CREATE INDEX idx_user_deleted ON note(user_id, deleted);

-- 全文索引支持搜索
CREATE FULLTEXT INDEX idx_title_content ON note(title, content);

-- 唯一索引防止重复
CREATE UNIQUE INDEX uk_user_name ON tag(user_id, name);
```

**查询优化**
```java
// 分页查询
Page<Note> page = new Page<>(pageNum, pageSize);
noteMapper.selectPage(page, queryWrapper);

// 批量操作
noteTagService.saveBatch(noteTags);

// 避免 N+1 查询
List<NoteVO> notes = noteMapper.selectNotesWithTags(userId);
```

### 前端优化

**路由懒加载**
```javascript
const routes = [
  {
    path: '/note/list',
    component: () => import('@/views/note/List.vue')
  }
]
```

**静态资源缓存**
```nginx
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
}
```

**Vite 构建优化**
```javascript
export default defineConfig({
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          'element-plus': ['element-plus'],
          'vue-vendor': ['vue', 'vue-router', 'pinia']
        }
      }
    }
  }
})
```

### 安全最佳实践

**密码加密**
```java
// 使用 BCrypt 加密
String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

// 验证密码
boolean matches = BCrypt.checkpw(inputPassword, hashedPassword);
```

**SQL 注入防护**
```java
// MyBatis Plus 自动防护
noteMapper.selectList(new LambdaQueryWrapper<Note>()
    .eq(Note::getUserId, userId)
    .like(Note::getTitle, keyword));
```

**XSS 防护**
```javascript
// 前端转义
import { escapeHtml } from '@/utils/security'
const safeContent = escapeHtml(userInput)
```

---

## 第10页：项目总结与展望

### 项目成果

**功能完整性**
- ✅ 用户认证与授权
- ✅ 笔记 CRUD 完整功能
- ✅ 富文本编辑器集成
- ✅ 标签系统重构
- ✅ 回收站机制
- ✅ 文件上传管理
- ✅ 响应式设计

**技术亮点**
- 🌟 前后端分离架构
- 🌟 JWT 无状态认证
- 🌟 MyBatis Plus 逻辑删除处理
- 🌟 标签系统数据库设计
- 🌟 图片上传优化方案
- 🌟 全局异常处理机制
- 🌟 iPhone 风格 UI 设计

### 技术收获

**后端技术**
- Spring Boot 项目架构设计
- MyBatis Plus 高级用法
- JWT 认证与权限控制
- 全局异常处理
- 文件上传处理

**前端技术**
- Vue 3 Composition API
- Element Plus 组件库
- Vite 构建工具
- Pinia 状态管理
- Quill 富文本编辑器

**数据库设计**
- ER 图设计
- 索引优化
- 多对多关系处理
- 软删除机制

### 未来展望

**功能扩展**
- 📌 笔记分享功能
- 📌 笔记导出（PDF、Markdown）
- 📌 协作编辑
- 📌 笔记模板
- 📌 移动端适配
- 📌 暗黑模式

**技术优化**
- 🚀 Redis 缓存
- 🚀 Elasticsearch 全文搜索
- 🚀 OSS 对象存储
- 🚀 消息队列
- 🚀 微服务架构
- 🚀 Docker 容器化

**性能提升**
- ⚡ 数据库读写分离
- ⚡ CDN 加速
- ⚡ 接口限流
- ⚡ 前端性能监控

---

## 感谢观看！

**项目地址**
- GitHub: [待补充]
- 在线演示: [待补充]

**联系方式**
- Email: [待补充]
- 技术博客: [待补充]

**文档资源**
- API 接口文档
- 系统设计文档
- 部署手册
- 数据库 ER 图

---

## 附录：技术栈详细版本

### 后端
- JDK: 1.8
- Spring Boot: 2.7.18
- MyBatis Plus: 3.5.3.1
- MySQL: 8.0
- JWT: 0.11.5
- Hutool: 5.8.20
- Lombok: 最新版

### 前端
- Vue: 3.4.21
- Vite: 5.2.0
- Element Plus: 2.12.0
- Vue Router: 4.6.4
- Pinia: 2.3.1
- Axios: 1.13.2
- Quill: 2.0.3

### 开发工具
- IDE: IntelliJ IDEA / VS Code
- 数据库工具: Navicat / DBeaver
- API 测试: Postman
- 版本控制: Git

### 部署环境
- 服务器: Linux / Windows
- Web 服务器: Nginx
- 构建工具: Maven
- 包管理: npm
