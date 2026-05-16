/**
 * 头像工具函数
 */

// 获取后端API地址
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

/**
 * 获取头像完整URL
 * @param {string} avatar - 头像路径或URL
 * @returns {string} 完整的头像URL
 */
export function getAvatarUrl(avatar) {
  if (!avatar) return ''
  
  // 如果是完整URL，直接返回
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar
  }
  
  // 如果是相对路径，拼接后端地址
  // 移除 /api 后缀（因为 API_BASE_URL 已经包含了）
  const baseUrl = API_BASE_URL.replace('/api', '')
  return `${baseUrl}${avatar}`
}
