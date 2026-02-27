package com.cyj.service.Impl;

import com.cyj.common.Result;
import com.cyj.dto.UpdateMerchantInformationFormDTO;
import com.cyj.dvo.MerchantInformationDVO;
import com.cyj.entity.Merchant;
import com.cyj.mapper.MerchantMapper;
import com.cyj.service.MerchantInformationManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MerchantInformationManagementImpl implements MerchantInformationManagement {
    @Autowired
    private MerchantMapper merchantMapper;
    @Override
    public Result<MerchantInformationDVO> getMerchantInformation() {
        //获取商家id
        Long merchantId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("商家id --> {}", merchantId);
        //获取信息
        Merchant merchant = null;
        try {
            merchant = merchantMapper.selectById(merchantId);

        }catch (Exception e){
            log.error("获取商家信息出错 ， merchantId --》 {} ， 错误信息 -- 》\n {}",merchantId,e.getMessage());
            return Result.error("获取商家信息出错");
        }
        if(merchant == null){
            return Result.error("获取商家信息异常，数据为空");
        }
        //返回数据
        MerchantInformationDVO merchantInformationDVO = MerchantInformationDVO.builder()
                .merchantId(merchantId)
                .merchantName(merchant.getMerchantName())
                .merchantPassword(merchant.getMerchantPassword())
                .shopName(merchant.getShopName())
                .shopLogoUrl(merchant.getShopLogoUrl())
                .description(merchant.getDescription())
                .contactEmail(merchant.getContactEmail())
                .contactName(merchant.getContactName())
                .contactPhone(merchant.getContactPhone())
                .status(1)
                .createTime(merchant.getCreateTime())
                .updateTime(merchant.getUpdateTime())
                .updateBy(merchant.getUpdateBy())
                .build();

        return Result.success(merchantInformationDVO);
    }

    @Override
    public Result<String> updateMerchantInformation(UpdateMerchantInformationFormDTO updateMerchantInformationFormDTO) {

        if(updateMerchantInformationFormDTO == null){
            return Result.error("修改失败，数据不完整");
        }

        //修改数据库
        try {
            merchantMapper.updateMerchantInformation(updateMerchantInformationFormDTO);
        }catch (Exception e){
            log.error("修改失败，数据库操作失败 , 错误信息 --》 {}",e.getMessage());
            return Result.error("修改失败，数据库操作失败");
        }


        return Result.success("修改成功");
    }
}
