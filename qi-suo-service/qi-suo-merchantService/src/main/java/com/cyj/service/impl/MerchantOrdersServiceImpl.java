package com.cyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.OrdersProductShow;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.entity.Merchant;
import com.cyj.entity.OrderItem;
import com.cyj.entity.Orders;
import com.cyj.entity.Product;
import com.cyj.mapper.*;
import com.cyj.service.MerchantOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MerchantOrdersServiceImpl implements MerchantOrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MerchantMapper merchantMapper;;
    @Override
    public PageResult<MerchantOrdersDVO> getOrders(Integer pageNum, Integer pageSize) {
        log.info("分页查询所有订单列表");

        //获取商家id
        Long merchantId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    public Result<UserOrdersDetailDVO> getOrdersDetail(Long orderId) {
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
        //获取商家名
        Merchant merchant = merchantMapper.selectById(orders.getMerchantId());
        userOrdersDetailDVO.setMerchantName(merchant.getMerchantName());
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
