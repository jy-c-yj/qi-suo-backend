package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;

/**
 * 后台商品查询：商品数据由商家服务提供，本服务经服务间调用获取
 */
public interface AdminProductQueryService {

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
     * 启用/禁用商品（转发到商家服务显式设置状态）
     *
     * @param id     商品 id
     * @param status 1=上架，0=下架
     */
    Result<String> setStatus(Integer id, Integer status);
}
