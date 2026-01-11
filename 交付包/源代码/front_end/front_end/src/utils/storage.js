/**
 * 本地存储工具类
 * 封装 localStorage 和 sessionStorage
 */

const storage = {
  /**
   * 设置 localStorage
   * @param {string} key 键名
   * @param {any} value 值（自动转换为 JSON）
   */
  set(key, value) {
    try {
      localStorage.setItem(key, JSON.stringify(value))
    } catch (error) {
      console.error('localStorage 存储失败:', error)
    }
  },

  /**
   * 获取 localStorage
   * @param {string} key 键名
   * @returns {any} 解析后的值
   */
  get(key) {
    try {
      const value = localStorage.getItem(key)
      return value ? JSON.parse(value) : null
    } catch (error) {
      console.error('localStorage 读取失败:', error)
      return null
    }
  },

  /**
   * 删除 localStorage
   * @param {string} key 键名
   */
  remove(key) {
    try {
      localStorage.removeItem(key)
    } catch (error) {
      console.error('localStorage 删除失败:', error)
    }
  },

  /**
   * 清空 localStorage
   */
  clear() {
    try {
      localStorage.clear()
    } catch (error) {
      console.error('localStorage 清空失败:', error)
    }
  },

  /**
   * 设置 sessionStorage
   * @param {string} key 键名
   * @param {any} value 值
   */
  setSession(key, value) {
    try {
      sessionStorage.setItem(key, JSON.stringify(value))
    } catch (error) {
      console.error('sessionStorage 存储失败:', error)
    }
  },

  /**
   * 获取 sessionStorage
   * @param {string} key 键名
   * @returns {any} 解析后的值
   */
  getSession(key) {
    try {
      const value = sessionStorage.getItem(key)
      return value ? JSON.parse(value) : null
    } catch (error) {
      console.error('sessionStorage 读取失败:', error)
      return null
    }
  },

  /**
   * 删除 sessionStorage
   * @param {string} key 键名
   */
  removeSession(key) {
    try {
      sessionStorage.removeItem(key)
    } catch (error) {
      console.error('sessionStorage 删除失败:', error)
    }
  },

  /**
   * 清空 sessionStorage
   */
  clearSession() {
    try {
      sessionStorage.clear()
    } catch (error) {
      console.error('sessionStorage 清空失败:', error)
    }
  }
}

export default storage
