package com.cyj.feign.file;


import com.cyj.back.FileFeignBack;
import com.cyj.common.Result;
import com.cyj.config.FeignMultipartConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "qi-suo-file",
        contextId = "fileFeign",
        fallback = FileFeignBack.class,
        configuration = FeignMultipartConfig.class)
@Primary
public interface FileFeign {


    @PostMapping(value = "/api/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> upload(@RequestPart("file") MultipartFile file);

}
