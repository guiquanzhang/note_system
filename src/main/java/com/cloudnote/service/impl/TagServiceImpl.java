package com.cloudnote.service.impl;

import com.cloudnote.dto.TagDTO;
import com.cloudnote.entity.Tag;
import com.cloudnote.mapper.NoteTagMapper;
import com.cloudnote.mapper.TagMapper;
import com.cloudnote.service.TagService;
import com.cloudnote.vo.TagVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签服务实现
 */
@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;
    
    @Resource
    private NoteTagMapper noteTagMapper;

    @Override
    public void createTag(TagDTO tagDTO, Integer userId) {
        // 检查标签是否已存在
        Tag existingTag = tagMapper.selectByName(userId, tagDTO.getName());
        if (existingTag != null) {
            throw new RuntimeException("标签已存在");
        }
        
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setColor(tagDTO.getColor());
        tag.setUserId(userId);
        tagMapper.insert(tag);
    }

    @Override
    public void updateTag(Integer tagId, TagDTO tagDTO, Integer userId) {
        Tag tag = tagMapper.selectById(tagId);
        if (tag == null || !tag.getUserId().equals(userId)) {
            throw new RuntimeException("标签不存在或无权限");
        }
        
        // 检查新名称是否与其他标签重复
        if (!tag.getName().equals(tagDTO.getName())) {
            Tag existingTag = tagMapper.selectByName(userId, tagDTO.getName());
            if (existingTag != null) {
                throw new RuntimeException("标签名称已存在");
            }
        }
        
        tag.setName(tagDTO.getName());
        tag.setColor(tagDTO.getColor());
        tagMapper.updateById(tag);
    }

    @Override
    @Transactional
    public void deleteTag(Integer tagId, Integer userId) {
        Tag tag = tagMapper.selectById(tagId);
        if (tag == null || !tag.getUserId().equals(userId)) {
            throw new RuntimeException("标签不存在或无权限");
        }
        
        // 删除标签
        tagMapper.deleteById(tagId);
        
        // 删除所有关联关系
        noteTagMapper.delete(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.cloudnote.entity.NoteTag>()
                .eq(com.cloudnote.entity.NoteTag::getTagId, tagId)
        );
    }

    @Override
    public List<TagVO> getTagList(Integer userId) {
        return tagMapper.selectTagsWithCount(userId);
    }

    @Override
    public Tag getOrCreateTag(String name, Integer userId) {
        Tag tag = tagMapper.selectByName(userId, name);
        if (tag == null) {
            tag = new Tag();
            tag.setName(name);
            tag.setUserId(userId);
            tagMapper.insert(tag);
        }
        return tag;
    }
}
