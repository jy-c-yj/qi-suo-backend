package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;

public interface ProductManagementService {

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
}
