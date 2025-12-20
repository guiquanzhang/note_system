# 查看新 Logo 的步骤

## 问题原因

浏览器缓存了旧的图标和页面，需要强制刷新。

## 解决方法

### 方法 1：强制刷新（推荐）

**Windows/Linux**：
```
Ctrl + Shift + R
或
Ctrl + F5
```

**Mac**：
```
Cmd + Shift + R
```

### 方法 2：清除浏览器缓存

#### Chrome/Edge
1. 按 `F12` 打开开发者工具
2. 右键点击刷新按钮
3. 选择"清空缓存并硬性重新加载"

或者：
1. 按 `Ctrl + Shift + Delete`
2. 选择"缓存的图片和文件"
3. 点击"清除数据"

#### Firefox
1. 按 `Ctrl + Shift + Delete`
2. 选择"缓存"
3. 点击"立即清除"

### 方法 3：无痕模式测试

1. 按 `Ctrl + Shift + N`（Chrome/Edge）或 `Ctrl + Shift + P`（Firefox）
2. 打开无痕窗口
3. 访问 `http://localhost:5173`
4. 应该能看到新 Logo

### 方法 4：重启开发服务器

如果前端服务器还在运行：
1. 停止前端服务器（Ctrl+C）
2. 重新启动：
```bash
cd front_end
npm run dev
```

## 验证 Logo 是否生效

### 1. 检查浏览器标签页
- 应该看到紫色渐变的圆角方形图标
- 里面有云朵和笔记本图案

### 2. 检查登录页面
- 打开 `http://localhost:5173/login`
- 应该看到大的 Logo（120x120px）
- 有云朵、笔记本、笔的组合图案
- Logo 有弹跳动画效果

### 3. 检查注册页面
- 打开 `http://localhost:5173/register`
- 应该看到相同的 Logo

## 如果还是看不到

### 检查文件是否存在

在项目根目录执行：
```bash
dir front_end\public\logo.svg
dir front_end\public\favicon.svg
```

应该看到两个文件。

### 检查浏览器控制台

1. 按 `F12` 打开开发者工具
2. 切换到 Console 标签
3. 刷新页面
4. 查看是否有 404 错误（找不到 logo.svg 或 favicon.svg）

### 手动访问 Logo 文件

在浏览器中直接访问：
- `http://localhost:5173/logo.svg`
- `http://localhost:5173/favicon.svg`

应该能看到 SVG 图标。

## Logo 预览

### favicon.svg（浏览器标签页）
- 紫色到粉红色的渐变背景
- 白色云朵在上方
- 白色笔记本在下方
- 笔记本上有横线

### logo.svg（登录/注册页）
- 淡紫色圆形背景
- 紫色渐变云朵
- 白色笔记本（带粉红色线条）
- 右下角有装饰性的笔

## 最简单的方法

**直接按 Ctrl + Shift + R 强制刷新！**

这会清除当前页面的所有缓存并重新加载。
