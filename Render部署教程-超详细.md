# Render 部署教程 - 超详细版

**完全免费 | 不需要信用卡 | 30分钟搞定**

---

## 🎯 你将获得

- ✅ 免费的后端服务（https://xxx.onrender.com）
- ✅ 免费的前端网站（https://xxx.onrender.com）
- ✅ 免费的MySQL数据库
- ✅ 自动HTTPS证书
- ✅ 可以发给老师的线上地址

**费用**：$0（完全免费）

---

## ⚠️ 注意事项

### 免费版限制
- 会休眠：15分钟无访问会休眠
- 首次访问：需要10-30秒唤醒
- 内存：512MB

### 适合场景
- ✅ 毕设展示
- ✅ 答辩演示
- ✅ 给老师看
- ❌ 不适合生产环境

---

## 📋 准备工作（10分钟）

### 第一步：注册GitHub账号

**为什么需要GitHub？**
- Render需要从GitHub读取代码
- 不用担心，很简单！

**操作步骤**：
1. 访问：https://github.com
2. 点击"Sign up"
3. 填写：
   - Email：你的邮箱
   - Password：设置密码
   - Username：用户名（如：zhangguiquan23002953）
4. 验证邮箱
5. 完成！

### 第二步：上传代码到GitHub

**在你的项目目录打开命令行**：

```bash
# 进入项目目录
cd C:\Users\张桂泉\Desktop\manage_system

# 初始化Git（如果还没有）
git init

# 添加所有文件
git add .

# 提交
git commit -m "准备部署到Render"
```

**在GitHub创建仓库**：
1. 访问：https://github.com/new
2. 填写：
   - Repository name：cloudnote
   - Description：云笔记管理系统
   - Public（公开）
3. 点击"Create repository"
4. 复制仓库地址（如：https://github.com/你的用户名/cloudnote.git）

**推送代码**：
```bash
# 关联远程仓库
git remote add origin https://github.com/你的用户名/cloudnote.git

# 推送代码
git branch -M main
git push -u origin main
```

如果提示输入用户名密码：
- Username：你的GitHub用户名
- Password：使用Personal Access Token（不是密码）

**如何获取Token**：
1. GitHub右上角头像 → Settings
2. 左侧最下面 → Developer settings
3. Personal access tokens → Tokens (classic)
4. Generate new token (classic)
5. 勾选：repo
6. 生成并复制Token
7. 用Token作为密码

---

## 🚀 开始部署（20分钟）

### 第一步：注册Render（2分钟）

1. 访问：https://render.com
2. 点击"Get Started"
3. 选择"Sign up with GitHub"
4. 授权Render访问GitHub
5. 完成注册！

### 第二步：部署后端（10分钟）

#### 1. 创建Web Service

1. 在Render Dashboard点击"New +"
2. 选择"Web Service"
3. 点击"Connect a repository"
4. 如果看不到仓库，点击"Configure account"授权
5. 选择你的"cloudnote"仓库
6. 点击"Connect"

#### 2. 配置后端服务

填写以下信息：

**Basic**：
- Name：`cloudnote-backend`
- Region：Singapore（新加坡，离中国近）
- Branch：main
- Root Directory：留空
- Runtime：Java

**Build & Deploy**：
- Build Command：
  ```bash
  mvn clean package -DskipTests
  ```
- Start Command：
  ```bash
  java -jar target/cloudnote-1.0.0.jar
  ```

**Instance Type**：
- 选择：**Free**（免费）

**Environment Variables**（先不填，等会配置数据库后再填）

点击"Create Web Service"

等待构建（约5-10分钟）...

#### 3. 查看构建日志

- 点击"Logs"标签
- 看到构建过程
- 如果失败，查看错误信息

### 第三步：创建数据库（5分钟）

**Render不提供免费MySQL，我们用Aiven**

#### 1. 注册Aiven

1. 访问：https://aiven.io/free-mysql-database
2. 点击"Try for free"
3. 使用Google账号或邮箱注册
4. 验证邮箱

#### 2. 创建MySQL数据库

1. 登录Aiven控制台
2. 点击"Create service"
3. 选择"MySQL"
4. 选择：
   - Cloud：AWS
   - Region：Singapore（新加坡）
   - Plan：Hobbyist（免费）
5. Service name：cloudnote-db
6. 点击"Create service"
7. 等待创建（约2-3分钟）

#### 3. 获取连接信息

1. 点击创建的数据库
2. 点击"Overview"
3. 复制以下信息：
   - Host：xxx.aivencloud.com
   - Port：12345
   - User：avnadmin
   - Password：xxxxxxxxxx
   - Database：defaultdb

#### 4. 导入数据库

**方式一：使用Navicat**
1. 新建连接
2. 填入上面的信息
3. 测试连接
4. 连接成功后，运行SQL文件
5. 导入cloudnote.sql

**方式二：使用MySQL命令行**
```bash
mysql -h xxx.aivencloud.com -P 12345 -u avnadmin -p defaultdb < cloudnote.sql
```

### 第四步：配置环境变量（3分钟）

回到Render后端服务：

1. 点击"Environment"标签
2. 点击"Add Environment Variable"
3. 添加以下变量：

```
MYSQL_HOST=xxx.aivencloud.com
MYSQL_PORT=12345
MYSQL_DATABASE=defaultdb
MYSQL_USER=avnadmin
MYSQL_PASSWORD=你的密码
```

4. 点击"Save Changes"
5. Render会自动重新部署

**修改application.yml**（在本地）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DATABASE}?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai
    username: ${MYSQL_USER}
    password: ${MYSQL_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver

server:
  port: ${PORT:8080}
```

提交并推送：
```bash
git add .
git commit -m "配置Render环境变量"
git push
```

Render会自动重新部署！

### 第五步：部署前端（5分钟）

#### 1. 创建Static Site

1. 在Render Dashboard点击"New +"
2. 选择"Static Site"
3. 选择你的"cloudnote"仓库
4. 点击"Connect"

#### 2. 配置前端服务

填写以下信息：

**Basic**：
- Name：`cloudnote-frontend`
- Branch：main
- Root Directory：`front_end`

**Build & Deploy**：
- Build Command：
  ```bash
  npm install && npm run build
  ```
- Publish Directory：`dist`

点击"Create Static Site"

等待构建（约3-5分钟）...

#### 3. 配置前端API地址

**获取后端地址**：
1. 进入后端服务
2. 复制URL（如：https://cloudnote-backend.onrender.com）

**修改前端配置**（在本地）：

创建 `front_end/.env.production`：
```
VITE_API_BASE_URL=https://cloudnote-backend.onrender.com
```

修改 `front_end/src/utils/request.js`：
```javascript
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 30000 // 增加超时时间，因为免费版可能需要唤醒
})
```

提交并推送：
```bash
git add .
git commit -m "配置生产环境API地址"
git push
```

前端会自动重新部署！

---

## ✅ 测试访问

### 1. 获取地址

**后端地址**：
- 进入后端服务
- 复制URL：`https://cloudnote-backend.onrender.com`

**前端地址**：
- 进入前端服务
- 复制URL：`https://cloudnote-frontend.onrender.com`

### 2. 测试后端

访问：`https://cloudnote-backend.onrender.com`

**第一次访问**：
- 可能需要等待10-30秒（唤醒服务）
- 看到页面或JSON响应表示成功

### 3. 测试前端

访问：`https://cloudnote-frontend.onrender.com`

**应该看到**：
- 登录页面
- 界面正常显示

### 4. 测试功能

1. 注册新账号
2. 登录系统
3. 创建笔记
4. 测试所有功能

---

## 🎉 完成！

你的项目已经成功部署到线上了！

**后端地址**：https://cloudnote-backend.onrender.com  
**前端地址**：https://cloudnote-frontend.onrender.com

---

## 📝 给老师的说明

```
尊敬的老师，您好！

我的云笔记系统已经部署到线上了：

访问地址：https://cloudnote-frontend.onrender.com

系统使用完全免费的云服务部署：
- 前端：Render Static Site（免费）
- 后端：Render Web Service（免费）
- 数据库：Aiven MySQL（免费）

注意：由于使用免费服务，首次访问可能需要10-30秒加载（服务唤醒），
之后访问会很快。

您可以直接访问，注册账号后使用所有功能。

系统功能包括：
- 用户注册登录
- 笔记管理（创建、编辑、删除）
- 分类和标签管理
- 搜索功能
- 回收站
- AI学习助手
- 个人信息管理

如有任何问题，请随时联系我。

学生：张桂泉
学号：23002953
```

---

## 🔧 常见问题

### Q1：构建失败怎么办？

**查看日志**：
1. 点击服务
2. 点击"Logs"
3. 查看错误信息

**常见错误**：

**Maven构建失败**：
- 检查pom.xml是否正确
- 确保Java版本匹配

**npm构建失败**：
- 检查package.json
- 确保依赖完整

### Q2：后端启动失败？

**检查环境变量**：
- 确保所有变量都已设置
- 检查数据库连接信息

**查看日志**：
- 点击"Logs"
- 查看启动日志
- 找到错误原因

### Q3：前端打不开？

**检查构建**：
- 确保构建成功
- 检查Publish Directory是否正确（dist）

**检查API地址**：
- 确保.env.production配置正确
- 确保后端地址正确

### Q4：数据库连接失败？

**检查Aiven数据库**：
- 确保数据库已启动
- 检查连接信息是否正确
- 确保SSL连接（Aiven需要SSL）

**修改连接字符串**：
```
jdbc:mysql://host:port/db?useSSL=true&requireSSL=true
```

### Q5：服务休眠了怎么办？

**免费版会休眠**：
- 15分钟无访问会休眠
- 首次访问需要10-30秒唤醒
- 这是正常的

**解决方案**：
- 演示前提前访问一次
- 或使用定时任务保持唤醒（不推荐）

### Q6：如何更新代码？

```bash
# 修改代码后
git add .
git commit -m "更新说明"
git push
```

Render会自动检测并重新部署！

### Q7：如何查看数据库？

**使用Navicat**：
1. 连接Aiven数据库
2. 查看数据

**使用Aiven控制台**：
1. 登录Aiven
2. 点击数据库
3. 使用内置工具查看

---

## 💡 优化建议

### 1. 增加超时时间

前端请求超时改为30秒：
```javascript
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000 // 30秒
})
```

### 2. 添加加载提示

在前端添加"服务唤醒中"的提示。

### 3. 配置CORS

确保后端CORS配置正确：
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://cloudnote-frontend.onrender.com")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

### 4. 添加健康检查

```java
@RestController
public class HealthController {
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
```

---

## 📊 部署检查清单

部署前：
- [ ] 代码已推送到GitHub
- [ ] application.yml已配置环境变量
- [ ] 前端API地址已配置
- [ ] 数据库SQL文件已准备

部署后：
- [ ] 后端服务构建成功
- [ ] 前端服务构建成功
- [ ] 数据库已创建
- [ ] 数据已导入
- [ ] 环境变量已配置
- [ ] 后端可以访问
- [ ] 前端可以访问
- [ ] 可以注册登录
- [ ] 所有功能正常

---

## 🎯 时间安排

- 注册GitHub：5分钟
- 上传代码：5分钟
- 注册Render：2分钟
- 部署后端：10分钟
- 创建数据库：5分钟
- 配置环境变量：3分钟
- 部署前端：5分钟
- 测试：5分钟

**总计：约40分钟**

---

## 🚀 立即开始

现在就开始部署吧！

1. 注册GitHub
2. 上传代码
3. 注册Render
4. 部署后端
5. 创建数据库
6. 部署前端
7. 完成！

**完全免费，永久使用！** 🎉

---

最后更新：2026-05-15
