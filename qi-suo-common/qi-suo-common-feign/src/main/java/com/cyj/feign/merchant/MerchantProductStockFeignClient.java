package com.cyj.feign.merchant;


import com.cyj.back.MerchantProductStockFeignClientFallBack;
import com.cyj.common.Result;
import com.cyj.dto.ProductStockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户服务 -> 商家服务：库存扣减与归还。
 */
@FeignClient(value = "qi-suo-merchant",
        contextId = "merchantProductStockFeignClient",
        fallback = MerchantProductStockFeignClientFallBack.class)
@Primary
public interface MerchantProductStockFeignClient {

    /**
     * 整单扣减库存（下单时）。
     */
    @PostMapping("/api/merchant/product/deductStock")
    Result<String> deductStock(@RequestBody ProductStockDTO productStockDTO);

    /**
     * 整单归还库存（订单超时取消时）。
     */
    @PostMapping("/api/merchant/product/restoreStock")
    Result<String> restoreStock(@RequestBody ProductStockDTO productStockDTO);
}
