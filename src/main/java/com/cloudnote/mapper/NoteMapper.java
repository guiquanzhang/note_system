package com.cloudnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudnote.entity.Note;
import org.apache.ibatis.annotations.Mapper;

/**
 * 笔记Mapper
 */
@Mapper
public interface NoteMapper extends BaseMapper<Note> {
}
