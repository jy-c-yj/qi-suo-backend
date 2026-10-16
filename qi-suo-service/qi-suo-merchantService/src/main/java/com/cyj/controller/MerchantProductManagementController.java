package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.*;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.service.MerchantProductManagementService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant/product/")
public class MerchantProductManagementController {

    @Autowired
    private MerchantProductManagementService productManagementService;

    /**
     * 获取商品列表
     * @param backgroundGetProductByConditionDTO
     * @return
     */
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/getProduct")
    public Result<PageResult<BackgroundGetProductDVO>> getProduct(@RequestBody BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO) {
        return productManagementService.getProduct(backgroundGetProductByConditionDTO);
    }

    /**
     * 获取商品详情（商家端用，同时也被管理服务经服务间调用）
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('Merchant','SystemADMIN','SuperAdmin','system')")
    @GetMapping("/getProductDetail/{id}")
    public Result<BackgroundGetProductDetailDVO> getProductDetail(@PathVariable(value = "id") Integer id) {
        return productManagementService.getProductDetail(id);
    }

    /**
     * 修改商品信息
     * @param merchantUpdateProductFormDTO
     * @return
     */
    @Log(module = "商品", desc = "修改商品")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/updateProduct")
    public Result<String> updateProduct(@RequestBody MerchantUpdateProductFormDTO merchantUpdateProductFormDTO) {
        return productManagementService.updateProduct(merchantUpdateProductFormDTO);
    }

    /**
     * 新增商品
     */
    @Log(module = "商品", desc = "新增商品")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/newProduct")
    public Result<String> newProduct(@RequestBody NewProductDTO newProductDTO) {
        return productManagementService.newProduct(newProductDTO);
    }

    /**
     * 分类新增
     */
    @Log(module = "商品", desc = "新增商品分类")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/newProductCategory")
    public Result<String> newProductCategory(@RequestBody NewProductCategoryDTO newProductCategoryDTO) {
        return productManagementService.newProductCategory(newProductCategoryDTO);
    }
    /**
     * 分类删除
     */
    @Log(module = "商品", desc = "删除商品分类")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/deleteProductCategory")
    public Result<String> deleteProductCategory(@RequestBody DeleteProductCategoryDTO deleteProductCategoryDTO) {
        return productManagementService.deleteProductCategory(deleteProductCategoryDTO);
    }
    /**
     * 属性新增
     */
    @Log(module = "商品", desc = "新增商品属性值")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/newProductAttributeValue")
    public Result<String> newProductAttributeValue(@RequestBody NewProductAttributeValueDTO newProductAttributeValueDTO) {
        return productManagementService.newProductAttributeValue(newProductAttributeValueDTO);
    }

    /**
     * 属性删除
     */
    @Log(module = "商品", desc = "删除商品属性值")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/deleteProductAttributeValue")
    public Result<String> deleteProductAttributeValue(@RequestBody DeleteProductAttributeValueDTO deleteProductAttributeValueDTO) {
        return productManagementService.deleteProductAttributeValue(deleteProductAttributeValueDTO);
    }
    /**
     * 商品上架|下架
     */
    @Log(module = "商品", desc = "上下架商品")
    @PreAuthorize("hasAuthority('Merchant')")
    @PutMapping("/setStatus")
    public Result<String> setStatus(Integer productId) {
        return productManagementService.setStatus(productId);
    }
    /**
     * 商品删除
     */
    @Log(module = "商品", desc = "删除商品")
    @PreAuthorize("hasAuthority('Merchant')")
    @DeleteMapping("/deleteProduct")
    public Result<String> deleteProduct(Integer productId) {
        return productManagementService.deleteProduct(productId);
    }

    /**
     * 整单扣减库存（服务间调用：用户服务下单时使用）
     */
    @PreAuthorize("hasAuthority('system')")
    @PostMapping("/deductStock")
    public Result<String> deductStock(@RequestBody ProductStockDTO productStockDTO) {
        return productManagementService.deductStock(productStockDTO);
    }

    /**
     * 整单归还库存（服务间调用：订单超时取消时使用）
     */
    @PreAuthorize("hasAuthority('system')")
    @PostMapping("/restoreStock")
    public Result<String> restoreStock(@RequestBody ProductStockDTO productStockDTO) {
        return productManagementService.restoreStock(productStockDTO);
    }

}
