package com.cloudnote.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * 更新用户信息 DTO
 */
@Data
public class UpdateUserInfoDTO {
    @Size(min = 3, max = 20, message = "用户名长度在 3 到 20 个字符")
    private String username;

    @Size(max = 50, message = "昵称长度不能超过 50 个字符")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String avatar;
}
