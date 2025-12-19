package com.cloudnote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 笔记实体
 */
@Data
@TableName("note")
public class Note {
    @TableId(type = IdType.AUTO)
    private Integer noteId;
    private String title;
    private String content;
    private Integer userId;
    private Integer categoryId;
    private String tags;
    private Integer deleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
