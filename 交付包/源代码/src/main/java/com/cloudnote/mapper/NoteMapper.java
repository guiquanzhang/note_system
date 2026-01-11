package com.cloudnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudnote.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 笔记Mapper
 */
@Mapper
public interface NoteMapper extends BaseMapper<Note> {
    
    /**
     * 软删除笔记（直接更新 deleted 字段）
     */
    @Update("UPDATE note SET deleted = 1, deleted_at = NOW() WHERE note_id = #{noteId}")
    int softDelete(@Param("noteId") Integer noteId);
    
    /**
     * 恢复笔记
     */
    @Update("UPDATE note SET deleted = 0, deleted_at = NULL WHERE note_id = #{noteId}")
    int restore(@Param("noteId") Integer noteId);
    
    /**
     * 查询已删除的笔记（回收站）
     */
    @Select("SELECT * FROM note WHERE user_id = #{userId} AND deleted = 1 ORDER BY deleted_at DESC LIMIT #{offset}, #{limit}")
    List<Note> selectDeletedNotes(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 统计已删除的笔记数量
     */
    @Select("SELECT COUNT(*) FROM note WHERE user_id = #{userId} AND deleted = 1")
    Long countDeletedNotes(@Param("userId") Integer userId);
    
    /**
     * 永久删除笔记
     */
    @Update("DELETE FROM note WHERE note_id = #{noteId}")
    int permanentDelete(@Param("noteId") Integer noteId);
    
    /**
     * 清空回收站
     */
    @Update("DELETE FROM note WHERE user_id = #{userId} AND deleted = 1")
    int emptyTrash(@Param("userId") Integer userId);
    
    /**
     * 根据 ID 查询笔记（包括已删除的）
     */
    @Select("SELECT * FROM note WHERE note_id = #{noteId}")
    Note selectByIdIncludeDeleted(@Param("noteId") Integer noteId);
}
