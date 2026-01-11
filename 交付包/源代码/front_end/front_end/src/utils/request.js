/**
 * Axios 请求封装
 * 统一处理请求拦截、响应拦截、错误处理
 */

import axios from 'axios'
import { ElMessage } from 'element-plus'
import auth from './auth'
import router from '@/router'

// 创建 axios 实例
const request = axios.create({
  baseURL: '/api', // 基础 URL，会被 Vite 代理到 http://localhost:8080/api
  timeout: 10000,  // 请求超时时间 10 秒
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 在请求发送前，自动添加 Token
    const token = auth.getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    console.log('📤 发送请求:', config.method.toUpperCase(), config.url)
    return config
  },
  (error) => {
    console.error('❌ 请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    console.log('📥 收到响应:', response.config.url, response.data)
    
    const { code, message, data } = response.data

    // 根据后端返回的 code 判断请求是否成功
    if (code === 200) {
      return data // 直接返回 data 部分
    } else {
      // 业务错误
      ElMessage.error(message || '请求失败')
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  (error) => {
    console.error('❌ 响应错误:', error)

    // HTTP 错误处理
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 400:
          ElMessage.error(data.message || '请求参数错误')
          break
        case 401:
          ElMessage.error('未授权，请重新登录')
          auth.clearAuth()
          router.push('/login')
          break
        case 403:
          ElMessage.error('拒绝访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error(data.message || '服务器内部错误')
          break
        default:
          ElMessage.error(data.message || '请求失败')
      }
    } else if (error.request) {
      // 请求已发出，但没有收到响应
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      // 其他错误
      ElMessage.error(error.message || '请求失败')
    }

    return Promise.reject(error)
  }
)

export default request
