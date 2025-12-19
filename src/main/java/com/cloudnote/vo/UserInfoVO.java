package com.cloudnote.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户信息 VO
 */
@Data
public class UserInfoVO {
    private Integer userId;
    private String username;
    private String nickname;
    private String email;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
