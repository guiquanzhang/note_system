/**
 * 分类相关 API
 */

import request from '@/utils/request'

/**
 * 创建分类
 * @param {object} data - 分类信息
 * @param {string} data.name - 分类名称
 * @returns {Promise}
 */
export function createCategory(data) {
  return request({
    url: '/category',
    method: 'post',
    data
  })
}

/**
 * 更新分类
 * @param {number} categoryId - 分类ID
 * @param {object} data - 分类信息
 * @param {string} data.name - 分类名称
 * @returns {Promise}
 */
export function updateCategory(categoryId, data) {
  return request({
    url: `/category/${categoryId}`,
    method: 'put',
    data
  })
}

/**
 * 删除分类
 * @param {number} categoryId - 分类ID
 * @returns {Promise}
 */
export function deleteCategory(categoryId) {
  return request({
    url: `/category/${categoryId}`,
    method: 'delete'
  })
}

/**
 * 获取分类列表
 * @returns {Promise<Array>} 返回分类数组
 */
export function getCategoryList() {
  return request({
    url: '/category/list',
    method: 'get'
  })
}
