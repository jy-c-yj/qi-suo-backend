package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;
import com.cyj.entity.Merchant;
import com.cyj.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {
    List<AdminMerchantManagementLDVO> list(Page<Merchant> page, AdminMerchantListSelectConditionDTO condition);
}
