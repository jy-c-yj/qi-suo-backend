package com.cyj.back;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.feign.merchant.MerchantProductQueryFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MerchantProductQueryFeignClientFallBack implements MerchantProductQueryFeignClient {

    @Override
    public Result<PageResult<BackgroundGetProductDVO>> queryProductList(BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> queryProductList 出错");
        return Result.error("查询商品列表失败");
    }

    @Override
    public Result<BackgroundGetProductDetailDVO> getProductDetail(Integer id) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> getProductDetail 出错");
        return Result.error("查询商品详情失败");
    }
}
