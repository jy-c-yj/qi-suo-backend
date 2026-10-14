package com.cyj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.OrdersProductShow;
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
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        //查询数据库，获取订单列表
        List<Orders> list = ordersMapper.selectList (new QueryWrapper<Orders>().eq("user_id",userID));
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
        //支付操作，默认成功
        Boolean success = true;
        //更改订单状态
        try {
            if (success) {
                Orders orders = new Orders();
                orders.setId(orderId);
                orders.setStatus("已支付");
                ordersMapper.updateById(orders);
                return Result.success("支付成功");
            }
        } catch (Exception e) {
            return Result.error("支付失败");
        }
        return Result.error("支付失败");
    }

    @Override
    public Result<String> confirm(Long orderId) {
        //确认订单完成
        try {
            Orders orders = new Orders();
            orders.setId(orderId);
            orders.setStatus("已完成");
            ordersMapper.updateById(orders);
            return Result.success("确认成功");
        } catch (Exception e) {
            return Result.error("确认失败");
        }
    }

    @Override
    public PageResult<MerchantOrdersDVO> merchantList(Long merchantId, Integer pageNum, Integer pageSize) {
        log.info("分页查询所有订单列表");

        //分页查询所有订单列表
        Page<Orders> page = new Page<>(pageNum,pageSize);
        List<Orders> ordersList = ordersMapper.selectList(page,new QueryWrapper<Orders>().eq("merchant_id",merchantId));
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
        //设置订单状态
        if(orders.getStatus().equals("未支付")){
            return Result.error("订单未支付");
        }
        if(orders.getStatus().equals("已支付")){
            orders.setStatus("已发货");
            ordersMapper.updateById(orders);
            return Result.success("订单状态修改成功");
        }
        return Result.error("订单状态错误");
    }
}
