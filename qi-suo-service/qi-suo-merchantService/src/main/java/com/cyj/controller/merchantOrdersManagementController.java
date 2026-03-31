package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.service.MerchantOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/merchant/orders/")
public class merchantOrdersManagementController {
    @Autowired
    private MerchantOrdersService merchantOrdersService;
    //分页查询所有订单列表
    @GetMapping("/list")
    public PageResult<MerchantOrdersDVO> getOrders(Integer pageNum, Integer pageSize) {
        return merchantOrdersService.getOrders(pageNum,pageSize);
    }
    //查询订单详情
    @GetMapping("/detail")
    public Result<UserOrdersDetailDVO> getOrdersDetail(Long orderId) {
        return merchantOrdersService.getOrdersDetail(orderId);
    }

    //设置订单状态
    @GetMapping("/status")
    public Result<String> setOrdersStatus(Long orderId) {
        return merchantOrdersService.setOrdersStatus(orderId);
    }
}
