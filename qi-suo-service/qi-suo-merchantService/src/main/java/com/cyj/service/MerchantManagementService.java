package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;

/**
 * 商家信息管理：商家表由商家服务提供
 * （管理服务的商家管理页面、用户服务取商家名都经服务间调用走这里）
 */
public interface MerchantManagementService {

    /**
     * 分页查询商家列表
     */
    Result<PageResult<AdminMerchantManagementLDVO>> list(AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO);

    /**
     * 商家状态切换
     */
    Result<String> setStatus(Long id, Integer pageNum);

    /**
     * 按商家id取商家名
     */
    Result<String> getMerchantNameById(Long id);
}
