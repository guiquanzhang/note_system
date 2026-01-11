/**
 * 分类状态管理
 * 管理分类列表
 */

import { defineStore } from 'pinia'
import {
  getCategoryList as getCategoryListApi,
  createCategory as createCategoryApi,
  updateCategory as updateCategoryApi,
  deleteCategory as deleteCategoryApi
} from '@/api/category'
import { ElMessage } from 'element-plus'

export const useCategoryStore = defineStore('category', {
  // 状态
  state: () => ({
    categoryList: [],       // 分类列表
    loading: false          // 加载状态
  }),

  // 计算属性
  getters: {
    // 是否有分类
    hasCategories: (state) => state.categoryList.length > 0,
    
    // 分类数量
    categoryCount: (state) => state.categoryList.length,
    
    // 根据ID获取分类
    getCategoryById: (state) => (categoryId) => {
      return state.categoryList.find(cat => cat.categoryId === categoryId)
    },
    
    // 获取分类名称
    getCategoryName: (state) => (categoryId) => {
      const category = state.categoryList.find(cat => cat.categoryId === categoryId)
      return category ? category.name : '未分类'
    }
  },

  // 方法
  actions: {
    /**
     * 获取分类列表
     */
    async fetchCategoryList() {
      try {
        this.loading = true
        const data = await getCategoryListApi()
        this.categoryList = data || []
        return data
      } catch (error) {
        ElMessage.error('获取分类列表失败')
        throw error
      } finally {
        this.loading = false
      }
    },

    /**
     * 创建分类
     * @param {object} categoryData - 分类数据 { name }
     */
    async createCategory(categoryData) {
      try {
        await createCategoryApi(categoryData)
        ElMessage.success('创建分类成功')
        
        // 刷新列表
        await this.fetchCategoryList()
        
        return true
      } catch (error) {
        ElMessage.error('创建分类失败')
        throw error
      }
    },

    /**
     * 更新分类
     * @param {number} categoryId - 分类ID
     * @param {object} categoryData - 分类数据 { name }
     */
    async updateCategory(categoryId, categoryData) {
      try {
        await updateCategoryApi(categoryId, categoryData)
        ElMessage.success('更新分类成功')
        
        // 刷新列表
        await this.fetchCategoryList()
        
        return true
      } catch (error) {
        ElMessage.error('更新分类失败')
        throw error
      }
    },

    /**
     * 删除分类
     * @param {number} categoryId - 分类ID
     */
    async deleteCategory(categoryId) {
      try {
        await deleteCategoryApi(categoryId)
        ElMessage.success('删除分类成功')
        
        // 刷新列表
        await this.fetchCategoryList()
        
        return true
      } catch (error) {
        ElMessage.error('删除分类失败')
        throw error
      }
    },

    /**
     * 重置状态
     */
    reset() {
      this.categoryList = []
      this.loading = false
    }
  }
})
