package com.cyj.back;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;
import com.cyj.feign.merchant.MerchantManagementFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MerchantManagementFeignClientFallBack implements MerchantManagementFeignClient {

    @Override
    public Result<PageResult<AdminMerchantManagementLDVO>> list(AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> list 出错");
        return Result.error("获取商家列表失败");
    }

    @Override
    public Result<String> setStatus(Long id, Integer pageNum) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> setStatus 出错");
        return Result.error("修改商家状态失败");
    }

    @Override
    public Result<String> getMerchantNameById(Long id) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> getMerchantNameById 出错");
        return Result.error("获取商家名失败");
    }
}
