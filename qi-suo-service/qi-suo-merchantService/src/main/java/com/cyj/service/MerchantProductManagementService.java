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

    /**
     * 设置商品状态
     *
     * @param productId 商品 id
     * @param status    1=上架，0=下架；为 null 时保留原来的 toggle 行为
     */
    Result<String> setStatus(Integer productId, Integer status);

    Result<String> deleteProduct(Integer productId);

    /**
     * 整单扣减库存（服务间调用：用户服务下单时）。
     */
    Result<String> deductStock(ProductStockDTO productStockDTO);

    /**
     * 整单归还库存（服务间调用：订单超时取消时）。
     */
    Result<String> restoreStock(ProductStockDTO productStockDTO);
}
