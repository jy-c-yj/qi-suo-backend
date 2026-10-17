package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.dto.CreateOrderDTO;
import com.cyj.dvo.CartDVO;
import com.cyj.dvo.CartMerchantGroupDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.service.UserCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cart")
public class UserCartController {

    @Autowired
    private UserCartService userCartService;

    //添加购物车
    @Log(module = "购物车", desc = "加入购物车")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/add")
    public Result<String> addCartItem(@RequestParam Long productId, @RequestParam Integer quantity){
        return userCartService.addCartItem(productId,quantity);
    }
    //删除购物车
    @Log(module = "购物车", desc = "删除购物车商品")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete/{productId}")
    public Result<String> deleteCartItem(@PathVariable Long productId){
        return userCartService.deleteCartItem(productId);
    }

    //查看购物车
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/list")
    public Result<List<CartDVO>> getCartList(@RequestParam Long merchantId){
        return userCartService.getCartList(merchantId);
    }

    //查看当前用户全部购物车（按商家分组）
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/listAll")
    public Result<List<CartMerchantGroupDVO>> getAllCartList(){
        return userCartService.getAllCartList();
    }

    //修改购物车中某个商品的数量
    @Log(module = "购物车", desc = "修改购物车商品数量")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/quantity")
    public Result<CartDVO> updateCartItemQuantity(@RequestParam Long productId,
                                                  @RequestParam Integer quantity){
        return userCartService.updateCartItemQuantity(productId, quantity);
    }

    //整项移除购物车中的某个商品
    @Log(module = "购物车", desc = "移除购物车商品")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/remove/{productId}")
    public Result<String> removeCartItem(@PathVariable Long productId){
        return userCartService.removeCartItem(productId);
    }

    //购物车结算生成订单
    @Log(module = "订单", desc = "创建订单")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/createOrder")
    public Result<UserOrdersDetailDVO> createOrder(@RequestBody CreateOrderDTO createOrderDTO){
        return userCartService.createOrder(createOrderDTO);
    }

}
