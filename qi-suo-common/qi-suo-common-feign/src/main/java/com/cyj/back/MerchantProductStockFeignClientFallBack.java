package com.cyj.back;


import com.cyj.common.Result;
import com.cyj.dto.ProductStockDTO;
import com.cyj.feign.merchant.MerchantProductStockFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 降级返回的是 Result.error（code=0）而不是抛异常，调用方必须判 getCode()。
 */
@Component
@Slf4j
public class MerchantProductStockFeignClientFallBack implements MerchantProductStockFeignClient {

    @Override
    public Result<String> deductStock(ProductStockDTO productStockDTO) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> deductStock 出错, orderId={}",
                productStockDTO == null ? null : productStockDTO.getOrderId());
        return Result.error("扣减库存失败");
    }

    @Override
    public Result<String> restoreStock(ProductStockDTO productStockDTO) {
        log.error("远程调用 -> qi-suo-merchant  方法 -> restoreStock 出错, orderId={}",
                productStockDTO == null ? null : productStockDTO.getOrderId());
        return Result.error("归还库存失败");
    }
}
