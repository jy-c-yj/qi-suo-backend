package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.*;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.service.MerchantProductManagementService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 修改商品信息
     * @param merchantUpdateProductFormDTO
     * @return
     */
    @PostMapping("/updateProduct")
    public Result<String> updateProduct(@RequestBody MerchantUpdateProductFormDTO merchantUpdateProductFormDTO) {
        return productManagementService.updateProduct(merchantUpdateProductFormDTO);
    }

    /**
     * 新增商品
     */
    @PostMapping("/newProduct")
    public Result<String> newProduct(@RequestBody NewProductDTO newProductDTO) {
        return productManagementService.newProduct(newProductDTO);
    }

    /**
     * 分类新增
     */
    @PostMapping("/newProductCategory")
    public Result<String> newProductCategory(@RequestBody NewProductCategoryDTO newProductCategoryDTO) {
        return productManagementService.newProductCategory(newProductCategoryDTO);
    }
    /**
     * 分类删除
     */
    @PostMapping("/deleteProductCategory")
    public Result<String> deleteProductCategory(@RequestBody DeleteProductCategoryDTO deleteProductCategoryDTO) {
        return productManagementService.deleteProductCategory(deleteProductCategoryDTO);
    }
    /**
     * 属性新增
     */
    @PostMapping("/newProductAttributeValue")
    public Result<String> newProductAttributeValue(@RequestBody NewProductAttributeValueDTO newProductAttributeValueDTO) {
        return productManagementService.newProductAttributeValue(newProductAttributeValueDTO);
    }

    /**
     * 属性删除
     */
    @PostMapping("/deleteProductAttributeValue")
    public Result<String> deleteProductAttributeValue(@RequestBody DeleteProductAttributeValueDTO deleteProductAttributeValueDTO) {
        return productManagementService.deleteProductAttributeValue(deleteProductAttributeValueDTO);
    }
    /**
     * 商品上架|下架
     */
    @PutMapping("/setStatus")
    public Result<String> setStatus(Integer productId) {
        return productManagementService.setStatus(productId);
    }
    /**
     * 商品删除
     */
    @DeleteMapping("/deleteProduct")
    public Result<String> deleteProduct(Integer productId) {
        return productManagementService.deleteProduct(productId);
    }

}
