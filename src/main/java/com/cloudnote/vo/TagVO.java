package com.cloudnote.vo;

import lombok.Data;

/**
 * 标签VO（包含使用次数）
 */
@Data
public class TagVO {
    private Integer tagId;
    private String name;
    private String color;
    private Integer count; // 使用次数
}
