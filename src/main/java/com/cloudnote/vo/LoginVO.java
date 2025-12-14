package com.cloudnote.vo;

import lombok.Data;

/**
 * 登录响应VO
 */
@Data
public class LoginVO {
    private String token;
    private Integer userId;
    private String username;
    private String email;
}
