package com.cyj.feign.merchant;


import com.cyj.back.MerchantProductFeignClientFallBack;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务 -> 商家服务：商品浏览相关查询
 */
@FeignClient(value = "qi-suo-merchant",
        contextId = "merchantProductFeignClient",
        fallback = MerchantProductFeignClientFallBack.class)
@Primary
public interface MerchantProductFeignClient {

    /**
     * 主页热门商品查询
     */
    @GetMapping("/api/merchant/product/queryHotProduct")
    Result<PageResult<HotProductDVO>> queryHotProduct(@RequestParam("pageNum") Integer pageNum,
                                                      @RequestParam("pageSize") Integer pageSize);

    /**
     * 商品详情查询
     */
    @GetMapping("/api/merchant/product/queryProductDetail")
    Result<BackgroundGetProductDetailDVO> queryProductDetail(@RequestParam("id") Integer id);

    /**
     * 条件查询商品
     */
    @PostMapping("/api/merchant/product/queryProductByCondition")
    Result<PageResult<QueryProductDVO>> queryProductByCondition(@RequestBody QueryProductByConditionDTO queryProductByConditionDTO);
}
