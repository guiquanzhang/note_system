/**
 * 认证工具类
 * 管理 Token 的存储、获取、删除
 */

import storage from './storage'

const TOKEN_KEY = 'cloudnote_token'
const USER_INFO_KEY = 'cloudnote_user_info'

const auth = {
  /**
   * 设置 Token
   * @param {string} token JWT Token
   */
  setToken(token) {
    storage.set(TOKEN_KEY, token)
  },

  /**
   * 获取 Token
   * @returns {string|null} Token 或 null
   */
  getToken() {
    return storage.get(TOKEN_KEY)
  },

  /**
   * 删除 Token
   */
  removeToken() {
    storage.remove(TOKEN_KEY)
  },

  /**
   * 检查是否已登录
   * @returns {boolean} 是否有 Token
   */
  isAuthenticated() {
    return !!this.getToken()
  },

  /**
   * 设置用户信息
   * @param {object} userInfo 用户信息对象
   */
  setUserInfo(userInfo) {
    storage.set(USER_INFO_KEY, userInfo)
  },

  /**
   * 获取用户信息
   * @returns {object|null} 用户信息或 null
   */
  getUserInfo() {
    return storage.get(USER_INFO_KEY)
  },

  /**
   * 删除用户信息
   */
  removeUserInfo() {
    storage.remove(USER_INFO_KEY)
  },

  /**
   * 清除所有认证信息（登出）
   */
  clearAuth() {
    this.removeToken()
    this.removeUserInfo()
  }
}

export default auth
