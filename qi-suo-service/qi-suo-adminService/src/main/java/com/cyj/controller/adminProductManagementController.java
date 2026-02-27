package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/productManagement")
public class adminProductManagementController {

    @Autowired
    private ProductManagementService productManagementService;

    /**
     * 获取商品列表
     * @param backgroundGetProductByConditionDTO
     * @return
     */
    @PostMapping("/getProduct")
    public Result<PageResult<BackgroundGetProductDVO>> getProduct(@RequestBody BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO) {
        return productManagementService.getProduct(backgroundGetProductByConditionDTO);
    }

    /**
     * 获取商品详情
     * @param id
     * @return
     */
    @GetMapping("/getProductDetail/{id}")
    public Result<BackgroundGetProductDetailDVO> getProductDetail(@PathVariable(value = "id") Integer id) {
        return productManagementService.getProductDetail(id);
    }
    
}
