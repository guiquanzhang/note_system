package com.cloudnote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 标签实体
 */
@Data
@TableName("tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Integer tagId;
    private String name;
    private String color;
    private Integer userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
