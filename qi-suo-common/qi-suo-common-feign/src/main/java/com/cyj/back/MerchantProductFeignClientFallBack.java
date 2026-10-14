package com.cyj.back;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;
import com.cyj.feign.merchant.MerchantProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MerchantProductFeignClientFallBack implements MerchantProductFeignClient {

    @Override
    public Result<PageResult<HotProductDVO>> queryHotProduct(Integer pageNum, Integer pageSize) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> queryHotProduct 出错");
        return Result.error("查询热门商品失败");
    }

    @Override
    public Result<BackgroundGetProductDetailDVO> queryProductDetail(Integer id) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> queryProductDetail 出错");
        return Result.error("查询商品详情失败");
    }

    @Override
    public Result<PageResult<QueryProductDVO>> queryProductByCondition(QueryProductByConditionDTO queryProductByConditionDTO) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> queryProductByCondition 出错");
        return Result.error("查询商品失败");
    }
}
