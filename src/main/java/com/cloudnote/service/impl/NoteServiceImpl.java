package com.cloudnote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudnote.dto.NoteDTO;
import com.cloudnote.entity.Note;
import com.cloudnote.entity.NoteTag;
import com.cloudnote.entity.Tag;
import com.cloudnote.mapper.NoteMapper;
import com.cloudnote.mapper.NoteTagMapper;
import com.cloudnote.mapper.TagMapper;
import com.cloudnote.service.NoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 笔记服务实现
 */
@Service
public class NoteServiceImpl implements NoteService {

    @Resource
    private NoteMapper noteMapper;
    
    @Resource
    private TagMapper tagMapper;
    
    @Resource
    private NoteTagMapper noteTagMapper;

    @Override
    @Transactional
    public void createNote(NoteDTO noteDTO, Integer userId) {
        Note note = new Note();
        BeanUtils.copyProperties(noteDTO, note);
        note.setUserId(userId);
        note.setDeleted(0); // 默认未删除
        noteMapper.insert(note);
        
        // 处理标签
        handleNoteTags(note.getNoteId(), noteDTO.getTags(), userId);
    }

    @Override
    @Transactional
    public void updateNote(Integer noteId, NoteDTO noteDTO, Integer userId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new RuntimeException("笔记不存在或无权限");
        }
        
        BeanUtils.copyProperties(noteDTO, note);
        noteMapper.updateById(note);
        
        // 删除旧的标签关联
        noteTagMapper.deleteByNoteId(noteId);
        
        // 处理新的标签
        handleNoteTags(noteId, noteDTO.getTags(), userId);
    }
    
    /**
     * 处理笔记标签
     */
    private void handleNoteTags(Integer noteId, String tagsStr, Integer userId) {
        if (tagsStr == null || tagsStr.trim().isEmpty()) {
            return;
        }
        
        // 解析标签字符串
        List<String> tagNames = Arrays.stream(tagsStr.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .distinct()
            .collect(Collectors.toList());
        
        // 为每个标签创建或获取标签ID，并创建关联
        for (String tagName : tagNames) {
            // 查找或创建标签
            Tag tag = tagMapper.selectByName(userId, tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag.setUserId(userId);
                tagMapper.insert(tag);
            }
            
            // 创建笔记标签关联
            NoteTag noteTag = new NoteTag();
            noteTag.setNoteId(noteId);
            noteTag.setTagId(tag.getTagId());
            noteTagMapper.insert(noteTag);
        }
    }

    @Override
    public void deleteNote(Integer noteId, Integer userId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new RuntimeException("笔记不存在或无权限");
        }
        
        // 使用自定义 SQL 软删除
        noteMapper.softDelete(noteId);
    }

    @Override
    public Note getNoteById(Integer noteId, Integer userId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId) || note.getDeleted() == 1) {
            throw new RuntimeException("笔记不存在或无权限");
        }
        return note;
    }

    @Override
    public Page<Note> getNoteList(Integer userId, Integer pageNum, Integer pageSize) {
        Page<Note> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Note::getUserId, userId)
               .eq(Note::getDeleted, 0) // 只查询未删除的笔记
               .orderByDesc(Note::getUpdatedAt);
        return noteMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<Note> searchNotes(Integer userId, String keyword, Integer pageNum, Integer pageSize) {
        Page<Note> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Note::getUserId, userId)
               .eq(Note::getDeleted, 0) // 只搜索未删除的笔记
               .and(w -> w.like(Note::getTitle, keyword).or().like(Note::getContent, keyword))
               .orderByDesc(Note::getUpdatedAt);
        return noteMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<Note> getDeletedNotes(Integer userId, Integer pageNum, Integer pageSize) {
        // 使用自定义 SQL 查询已删除的笔记
        int offset = (pageNum - 1) * pageSize;
        List<Note> records = noteMapper.selectDeletedNotes(userId, offset, pageSize);
        Long total = noteMapper.countDeletedNotes(userId);
        
        Page<Note> page = new Page<>(pageNum, pageSize);
        page.setRecords(records);
        page.setTotal(total);
        return page;
    }

    @Override
    public void restoreNote(Integer noteId, Integer userId) {
        // 使用自定义 SQL 查询（包括已删除的笔记）
        Note note = noteMapper.selectByIdIncludeDeleted(noteId);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new RuntimeException("笔记不存在或无权限");
        }
        
        // 使用自定义 SQL 恢复笔记
        noteMapper.restore(noteId);
    }

    @Override
    @Transactional
    public void permanentlyDeleteNote(Integer noteId, Integer userId) {
        // 使用自定义 SQL 查询（包括已删除的笔记）
        Note note = noteMapper.selectByIdIncludeDeleted(noteId);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new RuntimeException("笔记不存在或无权限");
        }
        
        // 删除标签关联
        noteTagMapper.deleteByNoteId(noteId);
        
        // 使用自定义 SQL 永久删除
        noteMapper.permanentDelete(noteId);
    }

    @Override
    public void emptyTrash(Integer userId) {
        // 使用自定义 SQL 清空回收站
        noteMapper.emptyTrash(userId);
    }
}
