import request from '@/utils/request'

/**
 * 上传头像
 * @param {File} file - 图片文件
 * @returns {Promise<string>} 返回图片URL
 */
export const uploadAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/file/upload/avatar',
    method: 'POST',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传笔记图片
 * @param {File} file - 图片文件
 * @returns {Promise<string>} 返回图片URL
 */
export const uploadNoteImage = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/file/upload/note-image',
    method: 'POST',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除文件
 * @param {string} url - 文件URL
 */
export const deleteFile = (url) => {
  return request({
    url: '/file/delete',
    method: 'DELETE',
    params: { url }
  })
}
