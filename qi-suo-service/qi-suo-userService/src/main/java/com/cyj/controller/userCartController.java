package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dto.CreateOrderDTO;
import com.cyj.dvo.CartDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.service.UserCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cart")
public class userCartController {

    @Autowired
    private UserCartService userCartService;

    //添加购物车
    @PostMapping("/add")
    public Result<String> addCartItem(@RequestParam Long productId, @RequestParam Integer quantity){
        return userCartService.addCartItem(productId,quantity);
    }
    //删除购物车
    @DeleteMapping("/delete/{productId}")
    public Result<String> deleteCartItem(@PathVariable Long productId){
        return userCartService.deleteCartItem(productId);
    }

    //查看购物车
    @GetMapping("/list")
    public Result<List<CartDVO>> getCartList(@RequestParam Long merchantId){
        return userCartService.getCartList(merchantId);
    }

    //购物车结算生成订单
    @PostMapping("/createOrder")
    public Result<UserOrdersDetailDVO> createOrder(@RequestBody CreateOrderDTO createOrderDTO){
        return userCartService.createOrder(createOrderDTO);
    }

}
