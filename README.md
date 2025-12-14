# 云笔记管理系统

基于 Spring Boot + MyBatis Plus + MySQL + Redis 的云笔记平台，支持笔记的创建、编辑、分类、标签、搜索等功能。

## 技术栈

### 后端
- Spring Boot 2.7.18
- MyBatis Plus 3.5.3.1
- MySQL 8.0+
- JWT (身份认证)
- Swagger 3.0 (API文档)
- Lombok
- Hutool (工具类)
- EasyExcel (Excel导入导出)

### 前端
- Vue 3
- Element-UI
- Vue Router
- Axios
- Vuex
- ECharts

## 功能模块

### 1. 用户管理
- 用户注册（邮箱/用户名）
- 用户登录（JWT认证）
- 角色权限管理（RBAC模型）

### 2. 笔记管理
- CRUD操作（创建、查看、编辑、删除）
- 分类管理
- 标签管理
- 全文搜索
- 分页查询

### 3. 分类管理
- 创建分类
- 编辑分类
- 删除分类
- 查询分类列表

## 项目结构

```
cloudnote/
├── src/main/java/com/cloudnote/
│   ├── common/          # 通用类（统一响应结果）
│   ├── config/          # 配置类（Swagger、Web配置）
│   ├── controller/      # 控制器层
│   ├── dto/             # 数据传输对象
│   ├── entity/          # 实体类
│   ├── exception/       # 异常处理
│   ├── interceptor/     # 拦截器（JWT拦截器）
│   ├── mapper/          # MyBatis Mapper
│   ├── service/         # 服务接口
│   │   └── impl/        # 服务实现
│   ├── util/            # 工具类（JWT工具）
│   └── vo/              # 视图对象
├── src/main/resources/
│   ├── db/              # 数据库脚本
│   └── application.yml  # 配置文件
└── pom.xml
```

## 快速开始

### 1. 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库初始化
执行 `src/main/resources/db/schema.sql` 创建数据库和表结构

### 3. 配置文件
修改 `application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cloudnote
    username: root
    password: your_password
```

### 4. 运行项目
```bash
mvn spring-boot:run
```

### 5. 访问接口文档
启动后访问: http://localhost:8080/api/doc.html

## API接口

### 用户接口
- POST `/api/user/register` - 用户注册
- POST `/api/user/login` - 用户登录

### 笔记接口
- POST `/api/note` - 创建笔记
- PUT `/api/note/{noteId}` - 更新笔记
- DELETE `/api/note/{noteId}` - 删除笔记
- GET `/api/note/{noteId}` - 获取笔记详情
- GET `/api/note/list` - 分页查询笔记
- GET `/api/note/search` - 搜索笔记

### 分类接口
- POST `/api/category` - 创建分类
- PUT `/api/category/{categoryId}` - 更新分类
- DELETE `/api/category/{categoryId}` - 删除分类
- GET `/api/category/list` - 获取分类列表

## 开发规范

- 遵循《阿里巴巴Java开发手册》
- 代码注释完整
- 统一异常处理
- 统一响应格式
- 参数校验
- 日志记录

## 安全性

- 密码使用 BCrypt 加密存储
- JWT Token 身份认证
- MyBatis Plus 防止 SQL 注入
- 跨域配置
- 参数校验防止 XSS 攻击

## 性能优化

- MyBatis Plus 分页查询
- 数据库索引优化
- 连接池配置
- 全文索引搜索

## 作者

云笔记开发团队

## 许可证

MIT License
