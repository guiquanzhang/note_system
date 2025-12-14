/**
 * 笔记相关 API
 */

import request from '@/utils/request'

/**
 * 创建笔记
 * @param {object} data - 笔记信息
 * @param {string} data.title - 标题
 * @param {string} data.content - 内容
 * @param {number} data.categoryId - 分类ID（可选）
 * @param {string} data.tags - 标签（可选）
 * @returns {Promise}
 */
export function createNote(data) {
  return request({
    url: '/note',
    method: 'post',
    data
  })
}

/**
 * 更新笔记
 * @param {number} noteId - 笔记ID
 * @param {object} data - 笔记信息
 * @returns {Promise}
 */
export function updateNote(noteId, data) {
  return request({
    url: `/note/${noteId}`,
    method: 'put',
    data
  })
}

/**
 * 删除笔记
 * @param {number} noteId - 笔记ID
 * @returns {Promise}
 */
export function deleteNote(noteId) {
  return request({
    url: `/note/${noteId}`,
    method: 'delete'
  })
}

/**
 * 获取笔记详情
 * @param {number} noteId - 笔记ID
 * @returns {Promise<object>}
 */
export function getNoteDetail(noteId) {
  return request({
    url: `/note/${noteId}`,
    method: 'get'
  })
}

/**
 * 获取笔记列表（分页）
 * @param {object} params - 查询参数
 * @param {number} params.pageNum - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回分页数据
 */
export function getNoteList(params) {
  return request({
    url: '/note/list',
    method: 'get',
    params
  })
}

/**
 * 搜索笔记
 * @param {object} params - 搜索参数
 * @param {string} params.keyword - 搜索关键词
 * @param {number} params.pageNum - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>}
 */
export function searchNotes(params) {
  return request({
    url: '/note/search',
    method: 'get',
    params
  })
}
