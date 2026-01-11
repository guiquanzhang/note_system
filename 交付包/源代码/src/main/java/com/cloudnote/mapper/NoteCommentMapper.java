package com.cloudnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudnote.entity.NoteComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论Mapper
 */
@Mapper
public interface NoteCommentMapper extends BaseMapper<NoteComment> {
}
