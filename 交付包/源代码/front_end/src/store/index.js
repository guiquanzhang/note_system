/**
 * Pinia Store 入口文件
 * 统一导出所有 Store 模块
 */

import { createPinia } from 'pinia'

// 创建 Pinia 实例
const pinia = createPinia()

// 导出 Store 模块（方便在组件中使用）
export { useUserStore } from './modules/user'
export { useNoteStore } from './modules/note'
export { useCategoryStore } from './modules/category'

export default pinia
