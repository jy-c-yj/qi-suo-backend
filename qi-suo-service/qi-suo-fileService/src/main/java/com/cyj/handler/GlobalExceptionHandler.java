package com.cyj.handler;


import com.cyj.common.Result;
import com.cyj.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = FileUploadException.class)
    @ResponseBody
    public Result<String> handleBadCredentialsException(FileUploadException e) {

        log.error("图片上传出错 >>>> {}", e.getMessage());

        return Result.error("图片上传出错");
    }
}
