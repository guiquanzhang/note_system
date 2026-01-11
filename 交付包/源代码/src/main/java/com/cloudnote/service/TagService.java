package com.cloudnote.service;

import com.cloudnote.dto.TagDTO;
import com.cloudnote.entity.Tag;
import com.cloudnote.vo.TagVO;
import java.util.List;

/**
 * 标签服务接口
 */
public interface TagService {
    
    /**
     * 创建标签
     */
    void createTag(TagDTO tagDTO, Integer userId);
    
    /**
     * 更新标签
     */
    void updateTag(Integer tagId, TagDTO tagDTO, Integer userId);
    
    /**
     * 删除标签
     */
    void deleteTag(Integer tagId, Integer userId);
    
    /**
     * 获取标签列表（包含使用次数）
     */
    List<TagVO> getTagList(Integer userId);
    
    /**
     * 根据名称获取或创建标签
     */
    Tag getOrCreateTag(String name, Integer userId);
}
