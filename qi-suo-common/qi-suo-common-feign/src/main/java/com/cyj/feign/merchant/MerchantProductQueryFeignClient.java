package com.cyj.feign.merchant;


import com.cyj.back.MerchantProductQueryFeignClientFallBack;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理服务 -> 商家服务：后台商品查询
 */
@FeignClient(value = "qi-suo-merchant",
        contextId = "merchantProductQueryFeignClient",
        fallback = MerchantProductQueryFeignClientFallBack.class)
@Primary
public interface MerchantProductQueryFeignClient {

    /**
     * 后台商品列表查询（不按商家归属过滤）
     */
    @PostMapping("/api/merchant/product/queryProductList")
    Result<PageResult<BackgroundGetProductDVO>> queryProductList(@RequestBody BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO);

    /**
     * 商品详情查询
     */
    @GetMapping("/api/merchant/product/getProductDetail/{id}")
    Result<BackgroundGetProductDetailDVO> getProductDetail(@PathVariable("id") Integer id);

    /**
     * 显式设置商品上下架状态：1=上架，0=下架
     */
    @PutMapping("/api/merchant/product/setStatus")
    Result<String> setStatus(@RequestParam("productId") Integer productId,
                             @RequestParam("status") Integer status);
}
