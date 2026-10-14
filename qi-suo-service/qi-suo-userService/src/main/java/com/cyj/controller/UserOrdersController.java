package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.service.UserOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
public class UserOrdersController {

    @Autowired
    private UserOrdersService userOrdersService;

    //查询所有订单列表
    @GetMapping("/list")
    public Result<List<UserOrdersDVO>> list() {
        return userOrdersService.list();
    }

    //查询订单详情
    @GetMapping("/detail")
    public Result<UserOrdersDetailDVO> detail(@RequestParam Long orderId) {
        return userOrdersService.detail(orderId);
    }

    //支付订单
    @GetMapping("/pay")
    public Result<String> pay(@RequestParam Long orderId) {
        return userOrdersService.pay(orderId);
    }

    //确认订单完成
    @GetMapping("/confirm")
    public Result<String> confirm(@RequestParam Long orderId) {
        return userOrdersService.confirm(orderId);
    }

    //商家维度的订单列表（供商家服务经服务间调用使用）
    @GetMapping("/merchantList")
    public PageResult<MerchantOrdersDVO> merchantList(@RequestParam Long merchantId,
                                                     @RequestParam Integer pageNum,
                                                     @RequestParam Integer pageSize) {
        return userOrdersService.merchantList(merchantId, pageNum, pageSize);
    }

    //商家发货（供商家服务经服务间调用使用）
    @GetMapping("/setStatus")
    public Result<String> setStatus(@RequestParam Long orderId) {
        return userOrdersService.setOrdersStatus(orderId);
    }

}
