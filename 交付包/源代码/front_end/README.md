# 云笔记前端项目

基于 Vue 3 + Vite + Element Plus 的云笔记管理系统

## 技术栈

- Vue 3 - 渐进式 JavaScript 框架
- Vite - 下一代前端构建工具
- Element Plus - Vue 3 UI 组件库
- Axios - HTTP 请求库
- Vue Router - 路由管理
- Pinia - 状态管理

## 开发环境

- Node.js >= 16.0.0
- npm >= 8.0.0

## 安装依赖

```bash
npm install
```

## 启动开发服务器

```bash
npm run dev
```

## 构建生产版本

```bash
npm run build
```

## 预览生产构建

```bash
npm run preview
```

## 项目结构

```
front_end/
├── public/          # 静态资源
├── src/             # 源代码
│   ├── api/        # API 接口
│   ├── assets/     # 资源文件
│   ├── components/ # 组件
│   ├── layout/     # 布局
│   ├── router/     # 路由
│   ├── store/      # 状态管理
│   ├── utils/      # 工具函数
│   ├── views/      # 页面
│   ├── App.vue     # 根组件
│   └── main.js     # 入口文件
├── .env.development    # 开发环境变量
├── .env.production     # 生产环境变量
├── index.html          # HTML 模板
├── package.json        # 项目配置
└── vite.config.js      # Vite 配置
```

## 后端接口

后端服务地址：http://localhost:8080/api

## 开发规范

- 使用 Vue 3 Composition API
- 组件使用 `<script setup>` 语法
- 遵循 ESLint 代码规范
- 使用 Git 进行版本控制
