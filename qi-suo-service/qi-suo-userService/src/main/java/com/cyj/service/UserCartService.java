package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.CreateOrderDTO;
import com.cyj.dvo.CartDVO;
import com.cyj.dvo.UserOrdersDetailDVO;

import java.util.List;

public interface UserCartService {
    /**
     * 添加商品到购物车的方法
     *
     * @param productId  商品ID，用于标识要添加的商品

     * @param quantity   商品数量，表示要添加的商品数量
     * @return 返回一个Result对象，其中包含操作结果和相关信息
     */
    Result<String> addCartItem(Long productId, Integer quantity);

    Result<String> deleteCartItem(Long productId);

    Result<List<CartDVO>> getCartList(Long merchantId);

    Result<UserOrdersDetailDVO> createOrder(CreateOrderDTO createOrderDTO);
}
