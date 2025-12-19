package com.cloudnote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudnote.dto.NoteDTO;
import com.cloudnote.entity.Note;
import com.cloudnote.mapper.NoteMapper;
import com.cloudnote.service.NoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 笔记服务实现
 */
@Service
public class NoteServiceImpl implements NoteService {

    @Resource
    private NoteMapper noteMapper;

    @Override
    public void createNote(NoteDTO noteDTO, Integer userId) {
        Note note = new Note();
        BeanUtils.copyProperties(noteDTO, note);
        note.setUserId(userId);
        note.setDeleted(0); // 默认未删除
        noteMapper.insert(note);
    }

    @Override
    public void updateNote(Integer noteId, NoteDTO noteDTO, Integer userId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new RuntimeException("笔记不存在或无权限");
        }
        
        BeanUtils.copyProperties(noteDTO, note);
        noteMapper.updateById(note);
    }

    @Override
    public void deleteNote(Integer noteId, Integer userId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new RuntimeException("笔记不存在或无权限");
        }
        
        // 软删除：标记为已删除
        note.setDeleted(1);
        note.setDeletedAt(java.time.LocalDateTime.now());
        noteMapper.updateById(note);
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
}
