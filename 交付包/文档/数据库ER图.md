# CloudNote 数据库 ER 图

## 1. 实体关系图（ER Diagram）

### 1.1 图形化 ER 图

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                            CloudNote 数据库                             │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘


┌──────────────────────┐
│       user           │  用户表
│──────────────────────│
│ PK  user_id          │  INT AUTO_INCREMENT
│     username         │  VARCHAR(100) UNIQUE NOT NULL
│     password         │  VARCHAR(255) NOT NULL
│     nickname         │  VARCHAR(100)
│     email            │  VARCHAR(100)
│     avatar           │  VARCHAR(500)
│     created_at       │  TIMESTAMP
│     updated_at       │  TIMESTAMP
└──────────────────────┘
         │ 1
         │ 拥有
         │
         ├──────────────────────────────────────────┐
         │                                          │
         │ *                                        │ *
┌──────────────────────┐                  ┌──────────────────────┐
│     category         │  分类表          │        tag           │  标签表
│──────────────────────│                  │──────────────────────│
│ PK  category_id      │  INT             │ PK  tag_id           │  INT AUTO_INCREMENT
│     name             │  VARCHAR(100)    │     name             │  VARCHAR(50) NOT NULL
│ FK  user_id          │  INT             │     color            │  VARCHAR(20)
│     created_at       │  TIMESTAMP       │ FK  user_id          │  INT
│     updated_at       │  TIMESTAMP       │     created_at       │  TIMESTAMP
└──────────────────────┘                  │     updated_at       │  TIMESTAMP
         │ 1                              └──────────────────────┘
         │ 分类                                     │ 1
         │                                          │ 标记
         │ *                                        │
┌──────────────────────┐                           │
│       note           │  笔记表                   │
│──────────────────────│                           │
│ PK  note_id          │  INT AUTO_INCREMENT       │
│     title            │  VARCHAR(255) NOT NULL    │
│     content          │  LONGTEXT                 │
│ FK  user_id          │  INT                      │
│ FK  category_id      │  INT                      │
│     tags             │  VARCHAR(255) [废弃]     │
│     deleted          │  TINYINT DEFAULT 0        │
│     deleted_at       │  TIMESTAMP NULL           │
│     created_at       │  TIMESTAMP                │
│     updated_at       │  TIMESTAMP                │
└──────────────────────┘                           │
         │ *                                       │
         │ 关联                                    │
         │                                         │ *
         │                              ┌──────────────────────┐
         └──────────────────────────────│     note_tag         │  笔记标签关联表
                                        │──────────────────────│
                                        │ PK  id               │  INT AUTO_INCREMENT
                                        │ FK  note_id          │  INT
                                        │ FK  tag_id           │  INT
                                        │     created_at       │  TIMESTAMP
                                        └──────────────────────┘
```

### 1.2 关系说明

#### 一对多关系（1:N）

1. **user → category**
   - 一个用户可以创建多个分类
   - 一个分类只属于一个用户
   - 外键：category.user_id → user.user_id
   - 删除策略：CASCADE（删除用户时删除其所有分类）

2. **user → note**
   - 一个用户可以创建多个笔记
   - 一个笔记只属于一个用户
   - 外键：note.user_id → user.user_id
   - 删除策略：CASCADE（删除用户时删除其所有笔记）

3. **user → tag**
   - 一个用户可以创建多个标签
   - 一个标签只属于一个用户
   - 外键：tag.user_id → user.user_id
   - 删除策略：CASCADE（删除用户时删除其所有标签）

4. **category → note**
   - 一个分类可以包含多个笔记
   - 一个笔记只属于一个分类（可选）
   - 外键：note.category_id → category.category_id
   - 删除策略：SET NULL（删除分类时笔记的分类ID设为NULL）

#### 多对多关系（M:N）

1. **note ↔ tag**
   - 一个笔记可以有多个标签
   - 一个标签可以标记多个笔记
   - 通过中间表 note_tag 实现
   - 外键：
     - note_tag.note_id → note.note_id
     - note_tag.tag_id → tag.tag_id
   - 删除策略：CASCADE（删除笔记或标签时删除关联关系）

## 2. 表详细设计

### 2.1 user 表（用户表）

```sql
CREATE TABLE `user` (
  `user_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  `username` VARCHAR(100) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` VARCHAR(100) COMMENT '昵称',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(500) COMMENT '头像URL',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_username (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

**字段说明**：
- `user_id`: 主键，自增
- `username`: 用户名，唯一，用于登录
- `password`: 密码，使用 BCrypt 加密存储
- `nickname`: 昵称，用于显示
- `email`: 邮箱，可用于找回密码
- `avatar`: 头像 URL，可以是相对路径或完整 URL

**索引**：
- PRIMARY KEY: user_id
- UNIQUE INDEX: username
- INDEX: idx_username（优化登录查询）

### 2.2 category 表（分类表）

```sql
CREATE TABLE `category` (
  `category_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
  `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
  INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';
```

**字段说明**：
- `category_id`: 主键，自增
- `name`: 分类名称
- `user_id`: 所属用户ID，外键

**索引**：
- PRIMARY KEY: category_id
- FOREIGN KEY: user_id → user(user_id)
- INDEX: idx_user_id（优化按用户查询）

**业务规则**：
- 同一用户可以创建多个同名分类（无唯一约束）
- 删除用户时级联删除其所有分类

### 2.3 note 表（笔记表）

```sql
CREATE TABLE `note` (
  `note_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '笔记ID',
  `title` VARCHAR(255) NOT NULL COMMENT '笔记标题',
  `content` LONGTEXT COMMENT '笔记内容（HTML格式）',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `category_id` INT COMMENT '分类ID',
  `tags` VARCHAR(255) COMMENT '标签（逗号分隔，已废弃）',
  `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `deleted_at` TIMESTAMP NULL COMMENT '删除时间',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
  FOREIGN KEY (`category_id`) REFERENCES `category`(`category_id`) ON DELETE SET NULL,
  INDEX idx_user_id (`user_id`),
  INDEX idx_category_id (`category_id`),
  INDEX idx_user_deleted (`user_id`, `deleted`),
  FULLTEXT INDEX idx_title_content (`title`, `content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='笔记表';
```

**字段说明**：
- `note_id`: 主键，自增
- `title`: 笔记标题
- `content`: 笔记内容，HTML 格式，使用 LONGTEXT 支持大量图片
- `user_id`: 所属用户ID，外键
- `category_id`: 所属分类ID，外键，可为 NULL
- `tags`: 旧的标签字段（逗号分隔），已废弃，保留用于兼容
- `deleted`: 软删除标记，0-未删除，1-已删除
- `deleted_at`: 删除时间，用于回收站

**索引**：
- PRIMARY KEY: note_id
- FOREIGN KEY: user_id → user(user_id)
- FOREIGN KEY: category_id → category(category_id)
- INDEX: idx_user_id（优化按用户查询）
- INDEX: idx_category_id（优化按分类查询）
- INDEX: idx_user_deleted（优化查询用户的未删除笔记）
- FULLTEXT INDEX: idx_title_content（全文搜索）

**业务规则**：
- 删除用户时级联删除其所有笔记
- 删除分类时笔记的 category_id 设为 NULL
- 软删除：deleted=1 表示在回收站，deleted=0 表示正常

### 2.4 tag 表（标签表）

```sql
CREATE TABLE `tag` (
  `tag_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
  `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
  `color` VARCHAR(20) DEFAULT '#409EFF' COMMENT '标签颜色',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `uk_user_name` (`user_id`, `name`),
  INDEX `idx_user_id` (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';
```

**字段说明**：
- `tag_id`: 主键，自增
- `name`: 标签名称
- `color`: 标签颜色，十六进制格式，默认 Element Plus 主题色
- `user_id`: 所属用户ID，外键

**索引**：
- PRIMARY KEY: tag_id
- UNIQUE KEY: uk_user_name (user_id, name)（同一用户不能创建重名标签）
- INDEX: idx_user_id（优化按用户查询）
- FOREIGN KEY: user_id → user(user_id)

**业务规则**：
- 同一用户不能创建重名标签（唯一约束）
- 删除用户时级联删除其所有标签

### 2.5 note_tag 表（笔记标签关联表）

```sql
CREATE TABLE `note_tag` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `note_id` INT NOT NULL COMMENT '笔记ID',
  `tag_id` INT NOT NULL COMMENT '标签ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `uk_note_tag` (`note_id`, `tag_id`),
  INDEX `idx_note_id` (`note_id`),
  INDEX `idx_tag_id` (`tag_id`),
  FOREIGN KEY (`note_id`) REFERENCES `note`(`note_id`) ON DELETE CASCADE,
  FOREIGN KEY (`tag_id`) REFERENCES `tag`(`tag_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='笔记标签关联表';
```

**字段说明**：
- `id`: 主键，自增
- `note_id`: 笔记ID，外键
- `tag_id`: 标签ID，外键

**索引**：
- PRIMARY KEY: id
- UNIQUE KEY: uk_note_tag (note_id, tag_id)（同一笔记不能关联同一标签多次）
- INDEX: idx_note_id（优化查询笔记的标签）
- INDEX: idx_tag_id（优化查询标签的笔记）
- FOREIGN KEY: note_id → note(note_id)
- FOREIGN KEY: tag_id → tag(tag_id)

**业务规则**：
- 同一笔记不能关联同一标签多次（唯一约束）
- 删除笔记时级联删除关联关系
- 删除标签时级联删除关联关系

## 3. 数据流转示例

### 3.1 创建笔记并关联标签

```
1. 用户创建笔记
   INSERT INTO note (title, content, user_id, category_id) 
   VALUES ('标题', '内容', 1, 2);
   -- 返回 note_id = 10

2. 用户选择标签 [前端, Vue, 学习笔记]
   
3. 检查标签是否存在，不存在则创建
   -- "前端" 已存在，tag_id = 5
   -- "Vue" 已存在，tag_id = 8
   -- "学习笔记" 不存在，创建
   INSERT INTO tag (name, color, user_id) 
   VALUES ('学习笔记', '#67C23A', 1);
   -- 返回 tag_id = 15

4. 创建笔记标签关联
   INSERT INTO note_tag (note_id, tag_id) VALUES
   (10, 5),   -- 前端
   (10, 8),   -- Vue
   (10, 15);  -- 学习笔记
```

### 3.2 查询笔记及其标签

```sql
-- 查询笔记基本信息
SELECT n.*, c.name as category_name
FROM note n
LEFT JOIN category c ON n.category_id = c.category_id
WHERE n.note_id = 10 AND n.user_id = 1 AND n.deleted = 0;

-- 查询笔记的标签
SELECT t.*
FROM tag t
INNER JOIN note_tag nt ON t.tag_id = nt.tag_id
WHERE nt.note_id = 10;
```

### 3.3 删除笔记（软删除）

```sql
-- 移到回收站
UPDATE note 
SET deleted = 1, deleted_at = NOW() 
WHERE note_id = 10 AND user_id = 1;

-- 注意：标签关联关系保留，恢复时可以恢复标签
```

### 3.4 恢复笔记

```sql
-- 从回收站恢复
UPDATE note 
SET deleted = 0, deleted_at = NULL 
WHERE note_id = 10 AND user_id = 1;
```

### 3.5 永久删除笔记

```sql
-- 物理删除（级联删除标签关联）
DELETE FROM note 
WHERE note_id = 10 AND user_id = 1 AND deleted = 1;

-- note_tag 表中的关联记录会自动删除（CASCADE）
```

## 4. 数据完整性约束

### 4.1 主键约束
- 所有表都有主键
- 使用 AUTO_INCREMENT 自动生成

### 4.2 外键约束
- user_id 外键确保数据一致性
- 级联删除策略保证数据清理

### 4.3 唯一约束
- user.username：用户名唯一
- tag (user_id, name)：同一用户标签名唯一
- note_tag (note_id, tag_id)：同一笔记不重复关联标签

### 4.4 非空约束
- 关键字段设置 NOT NULL
- 可选字段允许 NULL

### 4.5 默认值
- 时间戳字段自动设置
- deleted 默认为 0
- color 默认为 '#409EFF'

## 5. 索引策略

### 5.1 主键索引
- 所有表的主键自动创建聚簇索引

### 5.2 唯一索引
- user.username
- tag (user_id, name)
- note_tag (note_id, tag_id)

### 5.3 普通索引
- 外键字段（user_id, category_id, note_id, tag_id）
- 组合索引：note (user_id, deleted)

### 5.4 全文索引
- note (title, content)：支持全文搜索

## 6. 数据库优化建议

### 6.1 查询优化
```sql
-- 使用组合索引优化查询
SELECT * FROM note 
WHERE user_id = 1 AND deleted = 0 
ORDER BY created_at DESC;
-- 使用索引：idx_user_deleted

-- 避免 SELECT *，只查询需要的字段
SELECT note_id, title, created_at FROM note 
WHERE user_id = 1;
```

### 6.2 分页查询
```sql
-- 使用 LIMIT 分页
SELECT * FROM note 
WHERE user_id = 1 AND deleted = 0 
ORDER BY created_at DESC 
LIMIT 10 OFFSET 0;
```

### 6.3 批量操作
```sql
-- 批量插入标签关联
INSERT INTO note_tag (note_id, tag_id) VALUES
(10, 5), (10, 8), (10, 15);
```

### 6.4 定期维护
```sql
-- 清理30天前的回收站笔记
DELETE FROM note 
WHERE deleted = 1 
AND deleted_at < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- 优化表
OPTIMIZE TABLE note;
OPTIMIZE TABLE note_tag;
```

## 7. 数据字典

### 7.1 deleted 字段
| 值 | 说明 |
|----|------|
| 0  | 正常状态 |
| 1  | 已删除（回收站） |

### 7.2 标签颜色
| 颜色值 | 说明 |
|--------|------|
| #409EFF | 默认蓝色（Element Plus 主题色） |
| #67C23A | 绿色 |
| #E6A23C | 橙色 |
| #F56C6C | 红色 |
| #909399 | 灰色 |

## 8. 版本历史

### v1.0 - 初始版本
- user, category, note 表
- 基本的笔记管理功能

### v1.1 - 添加软删除
- note 表添加 deleted, deleted_at 字段
- 实现回收站功能

### v1.2 - 标签系统重构
- 创建 tag 表
- 创建 note_tag 关联表
- 废弃 note.tags 字段

### v1.3 - 用户信息扩展
- user 表添加 nickname, avatar 字段
- 支持个人信息管理

### v1.4 - 内容字段优化
- note.content 从 TEXT 改为 LONGTEXT
- 支持大量图片存储
