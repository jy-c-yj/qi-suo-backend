package com.cyj.service.impl;



import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;
import com.cyj.feign.merchant.MerchantManagementFeignClient;
import com.cyj.service.AdminMerchantManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员商户管理服务实现类：商家数据归商家服务所有，本服务只做转发
 */
@Service
@Slf4j
public class AdminMerchantManagementServiceImpl implements AdminMerchantManagementService {
    @Autowired
    private MerchantManagementFeignClient merchantManagementFeignClient;

    /**
     * 查询商户列表的方法
     * @param adminMerchantListSelectConditionDTO 商户列表查询条件数据传输对象
     * @return Result<PageResult<AdminMerchantManagementLDVO>> 返回结果对象
     */
    @Override
    public Result<PageResult<AdminMerchantManagementLDVO>> list(AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO) {
        return merchantManagementFeignClient.list(adminMerchantListSelectConditionDTO);
    }

    /**
     * 设置状态的方法
     * @param id 用于标识需要设置状态的唯一标识符
     * @param pageNum 分页页码
     * @return 返回一个Result<String>类型的结果对象
     */
    @Override
    public Result<String> setStatus(Long id,Integer pageNum) {
        return merchantManagementFeignClient.setStatus(id, pageNum);
    }
}
