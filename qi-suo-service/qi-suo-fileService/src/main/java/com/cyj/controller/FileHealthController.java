package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.config.MinioConfig;
import com.cyj.exception.FileUploadException;
import com.cyj.utils.MinIOUtils;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/file/")
public class FileHealthController {

    @Autowired
    private MinioConfig  minioUtil;



    @PostMapping("/health")
    public Result<String> health(){
        return Result.success("ok");


    }
}
