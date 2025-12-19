package com.cloudnote.config;

import com.cloudnote.interceptor.JwtInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;

/**
 * Web配置
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Resource
    private JwtInterceptor jwtInterceptor;

    @PostConstruct
    public void init() {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.isAbsolute()) {
            uploadDir = new File(System.getProperty("user.dir"), uploadPath);
        }
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        log.info("文件上传目录: {}", uploadDir.getAbsolutePath());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置JWT拦截器
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(    // 排除不需要认证的路径
                        "/user/login",
                        "/user/register",
                        "/uploads/**",
                        "/error"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取绝对路径
        File uploadDir = new File(uploadPath);
        if (!uploadDir.isAbsolute()) {
            uploadDir = new File(System.getProperty("user.dir"), uploadPath);
        }
        
        // 构建资源路径（Windows需要file:///格式）
        String resourceLocation = "file:///" + uploadDir.getAbsolutePath().replace("\\", "/") + "/";
        
        log.info("静态资源映射: /uploads/** -> {}", resourceLocation);
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
    }
}
