/**
 * 处理笔记内容中的图片 URL
 * 将相对路径转换为完整的 API 地址
 */
export const processNoteContent = (content) => {
  if (!content) return ''
  
  // 使用 Vite 代理的 /api 路径
  const apiBaseUrl = '/api'
  
  // 替换图片 src 中的相对路径为完整 URL
  // 匹配 <img src="/uploads/..."> 或 <img src="uploads/...">
  return content.replace(
    /<img([^>]*?)src=["'](?!http|data:|\/api)([^"']+)["']/gi,
    (match, attrs, src) => {
      // 如果 src 不是以 / 开头，添加 /
      const normalizedSrc = src.startsWith('/') ? src : '/' + src
      return `<img${attrs}src="${apiBaseUrl}${normalizedSrc}"`
    }
  )
}

/**
 * 去除 HTML 标签，用于内容预览
 */
export const stripHtml = (html) => {
  if (!html) return ''
  const tmp = document.createElement('div')
  tmp.innerHTML = html
  return tmp.textContent || tmp.innerText || ''
}
