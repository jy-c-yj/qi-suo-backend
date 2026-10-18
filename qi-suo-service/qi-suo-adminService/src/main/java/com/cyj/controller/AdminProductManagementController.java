package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.service.AdminProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/productManagement")
public class AdminProductManagementController {

    @Autowired
    private AdminProductQueryService productManagementService;

    /**
     * 获取商品列表
     * @param backgroundGetProductByConditionDTO
     * @return
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','system')")
    @PostMapping("/getProduct")
    public Result<PageResult<BackgroundGetProductDVO>> getProduct(@RequestBody BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO) {
        return productManagementService.getProduct(backgroundGetProductByConditionDTO);
    }

    /**
     * 获取商品详情
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','system')")
    @GetMapping("/getProductDetail/{id}")
    public Result<BackgroundGetProductDetailDVO> getProductDetail(@PathVariable(value = "id") Integer id) {
        return productManagementService.getProductDetail(id);
    }

    /**
     * 启用/禁用商品：显式传方向，避免再依赖 toggle
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','system')")
    @PutMapping("/status")
    public Result<String> setStatus(@RequestParam("id") Integer id,
                                    @RequestParam("status") Integer status) {
        return productManagementService.setStatus(id, status);
    }

}
