package com.cloudnote.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloudnote.dto.LoginDTO;
import com.cloudnote.dto.RegisterDTO;
import com.cloudnote.entity.User;
import com.cloudnote.mapper.UserMapper;
import com.cloudnote.service.UserService;
import com.cloudnote.util.JwtUtil;
import com.cloudnote.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        User existUser = userMapper.selectOne(wrapper);
        
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        String hashedPassword = BCrypt.hashpw(registerDTO.getPassword());
        user.setPassword(hashedPassword);
        
        log.info("注册用户: {}, 原始密码: {}, 加密后密码: {}", 
                registerDTO.getUsername(), registerDTO.getPassword(), hashedPassword);
        
        userMapper.insert(user);
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        log.info("登录请求 - 用户名: {}, 密码: {}", loginDTO.getUsername(), loginDTO.getPassword());
        
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            log.error("用户不存在: {}", loginDTO.getUsername());
            throw new RuntimeException("用户名或密码错误");
        }

        log.info("数据库中的用户 - ID: {}, 用户名: {}, 密码哈希: {}", 
                user.getUserId(), user.getUsername(), user.getPassword());

        // 验证密码
        boolean passwordMatch = BCrypt.checkpw(loginDTO.getPassword(), user.getPassword());
        log.info("密码验证结果: {}", passwordMatch);
        
        if (!passwordMatch) {
            log.error("密码验证失败 - 输入密码: {}, 数据库密码哈希: {}", 
                    loginDTO.getPassword(), user.getPassword());
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成Token
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(user.getUserId());
        loginVO.setUsername(user.getUsername());
        loginVO.setEmail(user.getEmail());

        log.info("登录成功 - 用户: {}, Token: {}", user.getUsername(), token);
        return loginVO;
    }
}
