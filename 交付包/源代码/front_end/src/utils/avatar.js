/**
 * 头像工具函数
 */

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
  // 注意：后端的静态资源路径需要加 /api 前缀
  return `http://localhost:8080/api${avatar}`
}
