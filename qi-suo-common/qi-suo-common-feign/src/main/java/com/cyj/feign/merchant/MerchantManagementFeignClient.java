package com.cyj.feign.merchant;


import com.cyj.back.MerchantManagementFeignClientFallBack;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理服务 / 用户服务 -> 商家服务：商家信息由商家服务提供
 * （管理服务用 list/setStatus，用户服务用 getMerchantNameById）
 */
@FeignClient(value = "qi-suo-merchant",
        contextId = "merchantManagementFeignClient",
        fallback = MerchantManagementFeignClientFallBack.class)
@Primary
public interface MerchantManagementFeignClient {

    /**
     * 商家列表查询
     */
    @PostMapping("/api/merchant/merchantManagement/list")
    Result<PageResult<AdminMerchantManagementLDVO>> list(@RequestBody AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO);

    /**
     * 商家状态切换
     */
    @PutMapping("/api/merchant/merchantManagement/status")
    Result<String> setStatus(@RequestParam("id") Long id, @RequestParam("pageNum") Integer pageNum);

    /**
     * 按商家id取商家名
     */
    @GetMapping("/api/merchant/merchantManagement/getMerchantNameById")
    Result<String> getMerchantNameById(@RequestParam("id") Long id);
}
