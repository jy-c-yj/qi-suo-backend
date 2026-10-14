package com.cyj.handler;


import com.cyj.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseBody
    public Result<String> handleBadCredentialsException(BadCredentialsException e) {

        log.error("账户或密码错误，请重新输入 >>>> {}", e.getMessage());

        return Result.error("账户或密码错误，请重新输入");
    }
}
