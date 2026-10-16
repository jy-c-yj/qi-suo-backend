package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.dto.MerchantRegisterDTO;
import com.cyj.service.MerchantRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/merchant/")
public class MerchantRegisterController {

    @Autowired
    private MerchantRegisterService merchantRegisterService;

    @Log(module = "认证", desc = "商家注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody MerchantRegisterDTO merchantRegisterDTO) {
        return merchantRegisterService.register(merchantRegisterDTO);
    }
}
