/**
 * 用户状态管理
 * 管理用户登录状态、用户信息
 */

import { defineStore } from 'pinia'
import { login as loginApi, register as registerApi } from '@/api/user'
import auth from '@/utils/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  // 状态
  state: () => ({
    token: auth.getToken() || '',
    userInfo: auth.getUserInfo() || null,
    isLoggedIn: !!auth.getToken()
  }),

  // 计算属性
  getters: {
    // 获取用户名
    username: (state) => state.userInfo?.username || '',
    
    // 获取用户ID
    userId: (state) => state.userInfo?.userId || null,
    
    // 获取邮箱
    email: (state) => state.userInfo?.email || '',
    
    // 获取头像
    avatar: (state) => state.userInfo?.avatar || '',
    
    // 是否已登录
    isAuthenticated: (state) => state.isLoggedIn
  },

  // 方法
  actions: {
    /**
     * 用户登录
     * @param {object} loginForm - 登录表单 { username, password }
     */
    async login(loginForm) {
      try {
        const data = await loginApi(loginForm)
        
        // 保存 Token
        this.token = data.token
        auth.setToken(data.token)
        
        // 保存用户信息
        this.userInfo = {
          userId: data.userId,
          username: data.username,
          email: data.email,
          avatar: data.avatar || ''
        }
        auth.setUserInfo(this.userInfo)
        
        // 更新登录状态
        this.isLoggedIn = true
        
        ElMessage.success('登录成功！')
        return data
      } catch (error) {
        ElMessage.error(error.message || '登录失败')
        throw error
      }
    },

    /**
     * 用户注册
     * @param {object} registerForm - 注册表单 { username, password, email }
     */
    async register(registerForm) {
      try {
        await registerApi(registerForm)
        ElMessage.success('注册成功！请登录')
        return true
      } catch (error) {
        ElMessage.error(error.message || '注册失败')
        throw error
      }
    },

    /**
     * 用户登出
     */
    logout() {
      // 清除状态
      this.token = ''
      this.userInfo = null
      this.isLoggedIn = false
      
      // 清除本地存储
      auth.clearAuth()
      
      ElMessage.success('已退出登录')
    },

    /**
     * 设置用户信息
     * @param {object} userInfo - 用户信息
     */
    setUserInfo(userInfo) {
      this.userInfo = userInfo
      auth.setUserInfo(userInfo)
    },

    /**
     * 检查登录状态
     */
    checkAuth() {
      const token = auth.getToken()
      const userInfo = auth.getUserInfo()
      
      if (token && userInfo) {
        this.token = token
        this.userInfo = userInfo
        this.isLoggedIn = true
        return true
      }
      
      return false
    }
  }
})
