package com.cloudnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudnote.entity.NoteTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 笔记标签关联Mapper
 */
@Mapper
public interface NoteTagMapper extends BaseMapper<NoteTag> {
    
    /**
     * 删除笔记的所有标签
     */
    @Delete("DELETE FROM note_tag WHERE note_id = #{noteId}")
    int deleteByNoteId(@Param("noteId") Integer noteId);
    
    /**
     * 获取笔记的所有标签ID
     */
    @Select("SELECT tag_id FROM note_tag WHERE note_id = #{noteId}")
    List<Integer> selectTagIdsByNoteId(@Param("noteId") Integer noteId);
    
    /**
     * 获取使用某个标签的所有笔记ID
     */
    @Select("SELECT note_id FROM note_tag WHERE tag_id = #{tagId}")
    List<Integer> selectNoteIdsByTagId(@Param("tagId") Integer tagId);
}
