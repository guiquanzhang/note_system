package com.cloudnote.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 分类请求DTO
 */
@Data
public class CategoryDTO {
    @NotBlank(message = "分类名称不能为空")
    private String name;
}
