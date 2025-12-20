package com.cloudnote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 笔记标签关联实体
 */
@Data
@TableName("note_tag")
public class NoteTag {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer noteId;
    private Integer tagId;
    private LocalDateTime createdAt;
}
