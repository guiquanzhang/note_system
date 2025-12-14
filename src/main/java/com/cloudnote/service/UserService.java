package com.cloudnote.service;

import com.cloudnote.dto.LoginDTO;
import com.cloudnote.dto.RegisterDTO;
import com.cloudnote.vo.LoginVO;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    void register(RegisterDTO registerDTO);
    
    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);
}
