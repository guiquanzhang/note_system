package com.cloudnote.service;

import com.cloudnote.dto.CategoryDTO;
import com.cloudnote.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    
    /**
     * 创建分类
     */
    void createCategory(CategoryDTO categoryDTO, Integer userId);
    
    /**
     * 更新分类
     */
    void updateCategory(Integer categoryId, CategoryDTO categoryDTO, Integer userId);
    
    /**
     * 删除分类
     */
    void deleteCategory(Integer categoryId, Integer userId);
    
    /**
     * 获取用户所有分类
     */
    List<Category> getCategoryList(Integer userId);
}
