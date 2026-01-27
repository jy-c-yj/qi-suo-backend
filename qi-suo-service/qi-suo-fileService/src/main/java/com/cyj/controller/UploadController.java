package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.config.minioConfig;
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
    private minioConfig  minioUtil;



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
