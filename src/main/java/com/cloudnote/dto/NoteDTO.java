package com.cloudnote.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 笔记请求DTO
 */
@Data
public class NoteDTO {
    @NotBlank(message = "标题不能为空")
    private String title;
    
    private String content;
    private Integer categoryId;
    private String tags;
}
