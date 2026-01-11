package com.cloudnote.service;

import com.cloudnote.dto.LoginDTO;
import com.cloudnote.dto.RegisterDTO;
import com.cloudnote.dto.UpdatePasswordDTO;
import com.cloudnote.dto.UpdateUserInfoDTO;
import com.cloudnote.vo.LoginVO;
import com.cloudnote.vo.UserInfoVO;

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
    
    /**
     * 获取用户信息
     */
    UserInfoVO getUserInfo(Integer userId);
    
    /**
     * 更新用户信息
     */
    void updateUserInfo(Integer userId, UpdateUserInfoDTO updateUserInfoDTO);
    
    /**
     * 修改密码
     */
    void updatePassword(Integer userId, UpdatePasswordDTO updatePasswordDTO);
}
