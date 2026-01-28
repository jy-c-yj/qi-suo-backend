package com.cyj.back;


import com.cyj.common.Result;
import com.cyj.feign.file.FileFeign;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileFeignBack implements FileFeign {

    public Result<String> upload(@RequestParam MultipartFile file) {
            return Result.error("文件上传失败");
    }
}
