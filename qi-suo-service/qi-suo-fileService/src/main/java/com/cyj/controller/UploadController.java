package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.config.MinioConfig;
import com.cyj.exception.FileUploadException;
import com.cyj.utils.MinIOUtils;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/file/")
public class UploadController {

    @Autowired
    private MinioConfig  minioUtil;



    // 上传接口在白名单里（匿名可用，如登录前换头像），所以只标注日志不做鉴权
    @Log(module = "文件", desc = "上传文件")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam MultipartFile file) throws Exception {

        if(file.isEmpty()) {
            throw new FileUploadException("图片上传出错");
        }
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                .toLowerCase();

        String fileName = UUID.randomUUID().toString() + "." + extension;

        switch (extension) {
            case "jpg":
            case "jpeg":
                extension = "image/jpeg";
            case "png":
                extension = "image/png";
        }
        ObjectWriteResponse objectWriteResponse = MinIOUtils.uploadFile("qisuo", file, fileName, extension);
        String fileUrl = MinIOUtils.getBasisUrl()+fileName;
        return Result.success(fileUrl);

    }
}
