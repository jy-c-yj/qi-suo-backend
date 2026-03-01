package com.cyj.controller;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;
import com.cyj.service.AdminMerchantManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商户管理控制器类
 * 该类用于处理商户管理相关的业务逻辑请求
 */
@RestController
@RequestMapping("/api/admin/merchantManagement")
public class merchantManagementController {
    @Autowired
    private AdminMerchantManagementService adminMerchantManagementService;
    /**
     * 条件查询商家列表
     */
    @PostMapping("/list")
    public Result<PageResult<AdminMerchantManagementLDVO>> list(@RequestBody AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO) {
        return adminMerchantManagementService.list(adminMerchantListSelectConditionDTO);
    }

    /**
     * 设置商家状态
     */
    @PutMapping("/status")
    public Result<String> setStatus(@RequestParam Long id,@RequestParam Integer pageNum) {
        return adminMerchantManagementService.setStatus(id,pageNum);
    }
}
