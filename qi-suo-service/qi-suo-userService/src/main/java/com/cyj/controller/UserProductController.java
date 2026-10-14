package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;
import com.cyj.service.UserProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/userProduct")
public class UserProductController {
    @Autowired
    private UserProductService userProductService;

    /**
     * 主页热门商品查询
     */
    @GetMapping("/queryHotProduct")
    public Result<PageResult<HotProductDVO>> queryHotProduct(Integer pageNum, Integer pageSize){

        return userProductService.queryHotProduct(pageNum,pageSize);
    }

    /**
     * 商品详情查询
     */
    @GetMapping("/queryProductDetail")
    public Result<BackgroundGetProductDetailDVO> queryProductDetail(Integer id){
        return userProductService.queryProductDetail(id);
    }


    /**
     * 条件查询商品
     */
    @PostMapping("/queryProductByCondition")
    public Result<PageResult<QueryProductDVO>> queryProductByCondition(@RequestBody  QueryProductByConditionDTO queryProductByConditionDTO){
        return userProductService.queryProductByCondition(queryProductByConditionDTO);
    }

}
