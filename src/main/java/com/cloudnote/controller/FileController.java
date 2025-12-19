package com.cloudnote.controller;

import com.cloudnote.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    // 文件上传目录（可在 application.yml 中配置）
    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    // 文件访问URL前缀
    @Value("${file.access.url:/uploads}")
    private String accessUrl;

    /**
     * 上传头像
     */
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择文件");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只能上传图片文件");
        }

        // 验证文件大小（5MB）
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return Result.error("文件大小不能超过 5MB");
        }

        try {
            // 创建上传目录
            String avatarDir = uploadPath + File.separator + "avatars";
            File dir = new File(avatarDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = Paths.get(avatarDir, filename);
            Files.write(filePath, file.getBytes());

            // 返回文件访问URL
            String fileUrl = accessUrl + "/avatars/" + filename;
            log.info("文件上传成功: {}", fileUrl);

            return Result.success(fileUrl);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    public Result<Void> deleteFile(@RequestParam("url") String url) {
        try {
            // 从URL中提取文件路径
            String filename = url.substring(url.lastIndexOf("/") + 1);
            String avatarDir = uploadPath + File.separator + "avatars";
            Path filePath = Paths.get(avatarDir, filename);

            // 删除文件
            Files.deleteIfExists(filePath);
            log.info("文件删除成功: {}", url);

            return Result.success();
        } catch (IOException e) {
            log.error("文件删除失败", e);
            return Result.error("文件删除失败: " + e.getMessage());
        }
    }
}
