package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;
import com.cyj.service.MerchantProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品读接口的提供方：用户端浏览与管理端查询都通过服务间调用走这里
 */
@RestController
@RequestMapping("/api/merchant/product/")
public class MerchantProductQueryController {

    @Autowired
    private MerchantProductQueryService merchantProductQueryService;

    /**
     * 主页热门商品查询
     */
    @GetMapping("/queryHotProduct")
    public Result<PageResult<HotProductDVO>> queryHotProduct(Integer pageNum, Integer pageSize){
        return merchantProductQueryService.queryHotProduct(pageNum,pageSize);
    }

    /**
     * 商品详情查询
     */
    @GetMapping("/queryProductDetail")
    public Result<BackgroundGetProductDetailDVO> queryProductDetail(Integer id){
        return merchantProductQueryService.queryProductDetail(id);
    }

    /**
     * 条件查询商品
     */
    @PostMapping("/queryProductByCondition")
    public Result<PageResult<QueryProductDVO>> queryProductByCondition(@RequestBody QueryProductByConditionDTO queryProductByConditionDTO){
        return merchantProductQueryService.queryProductByCondition(queryProductByConditionDTO);
    }

    /**
     * 后台商品列表查询（不按商家归属过滤，供管理服务使用）
     */
    @PostMapping("/queryProductList")
    public Result<PageResult<BackgroundGetProductDVO>> queryProductList(@RequestBody BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO){
        return merchantProductQueryService.queryProductList(backgroundGetProductByConditionDTO);
    }
}
