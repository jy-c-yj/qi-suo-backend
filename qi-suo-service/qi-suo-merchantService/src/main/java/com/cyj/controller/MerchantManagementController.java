package com.cyj.controller;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;
import com.cyj.service.MerchantManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商家信息管理：商家表由商家服务提供，管理服务的商家管理页面与用户服务取商家名都经服务间调用走这里
 */
@RestController
@RequestMapping("/api/merchant/merchantManagement")
public class MerchantManagementController {
    @Autowired
    private MerchantManagementService merchantManagementService;

    /**
     * 条件查询商家列表
     */
    @PostMapping("/list")
    public Result<PageResult<AdminMerchantManagementLDVO>> list(@RequestBody AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO) {
        return merchantManagementService.list(adminMerchantListSelectConditionDTO);
    }

    /**
     * 设置商家状态
     */
    @PutMapping("/status")
    public Result<String> setStatus(@RequestParam Long id,@RequestParam Integer pageNum) {
        return merchantManagementService.setStatus(id,pageNum);
    }

    /**
     * 按商家id取商家名
     */
    @GetMapping("/getMerchantNameById")
    public Result<String> getMerchantNameById(@RequestParam Long id) {
        return merchantManagementService.getMerchantNameById(id);
    }
}
