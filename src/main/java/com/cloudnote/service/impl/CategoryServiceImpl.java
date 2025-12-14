package com.cloudnote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloudnote.dto.CategoryDTO;
import com.cloudnote.entity.Category;
import com.cloudnote.mapper.CategoryMapper;
import com.cloudnote.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类服务实现
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public void createCategory(CategoryDTO categoryDTO, Integer userId) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setUserId(userId);
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Integer categoryId, CategoryDTO categoryDTO, Integer userId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new RuntimeException("分类不存在或无权限");
        }
        
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Integer categoryId, Integer userId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new RuntimeException("分类不存在或无权限");
        }
        categoryMapper.deleteById(categoryId);
    }

    @Override
    public List<Category> getCategoryList(Integer userId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getUserId, userId)
               .orderByDesc(Category::getCreatedAt);
        return categoryMapper.selectList(wrapper);
    }
}
