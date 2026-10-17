package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.service.MerchantOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/merchant/orders/")
public class MerchantOrdersManagementController {
    @Autowired
    private MerchantOrdersService merchantOrdersService;
    //分页查询所有订单列表
    @PreAuthorize("hasAuthority('Merchant')")
    @GetMapping("/list")
    public PageResult<MerchantOrdersDVO> getOrders(Integer pageNum, Integer pageSize,
                                                   String status, String sort) {
        return merchantOrdersService.getOrders(pageNum, pageSize, status, sort);
    }
    //查询订单详情
    @PreAuthorize("hasAuthority('Merchant')")
    @GetMapping("/detail")
    public Result<UserOrdersDetailDVO> getOrdersDetail(Long orderId) {
        return merchantOrdersService.getOrdersDetail(orderId);
    }

    //设置订单状态（发货）
    @Log(module = "订单", desc = "商家发货")
    @PreAuthorize("hasAuthority('Merchant')")
    @GetMapping("/status")
    public Result<String> setOrdersStatus(Long orderId) {
        return merchantOrdersService.setOrdersStatus(orderId);
    }
}
