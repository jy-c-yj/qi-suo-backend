package com.cyj.service.impl;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;
import com.cyj.feign.merchant.MerchantProductFeignClient;
import com.cyj.service.UserProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserProductServiceImpl implements UserProductService {

    @Autowired
    private MerchantProductFeignClient merchantProductFeignClient;

    /**
     * 查询热门商品方法
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 返回包含热门商品分页结果的Result对象
     */
    @Override
    public Result<PageResult<HotProductDVO>> queryHotProduct(Integer pageNum, Integer pageSize){
        return merchantProductFeignClient.queryHotProduct(pageNum, pageSize);
    }

    @Override
    /**
     * 查询商品详情信息
     * @param id 商品ID
     * @return 返回商品详情信息的结果对象
     */
    public Result<BackgroundGetProductDetailDVO> queryProductDetail(Integer id) {
        return merchantProductFeignClient.queryProductDetail(id);
    }

    @Override
    /**
     * 根据条件查询商品信息
     * @param queryProductByConditionDTO 查询条件参数对象，包含分页信息和查询条件
     * @return 返回查询结果，包含商品分页数据
     */
    public Result<PageResult<QueryProductDVO>> queryProductByCondition(QueryProductByConditionDTO queryProductByConditionDTO) {
        return merchantProductFeignClient.queryProductByCondition(queryProductByConditionDTO);
    }
}
