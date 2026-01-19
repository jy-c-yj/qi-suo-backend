package com.cyj.controller.user;


import com.cyj.common.Result;

import com.cyj.dto.UserLoginDTO;
import com.cyj.dvo.AccessTokenDVO;
import com.cyj.service.userService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/auth/user")
public class userController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Result<AccessTokenDVO> login(@RequestBody UserLoginDTO userLoginDTO) {

        return userService.userLogin(userLoginDTO);

    }
}
