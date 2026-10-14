package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dto.UserRegisterDTO;
import com.cyj.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/user/")
public class UserRegisterController {

    @Autowired
    private UserRegisterService userRegisterService;

    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userRegisterService.register(userRegisterDTO);
    }
}
