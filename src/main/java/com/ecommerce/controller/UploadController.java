package com.ecommerce.controller;

import com.ecommerce.common.Result;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.util.UserContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 商品图片上传（管理员）。
 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @PostMapping("/image")
    public Result<Map<String, String>> upload(MultipartFile file) throws IOException {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择文件");
        }
        File dir = new File("./uploads");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BusinessException("创建上传目录失败");
        }
        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        file.transferTo(new File(dir, filename));
        Map<String, String> data = new HashMap<>();
        data.put("url", "/uploads/" + filename);
        return Result.ok(data);
    }
}
