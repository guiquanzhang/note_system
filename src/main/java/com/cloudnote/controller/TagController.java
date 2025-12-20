package com.cloudnote.controller;

import com.cloudnote.common.Result;
import com.cloudnote.dto.TagDTO;
import com.cloudnote.service.TagService;
import com.cloudnote.vo.TagVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 标签控制器
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    /**
     * 创建标签
     */
    @PostMapping
    public Result<Void> createTag(@Valid @RequestBody TagDTO tagDTO,
                                   @RequestAttribute Integer userId) {
        tagService.createTag(tagDTO, userId);
        return Result.success();
    }

    /**
     * 更新标签
     */
    @PutMapping("/{tagId}")
    public Result<Void> updateTag(@PathVariable Integer tagId,
                                   @Valid @RequestBody TagDTO tagDTO,
                                   @RequestAttribute Integer userId) {
        tagService.updateTag(tagId, tagDTO, userId);
        return Result.success();
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{tagId}")
    public Result<Void> deleteTag(@PathVariable Integer tagId,
                                   @RequestAttribute Integer userId) {
        tagService.deleteTag(tagId, userId);
        return Result.success();
    }

    /**
     * 获取标签列表（包含使用次数）
     */
    @GetMapping("/list")
    public Result<List<TagVO>> getTagList(@RequestAttribute Integer userId) {
        List<TagVO> tagList = tagService.getTagList(userId);
        return Result.success(tagList);
    }
}
