/**
 * 笔记状态管理
 * 管理笔记列表、当前笔记
 */

import { defineStore } from 'pinia'
import {
  getNoteList as getNoteListApi,
  getNoteDetail as getNoteDetailApi,
  createNote as createNoteApi,
  updateNote as updateNoteApi,
  deleteNote as deleteNoteApi,
  searchNotes as searchNotesApi
} from '@/api/note'
import { ElMessage } from 'element-plus'

export const useNoteStore = defineStore('note', {
  // 状态
  state: () => ({
    noteList: [],           // 笔记列表
    currentNote: null,      // 当前笔记
    total: 0,               // 总数
    pageNum: 1,             // 当前页码
    pageSize: 10,           // 每页数量
    loading: false,         // 加载状态
    searchKeyword: ''       // 搜索关键词
  }),

  // 计算属性
  getters: {
    // 是否有笔记
    hasNotes: (state) => state.noteList.length > 0,
    
    // 总页数
    totalPages: (state) => Math.ceil(state.total / state.pageSize),
    
    // 是否在搜索
    isSearching: (state) => !!state.searchKeyword
  },

  // 方法
  actions: {
    /**
     * 获取笔记列表
     * @param {number} pageNum - 页码
     * @param {number} pageSize - 每页数量
     */
    async fetchNoteList(pageNum = 1, pageSize = 10) {
      try {
        this.loading = true
        this.pageNum = pageNum
        this.pageSize = pageSize
        
        const data = await getNoteListApi({ pageNum, pageSize })
        
        this.noteList = data.records || []
        this.total = data.total || 0
        
        return data
      } catch (error) {
        ElMessage.error('获取笔记列表失败')
        throw error
      } finally {
        this.loading = false
      }
    },

    /**
     * 搜索笔记
     * @param {string} keyword - 搜索关键词
     * @param {number} pageNum - 页码
     * @param {number} pageSize - 每页数量
     */
    async searchNotes(keyword, pageNum = 1, pageSize = 10) {
      try {
        this.loading = true
        this.searchKeyword = keyword
        this.pageNum = pageNum
        this.pageSize = pageSize
        
        const data = await searchNotesApi({ keyword, pageNum, pageSize })
        
        this.noteList = data.records || []
        this.total = data.total || 0
        
        return data
      } catch (error) {
        ElMessage.error('搜索笔记失败')
        throw error
      } finally {
        this.loading = false
      }
    },

    /**
     * 清除搜索
     */
    clearSearch() {
      this.searchKeyword = ''
      this.fetchNoteList(1, this.pageSize)
    },

    /**
     * 获取笔记详情
     * @param {number} noteId - 笔记ID
     */
    async fetchNoteDetail(noteId) {
      try {
        this.loading = true
        const data = await getNoteDetailApi(noteId)
        this.currentNote = data
        return data
      } catch (error) {
        ElMessage.error('获取笔记详情失败')
        throw error
      } finally {
        this.loading = false
      }
    },

    /**
     * 创建笔记
     * @param {object} noteData - 笔记数据
     */
    async createNote(noteData) {
      try {
        await createNoteApi(noteData)
        ElMessage.success('创建笔记成功')
        
        // 刷新列表
        await this.fetchNoteList(this.pageNum, this.pageSize)
        
        return true
      } catch (error) {
        ElMessage.error('创建笔记失败')
        throw error
      }
    },

    /**
     * 更新笔记
     * @param {number} noteId - 笔记ID
     * @param {object} noteData - 笔记数据
     */
    async updateNote(noteId, noteData) {
      try {
        await updateNoteApi(noteId, noteData)
        ElMessage.success('更新笔记成功')
        
        // 刷新列表
        await this.fetchNoteList(this.pageNum, this.pageSize)
        
        // 如果是当前笔记，更新当前笔记
        if (this.currentNote?.noteId === noteId) {
          await this.fetchNoteDetail(noteId)
        }
        
        return true
      } catch (error) {
        ElMessage.error('更新笔记失败')
        throw error
      }
    },

    /**
     * 删除笔记
     * @param {number} noteId - 笔记ID
     */
    async deleteNote(noteId) {
      try {
        await deleteNoteApi(noteId)
        ElMessage.success('删除笔记成功')
        
        // 刷新列表
        await this.fetchNoteList(this.pageNum, this.pageSize)
        
        // 如果删除的是当前笔记，清空当前笔记
        if (this.currentNote?.noteId === noteId) {
          this.currentNote = null
        }
        
        return true
      } catch (error) {
        ElMessage.error('删除笔记失败')
        throw error
      }
    },

    /**
     * 设置当前笔记
     * @param {object} note - 笔记对象
     */
    setCurrentNote(note) {
      this.currentNote = note
    },

    /**
     * 清空当前笔记
     */
    clearCurrentNote() {
      this.currentNote = null
    },

    /**
     * 重置状态
     */
    reset() {
      this.noteList = []
      this.currentNote = null
      this.total = 0
      this.pageNum = 1
      this.pageSize = 10
      this.loading = false
      this.searchKeyword = ''
    }
  }
})
