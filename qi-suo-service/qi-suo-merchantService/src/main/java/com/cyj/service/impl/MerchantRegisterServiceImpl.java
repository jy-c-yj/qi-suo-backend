package com.cyj.service.impl;

import com.cyj.common.Result;
import com.cyj.dto.MerchantRegisterDTO;
import com.cyj.entity.Merchant;
import com.cyj.mapper.MerchantMapper;
import com.cyj.service.MerchantRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MerchantRegisterServiceImpl implements MerchantRegisterService {
    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public Result<String> register(MerchantRegisterDTO merchantRegisterDTO) {
        if(merchantRegisterDTO==null){
            return Result.error("数据有误");
        }

        //插入数据库
        Merchant merchant = Merchant.builder()
                .merchantName(merchantRegisterDTO.getMerchantName())
                .merchantPassword(merchantRegisterDTO.getMerchantPassword())
                .shopName(merchantRegisterDTO.getShopName())
                .shopLogoUrl(merchantRegisterDTO.getShopLogoUrl())
                .description(merchantRegisterDTO.getDescription())
                .contactEmail(merchantRegisterDTO.getContactEmail())
                .contactName(merchantRegisterDTO.getContactName())
                .contactPhone(merchantRegisterDTO.getContactPhone())
                .status(1)
                .isDeleted(0)
                .build();

        try {
            merchantMapper.insert(merchant);
        }catch (Exception e){
            log.error("注册失败，数据插入异常 --> {}", e.getMessage());
            return Result.error("注册失败，数据插入异常");
        }

        return Result.success("注册成功");
    }
}
