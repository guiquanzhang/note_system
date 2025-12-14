/**
 * API 统一导出
 * 方便在组件中统一导入
 */

import * as userApi from './user'
import * as noteApi from './note'
import * as categoryApi from './category'

export default {
  user: userApi,
  note: noteApi,
  category: categoryApi
}

// 也可以单独导出
export { userApi, noteApi, categoryApi }
