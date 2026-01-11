package com.cloudnote.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 标签DTO
 */
@Data
public class TagDTO {
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称不能超过50个字符")
    private String name;
    
    @Size(max = 20, message = "颜色值不能超过20个字符")
    private String color;
}
