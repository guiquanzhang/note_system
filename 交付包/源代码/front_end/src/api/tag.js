import request from '@/utils/request'

/**
 * 创建标签
 * @param {Object} data - 标签数据 { name, color }
 */
export const createTag = (data) => {
  return request({
    url: '/tag',
    method: 'POST',
    data
  })
}

/**
 * 更新标签
 * @param {Number} tagId - 标签ID
 * @param {Object} data - 标签数据 { name, color }
 */
export const updateTag = (tagId, data) => {
  return request({
    url: `/tag/${tagId}`,
    method: 'PUT',
    data
  })
}

/**
 * 删除标签
 * @param {Number} tagId - 标签ID
 */
export const deleteTag = (tagId) => {
  return request({
    url: `/tag/${tagId}`,
    method: 'DELETE'
  })
}

/**
 * 获取标签列表（包含使用次数）
 */
export const getTagList = () => {
  return request({
    url: '/tag/list',
    method: 'GET'
  })
}
