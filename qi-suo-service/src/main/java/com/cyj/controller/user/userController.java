package com.cyj.controller.user;


import com.cyj.common.Result;

import com.cyj.dto.UserLoginDTO;
import com.cyj.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/user")
public class userController {
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest request;
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO userLoginDTO) {

        return userService.userLogin(userLoginDTO);

    }
}
