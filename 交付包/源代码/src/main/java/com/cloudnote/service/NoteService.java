package com.cloudnote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudnote.dto.NoteDTO;
import com.cloudnote.entity.Note;

/**
 * 笔记服务接口
 */
public interface NoteService {
    
    /**
     * 创建笔记
     */
    void createNote(NoteDTO noteDTO, Integer userId);
    
    /**
     * 更新笔记
     */
    void updateNote(Integer noteId, NoteDTO noteDTO, Integer userId);
    
    /**
     * 删除笔记
     */
    void deleteNote(Integer noteId, Integer userId);
    
    /**
     * 获取笔记详情
     */
    Note getNoteById(Integer noteId, Integer userId);
    
    /**
     * 分页查询笔记
     */
    Page<Note> getNoteList(Integer userId, Integer pageNum, Integer pageSize);
    
    /**
     * 搜索笔记
     */
    Page<Note> searchNotes(Integer userId, String keyword, Integer pageNum, Integer pageSize);
    
    /**
     * 获取回收站笔记列表
     */
    Page<Note> getDeletedNotes(Integer userId, Integer pageNum, Integer pageSize);
    
    /**
     * 恢复笔记
     */
    void restoreNote(Integer noteId, Integer userId);
    
    /**
     * 永久删除笔记
     */
    void permanentlyDeleteNote(Integer noteId, Integer userId);
    
    /**
     * 清空回收站
     */
    void emptyTrash(Integer userId);
}
