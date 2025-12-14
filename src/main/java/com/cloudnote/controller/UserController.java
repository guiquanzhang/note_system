package com.cloudnote.controller;

import com.cloudnote.common.Result;
import com.cloudnote.dto.LoginDTO;
import com.cloudnote.dto.RegisterDTO;
import com.cloudnote.service.UserService;
import com.cloudnote.vo.LoginVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<com.cloudnote.vo.UserInfoVO> getUserInfo() {
        Integer userId = com.cloudnote.context.UserContext.getUserId();
        com.cloudnote.vo.UserInfoVO userInfoVO = userService.getUserInfo(userId);
        return Result.success(userInfoVO);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@Valid @RequestBody com.cloudnote.dto.UpdateUserInfoDTO updateUserInfoDTO) {
        Integer userId = com.cloudnote.context.UserContext.getUserId();
        userService.updateUserInfo(userId, updateUserInfoDTO);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody com.cloudnote.dto.UpdatePasswordDTO updatePasswordDTO) {
        Integer userId = com.cloudnote.context.UserContext.getUserId();
        userService.updatePassword(userId, updatePasswordDTO);
        return Result.success();
    }
}
