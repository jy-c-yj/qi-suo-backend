package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.dto.UpdateMerchantInformationFormDTO;
import com.cyj.entity.Merchant;
import com.cyj.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {
    /**
     * 商家信息修改
     */
    void updateMerchantInformation(UpdateMerchantInformationFormDTO updateMerchantInformationFormDTO);
}
