package com.cloudnote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论实体
 */
@Data
@TableName("note_comment")
public class NoteComment {
    @TableId(type = IdType.AUTO)
    private Integer commentId;
    private Integer noteId;
    private Integer userId;
    private String content;
    private LocalDateTime createdAt;
}
