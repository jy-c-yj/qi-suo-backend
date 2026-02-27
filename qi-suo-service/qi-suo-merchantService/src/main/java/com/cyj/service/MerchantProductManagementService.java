package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.*;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;

public interface MerchantProductManagementService {

    /**
     * 查询商品列表
     *
     * @param backgroundGetProductByConditionDTO
     * @return
     */
    Result<PageResult<BackgroundGetProductDVO>> getProduct(BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO);

    /**
     * 查询商品详情
     * @param id
     * @return
     */
    Result<BackgroundGetProductDetailDVO> getProductDetail(Integer id);

    /**
     * 修改商品信息
     * @param merchantUpdateProductFormDTO
     * @return
     */
    Result<String> updateProduct(MerchantUpdateProductFormDTO merchantUpdateProductFormDTO);

    /**
     * 新增商品
     * @param newProductDTO
     * @return
     */
    Result<String> newProduct(NewProductDTO newProductDTO);

    Result<String> newProductCategory(NewProductCategoryDTO newProductCategoryDTO);

    Result<String> deleteProductCategory(DeleteProductCategoryDTO deleteProductCategoryDTO);

    Result<String> newProductAttributeValue(NewProductAttributeValueDTO newProductAttributeValueDTO);

    Result<String> deleteProductAttributeValue(DeleteProductAttributeValueDTO deleteProductAttributeValueDTO);

    Result<String> setStatus(Integer productId);

    Result<String> deleteProduct(Integer productId);
}
