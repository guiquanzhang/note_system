# Railway 快速部署指南

**最简单的部署方案 - 5分钟上线！**

---

## 🎯 为什么选择Railway？

- ✅ **完全免费**（每月$5额度，够用）
- ✅ **5分钟部署**
- ✅ **自动HTTPS**
- ✅ **免费域名**（xxx.railway.app）
- ✅ **支持Spring Boot + MySQL**
- ✅ **不会休眠**（比Render好）

---

## 📋 准备工作（5分钟）

### 1. 注册GitHub账号（如果没有）
- 访问：https://github.com
- 注册账号

### 2. 上传代码到GitHub

```bash
# 在项目目录打开命令行
cd C:\Users\张桂泉\Desktop\manage_system

# 初始化Git（如果还没有）
git init

# 添加所有文件
git add .

# 提交
git commit -m "准备部署到Railway"

# 在GitHub创建新仓库（网页操作）
# 仓库名：cloudnote

# 关联远程仓库
git remote add origin https://github.com/你的用户名/cloudnote.git

# 推送代码
git push -u origin master
```

### 3. 修改配置文件

修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    # Railway会自动注入这些环境变量
    url: jdbc:mysql://${MYSQLHOST:localhost}:${MYSQLPORT:3306}/${MYSQLDATABASE:cloudnote}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: ${MYSQLUSER:root}
    password: ${MYSQLPASSWORD:123456}
    driver-class-name: com.mysql.cj.jdbc.Driver

server:
  # Railway会自动分配端口
  port: ${PORT:8080}
```

提交更改：
```bash
git add .
git commit -m "配置Railway环境变量"
git push
```

---

## 🚀 开始部署（10分钟）

### 第一步：登录Railway

1. 访问：https://railway.com
2. 点击右上角"Login"
3. 选择"Login with GitHub"
4. 授权Railway访问你的GitHub

### 第二步：创建项目

1. 点击"New Project"
2. 选择"Deploy from GitHub repo"
3. 选择你的"cloudnote"仓库
4. Railway会自动检测到Spring Boot项目
5. 自动开始构建（约2-3分钟）

### 第三步：添加MySQL数据库

1. 在项目页面，点击右上角"New"
2. 选择"Database"
3. 选择"Add MySQL"
4. Railway会自动创建MySQL数据库
5. 自动注入环境变量到后端服务

### 第四步：导入数据库

1. 点击MySQL服务卡片
2. 点击"Connect"标签
3. 复制连接信息：
   ```
   Host: xxx.railway.app
   Port: xxxx
   User: root
   Password: xxxxxxxxxx
   Database: railway
   ```

4. 使用Navicat连接：
   - 新建连接 → MySQL
   - 填入上面的信息
   - 测试连接
   - 连接成功

5. 导入数据：
   - 右键数据库 → 运行SQL文件
   - 选择你的 cloudnote.sql
   - 点击开始

### 第五步：获取后端域名

1. 点击后端服务卡片（cloudnote）
2. 点击"Settings"标签
3. 点击"Networking"
4. 点击"Generate Domain"
5. 获得域名：`https://cloudnote-production-xxxx.railway.app`
6. 复制这个域名

### 第六步：部署前端

**方式一：使用Vercel（推荐，最简单）**

1. 访问：https://vercel.com
2. 使用GitHub登录
3. 点击"Add New" → "Project"
4. 选择cloudnote仓库
5. 配置：
   - Framework Preset: Vite
   - Root Directory: front_end
   - Build Command: npm run build
   - Output Directory: dist
6. 环境变量：
   - Name: VITE_API_URL
   - Value: https://你的Railway后端域名
7. 点击"Deploy"
8. 等待部署完成（约1分钟）
9. 获得前端域名：`https://cloudnote.vercel.app`

**方式二：Railway部署前端**

1. 在Railway项目点击"New"
2. 选择"Empty Service"
3. 连接GitHub仓库
4. 设置Root Directory: front_end
5. 添加Nixpacks配置

### 第七步：配置前端API地址

修改 `front_end/src/utils/request.js`：

```javascript
const request = axios.create({
  baseURL: 'https://你的Railway后端域名', // 替换成你的域名
  timeout: 10000
})
```

或者使用环境变量（推荐）：

创建 `front_end/.env.production`：
```
VITE_API_BASE_URL=https://你的Railway后端域名
```

修改 `front_end/src/utils/request.js`：
```javascript
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000
})
```

提交更改：
```bash
git add .
git commit -m "配置生产环境API地址"
git push
```

Railway和Vercel会自动重新部署。

---

## ✅ 测试访问

1. **访问后端**：https://你的后端域名.railway.app
   - 应该看到页面或JSON响应

2. **访问前端**：https://你的前端域名.vercel.app
   - 应该看到登录页面

3. **测试功能**：
   - 注册新账号
   - 登录系统
   - 创建笔记
   - 测试所有功能

---

## 🎉 完成！

你的项目已经成功部署到线上了！

**后端地址**：https://xxx.railway.app  
**前端地址**：https://xxx.vercel.app

可以把这两个地址发给老师了！

---

## 💰 费用说明

### Railway
- 免费额度：$5/月
- 你的使用：约$2-3/月
- **完全免费，不需要付费！**

### Vercel
- 免费额度：100GB带宽/月
- 你的使用：约1-5GB/月
- **完全免费！**

### 总费用：$0

---

## 📊 监控使用情况

### Railway
1. 进入项目
2. 点击"Usage"
3. 查看当前使用量
4. 确保不超过$5

### Vercel
1. 进入Dashboard
2. 查看"Usage"
3. 查看带宽使用

---

## 🔧 常见问题

### Q1：部署失败怎么办？

**查看日志**：
1. 点击服务卡片
2. 点击"Deployments"
3. 点击最新的部署
4. 查看"Build Logs"和"Deploy Logs"

**常见错误**：
- Maven构建失败：检查pom.xml
- 数据库连接失败：检查环境变量
- 端口错误：确保使用${PORT}

### Q2：如何更新代码？

```bash
# 修改代码后
git add .
git commit -m "更新说明"
git push
```

Railway和Vercel会自动重新部署！

### Q3：如何查看数据库？

1. 点击MySQL服务
2. 点击"Connect"
3. 使用Navicat连接
4. 查看数据

### Q4：如何自定义域名？

**Railway**：
1. 购买域名（阿里云/腾讯云）
2. 在Railway点击"Settings" → "Networking"
3. 点击"Custom Domain"
4. 添加你的域名
5. 在域名控制台添加CNAME记录

**Vercel**：
1. 在项目设置中点击"Domains"
2. 添加你的域名
3. 按提示配置DNS

### Q5：免费额度用完了怎么办？

**Railway**：
- 服务会暂停
- 不会扣费
- 可以升级到付费计划（$5/月起）

**Vercel**：
- 超出带宽会限速
- 不会扣费
- 可以升级到Pro计划（$20/月）

---

## 🎯 优化建议

### 1. 添加健康检查

在Spring Boot中添加：
```java
@RestController
public class HealthController {
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
```

### 2. 配置日志

在application.yml中：
```yaml
logging:
  level:
    root: INFO
    com.cloudnote: DEBUG
```

### 3. 启用CORS

确保CorsConfig正确配置：
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://你的前端域名.vercel.app")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

---

## 📝 部署检查清单

部署前：
- [ ] 代码已推送到GitHub
- [ ] application.yml已配置环境变量
- [ ] 前端API地址已配置
- [ ] 数据库SQL文件已准备

部署后：
- [ ] 后端服务正常启动
- [ ] 数据库连接成功
- [ ] 数据已导入
- [ ] 前端可以访问
- [ ] 可以注册登录
- [ ] 所有功能正常
- [ ] 域名已记录

---

## 🎊 给老师的说明

部署完成后，给老师发送：

```
尊敬的老师，您好！

我的云笔记系统已经部署到线上了：

前端地址：https://xxx.vercel.app
后端地址：https://xxx.railway.app

您可以直接访问前端地址，注册账号后使用所有功能。

系统功能包括：
- 用户注册登录
- 笔记管理（创建、编辑、删除）
- 分类和标签管理
- 搜索功能
- 回收站
- AI学习助手
- 个人信息管理

测试账号（可选）：
用户名：testuser
密码：123456

如有任何问题，请随时联系我。

学生：张桂泉
学号：23002953
```

---

## 🚀 立即开始

现在就开始部署吧！只需要30分钟！

1. 上传代码到GitHub
2. 登录Railway
3. 部署后端和数据库
4. 登录Vercel
5. 部署前端
6. 完成！

**祝部署顺利！** 🎉

---

最后更新：2026-05-15
