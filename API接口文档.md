# 云笔记管理系统 API 接口文档

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **认证方式**: JWT Token (除登录、注册接口外，其他接口需要在 Header 中携带 Token)
- **Header 格式**: `Authorization: Bearer {token}`
- **响应格式**: JSON

## 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

**响应码说明：**
- `200`: 成功
- `400`: 参数错误
- `401`: 未授权
- `500`: 服务器错误

---

## 1. 用户管理模块

### 1.1 用户注册

**接口地址**: `POST /user/register`

**是否需要认证**: 否

**请求参数**:
```json
{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com"
}
```

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名，3-20个字符 |
| password | String | 是 | 密码，6-20个字符 |
| email | String | 否 | 邮箱地址 |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "用户名已存在",
  "data": null
}
```

---

### 1.2 用户登录

**接口地址**: `POST /user/login`

**是否需要认证**: 否

**请求参数**:
```json
{
  "username": "testuser",
  "password": "123456"
}
```

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "testuser",
    "email": "test@example.com"
  }
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "用户名或密码错误",
  "data": null
}
```

---

### 1.3 获取用户信息

**接口地址**: `GET /user/info`

**是否需要认证**: 是

**请求头**:
```
Authorization: Bearer {token}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "username": "testuser",
    "nickname": "测试用户",
    "email": "test@example.com",
    "avatar": "data:image/png;base64,iVBORw0KG...",
    "createdAt": "2024-12-14T00:00:00",
    "updatedAt": "2024-12-14T00:00:00"
  }
}
```

---

### 1.4 更新用户信息

**接口地址**: `PUT /user/info`

**是否需要认证**: 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "username": "newusername",
  "nickname": "新昵称",
  "email": "newemail@example.com",
  "avatar": "data:image/png;base64,iVBORw0KG..."
}
```

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 否 | 用户名，3-20个字符 |
| nickname | String | 否 | 昵称，最多50个字符 |
| email | String | 否 | 邮箱地址 |
| avatar | String | 否 | 头像（Base64或URL） |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "用户名已存在",
  "data": null
}
```

---

### 1.5 修改密码

**接口地址**: `PUT /user/password`

**是否需要认证**: 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oldPassword | String | 是 | 旧密码 |
| newPassword | String | 是 | 新密码，6-20个字符 |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "旧密码错误",
  "data": null
}
```

---

## 2. 笔记管理模块

### 2.1 创建笔记

**接口地址**: `POST /note`

**是否需要认证**: 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "title": "我的第一篇笔记",
  "content": "这是笔记内容...",
  "categoryId": 1,
  "tags": "学习,Java"
}
```

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| title | String | 是 | 笔记标题 |
| content | String | 否 | 笔记内容 |
| categoryId | Integer | 否 | 分类ID |
| tags | String | 否 | 标签，逗号分隔 |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 2.2 更新笔记

**接口地址**: `PUT /note/{noteId}`

**是否需要认证**: 是

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| noteId | Integer | 是 | 笔记ID |

**请求参数**:
```json
{
  "title": "更新后的标题",
  "content": "更新后的内容...",
  "categoryId": 2,
  "tags": "学习,Spring"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "笔记不存在或无权限",
  "data": null
}
```

---

### 2.3 删除笔记

**接口地址**: `DELETE /note/{noteId}`

**是否需要认证**: 是

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| noteId | Integer | 是 | 笔记ID |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 2.4 获取笔记详情

**接口地址**: `GET /note/{noteId}`

**是否需要认证**: 是

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| noteId | Integer | 是 | 笔记ID |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "noteId": 1,
    "title": "我的第一篇笔记",
    "content": "这是笔记内容...",
    "userId": 1,
    "categoryId": 1,
    "tags": "学习,Java",
    "createdAt": "2024-12-14T00:00:00",
    "updatedAt": "2024-12-14T00:00:00"
  }
}
```

---

### 2.5 分页查询笔记列表

**接口地址**: `GET /note/list`

**是否需要认证**: 是

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| pageNum | Integer | 否 | 1 | 页码 |
| pageSize | Integer | 否 | 10 | 每页数量 |

**请求示例**:
```
GET /note/list?pageNum=1&pageSize=10
```

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "noteId": 1,
        "title": "我的第一篇笔记",
        "content": "这是笔记内容...",
        "userId": 1,
        "categoryId": 1,
        "tags": "学习,Java",
        "createdAt": "2024-12-14T00:00:00",
        "updatedAt": "2024-12-14T00:00:00"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

---

### 2.6 搜索笔记

**接口地址**: `GET /note/search`

**是否需要认证**: 是

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| keyword | String | 是 | - | 搜索关键词 |
| pageNum | Integer | 否 | 1 | 页码 |
| pageSize | Integer | 否 | 10 | 每页数量 |

**请求示例**:
```
GET /note/search?keyword=Java&pageNum=1&pageSize=10
```

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "noteId": 1,
        "title": "Java学习笔记",
        "content": "Java基础知识...",
        "userId": 1,
        "categoryId": 1,
        "tags": "学习,Java",
        "createdAt": "2024-12-14T00:00:00",
        "updatedAt": "2024-12-14T00:00:00"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

---

## 3. 分类管理模块

### 3.1 创建分类

**接口地址**: `POST /category`

**是否需要认证**: 是

**请求参数**:
```json
{
  "name": "工作笔记"
}
```

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 分类名称 |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 3.2 更新分类

**接口地址**: `PUT /category/{categoryId}`

**是否需要认证**: 是

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Integer | 是 | 分类ID |

**请求参数**:
```json
{
  "name": "学习笔记"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 3.3 删除分类

**接口地址**: `DELETE /category/{categoryId}`

**是否需要认证**: 是

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Integer | 是 | 分类ID |

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 3.4 获取分类列表

**接口地址**: `GET /category/list`

**是否需要认证**: 是

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "categoryId": 1,
      "name": "工作笔记",
      "userId": 1,
      "createdAt": "2024-12-14T00:00:00",
      "updatedAt": "2024-12-14T00:00:00"
    },
    {
      "categoryId": 2,
      "name": "学习笔记",
      "userId": 1,
      "createdAt": "2024-12-14T00:00:00",
      "updatedAt": "2024-12-14T00:00:00"
    }
  ]
}
```

---

## 4. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 参数校验失败 |
| 401 | 未授权（Token无效或过期） |
| 500 | 服务器内部错误 |

---

## 5. 使用示例

### 5.1 Postman 测试步骤

1. **注册用户**
   ```
   POST http://localhost:8080/api/user/register
   Content-Type: application/json
   
   {
     "username": "testuser",
     "password": "123456",
     "email": "test@example.com"
   }
   ```

2. **登录获取 Token**
   ```
   POST http://localhost:8080/api/user/login
   Content-Type: application/json
   
   {
     "username": "testuser",
     "password": "123456"
   }
   ```
   
   复制响应中的 `token` 值

3. **创建笔记（需要 Token）**
   ```
   POST http://localhost:8080/api/note
   Content-Type: application/json
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   
   {
     "title": "我的第一篇笔记",
     "content": "这是笔记内容",
     "tags": "学习,Java"
   }
   ```

### 5.2 cURL 测试示例

**注册用户**:
```bash
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456","email":"test@example.com"}'
```

**登录**:
```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'
```

**创建笔记**:
```bash
curl -X POST http://localhost:8080/api/note \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"title":"我的笔记","content":"笔记内容","tags":"学习"}'
```

**查询笔记列表**:
```bash
curl -X GET "http://localhost:8080/api/note/list?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 6. 注意事项

1. **Token 有效期**: 7天，过期后需要重新登录
2. **Token 格式**: 请求头中必须使用 `Bearer {token}` 格式
3. **时间格式**: 所有时间字段使用 ISO 8601 格式
4. **分页参数**: pageNum 从 1 开始
5. **标签格式**: 多个标签使用英文逗号分隔
6. **密码安全**: 密码使用 BCrypt 加密存储

---

## 7. 数据库表结构

### 用户表 (user)
| 字段 | 类型 | 说明 |
|------|------|------|
| user_id | INT | 主键 |
| username | VARCHAR(100) | 用户名 |
| nickname | VARCHAR(50) | 昵称 |
| password | VARCHAR(255) | 密码（加密） |
| email | VARCHAR(100) | 邮箱 |
| avatar | TEXT | 头像（Base64或URL） |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

### 笔记表 (note)
| 字段 | 类型 | 说明 |
|------|------|------|
| note_id | INT | 主键 |
| title | VARCHAR(255) | 标题 |
| content | TEXT | 内容 |
| user_id | INT | 用户ID |
| category_id | INT | 分类ID |
| tags | VARCHAR(255) | 标签 |
| deleted | TINYINT | 是否删除（0=正常，1=已删除） |
| deleted_at | TIMESTAMP | 删除时间 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

### 分类表 (category)
| 字段 | 类型 | 说明 |
|------|------|------|
| category_id | INT | 主键 |
| name | VARCHAR(100) | 分类名称 |
| user_id | INT | 用户ID |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

---

**文档版本**: v1.0.0  
**最后更新**: 2024-12-14  
**联系方式**: 云笔记开发团队
