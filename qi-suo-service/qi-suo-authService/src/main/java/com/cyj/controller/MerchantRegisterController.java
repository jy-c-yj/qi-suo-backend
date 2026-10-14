package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dto.MerchantRegisterDTO;
import com.cyj.service.MerchantRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/merchant/")
public class MerchantRegisterController {

    @Autowired
    private MerchantRegisterService merchantRegisterService;

    @PostMapping("/register")
    public Result<String> register(@RequestBody MerchantRegisterDTO merchantRegisterDTO) {
        return merchantRegisterService.register(merchantRegisterDTO);
    }
}
