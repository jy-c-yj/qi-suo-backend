package com.cyj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.OrdersProductShow;
import com.cyj.common.OrderStatus;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.feign.merchant.MerchantManagementFeignClient;
import com.cyj.entity.*;
import com.cyj.mapper.*;
import com.cyj.service.UserOrdersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserOrdersServiceImpl implements UserOrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MerchantManagementFeignClient merchantManagementFeignClient;
    @Override
    public Result<List<UserOrdersDVO>> list() {
        //获取用户id
        Long userID = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //查询数据库，获取订单列表：时间近的在前
        List<Orders> list = ordersMapper.selectList(new QueryWrapper<Orders>()
                .eq("user_id", userID)
                .orderByDesc("created_at"));
        //返回结果
        List<UserOrdersDVO> list1 = new ArrayList<>();
        for (Orders orders : list){
            UserOrdersDVO userOrdersDVO = new UserOrdersDVO();
            userOrdersDVO.setId(orders.getId());
            userOrdersDVO.setOrderNo(orders.getOrderNo());
            //获取商家名（商家信息由商家服务提供）
            Result<String> merchantNameResult = merchantManagementFeignClient.getMerchantNameById(orders.getMerchantId());
            userOrdersDVO.setMerchantName(merchantNameResult.getData());
            userOrdersDVO.setTotalAmount(orders.getTotalAmount());
            userOrdersDVO.setStatus(orders.getStatus());
            userOrdersDVO.setCreatedAt(orders.getCreatedAt());
            list1.add(userOrdersDVO);
        }
        return Result.success(list1);
    }

    @Override
    public Result<UserOrdersDetailDVO> detail(Long orderId) {
        //获取订单
        Orders orders = ordersMapper.selectById(orderId);

        //获取订单项
        List<OrderItem> items = orderItemMapper.selectList(new QueryWrapper<OrderItem>().eq("order_id",orderId));

        //返回结果
        UserOrdersDetailDVO userOrdersDetailDVO = new UserOrdersDetailDVO();
        userOrdersDetailDVO.setId(orders.getId());
        userOrdersDetailDVO.setOrderNo(orders.getOrderNo());
        userOrdersDetailDVO.setTotalAmount(orders.getTotalAmount());
        userOrdersDetailDVO.setStatus(orders.getStatus());
        userOrdersDetailDVO.setCreatedAt(orders.getCreatedAt());
        userOrdersDetailDVO.setMerchantId(orders.getMerchantId());
        userOrdersDetailDVO.setUserAddressId(orders.getUserAddressId());
        //获取商家名（商家信息由商家服务提供）
        Result<String> merchantNameResult = merchantManagementFeignClient.getMerchantNameById(orders.getMerchantId());
        userOrdersDetailDVO.setMerchantName(merchantNameResult.getData());
        //获取用户地址详情
        userOrdersDetailDVO.setDetailAddress(userAddressMapper.selectById(orders.getUserAddressId()).getDetailAddress());
        //生成订单显示处商品列表
        List<OrdersProductShow> productList = new ArrayList<>();
        for(OrderItem item : items){
            Product product = productMapper.selectById(item.getProductId());
            OrdersProductShow ordersProductShow = new OrdersProductShow();
            ordersProductShow.setProductId(product.getId());
            ordersProductShow.setProductName(product.getProductName());
            ordersProductShow.setPrice(product.getPrice());
            ordersProductShow.setQuantity(item.getQuantity());
            ordersProductShow.setTotalPrice(item.getTotalPrice());
            ordersProductShow.setProductImageUrl(product.getProductImageUrl());
            productList.add(ordersProductShow);
        }
        userOrdersDetailDVO.setProductList(productList);
        return Result.success(userOrdersDetailDVO);
    }

    @Override
    public Result<String> pay(Long orderId) {
        //先校验归属：原来不校验，任何人拿别人的 orderId 就能支付
        Orders orders = ordersMapper.selectById(orderId);
        if (orders == null || !Objects.equals(orders.getUserId(), currentUserId())) {
            return Result.error("订单不存在");
        }
        if (OrderStatus.CANCELLED.equals(orders.getStatus())) {
            return Result.error("订单已取消，无法支付");
        }
        //CAS：只允许「未支付 -> 已支付」这一个迁移，同时与超时取消互斥（谁先拿到那一行谁赢）
        int rows = ordersMapper.casStatus(orderId, OrderStatus.UNPAID, OrderStatus.PAID);
        return rows == 1 ? Result.success("支付成功") : Result.error("订单状态已变更，无法支付");
    }

    @Override
    public Result<String> confirm(Long orderId) {
        Orders orders = ordersMapper.selectById(orderId);
        if (orders == null || !Objects.equals(orders.getUserId(), currentUserId())) {
            return Result.error("订单不存在");
        }
        // 用户的「确认收货」发生在商家发货之后，正确迁移是「已发货 -> 已完成」
        int rows = ordersMapper.casStatus(orderId, OrderStatus.SHIPPED, OrderStatus.COMPLETED);
        return rows == 1 ? Result.success("确认成功") : Result.error("订单状态错误");
    }

    /**
     * 当前登录用户 id。调用方都在 hasAuthority('user') 保护下，principal 就是用户 id。
     */
    private Long currentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public PageResult<MerchantOrdersDVO> merchantList(Long merchantId, Integer pageNum, Integer pageSize,
                                                      String status, String sort) {
        log.info("分页查询商家订单列表, merchantId={}, status={}, sort={}", merchantId, status, sort);

        QueryWrapper<Orders> query = new QueryWrapper<Orders>().eq("merchant_id", merchantId);
        if (status != null && !status.isBlank()) {
            // 兼容历史数据里的「待支付」
            if (OrderStatus.UNPAID.equals(status)) {
                query.in("status", OrderStatus.UNPAID, "待支付");
            } else {
                query.eq("status", status);
            }
        }
        // 默认最新在前；sort=asc 时最早在前
        query.orderBy(true, "asc".equalsIgnoreCase(sort), "created_at");

        Page<Orders> page = new Page<>(pageNum, pageSize);
        List<Orders> ordersList = ordersMapper.selectList(page, query);
        List<MerchantOrdersDVO> list = ordersList.stream().map(MerchantOrdersDVO::toMerchantOrdersDVO).toList();
        PageResult<MerchantOrdersDVO> pageResult = new PageResult<>();
        pageResult.setRows(list);
        pageResult.setTotal(page.getTotal());
        return pageResult;
    }

    @Override
    public Result<String> setOrdersStatus(Long orderId) {
        //查询订单
        Orders orders = ordersMapper.selectById(orderId);
        if (orders == null) {
            return Result.error("订单不存在");
        }
        //设置订单状态：保留原有的两种提示，写入换成 CAS
        if (OrderStatus.UNPAID.equals(orders.getStatus())) {
            return Result.error("订单未支付");
        }
        int rows = ordersMapper.casStatus(orderId, OrderStatus.PAID, OrderStatus.SHIPPED);
        return rows == 1 ? Result.success("订单状态修改成功") : Result.error("订单状态错误");
    }
}
