import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'

// 导入 Quill 编辑器样式
import '@vueup/vue-quill/dist/vue-quill.snow.css'

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

console.log('CloudNote Frontend - 应用启动成功！')
