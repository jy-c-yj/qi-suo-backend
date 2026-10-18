package com.cyj.service.impl;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.feign.merchant.MerchantProductQueryFeignClient;
import com.cyj.service.AdminProductQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminProductQueryServiceImpl implements AdminProductQueryService {

    @Autowired
    private MerchantProductQueryFeignClient merchantProductQueryFeignClient;

    @Override
    public Result<PageResult<BackgroundGetProductDVO>> getProduct(BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO) {
        return merchantProductQueryFeignClient.queryProductList(backgroundGetProductByConditionDTO);
    }


    /**
     * 查询商品详情
     * @param id
     * @return
     */
    @Override
    public Result<BackgroundGetProductDetailDVO> getProductDetail(Integer id) {
        return merchantProductQueryFeignClient.getProductDetail(id);
    }

    @Override
    public Result<String> setStatus(Integer id, Integer status) {
        if (id == null || status == null || (status != 0 && status != 1)) {
            return Result.error("参数不合法");
        }
        return merchantProductQueryFeignClient.setStatus(id, status);
    }
}
