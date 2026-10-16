package com.cyj.controller.merchant;

import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.dto.MerchantLoginDTO;
import com.cyj.dto.UserLoginDTO;
import com.cyj.dvo.AccessTokenDVO;
import com.cyj.service.merchantService.MerchantService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth/merchant")
public class MerchantController {
    @Autowired
    MerchantService merchantService;

    @Log(module = "认证", desc = "商家登录")
    @PostMapping("/login")
    public Result<AccessTokenDVO> login(@RequestBody MerchantLoginDTO merchantLoginDTO) {

        return merchantService.merchantLogin(merchantLoginDTO);

    }
}