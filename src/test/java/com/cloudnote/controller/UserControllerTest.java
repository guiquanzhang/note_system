package com.cloudnote.controller;

import com.cloudnote.dto.RegisterDTO;
import com.cloudnote.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 用户控制器测试类
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试用例1：正常注册 - 成功
     */
    @Test
    public void testRegister_Success() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser001");
        registerDTO.setPassword("123456");
        registerDTO.setEmail("test001@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

    /**
     * 测试用例2：用户名为空 - 失败
     */
    @Test
    public void testRegister_UsernameEmpty() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("");
        registerDTO.setPassword("123456");
        registerDTO.setEmail("test@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    /**
     * 测试用例3：用户名过短 - 失败
     */
    @Test
    public void testRegister_UsernameTooShort() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("ab");
        registerDTO.setPassword("123456");
        registerDTO.setEmail("test@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    /**
     * 测试用例4：用户名过长 - 失败
     */
    @Test
    public void testRegister_UsernameTooLong() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        // 生成21个字符的用户名
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 21; i++) {
            sb.append("a");
        }
        registerDTO.setUsername(sb.toString());
        registerDTO.setPassword("123456");
        registerDTO.setEmail("test@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    /**
     * 测试用例5：密码为空 - 失败
     */
    @Test
    public void testRegister_PasswordEmpty() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("");
        registerDTO.setEmail("test@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    /**
     * 测试用例6：密码过短 - 失败
     */
    @Test
    public void testRegister_PasswordTooShort() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("12345");
        registerDTO.setEmail("test@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    /**
     * 测试用例7：邮箱格式错误 - 失败
     */
    @Test
    public void testRegister_InvalidEmail() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("123456");
        registerDTO.setEmail("invalid-email");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    /**
     * 测试用例8：邮箱为空（可选字段） - 成功
     */
    @Test
    public void testRegister_EmailNull() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser002");
        registerDTO.setPassword("123456");
        registerDTO.setEmail(null);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试用例9：用户名已存在 - 失败
     */
    @Test
    public void testRegister_UsernameDuplicate() throws Exception {
        // 第一次注册
        RegisterDTO registerDTO1 = new RegisterDTO();
        registerDTO1.setUsername("duplicateuser");
        registerDTO1.setPassword("123456");
        registerDTO1.setEmail("test1@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 第二次注册相同用户名
        RegisterDTO registerDTO2 = new RegisterDTO();
        registerDTO2.setUsername("duplicateuser");
        registerDTO2.setPassword("654321");
        registerDTO2.setEmail("test2@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户名已存在"));
    }

    /**
     * 测试用例10：特殊字符用户名 - 成功
     */
    @Test
    public void testRegister_UsernameWithSpecialChars() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("test_user-123");
        registerDTO.setPassword("123456");
        registerDTO.setEmail("test@example.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
