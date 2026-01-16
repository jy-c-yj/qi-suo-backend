package com.cyj.controller.merchant;

import com.cyj.common.Result;
import com.cyj.dto.MerchantLoginDTO;
import com.cyj.dto.UserLoginDTO;
import com.cyj.service.merchantService.MerchantService;
import com.cyj.service.userService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/merchant")
public class merchantController {
    @Autowired
    MerchantService merchantService;
    @Autowired
    HttpServletRequest request;
    @PostMapping("/login")
    public Result<String> login(@RequestBody MerchantLoginDTO merchantLoginDTO) {

        return merchantService.merchantLogin(merchantLoginDTO);

    }
}