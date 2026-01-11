import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'

// 导入 Quill 编辑器样式
import '@vueup/vue-quill/dist/vue-quill.snow.css'

// 导入 Element Plus 暗色主题
import 'element-plus/theme-chalk/dark/css-vars.css'

// 导入自定义主题样式
import './styles/theme.css'

// 导入动画效果
import './styles/animations.css'

// 导入毛玻璃效果
import './styles/glassmorphism.css'

// 创建 Vue 应用实例
const app = createApp(App)

// 创建 Pinia 实例
const pinia = createPinia()

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 使用插件
app.use(pinia)           // 状态管理
app.use(router)          // 路由
app.use(ElementPlus)     // UI 组件库

// 挂载应用
app.mount('#app')

// 初始化主题
const initTheme = () => {
  const theme = localStorage.getItem('theme') || 'light'
  let actualTheme = theme
  
  if (theme === 'auto') {
    const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    actualTheme = isDark ? 'dark' : 'light'
  }
  
  const html = document.documentElement
  html.classList.remove('dark', 'blue')
  
  if (actualTheme === 'dark') {
    html.classList.add('dark')
    html.style.colorScheme = 'dark'
  } else if (actualTheme === 'blue') {
    html.classList.add('blue')
    html.style.colorScheme = 'dark'
  } else {
    html.style.colorScheme = 'light'
  }
}

initTheme()

console.log('CloudNote Frontend - 应用启动成功！')
