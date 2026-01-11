package com.cloudnote.controller;

import com.cloudnote.common.Result;
import com.cloudnote.dto.CategoryDTO;
import com.cloudnote.entity.Category;
import com.cloudnote.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping
    public Result<Void> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                        @RequestAttribute Integer userId) {
        categoryService.createCategory(categoryDTO, userId);
        return Result.success();
    }

    @PutMapping("/{categoryId}")
    public Result<Void> updateCategory(@PathVariable Integer categoryId,
                                        @Valid @RequestBody CategoryDTO categoryDTO,
                                        @RequestAttribute Integer userId) {
        categoryService.updateCategory(categoryId, categoryDTO, userId);
        return Result.success();
    }

    @DeleteMapping("/{categoryId}")
    public Result<Void> deleteCategory(@PathVariable Integer categoryId,
                                        @RequestAttribute Integer userId) {
        categoryService.deleteCategory(categoryId, userId);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(@RequestAttribute Integer userId) {
        List<Category> list = categoryService.getCategoryList(userId);
        return Result.success(list);
    }
}
